package pl.bartoszbulaj.moonrock.service.impl;

import io.micrometer.core.instrument.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bartoszbulaj.moonrock.config.BitmexClientConfig;
import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.entity.InstrumentHistoryEntity;
import pl.bartoszbulaj.moonrock.mapper.InstrumentHistoryMapper;
import pl.bartoszbulaj.moonrock.repository.InstrumentHistoryRepository;
import pl.bartoszbulaj.moonrock.service.ConnectionService;
import pl.bartoszbulaj.moonrock.service.EmailService;
import pl.bartoszbulaj.moonrock.service.HistoryAnalyzerService;
import pl.bartoszbulaj.moonrock.service.InstrumentService;
import pl.bartoszbulaj.moonrock.validator.InstrumentServiceValidator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class InstrumentServiceImpl implements InstrumentService {

	private static final Logger LOG = LogManager.getLogger(InstrumentServiceImpl.class);
	private InstrumentHistoryRepository instrumentHistoryRepository;
	private InstrumentHistoryMapper instrumentHistoryMapper;
	private HistoryAnalyzerService historyAnalyzerService;
	private EmailService emailService;
	private ConnectionService connectionService;
	private InstrumentServiceValidator validator;

	@Autowired
	public InstrumentServiceImpl(InstrumentHistoryRepository instrumentHistoryRepository,
			ConnectionService connectionService, InstrumentHistoryMapper instrumentHistoryMapper,
			HistoryAnalyzerService analyzer, EmailService emailService, InstrumentServiceValidator validator) {
		this.instrumentHistoryRepository = instrumentHistoryRepository;
		this.instrumentHistoryMapper = instrumentHistoryMapper;
		this.historyAnalyzerService = analyzer;
		this.emailService = emailService;
		this.validator = validator;
		this.connectionService = connectionService;
	}

	@Override
	public List<InstrumentHistoryDto> getInstrumentHistory(String candleSize, String instrument, String count,
			String reverse) {
		if (!validator.isAllArgumentsValid(candleSize, instrument, count, reverse)) {
			return Collections.emptyList();
		}

		try {
			StringBuilder urlString = createUrlString(candleSize, instrument.toUpperCase(), count, reverse);
			URL instrumentHistoryUrl = new URL(urlString.toString());
			HttpURLConnection connection = (HttpURLConnection) instrumentHistoryUrl.openConnection();
			connection.setRequestMethod("GET");
			String jsonString = connectionService.getResultFromHttpRequest(connection);
			connection.disconnect();

			return instrumentHistoryMapper.mapToInstrumentHistoryDtoList(jsonString, candleSize);
		} catch (IOException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	public List<InstrumentHistoryDto> getInstrumentHistory(String instrument) {
		if (!validator.isInstrumentSymbolValid(instrument)) {
			return Collections.emptyList();
		}
		try {
			StringBuilder urlString = createUrlString("1h", instrument.toUpperCase(), "5", "false");
			URL instrumentHistoryUrl = new URL(urlString.toString());
			HttpURLConnection connection = (HttpURLConnection) instrumentHistoryUrl.openConnection();
			connection.setRequestMethod("GET");
			String jsonString = connectionService.getResultFromHttpRequest(connection);
			connection.disconnect();

			return instrumentHistoryMapper.mapToInstrumentHistoryDtoList(jsonString, instrument);
		} catch (IOException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	public boolean analyzeInstrumentHistory() {
		boolean isSignal = false;

		for (String instrument : BitmexClientConfig.getActiveInstruments()) {
			List<InstrumentHistoryDto> instrumentHistoryDtoList = loadInstrumentHistoryFromRepository(instrument);

			String signalDirection = historyAnalyzerService.checkForSignal(instrumentHistoryDtoList);
			if (!signalDirection.isEmpty()) {
				isSignal = true;
			}
		}
		return isSignal;
	}

	@Override
	public void sendEmailWithSignals() {
		StringBuilder emailText = new StringBuilder();
		for (String instrument : BitmexClientConfig.getActiveInstruments()) {
			List<InstrumentHistoryDto> instrumentHistoryDtoList = loadInstrumentHistoryFromRepository(instrument);
			String signalDirection = historyAnalyzerService.checkForSignal(instrumentHistoryDtoList);
			if (!signalDirection.isEmpty()) {
				emailText.append(emailService.createEmailText(instrument, signalDirection));
			}
		}
		sendEmailWIthSignal(emailText.toString());
	}

	@Override
	public List<InstrumentHistoryEntity> saveInstrumentHistory() {
		List<InstrumentHistoryDto> instrumentHistoryDtoList = new ArrayList<>();
		for (String instrument : BitmexClientConfig.getActiveInstruments()) {
			instrumentHistoryDtoList.addAll(getInstrumentHistory(instrument));
		}
		LOG.info("[History] is saved");
		return instrumentHistoryRepository
				.saveAll(instrumentHistoryMapper.mapToInstrumentHistoryEntityList(instrumentHistoryDtoList));
	}

	@Override
	public void deleteInstrumentHistory() {
		LOG.info("[History] is deleted");
		instrumentHistoryRepository.deleteAll();
	}

	@Override
	public boolean sendEmailWIthSignal(String emailText) {
		if (StringUtils.isBlank(emailText)) {
			return false;
		}
		try {
			emailService.sendEmail(emailText);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private List<InstrumentHistoryDto> loadInstrumentHistoryFromRepository(String instrument) {
		return instrumentHistoryMapper
				.mapToInstrumentHistoryDtoList(instrumentHistoryRepository.findAllBySymbol(instrument));
	}

	private StringBuilder createUrlString(String candleSize, String symbol, String count, String reverse) {
		StringBuilder urlString = new StringBuilder();
		urlString.append(BitmexClientConfig.getBitmexApiUrl());
		urlString.append("/trade/bucketed");
		urlString.append("?binSize=").append(candleSize);
		urlString.append("&symbol=").append(symbol);
		urlString.append("&count=").append(count);
		urlString.append("&reverse=").append(reverse);
		urlString.append("&startTime=").append(getTimestampStringFormattedToUTC5HoursAgo());
		return urlString;
	}

	private String getTimestampStringFormattedToUTC5HoursAgo() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.now(ZoneOffset.UTC).minusHours(5).format(formatter).replace(" ", "T");
	}

}
