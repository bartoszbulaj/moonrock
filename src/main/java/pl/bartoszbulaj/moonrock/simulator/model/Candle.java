package pl.bartoszbulaj.moonrock.simulator.model;

public class Candle {

	public String timestamp;
	public String symbol;
	public double open;
	public double high;
	public double low;
	public double close;
	public int trades;
	public double volume;
	public double vwap;
	public int lastSize;
	public long turnover;
	public double homeNotional;
	public int foreignNotional;

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

	public int getTrades() {
		return trades;
	}

	public void setTrades(int trades) {
		this.trades = trades;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
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