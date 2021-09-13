package pl.bartoszbulaj.moonrock.simulator.mapper;

import pl.bartoszbulaj.moonrock.simulator.model.Candle;
import pl.bartoszbulaj.moonrock.simulator.model.CandleOHLC;

public interface CandleMapper {

	public Candle map(CandleOHLC candleOHLC);

	public CandleOHLC mapToOhlc(Candle candle);
}
