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
public class InstrumentHistoryDto {

	private Long id;
	private String timestamp;
	private String symbol;
	private Double open;
	private Double high;
	private Double low;
	private Double close;
	private Double trades;
	private Double volume;
	private Double vwap;
	private Double lastSize;
	private Double turnover;
	private Double homeNotional;
	private Double foreignNotional;

	private String candleSize;
}
