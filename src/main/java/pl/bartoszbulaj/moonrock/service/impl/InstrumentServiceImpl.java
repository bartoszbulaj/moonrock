package pl.bartoszbulaj.moonrock.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.core.instrument.util.StringUtils;
import pl.bartoszbulaj.moonrock.config.BitmexClientConfig;
import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.entity.InstrumentHistoryEntity;
import pl.bartoszbulaj.moonrock.mapper.InstrumentHistoryMapper;
import pl.bartoszbulaj.moonrock.repository.InstrumentHistoryRepository;
import pl.bartoszbulaj.moonrock.service.EmailClient;
import pl.bartoszbulaj.moonrock.service.HistoryAnalyzer;
import pl.bartoszbulaj.moonrock.service.InstrumentService;
import pl.bartoszbulaj.moonrock.validator.InstrumentServiceValidator;

@Service
@Transactional
public class InstrumentServiceImpl implements InstrumentService {

	private static final Logger LOG = LogManager.getLogger(InstrumentServiceImpl.class);
	private InstrumentHistoryRepository instrumentHistoryRepository;
	private InstrumentHistoryMapper instrumentHistoryMapper;
	private HistoryAnalyzer historyAnalyzer;
	private EmailClient emailClient;
	private InstrumentServiceValidator validator;

	@Autowired
	public InstrumentServiceImpl(InstrumentHistoryRepository instrumentHistoryRepository,
			BitmexClientConfig bitmexClientConfig, InstrumentHistoryMapper instrumentHistoryMapper,
			HistoryAnalyzer analyzer, EmailClient emailClient, InstrumentServiceValidator validator) {
		this.instrumentHistoryRepository = instrumentHistoryRepository;
		this.instrumentHistoryMapper = instrumentHistoryMapper;
		this.historyAnalyzer = analyzer;
		this.emailClient = emailClient;
		this.validator = validator;
	}

	@Override
	public List<InstrumentHistoryDto> getInstrumentHistory(String candleSize, String instrument, String count,
			String reverse) {
		if (!validator.isAllArgumentsValid(candleSize, instrument, count, reverse)) {
			return new ArrayList<>();
		}

		try {
			StringBuilder urlString = createUrlString(candleSize, instrument.toUpperCase(), count, reverse);
			URL instrumentHistoryUrl = new URL(urlString.toString());
			HttpURLConnection connection = (HttpURLConnection) instrumentHistoryUrl.openConnection();
			connection.setRequestMethod("GET");
			String jsonString = getRequestResultString(connection);
			connection.disconnect();

			return instrumentHistoryMapper.mapToInstrumentHistoryDtoList(jsonString, candleSize);
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	public List<InstrumentHistoryDto> getInstrumentHistory(String instrument) {
		if (!validator.isInstrumentSymbolValid(instrument)) {
			return new ArrayList<>();
		}
		try {
			StringBuilder urlString = createUrlString("1h", instrument.toUpperCase(), "5", "false");
			URL instrumentHistoryUrl = new URL(urlString.toString());
			HttpURLConnection connection = (HttpURLConnection) instrumentHistoryUrl.openConnection();
			connection.setRequestMethod("GET");
			String jsonString = getRequestResultString(connection);
			connection.disconnect();

			return instrumentHistoryMapper.mapToInstrumentHistoryDtoList(jsonString, instrument);
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	public boolean analyzeInstrumentHistoryAndSendEmailWithSignals() {
		StringBuilder emailText = new StringBuilder();
		for (String instrument : BitmexClientConfig.getActiveInstruments()) {
			List<InstrumentHistoryDto> instrumentHistoryDtoList = loadInstrumentHistoryFromRepository(instrument);

			String signalDirection = historyAnalyzer.checkForSignal(instrumentHistoryDtoList);
			if (!signalDirection.isEmpty()) {
				emailText.append(emailClient.createEmailText(instrument, signalDirection));
			}
		}

		if (!emailText.toString().isEmpty()) {
			sendEmailWIthSignal(emailText.toString());
			return true;
		} else {
			LOG.info("[No Signal.]");
			return false;
		}
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
			emailClient.sendEmail(emailText);
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

	private String getRequestResultString(HttpURLConnection connection) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuilder content = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		return content.toString();
	}

	private String getTimestampStringFormattedToUTC5HoursAgo() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.now(ZoneOffset.UTC).minusHours(5).format(formatter).replace(" ", "T");
	}

	@Override
	public void sendTestEmail() {
		StringBuilder emailText = new StringBuilder();
		emailText.append(emailClient.createEmailText("Test instrument", "Test signal"));
		sendEmailWIthSignal(emailText.toString());
	}

}
