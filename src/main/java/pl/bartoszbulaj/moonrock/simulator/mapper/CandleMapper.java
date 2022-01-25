package pl.bartoszbulaj.moonrock.simulator.mapper;

import org.jfree.data.xy.DefaultHighLowDataset;
import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.simulator.model.Candle;
import pl.bartoszbulaj.moonrock.simulator.model.CandleOHLC;

import java.util.List;

public interface CandleMapper {

	Candle mapToCandle(CandleOHLC candleOHLC);

	CandleOHLC mapToOhlc(Candle candle);

	Candle mapToCandle(InstrumentHistoryDto instrumentHistoryDto);

	CandleOHLC mapToOhlc(InstrumentHistoryDto instrumentHistoryDto);

	DefaultHighLowDataset mapToDefaultHighLowDataset(List<Candle> candleList);
}
