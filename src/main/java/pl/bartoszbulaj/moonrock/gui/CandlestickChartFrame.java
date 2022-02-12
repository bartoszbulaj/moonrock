package pl.bartoszbulaj.moonrock.gui;

import org.apache.logging.log4j.util.Strings;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.ui.UIUtils;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bartoszbulaj.moonrock.service.AppConfigurationService;
import pl.bartoszbulaj.moonrock.service.HistoryService;
import pl.bartoszbulaj.moonrock.simulator.mapper.CandleMapper;
import pl.bartoszbulaj.moonrock.simulator.model.Candle;
import pl.bartoszbulaj.moonrock.simulator.service.SimulatorChartAnalyzerService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class CandlestickChartFrame extends JFrame {
	// TODO make this as parameter from gui
	private static final String COUNT = "150";

	public static final String J_FREE_CHART = "JFreeChart";
	public static final String REVERSE_FALSE = "false";
	public static final String VALUE = "Value ($)";
	public static final String TIME_LINE = "Time line";

	private final AppConfigurationService appConfigurationService;
	private final SimulatorChartAnalyzerService simulatorChartAnalyzerService;
	private final CandleMapper candleMapper;
	private final HistoryService historyService;

	private String cryptoPair;

	@Autowired
	public CandlestickChartFrame(AppConfigurationService appConfigurationService,
			SimulatorChartAnalyzerService simulatorChartAnalyzerService, CandleMapper candleMapper,
			HistoryService historyService) {
		super(J_FREE_CHART);
		this.appConfigurationService = appConfigurationService;
		this.simulatorChartAnalyzerService = simulatorChartAnalyzerService;
		this.candleMapper = candleMapper;
		this.historyService = historyService;
		this.cryptoPair = appConfigurationService.getCryptoPairForSimulator();

		List<Candle> historyCandleList = historyService.collectCandleHistoryForGivenInstrument(cryptoPair,
				appConfigurationService.getHistoryAnalyzerInterval(), COUNT, REVERSE_FALSE);
		final DefaultHighLowDataset dataset = candleMapper.mapToDefaultHighLowDataset(historyCandleList);
		double lowestLow = getLowestLow(dataset);
		double highestHigh = getHighestHigh(dataset);

		final JFreeChart chart = createChart(cryptoPair, dataset);
		Range range = new Range(lowestLow * 0.99D, highestHigh * 1.01D);

		CustomCandlestickRenderer customCandlestickRenderer = new CustomCandlestickRenderer();
		customCandlestickRenderer.setUpPaint(new Color(0x53B987));
		customCandlestickRenderer.setDownPaint(new Color(0xeb4d5c));
		customCandlestickRenderer.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_SMALLEST);
		chart.getXYPlot().setRenderer(customCandlestickRenderer);

		chart.getXYPlot().setOrientation(PlotOrientation.VERTICAL);
		chart.getXYPlot().getRangeAxis().setRange(range);
		chart.getXYPlot().getRangeAxis().setDefaultAutoRange(range);
		chart.getXYPlot().getRangeAxis().setAutoRange(true);

		chart.getXYPlot().setRangePannable(true);
		chart.getXYPlot().setDomainPannable(true);

		NumberAxis numberAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
		numberAxis.setAutoRangeIncludesZero(false);

		searchForSignals(dataset, chart);

		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(1024, 700));
		chartPanel.setHorizontalAxisTrace(true);
		chartPanel.setVerticalAxisTrace(true);
		chartPanel.setMouseWheelEnabled(true);

		setContentPane(chartPanel);

	}

	private void searchForSignals(DefaultHighLowDataset dataset, JFreeChart chart) {
		simulatorChartAnalyzerService.markSignals(dataset, chart);
	}

	public static void addPointer(Number datasetItemTimestamp, double price, XYPlot plot, boolean isBuyOrder) {
		final XYPointerAnnotation pointer = new XYPointerAnnotation(Strings.EMPTY, datasetItemTimestamp.doubleValue(),
				price, Math.PI);
		pointer.setBaseRadius(8.0);
		pointer.setTipRadius(5.0);
		pointer.setArrowWidth(10);
		pointer.setArrowLength(10);
		pointer.setArrowPaint(isBuyOrder ? new Color(0xFF00FF00, true) : new Color(0xFFFF960C, true));
		plot.addAnnotation(pointer);
	}

	private JFreeChart createChart(String title, final DefaultHighLowDataset dataset) {
		return ChartFactory.createCandlestickChart(title, TIME_LINE, VALUE, dataset, true);
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
