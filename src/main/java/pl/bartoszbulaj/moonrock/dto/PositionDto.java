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
public class PositionDto {

	private String symbol;
	private String currentQty;
	private String riskValue;
	private String avgEntryPrice;
	private String markPrice;
	private String liquidationPrice;
	private String maintMargin;
	private String realisedPNL;
	private Boolean isOpen;
	private String lastPrice;
	private String leverage;
	private String unrealisedGrossPnl;
	private String unrealisedPnlPcnt;
	private String unrealisedRoePcnt;
}
