package pl.bartoszbulaj.moonrock.mapper.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.bartoszbulaj.moonrock.dto.OrderDto;
import pl.bartoszbulaj.moonrock.dto.PositionDto;
import pl.bartoszbulaj.moonrock.mapper.OrderMapper;

@Component
public class OrderMapperImpl implements OrderMapper {

	private ObjectMapper objectMapper;

	@Autowired
	public OrderMapperImpl(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public List<OrderDto> mapToOrderDtoList(String jsonString) throws IOException {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper.readValue(jsonString, new TypeReference<List<PositionDto>>() {
		});
	}

}
