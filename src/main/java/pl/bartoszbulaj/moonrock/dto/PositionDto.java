package pl.bartoszbulaj.moonrock.dto;

import javax.persistence.Id;

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

	@Id
	private String symbol;
	private String currentQty;
	private String riskValue;
	private String avgEntryPrice;
	private String markPrice;
	private String liquidationPrice;
	private String maintMargin;
	private String unrealisedGrossPnl;
	private String unrealisedPnlPcnt;
	private String realisedPNL;

}
