package pl.bartoszbulaj.moonrock.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class InstrumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String highPrice;
    private String lowPrice;
    private String lastPrice;
    private String midPrice;
    private String bidPrice;
    private String askPrice;
    private String markPrice;
    private String timestamp; // np. 2019-06-04T10:34:00.922Z

    private String tickSize;
    private String fundingRate;
    private String indicativeFundingRate;
    private String lastTickDirection;
    private String lastChangePcnt;

    private String makerFee;
    private String takerFee;

    private String prevTotalVolume;
    private String totalVolume;
    private String volume;
    private String volume24h;
    private String prevPrice24h;
    private String vwap;

}
