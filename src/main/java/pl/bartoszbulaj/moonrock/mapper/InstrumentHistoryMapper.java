package pl.bartoszbulaj.moonrock.mapper;

import java.io.IOException;
import java.util.List;

import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.entity.InstrumentHistoryEntity;

public interface InstrumentHistoryMapper {

	List<InstrumentHistoryDto> mapToInstrumentHistoryDtoList(String jsonString, String candleSize) throws IOException;

	List<InstrumentHistoryEntity> mapToInstrumentHistoryEntityList(List<InstrumentHistoryDto> instrumentHistoryDtoList);

	InstrumentHistoryEntity mapToInstrumentHistoryEntity(InstrumentHistoryDto instrumentHistoryDto);

	InstrumentHistoryDto mapToInstrumentHistoryDto(InstrumentHistoryEntity instrumentHistoryEntity);

	List<InstrumentHistoryDto> mapToInstrumentHistoryDtoList(List<InstrumentHistoryEntity> instrumentHistoryEntityList);
}
