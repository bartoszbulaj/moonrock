package pl.bartoszbulaj.moonrock.simulator.model;

import java.time.LocalDate;

public class CandleOHLC {

	public LocalDate timestamp;
	public String symbol;
	public double open;
	public int high;
	public int low;
	public double close;

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

	@Override
	public String toString() {
		return "CandleOHLC{" + "timestamp=" + timestamp + ", symbol='" + symbol + '\'' + ", open=" + open + ", high="
				+ high + ", low=" + low + ", close=" + close + '}';
	}
}
