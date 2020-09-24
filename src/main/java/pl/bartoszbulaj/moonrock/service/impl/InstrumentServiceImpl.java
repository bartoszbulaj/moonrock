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

import pl.bartoszbulaj.moonrock.config.BitmexClientConfig;
import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.entity.InstrumentHistoryEntity;
import pl.bartoszbulaj.moonrock.mapper.InstrumentHistoryMapper;
import pl.bartoszbulaj.moonrock.repository.InstrumentHistoryRepository;
import pl.bartoszbulaj.moonrock.service.EmailClient;
import pl.bartoszbulaj.moonrock.service.HistoryAnalyzer;
import pl.bartoszbulaj.moonrock.service.InstrumentService;

@Service
@Transactional
public class InstrumentServiceImpl implements InstrumentService {

	private static final Logger LOG = LogManager.getLogger(InstrumentServiceImpl.class);
	private InstrumentHistoryRepository instrumentHistoryRepository;
	private InstrumentHistoryMapper instrumentHistoryMapper;
	private HistoryAnalyzer historyAnalyzer;
	private EmailClient emailClient;

	@Autowired
	public InstrumentServiceImpl(InstrumentHistoryRepository instrumentHistoryRepository,
			BitmexClientConfig bitmexClientConfig, InstrumentHistoryMapper instrumentHistoryMapper,
			HistoryAnalyzer analyzer, EmailClient emailClient) {
		this.instrumentHistoryRepository = instrumentHistoryRepository;
		this.instrumentHistoryMapper = instrumentHistoryMapper;
		this.historyAnalyzer = analyzer;
		this.emailClient = emailClient;
	}

	@Override
	public List<InstrumentHistoryDto> getInstrumentHistory(String candleSize, String symbol, String count,
			String reverse) {
		List<InstrumentHistoryDto> instrumentHistoryDtoList = new ArrayList<>();

		try {
			StringBuilder urlString = createUrlString(candleSize, symbol, count, reverse);
			URL instrumentHistoryUrl = new URL(urlString.toString());
			HttpURLConnection connection = (HttpURLConnection) instrumentHistoryUrl.openConnection();
			connection.setRequestMethod("GET");
			String jsonString = getRequestResultString(connection);
			connection.disconnect();

			instrumentHistoryDtoList = instrumentHistoryMapper.mapToInstrumentHistoryDtoList(jsonString, candleSize);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return instrumentHistoryDtoList;
	}

	@Override
	public void analyzeInstrumentHistory() {
		LOG.info("Analyzing instrument history in progress...");
		List<InstrumentHistoryDto> instrumentHistoryDtoList = instrumentHistoryMapper
				.mapToInstrumentHistoryDtoList(instrumentHistoryRepository.findAll());

		String signalDirection = historyAnalyzer.checkForSignal(instrumentHistoryDtoList);
		if (signalDirection != "") {
			try {
				emailClient.sendEmail(instrumentHistoryDtoList.get(0), signalDirection);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<InstrumentHistoryEntity> saveInstrumentHistory() {
		// TODO get All active instruments from Bitmex
		List<InstrumentHistoryDto> instrumentHistoryDtoList = getInstrumentHistory("1h", "xbt", "5", "false");
		LOG.info("Hello! instrument history is saved");
		return instrumentHistoryRepository
				.saveAll(instrumentHistoryMapper.mapToInstrumentHistoryEntityList(instrumentHistoryDtoList));
	}

	@Override
	public void deleteInstrumentHistory() {
		LOG.info("Hello! instrument history is deleted");
		instrumentHistoryRepository.deleteAll();
	}

	private StringBuilder createUrlString(String candleSize, String symbol, String count, String reverse) {
		StringBuilder urlString = new StringBuilder();
		urlString.append(BitmexClientConfig.getBitmexApiUrl());
		urlString.append("trade/bucketed");
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
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		return content.toString();
	}

	private String getTimestampStringFormattedToUTC5HoursAgo() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String timestampString = LocalDateTime.now(ZoneOffset.UTC).minusHours(5).format(formatter).toString()
				.replace(" ", "T");
		return timestampString;
	}

}
