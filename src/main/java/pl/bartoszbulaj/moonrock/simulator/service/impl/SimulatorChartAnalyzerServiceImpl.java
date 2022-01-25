package pl.bartoszbulaj.moonrock.simulator.service.impl;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import pl.bartoszbulaj.moonrock.gui.JFreeCandlestickChart;
import pl.bartoszbulaj.moonrock.simulator.service.SimulatorChartAnalyzerService;

@Slf4j
@Component
public class SimulatorChartAnalyzerServiceImpl implements SimulatorChartAnalyzerService {

	private static final int series = JFreeCandlestickChart.XBTUSD.hashCode();
	private static final double PERCENT_IN_SWING = 0.3;

	@Override
	public void markSignals(DefaultHighLowDataset dataset, JFreeChart chart) {
		int itemCount = dataset.getItemCount(series);

		for (int i = 0; i < itemCount; i++) {
			if (i >= 3) {
				markBuySignals(dataset, chart, i);
				markSellSignals(dataset, chart, i);
			}
		}
	}
	// TODO refactor this class
	private void markSellSignals(DefaultHighLowDataset dataset, JFreeChart chart, int i) {
		boolean first = isCandleGreen(dataset, i - 3);
		boolean second = isCandleGreen(dataset, i - 2);
		boolean third = isCandleGreen(dataset, i - 1);
		boolean fourth = isCandleRed(dataset, i);

		if (first && second && third && fourth
				&& (percentInSwing(getOpenPrice(dataset, i - 3), getClosePrice(dataset, i - 1)) > PERCENT_IN_SWING)) {
			JFreeCandlestickChart.addPointer(dataset.getX(series, i), dataset.getCloseValue(series, i),
					chart.getXYPlot(), false);
		}
	}
	private void markBuySignals(DefaultHighLowDataset dataset, JFreeChart chart, int i) {
		// TODO refactor this to PATTERN
		boolean first = isCandleRed(dataset, i - 3);
		boolean second = isCandleRed(dataset, i - 2);
		boolean third = isCandleRed(dataset, i - 1);
		boolean fourth = isCandleGreen(dataset, i);

		if (first && second && third && fourth
				&& (percentInSwing(getOpenPrice(dataset, i - 3), getClosePrice(dataset, i - 1)) > PERCENT_IN_SWING)) {
			JFreeCandlestickChart.addPointer(dataset.getX(series, i), dataset.getCloseValue(series, i),
					chart.getXYPlot(), true);
		}
	}

	private double percentInSwing(double openPrice, double closePrice) {
		// round up to 2 decimal places
		double val = Math.round(((openPrice - closePrice) / openPrice * 100) * 100) / 100.0;
		return Math.abs(val);
	}

	private double getClosePrice(DefaultHighLowDataset dataset, int i) {
		return dataset.getCloseValue(series, i);
	}

	private double getOpenPrice(DefaultHighLowDataset dataset, int i) {
		return dataset.getOpenValue(series, i);
	}

	private boolean isCandleRed(DefaultHighLowDataset dataset, int i) {
		double open = (double) dataset.getOpen(series, i);
		double close = (double) dataset.getClose(series, i);
		return close < open;
	}

	private boolean isCandleGreen(DefaultHighLowDataset dataset, int i) {
		double open = (double) dataset.getOpen(series, i);
		double close = (double) dataset.getClose(series, i);
		return close > open;
	}
}
