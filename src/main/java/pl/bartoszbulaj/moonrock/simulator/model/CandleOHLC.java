package pl.bartoszbulaj.moonrock.simulator.model;

public class CandleOHLC {

	public String timestamp;
	public String symbol;
	public double open;
	public double high;
	public double low;
	public double close;
	public double volume;

	public CandleOHLC() {
	}

	public CandleOHLC(String timestamp, String symbol, double open, double high, double low, double close, double volume) {
		this.timestamp = timestamp;
		this.symbol = symbol;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
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

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
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
