package pl.bartoszbulaj.moonrock.mapper;

import java.io.IOException;
import java.util.List;

import pl.bartoszbulaj.moonrock.dto.OrderDto;

public interface OrderMapper {

	List<OrderDto> mapToOrderDtoList(String resultJson) throws IOException;
}
