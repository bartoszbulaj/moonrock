package pl.bartoszbulaj.moonrock.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDto {

	private String orderID;
	private String symbol;
	private String side;
	private String simpleOrderQty;
	private String orderQty;
	private String price;
	private String displayQty;
	// "stopPx": 0,
	// "pegOffsetValue": 0,
	// "pegPriceType": "string",
	private String currency;
	// "settlCurrency": "string",
	private String ordType;
	// "timeInForce": "string",
	// "execInst": "string",
	// "contingencyType": "string",
	// "exDestination": "string",
	// "ordStatus": "string",
	// "triggered": "string",
	// "workingIndicator": true,
	// "ordRejReason": "string",
	// "simpleLeavesQty": 0,
	// "leavesQty": 0,
	// "simpleCumQty": 0,
	// "cumQty": 0,
	// "avgPx": 0,
	// "multiLegReportingType": "string",
	// "text": "string",
	private String transactTime; // "2019-06-24T06:11:25.680Z"
	private String timestamp;
}
