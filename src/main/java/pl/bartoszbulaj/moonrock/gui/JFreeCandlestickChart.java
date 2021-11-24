package pl.bartoszbulaj.moonrock.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.UIUtils;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bartoszbulaj.moonrock.simulator.mapper.CandleMapper;
import pl.bartoszbulaj.moonrock.simulator.model.Candle;
import pl.bartoszbulaj.moonrock.simulator.service.SimulatorHistoryService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class JFreeCandlestickChart extends ApplicationFrame {

	private final SimulatorHistoryService simulatorHistoryService;
	private final CandleMapper candleMapper;

	@Autowired
	public JFreeCandlestickChart(SimulatorHistoryService simulatorHistoryService, CandleMapper candleMapper) {
		super("JFreeChart");
		this.simulatorHistoryService = simulatorHistoryService;
		this.candleMapper = candleMapper;
		List<Candle> historyCandleList = simulatorHistoryService.getHistoryCandleList("1h", "XBTUSD", "150", "false");

		final DefaultHighLowDataset dataset = candleMapper.mapToDefaultHighLowDataset(historyCandleList);
		double lowestLow = getLowestLow(dataset);
		double highestHigh = getHighestHigh(dataset);

		final JFreeChart chart = createChart(dataset);
		chart.getXYPlot().setOrientation(PlotOrientation.VERTICAL);
		chart.getXYPlot().getRangeAxis().setRange(lowestLow * 0.99D, highestHigh * 1.01D);

		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 450));
		chartPanel.setHorizontalAxisTrace(true);
		chartPanel.setVerticalAxisTrace(true);
		setContentPane(chartPanel);
	}

	private JFreeChart createChart(final DefaultHighLowDataset dataset) {
		return ChartFactory.createCandlestickChart("Candlestick Chart", "Time", "Value", dataset, true);
	}

	public void showChart() {
		this.pack();
		UIUtils.centerFrameOnScreen(this);
		this.setVisible(true);
	}

	private double getLowestLow(DefaultHighLowDataset dataset) {
		List<Double> list = new ArrayList<>();
		for (int i = 1; i < dataset.getItemCount(0); i++) {
			list.add(dataset.getLowValue(0, i));
		}
		list.sort(Comparator.naturalOrder());
		return list.get(0);
	}

	private double getHighestHigh(DefaultHighLowDataset dataset) {
		List<Double> list = new ArrayList<>();
		for (int i = 1; i < dataset.getItemCount(0); i++) {
			list.add(dataset.getLowValue(0, i));
		}
		list.sort(Comparator.naturalOrder());
		list.sort(Comparator.reverseOrder());
		return list.get(0);
	}
}
