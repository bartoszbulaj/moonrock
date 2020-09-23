package pl.bartoszbulaj.moonrock.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class InstrumentHistoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
