package pl.bartoszbulaj.moonrock.service;

import pl.bartoszbulaj.moonrock.dto.OrderDto;

import java.io.IOException;
import java.util.List;

public interface OrderService {

	List<OrderDto> getAllOrders(String owner) throws IOException;

	List<OrderDto> getOpenOrders(String owner) throws IOException;

	List<OrderDto> getOpenOrders(String owner, String symbol) throws IOException;

	OrderDto createOrderCloseWithMarket(String symbol, String orderQty);

	String cancelAllActiveOrders(String owner, String symbol) throws IOException;

}
