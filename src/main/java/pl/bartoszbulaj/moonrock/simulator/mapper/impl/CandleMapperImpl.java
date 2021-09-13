package pl.bartoszbulaj.moonrock.simulator.mapper.impl;

import org.springframework.stereotype.Component;

import pl.bartoszbulaj.moonrock.simulator.mapper.CandleMapper;
import pl.bartoszbulaj.moonrock.simulator.model.Candle;
import pl.bartoszbulaj.moonrock.simulator.model.CandleOHLC;

@Component
public class CandleMapperImpl implements CandleMapper {
	public Candle map(CandleOHLC candleOHLC) {
		Candle candle = new Candle();
		candle.setClose(candleOHLC.getClose());
		candle.setHigh(candleOHLC.getHigh());
		candle.setLow(candleOHLC.getLow());
		candle.setOpen(candleOHLC.getOpen());
		candle.setSymbol(candleOHLC.getSymbol());
		candle.setTimestamp(candleOHLC.getTimestamp());
		return candle;
	}

	public CandleOHLC mapToOhlc(Candle candle) {
		CandleOHLC candleOHLC = new CandleOHLC();
		candleOHLC.setClose(candle.getClose());
		candleOHLC.setHigh(candle.getHigh());
		candleOHLC.setLow(candle.getLow());
		candleOHLC.setOpen(candle.getOpen());
		candleOHLC.setSymbol(candle.getSymbol());
		candleOHLC.setTimestamp(candle.getTimestamp());
		return candleOHLC;
	}
}
