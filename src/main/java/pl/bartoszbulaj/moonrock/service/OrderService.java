package pl.bartoszbulaj.moonrock.service;

import java.io.IOException;
import java.util.List;

import pl.bartoszbulaj.moonrock.dto.OrderDto;

public interface OrderService {

	List<OrderDto> getAllOrders(String owner) throws IOException;

	List<OrderDto> getOpenOrders(String owner) throws IOException;

	List<OrderDto> getOpenOrders(String owner, String symbol) throws IOException;

	OrderDto createOrderCloseWithMarket(String symbol, String orderQty);

	String closeAllOrders(String owner, String symbol) throws IOException;

}
