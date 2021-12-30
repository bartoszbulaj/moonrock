package pl.bartoszbulaj.moonrock.simulator.service;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultHighLowDataset;

public interface SimulatorChartAnalyzerService {

	void markSignals(DefaultHighLowDataset dataset, JFreeChart chart);
}
