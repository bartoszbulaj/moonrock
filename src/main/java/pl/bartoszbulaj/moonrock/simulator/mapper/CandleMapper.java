package pl.bartoszbulaj.moonrock.simulator.mapper;

import org.jfree.data.xy.DefaultHighLowDataset;
import pl.bartoszbulaj.moonrock.simulator.model.Candle;
import pl.bartoszbulaj.moonrock.simulator.model.CandleOHLC;

import java.util.List;

public interface CandleMapper {

	Candle map(CandleOHLC candleOHLC);

	CandleOHLC mapToOhlc(Candle candle);

	DefaultHighLowDataset mapToDefaultHighLowDataset(List<Candle> candleList);
}
