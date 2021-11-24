package pl.bartoszbulaj.moonrock.simulator.mapper.impl;

import org.jfree.data.xy.DefaultHighLowDataset;
import org.springframework.stereotype.Component;
import pl.bartoszbulaj.moonrock.simulator.mapper.CandleMapper;
import pl.bartoszbulaj.moonrock.simulator.model.Candle;
import pl.bartoszbulaj.moonrock.simulator.model.CandleOHLC;

import java.util.Date;
import java.util.List;

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

	public DefaultHighLowDataset mapToDefaultHighLowDataset(List<Candle> candleList) {
		Date[] date = new Date[candleList.size()];
		double[] high = new double[candleList.size()];
		double[] low = new double[candleList.size()];
		double[] open = new double[candleList.size()];
		double[] close = new double[candleList.size()];
		double[] volume = new double[candleList.size()];

		for (int i = 0; i < candleList.size(); i++) {
			date[i] = java.sql.Timestamp.valueOf(candleList.get(i).getTimestamp());
			high[i] = candleList.get(i).getHigh();
			low[i] = candleList.get(i).getLow();
			open[i] = candleList.get(i).getOpen();
			close[i] = candleList.get(i).getClose();
			volume[i] = candleList.get(i).getVolume();
		}
		return new DefaultHighLowDataset(candleList.iterator().next().getSymbol(), date, high, low, open, close,
				volume);
	}
}
