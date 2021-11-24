package pl.bartoszbulaj.moonrock.service.impl;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bartoszbulaj.moonrock.config.BitmexClientConfig;
import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.entity.InstrumentHistoryEntity;
import pl.bartoszbulaj.moonrock.mapper.InstrumentHistoryMapper;
import pl.bartoszbulaj.moonrock.repository.InstrumentHistoryRepository;
import pl.bartoszbulaj.moonrock.service.AppConfigurationService;
import pl.bartoszbulaj.moonrock.service.ConnectionService;
import pl.bartoszbulaj.moonrock.service.EmailService;
import pl.bartoszbulaj.moonrock.service.HistoryAnalyzerService;
import pl.bartoszbulaj.moonrock.service.HistoryService;
import pl.bartoszbulaj.moonrock.validator.InstrumentServiceValidator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@Slf4j
public class HistoryServiceImpl implements HistoryService {

	private final AppConfigurationService appConfigurationService;
	private final InstrumentHistoryRepository instrumentHistoryRepository;
	private final InstrumentHistoryMapper instrumentHistoryMapper;
	private final HistoryAnalyzerService historyAnalyzerService;
	private final EmailService emailService;
	private final ConnectionService connectionService;
	private final InstrumentServiceValidator validator;

	@Autowired
	public HistoryServiceImpl(AppConfigurationService appConfigurationService,
			InstrumentHistoryRepository instrumentHistoryRepository, ConnectionService connectionService,
			InstrumentHistoryMapper instrumentHistoryMapper, HistoryAnalyzerService analyzer, EmailService emailService,
			InstrumentServiceValidator validator) {
		this.appConfigurationService = appConfigurationService;
		this.instrumentHistoryRepository = instrumentHistoryRepository;
		this.instrumentHistoryMapper = instrumentHistoryMapper;
		this.historyAnalyzerService = analyzer;
		this.emailService = emailService;
		this.validator = validator;
		this.connectionService = connectionService;
	}

	@Override
	public List<InstrumentHistoryDto> collectHistoryForGivenInstrument(String instrument, String candleSize,
			String count, String reverse) {
		validator.isAllArgumentsValid(candleSize, instrument, count, reverse);

		try {
			if (candleSize.equals("15m")) {// TODO refactor this block
				StringBuilder urlString = createUrlString("5m", instrument.toUpperCase(), "15", reverse);
				URL instrumentHistoryUrl = new URL(urlString.toString());
				HttpURLConnection connection = (HttpURLConnection) instrumentHistoryUrl.openConnection();
				connection.setRequestMethod("GET");
				String jsonString = connectionService.getResultFromHttpRequest(connection);
				connection.disconnect();
				List<InstrumentHistoryDto> instrumentHistoryDtoList = instrumentHistoryMapper
						.mapToInstrumentHistoryDtoList(jsonString, "15");
				return mergeCandleListToSizeM15(instrumentHistoryDtoList);
			}
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

	private List<InstrumentHistoryDto> mergeCandleListToSizeM15(List<InstrumentHistoryDto> instrumentHistoryDtoList) {
		int numberOfCandles = instrumentHistoryDtoList.size() / 3;
		List<InstrumentHistoryDto> m15List = new ArrayList<>();
		for (int i = 0; i < numberOfCandles; i++) {
			List<InstrumentHistoryDto> m5CandleList = new ArrayList<>();
			for (int j = 0; j < 3; j++) {
				m5CandleList.add(instrumentHistoryDtoList.remove(0));
			}
			InstrumentHistoryDto m15Candle = new InstrumentHistoryDto();
			m15Candle.setCandleSize("15m");
			m15Candle.setOpen(m5CandleList.get(2).getOpen());
			m15Candle.setHigh(m5CandleList.stream().map(InstrumentHistoryDto::getHigh).max(Comparator.naturalOrder())
					.orElse(null));
			m15Candle.setLow(m5CandleList.stream().map(InstrumentHistoryDto::getLow).min(Comparator.naturalOrder())
					.orElse(null));
			m15Candle.setClose(m5CandleList.get(0).getClose());
			m15Candle.setLastSize(m5CandleList.get(0).getLastSize());
			m15Candle.setSymbol(m5CandleList.get(0).getSymbol());
			m15Candle.setTimestamp(m5CandleList.get(0).getTimestamp());
			m15Candle.setTrades(
					m5CandleList.stream().map(InstrumentHistoryDto::getTrades).mapToDouble(Double::doubleValue).sum());
			m15Candle.setTurnover(m5CandleList.stream().map(InstrumentHistoryDto::getTurnover)
					.mapToDouble(Double::doubleValue).sum());
			m15Candle.setVolume(
					m5CandleList.stream().map(InstrumentHistoryDto::getVolume).mapToDouble(Double::doubleValue).sum());
			m15Candle.setVwap(
					m5CandleList.stream().map(InstrumentHistoryDto::getVwap).mapToDouble(Double::doubleValue).sum());
			m15Candle.setHomeNotional(null);
			m15Candle.setForeignNotional(null);
			m15List.add(m15Candle);
		}
		return m15List;
	}

	@Override
	public boolean analyzeInstrumentHistory() {// TODO remove analyzing from this service
		log.info("[Analyzer] Searching for signals.");
		boolean isSignal = false;
		for (String instrument : BitmexClientConfig.getActiveInstruments()) {
			List<InstrumentHistoryDto> instrumentHistoryDtoList = loadInstrumentHistoryFromRepository(instrument);

			String signalDirection = historyAnalyzerService.checkForSignal(instrumentHistoryDtoList);
			if (!signalDirection.isEmpty()) {
				isSignal = true;
			}
		}
		log.info("[Analyzer] End of analysis.");
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
		sendEmailWithGivenMessage(emailText.toString());
	}

	@Override
	public List<InstrumentHistoryEntity> saveInstrumentHistory() {
		List<InstrumentHistoryDto> instrumentHistoryDtoList = new ArrayList<>();
		String candleSize = appConfigurationService.getHistoryAnalyzerInterval();
		for (String instrument : BitmexClientConfig.getActiveInstruments()) {
			instrumentHistoryDtoList.addAll(collectHistoryForGivenInstrument(instrument, candleSize, "5", "true"));
		}
		if (instrumentHistoryDtoList.isEmpty()) {
			throw new IllegalStateException("No result from method collectHistoryForGivenInstrument");
		}
		log.info("[History] Saved");
		return instrumentHistoryRepository
				.saveAll(instrumentHistoryMapper.mapToInstrumentHistoryEntityList(instrumentHistoryDtoList));
	}

	@Override
	public void deleteInstrumentHistory() {
		log.info("[History] Deleted");
		instrumentHistoryRepository.deleteAll();
	}

	@Override
	public boolean sendEmailWithGivenMessage(String emailText) {// TODO remove sending email from this service
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
		return urlString;
	}
}
