package pl.bartoszbulaj.moonrock.mapper.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.entity.InstrumentHistoryEntity;
import pl.bartoszbulaj.moonrock.mapper.InstrumentHistoryMapper;

@Component
public class InstrumentHistoryMapperImpl implements InstrumentHistoryMapper {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<InstrumentHistoryDto> mapToInstrumentHistoryDtoList(String jsonString, String candleSize)
			throws IOException {
		List<InstrumentHistoryDto> instrumentHistoryDtoList = objectMapper.readValue(jsonString,
				new TypeReference<List<InstrumentHistoryDto>>() {
				});
		instrumentHistoryDtoList.forEach(instrument -> instrument.setCandleSize(candleSize));

		return instrumentHistoryDtoList;
	}

	@Override
	public List<InstrumentHistoryEntity> mapToInstrumentHistoryEntityList(
			List<InstrumentHistoryDto> instrumentHistoryDtoList) {
		return instrumentHistoryDtoList.stream().map(this::mapToInstrumentHistoryEntity).collect(Collectors.toList());
	}

	@Override
	public InstrumentHistoryEntity mapToInstrumentHistoryEntity(InstrumentHistoryDto instrumentHistoryDto) {
		return modelMapper.map(instrumentHistoryDto, InstrumentHistoryEntity.class);
	}

	@Override
	public List<InstrumentHistoryDto> mapToInstrumentHistoryDtoList(
			List<InstrumentHistoryEntity> instrumentHistoryEntityList) {
		return instrumentHistoryEntityList.stream().map(this::mapToInstrumentHistoryDto).collect(Collectors.toList());
	}

	@Override
	public InstrumentHistoryDto mapToInstrumentHistoryDto(InstrumentHistoryEntity instrumentHistoryEntity) {
		return modelMapper.map(instrumentHistoryEntity, InstrumentHistoryDto.class);
	}

}
