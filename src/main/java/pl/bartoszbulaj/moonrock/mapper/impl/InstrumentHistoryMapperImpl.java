package pl.bartoszbulaj.moonrock.mapper.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.entity.InstrumentHistoryEntity;
import pl.bartoszbulaj.moonrock.mapper.InstrumentHistoryMapper;
import pl.bartoszbulaj.moonrock.simulator.mapper.CandleMapper;
import pl.bartoszbulaj.moonrock.simulator.model.Candle;
import pl.bartoszbulaj.moonrock.simulator.model.CandleOHLC;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InstrumentHistoryMapperImpl implements InstrumentHistoryMapper {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private CandleMapper candleMapper;

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
	public List<Candle> mapToCandleList(List<InstrumentHistoryDto> instrumentHistoryDtoList) {
		return instrumentHistoryDtoList.stream().map(item -> candleMapper.mapToCandle(item)).collect(Collectors.toList());
	}

	@Override
	public List<CandleOHLC> mapToCandleOHLCList(List<InstrumentHistoryDto> instrumentHistoryDtoList) {
		return instrumentHistoryDtoList.stream().map(item -> candleMapper.mapToOhlc(item)).collect(Collectors.toList());
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
