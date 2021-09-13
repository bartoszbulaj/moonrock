package pl.bartoszbulaj.moonrock.simulator.model;

import java.time.LocalDate;

public class Candle {

	public LocalDate timestamp;
	public String symbol;
	public double open;
	public int high;
	public int low;
	public double close;
	public int trades;
	public int volume;
	public double vwap;
	public int lastSize;
	public long turnover;
	public double homeNotional;
	public int foreignNotional;

	public LocalDate getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDate timestamp) {
		this.timestamp = timestamp;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public int getHigh() {
		return high;
	}

	public void setHigh(int high) {
		this.high = high;
	}

	public int getLow() {
		return low;
	}

	public void setLow(int low) {
		this.low = low;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public int getTrades() {
		return trades;
	}

	public void setTrades(int trades) {
		this.trades = trades;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public double getVwap() {
		return vwap;
	}

	public void setVwap(double vwap) {
		this.vwap = vwap;
	}

	public int getLastSize() {
		return lastSize;
	}

	public void setLastSize(int lastSize) {
		this.lastSize = lastSize;
	}

	public long getTurnover() {
		return turnover;
	}

	public void setTurnover(long turnover) {
		this.turnover = turnover;
	}

	public double getHomeNotional() {
		return homeNotional;
	}

	public void setHomeNotional(double homeNotional) {
		this.homeNotional = homeNotional;
	}

	public int getForeignNotional() {
		return foreignNotional;
	}

	public void setForeignNotional(int foreignNotional) {
		this.foreignNotional = foreignNotional;
	}

	@Override
	public String toString() {
		return "Candle{" + "timestamp=" + timestamp + ", symbol='" + symbol + '\'' + ", open=" + open + ", high=" + high
				+ ", low=" + low + ", close=" + close + ", trades=" + trades + ", volume=" + volume + ", vwap=" + vwap
				+ ", lastSize=" + lastSize + ", turnover=" + turnover + ", homeNotional=" + homeNotional
				+ ", foreignNotional=" + foreignNotional + '}';
	}
}