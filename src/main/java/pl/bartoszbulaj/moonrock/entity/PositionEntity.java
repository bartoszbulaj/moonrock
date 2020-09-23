package pl.bartoszbulaj.moonrock.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "positions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PositionEntity {

	private String account; // 123456
	@Id
	private String symbol; // example "ETHXBT"
	private String currency; // "XBt"
	private String underlying; // "ETH"
	private String quoteCurrency; // "XBT"
	private String commission; // 0.00075
	private String initMarginReq; // 0.02
	private String maintMarginReq; // 0.01
	private String riskLimit; // 5000000000
	private String leverage; // 50
	private String crossMargin; // true
	private String deleveragePercentile; // 1
	private String rebalancedPnl; // -1501
	private String prevRealisedPnl; // 0
	private String prevUnrealisedPnl; // 0
	private String prevClosePrice; // 0.02003
	private String openingTimestamp; // "2019-12-12T12:00:00.000Z"
	private String openingQty; // 1
	private String openingCost; // 2002000
	private String openingComm; // 0
	private String openOrderBuyQty; // 0
	private String openOrderBuyCost; // 0
	private String openOrderBuyPremium; // 0
	private String openOrderSellQty; // 0
	private String openOrderSellCost; // 0
	private String openOrderSellPremium; // 0
	private String execBuyQty; // 0
	private String execBuyCost; // 0
	private String execSellQty; // 0
	private String execSellCost; // 0
	private String execQty; // 0
	private String execCost; // 0
	private String execComm; // 0

	@Column(name = "CURRENTTIMESTAMP")
	private String currentTimestamp; // "2019-12-12T12:15:55.315Z"

	private String currentQty; // 1
	private String currentCost; // 2002000
	private String currentComm; // 0
	private String realisedCost; // 0
	private String unrealisedCost; // 2002000
	private String grossOpenCost; // 0
	private String grossOpenPremium; // 0
	private String grossExecCost; // 0
	private String isOpen; // true
	private String markPrice; // 0.02005
	private String markValue; // 2005000
	private String riskValue; // 2005000
	private String homeNotional; // 1
	private String foreignNotional; // -0.02005
	private String posState; // ""
	private String posCost; // 2002000
	private String posCost2; // 2002000
	private String posCross; // 0
	private String posInit; // 40040
	private String posComm; // 1532
	private String posLoss; // 0
	private String posMargin; // 41572
	private String posMaint; // 21552
	private String posAllowance; // 0
	private String taxableMargin; // 0
	private String initMargin; // 0
	private String maintMargin; // 44572
	private String sessionMargin; // 0
	private String targetExcessMargin; // 0
	private String varMargin; // 0
	private String realisedGrossPnl; // 0
	private String realisedTax; // 0
	private String realisedPnl; // 0
	private String unrealisedGrossPnl; // 3000
	private String longBankrupt; // 0
	private String shortBankrupt; // 0
	private String taxBase; // 0
	private String indicativeTaxRate; // null
	private String indicativeTax; // 0
	private String unrealisedTax; // 0
	private String unrealisedPnl; // 3000
	private String unrealisedPnlPcnt; // 0.0015
	private String unrealisedRoePcnt; // 0.0749
	private String simpleQty; // null
	private String simpleCost; // null
	private String simpleValue; // null
	private String simplePnl; // null
	private String simplePnlPcnt; // null
	private String avgCostPrice; // 0.02002
	private String avgEntryPrice; // 0.02002
	private String breakEvenPrice; // 0.02004
	private String marginCallPrice; // 0.01208
	private String liquidationPrice; // 0.01208
	private String bankruptPrice; // 0.01188
	private String timestamp; // "2019-12-12T12:15:55.315Z"
	private String lastPrice; // 0.02005
	private String lastValue; // 2005000
}
