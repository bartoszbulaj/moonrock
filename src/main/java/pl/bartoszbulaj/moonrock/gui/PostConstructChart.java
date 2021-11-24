package pl.bartoszbulaj.moonrock.gui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bartoszbulaj.moonrock.simulator.mapper.CandleMapper;
import pl.bartoszbulaj.moonrock.simulator.service.SimulatorHistoryService;

import javax.annotation.PostConstruct;

@Component
public class PostConstructChart {

	@Autowired
	private SimulatorHistoryService simulatorHistoryService;
	@Autowired
	private CandleMapper candleMapper;

	@PostConstruct
	public void create() {
		JFreeCandlestickChart jfreeCandlestickChart = new JFreeCandlestickChart(simulatorHistoryService, candleMapper);
		jfreeCandlestickChart.showChart();
	}
}
