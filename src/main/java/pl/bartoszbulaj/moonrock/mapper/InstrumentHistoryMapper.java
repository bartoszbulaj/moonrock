package pl.bartoszbulaj.moonrock.mapper;

import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.entity.InstrumentHistoryEntity;
import pl.bartoszbulaj.moonrock.simulator.model.Candle;
import pl.bartoszbulaj.moonrock.simulator.model.CandleOHLC;

import java.io.IOException;
import java.util.List;

public interface InstrumentHistoryMapper {

	List<InstrumentHistoryDto> mapToInstrumentHistoryDtoList(String jsonString, String candleSize) throws IOException;

	List<InstrumentHistoryEntity> mapToInstrumentHistoryEntityList(List<InstrumentHistoryDto> instrumentHistoryDtoList);

	List<Candle> mapToCandleList(List<InstrumentHistoryDto> instrumentHistoryDtoList);

	List<CandleOHLC> mapToCandleOHLCList(List<InstrumentHistoryDto> instrumentHistoryDtoList);

	InstrumentHistoryEntity mapToInstrumentHistoryEntity(InstrumentHistoryDto instrumentHistoryDto);

	InstrumentHistoryDto mapToInstrumentHistoryDto(InstrumentHistoryEntity instrumentHistoryEntity);

	List<InstrumentHistoryDto> mapToInstrumentHistoryDtoList(List<InstrumentHistoryEntity> instrumentHistoryEntityList);
}
