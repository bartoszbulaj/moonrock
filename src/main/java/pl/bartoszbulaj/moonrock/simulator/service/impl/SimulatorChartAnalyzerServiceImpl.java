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

	private void markSellSignals(DefaultHighLowDataset dataset, JFreeChart chart, int i) {
		boolean first = isCandleGreen(dataset, i - 3);
		boolean second = isCandleGreen(dataset, i - 2);
		boolean third = isCandleGreen(dataset, i - 1);
		boolean fourth = isCandleRed(dataset, i);
		if (first && second && third && fourth) {
			JFreeCandlestickChart.addPointer(dataset.getX(series, i), (double) dataset.getClose(series, i),
					chart.getXYPlot(), false);
		}
	}

	private void markBuySignals(DefaultHighLowDataset dataset, JFreeChart chart, int i) {
		boolean first = isCandleRed(dataset, i - 3);
		boolean second = isCandleRed(dataset, i - 2);
		boolean third = isCandleRed(dataset, i - 1);
		boolean fourth = isCandleGreen(dataset, i);
		if (first && second && third && fourth) {
			JFreeCandlestickChart.addPointer(dataset.getX(series, i), (double) dataset.getClose(series, i),
					chart.getXYPlot(), true);
		}
	}

	private boolean isCandleRed(DefaultHighLowDataset dataset, int i) {
		double open = (double) dataset.getOpen(series, i);
		double high = (double) dataset.getHigh(series, i);
		double low = (double) dataset.getLow(series, i);
		double close = (double) dataset.getClose(series, i);
		return close < open;
	}

	private boolean isCandleGreen(DefaultHighLowDataset dataset, int i) {
		double open = (double) dataset.getOpen(series, i);
		double high = (double) dataset.getHigh(series, i);
		double low = (double) dataset.getLow(series, i);
		double close = (double) dataset.getClose(series, i);
		return close > open;
	}
}
