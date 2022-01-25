package pl.bartoszbulaj.moonrock.simulator.mapper.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.jfree.data.xy.DefaultHighLowDataset;
import org.springframework.stereotype.Component;

import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.simulator.mapper.CandleMapper;
import pl.bartoszbulaj.moonrock.simulator.model.Candle;
import pl.bartoszbulaj.moonrock.simulator.model.CandleOHLC;

@Component
public class CandleMapperImpl implements CandleMapper {
	public Candle mapToCandle(CandleOHLC candleOHLC) {
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

	@Override
	public Candle mapToCandle(InstrumentHistoryDto instrumentHistoryDto) {
		Candle candle = new Candle();
		candle.setClose(instrumentHistoryDto.getClose());
		candle.setHigh(instrumentHistoryDto.getHigh());
		candle.setLow(instrumentHistoryDto.getLow());
		candle.setOpen(instrumentHistoryDto.getOpen());
		candle.setSymbol(instrumentHistoryDto.getSymbol());
		candle.setTimestamp(instrumentHistoryDto.getTimestamp());
		candle.setVolume(instrumentHistoryDto.getVolume());
		return candle;
	}

	@Override
	public CandleOHLC mapToOhlc(InstrumentHistoryDto instrument) {
		return new CandleOHLC(instrument.getTimestamp(), instrument.getSymbol(), instrument.getOpen(),
				instrument.getHigh(), instrument.getLow(), instrument.getClose(), instrument.getVolume());
	}

	public DefaultHighLowDataset mapToDefaultHighLowDataset(List<Candle> candleList) {
		Date[] date = new Date[candleList.size()];
		double[] high = new double[candleList.size()];
		double[] low = new double[candleList.size()];
		double[] open = new double[candleList.size()];
		double[] close = new double[candleList.size()];
		double[] volume = new double[candleList.size()];

		for (int i = 0; i < candleList.size(); i++) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
					.withZone(ZoneId.of("UTC"));
			LocalDateTime localDateTime = LocalDateTime.parse(candleList.get(i).getTimestamp(), formatter);
			// TODO implement datetime mapper

			date[i] = java.sql.Timestamp.valueOf(localDateTime);
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
