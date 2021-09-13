package pl.bartoszbulaj.moonrock.simulator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.bartoszbulaj.moonrock.simulator.mapper.CandleMapper;
import pl.bartoszbulaj.moonrock.simulator.model.Candle;
import pl.bartoszbulaj.moonrock.simulator.model.CandleOHLC;
import pl.bartoszbulaj.moonrock.simulator.service.SimulatorHistoryService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SimulatorHistoryServiceImpl implements SimulatorHistoryService {

	private final RestTemplate restTemplate;
	private final CandleMapper candleMapper;

	public SimulatorHistoryServiceImpl(RestTemplate restTemplate, CandleMapper candleMapper) {
		this.restTemplate = restTemplate;
		this.candleMapper = candleMapper;
	}

	@Override
	public List<CandleOHLC> getHistoryCandleOhlcList(String candleSize, String symbol, String numberOfCandles,
			String reverse) {
		StringBuilder urlString = createUrlString(candleSize, symbol, numberOfCandles, reverse);
		List<Candle> result = restTemplate
				.exchange(urlString.toString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<Candle>>() {
				}).getBody();

		List<CandleOHLC> ohlcs = null;
		if (result != null) {
			ohlcs = result.stream().map(candleMapper::mapToOhlc).collect(Collectors.toList());
		}

		return ohlcs;
	}

	@Override
	public List<Candle> getHistoryCandleList(String candleSize, String symbol, String numberOfCandles, String reverse) {
		String urlString = createUrlString(candleSize, symbol, numberOfCandles, reverse).toString();

		return restTemplate.exchange(urlString, HttpMethod.GET, null, new ParameterizedTypeReference<List<Candle>>() {
		}).getBody();
	}

	private StringBuilder createUrlString(String candleSize, String symbol, String count, String reverse) {
		StringBuilder urlString = new StringBuilder();
		urlString.append("https://www.bitmex.com/api/v1");
		urlString.append("/trade/bucketed");
		urlString.append("?binSize=").append(candleSize);
		urlString.append("&symbol=").append(symbol);
		urlString.append("&count=").append(count);
		urlString.append("&reverse=").append(reverse);
		urlString.append("&startTime=").append(getTimestampStringFormattedToUtcGivenHoursAgo(count));
		return urlString;
	}

	private String getTimestampStringFormattedToUtcGivenHoursAgo(String count) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.now(ZoneOffset.UTC).minusHours(Long.parseLong(count)).format(formatter).replace(" ", "T");
	}

}
