package pl.bartoszbulaj.moonrock.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PositionEntity {

	// @Column(columnDefinition = "TEXT")
	// private String account; // 123456
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(columnDefinition = "TEXT")
	private String symbol; // example "ETHXBT"

	@Column(columnDefinition = "TEXT")
	private String currency; // "XBt"
	@Column(columnDefinition = "TEXT")
	private String underlying; // "ETH"
	@Column(columnDefinition = "TEXT")
	private String quoteCurrency; // "XBT"
	@Column(columnDefinition = "TEXT")
	private String commission; // 0.00075
	@Column(columnDefinition = "TEXT")
	private String initMarginReq; // 0.02
	@Column(columnDefinition = "TEXT")
	private String maintMarginReq; // 0.01
	@Column(columnDefinition = "TEXT")
	private String riskLimit; // 5000000000
	@Column(columnDefinition = "TEXT")
	private String leverage; // 50
	@Column(columnDefinition = "TEXT")
	private String crossMargin; // true
	@Column(columnDefinition = "TEXT")
	private String deleveragePercentile; // 1
	@Column(columnDefinition = "TEXT")
	private String rebalancedPnl; // -1501
	@Column(columnDefinition = "TEXT")
	private String prevRealisedPnl; // 0
	@Column(columnDefinition = "TEXT")
	private String prevUnrealisedPnl; // 0
	@Column(columnDefinition = "TEXT")
	private String prevClosePrice; // 0.02003
	@Column(columnDefinition = "TEXT")
	private String openingTimestamp; // "2019-12-12T12:00:00.000Z"
	@Column(columnDefinition = "TEXT")
	private String openingQty; // 1
	@Column(columnDefinition = "TEXT")
	private String openingCost; // 2002000
	@Column(columnDefinition = "TEXT")
	private String openingComm; // 0
	@Column(columnDefinition = "TEXT")
	private String openOrderBuyQty; // 0
	@Column(columnDefinition = "TEXT")
	private String openOrderBuyCost; // 0
	@Column(columnDefinition = "TEXT")
	private String openOrderBuyPremium; // 0
	@Column(columnDefinition = "TEXT")
	private String openOrderSellQty; // 0
	@Column(columnDefinition = "TEXT")
	private String openOrderSellCost; // 0
	@Column(columnDefinition = "TEXT")
	private String openOrderSellPremium; // 0
	@Column(columnDefinition = "TEXT")
	private String execBuyQty; // 0
	@Column(columnDefinition = "TEXT")
	private String execBuyCost; // 0
	@Column(columnDefinition = "TEXT")
	private String execSellQty; // 0
	@Column(columnDefinition = "TEXT")
	private String execSellCost; // 0
	@Column(columnDefinition = "TEXT")
	private String execQty; // 0
	@Column(columnDefinition = "TEXT")
	private String execCost; // 0
	@Column(columnDefinition = "TEXT")
	private String execComm; // 0
	@Column(name = "CURRENTTIMESTAMP")
	private String currentTimestamp; // "2019-12-12T12:15:55.315Z"
	@Column(columnDefinition = "TEXT")
	private String currentQty; // 1
	@Column(columnDefinition = "TEXT")
	private String currentCost; // 2002000
	@Column(columnDefinition = "TEXT")
	private String currentComm; // 0
	@Column(columnDefinition = "TEXT")
	private String realisedCost; // 0
	@Column(columnDefinition = "TEXT")
	private String unrealisedCost; // 2002000
	@Column(columnDefinition = "TEXT")
	private String grossOpenCost; // 0
	@Column(columnDefinition = "TEXT")
	private String grossOpenPremium; // 0
	@Column(columnDefinition = "TEXT")
	private String grossExecCost; // 0
	@Column(columnDefinition = "TEXT")
	private String isOpen; // true
	@Column(columnDefinition = "TEXT")
	private String markPrice; // 0.02005
	@Column(columnDefinition = "TEXT")
	private String markValue; // 2005000
	@Column(columnDefinition = "TEXT")
	private String riskValue; // 2005000
	@Column(columnDefinition = "TEXT")
	private String homeNotional; // 1
	@Column(columnDefinition = "TEXT")
	private String foreignNotional; // -0.02005
	@Column(columnDefinition = "TEXT")
	private String posState; // ""
	@Column(columnDefinition = "TEXT")
	private String posCost; // 2002000
	@Column(columnDefinition = "TEXT")
	private String posCost2; // 2002000
	@Column(columnDefinition = "TEXT")
	private String posCross; // 0
	@Column(columnDefinition = "TEXT")
	private String posInit; // 40040
	@Column(columnDefinition = "TEXT")
	private String posComm; // 1532
	@Column(columnDefinition = "TEXT")
	private String posLoss; // 0
	@Column(columnDefinition = "TEXT")
	private String posMargin; // 41572
	@Column(columnDefinition = "TEXT")
	private String posMaint; // 21552
	@Column(columnDefinition = "TEXT")
	private String posAllowance; // 0
	@Column(columnDefinition = "TEXT")
	private String taxableMargin; // 0
	@Column(columnDefinition = "TEXT")
	private String initMargin; // 0
	@Column(columnDefinition = "TEXT")
	private String maintMargin; // 44572
	@Column(columnDefinition = "TEXT")
	private String sessionMargin; // 0
	@Column(columnDefinition = "TEXT")
	private String targetExcessMargin; // 0
	@Column(columnDefinition = "TEXT")
	private String varMargin; // 0
	@Column(columnDefinition = "TEXT")
	private String realisedGrossPnl; // 0
	@Column(columnDefinition = "TEXT")
	private String realisedTax; // 0
	@Column(columnDefinition = "TEXT")
	private String realisedPnl; // 0
	@Column(columnDefinition = "TEXT")
	private String unrealisedGrossPnl; // 3000
	@Column(columnDefinition = "TEXT")
	private String longBankrupt; // 0
	@Column(columnDefinition = "TEXT")
	private String shortBankrupt; // 0
	@Column(columnDefinition = "TEXT")
	private String taxBase; // 0
	@Column(columnDefinition = "TEXT")
	private String indicativeTaxRate; // null
	@Column(columnDefinition = "TEXT")
	private String indicativeTax; // 0
	@Column(columnDefinition = "TEXT")
	private String unrealisedTax; // 0
	@Column(columnDefinition = "TEXT")
	private String unrealisedPnl; // 3000
	@Column(columnDefinition = "TEXT")
	private String unrealisedPnlPcnt; // 0.0015
	@Column(columnDefinition = "TEXT")
	private String unrealisedRoePcnt; // 0.0749
	@Column(columnDefinition = "TEXT")
	private String simpleQty; // null
	@Column(columnDefinition = "TEXT")
	private String simpleCost; // null
	@Column(columnDefinition = "TEXT")
	private String simpleValue; // null
	@Column(columnDefinition = "TEXT")
	private String simplePnl; // null
	@Column(columnDefinition = "TEXT")
	private String simplePnlPcnt; // null
	@Column(columnDefinition = "TEXT")
	private String avgCostPrice; // 0.02002
	@Column(columnDefinition = "TEXT")
	private String avgEntryPrice; // 0.02002
	@Column(columnDefinition = "TEXT")
	private String breakEvenPrice; // 0.02004
	@Column(columnDefinition = "TEXT")
	private String marginCallPrice; // 0.01208
	@Column(columnDefinition = "TEXT")
	private String liquidationPrice; // 0.01208
	@Column(columnDefinition = "TEXT")
	private String bankruptPrice; // 0.01188
	@Column(columnDefinition = "TEXT")
	private String timestamp; // "2019-12-12T12:15:55.315Z"
	@Column(columnDefinition = "TEXT")
	private String lastPrice; // 0.02005
	@Column(name = "LASTVALUE")
	private String lastValue; // 2005000
}
