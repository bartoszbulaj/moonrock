package pl.bartoszbulaj.moonrock.simulator.service;

import pl.bartoszbulaj.moonrock.simulator.model.Candle;
import pl.bartoszbulaj.moonrock.simulator.model.CandleOHLC;

import java.util.List;

public interface HistoryService {
	List<CandleOHLC> getHistoryCandleOhlcList(String candleSize, String symbol, String numberOfCandles, String reverse);

	List<Candle> getHistoryCandleList(String candleSize, String symbol, String numberOfCandles, String reverse);
}
