package pl.bartoszbulaj.moonrock.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WalletEntity {

	@Id
	private Long account;
	private String currency;
	private String prevDeposited;
	private String prevWithdrawn;
	private String prevTransferIn;
	private String prevTransferOut;
	private String prevAmount;
	private String prevTimestamp;
	private String deltaDeposited;
	private String deltaWithdrawn;
	private String deltaTransferIn;
	private String deltaTransferOut;
	private String deltaAmount;
	private String deposited;
	private String withdrawn;
	private String transferIn;
	private String transferOut;
	private String amount;
	private String pendingCredit;
	private String pendingDebit;
	private String confirmedDebit;
	private String timestamp;
	private String addr;
	private String script;

}
