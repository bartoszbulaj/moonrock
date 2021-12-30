package pl.bartoszbulaj.moonrock.simulator.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.bartoszbulaj.moonrock.gui.JFreeCandlestickChart;
import pl.bartoszbulaj.moonrock.simulator.mapper.CandleMapper;
import pl.bartoszbulaj.moonrock.simulator.model.Candle;
import pl.bartoszbulaj.moonrock.simulator.service.SimulatorChartAnalyzerService;
import pl.bartoszbulaj.moonrock.simulator.service.SimulatorHistoryService;

import java.util.List;

@Route("/simulator")
@PageTitle("Moonrock simulator")
public class SimulatorView extends VerticalLayout {

	private final SimulatorHistoryService simulatorHistoryService;
	private final SimulatorChartAnalyzerService simulatorChartAnalyzerService;
	private final CandleMapper candleMapper;

	SimulatorView(SimulatorHistoryService simulatorHistoryService,
			SimulatorChartAnalyzerService simulatorChartAnalyzerService, CandleMapper candleMapper) {
		this.simulatorHistoryService = simulatorHistoryService;
		this.simulatorChartAnalyzerService = simulatorChartAnalyzerService;
		this.candleMapper = candleMapper;

		List<Candle> historyCandleList = simulatorHistoryService.getHistoryCandleList("1h", "XBTUSD", "15", "false");

		Grid<Candle> gridCandleList = new Grid<>(Candle.class);
		gridCandleList.setItems(historyCandleList);
		gridCandleList.removeColumnByKey("lastSize");
		gridCandleList.removeColumnByKey("foreignNotional");
		gridCandleList.removeColumnByKey("vwap");
		gridCandleList.removeColumnByKey("turnover");
		gridCandleList.removeColumnByKey("homeNotional");
		gridCandleList.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		add(gridCandleList);

		Button showChartButton = new Button("Show Chart", click -> create());
		add(showChartButton);
	}

	public void create() {
		JFreeCandlestickChart jfreeCandlestickChart = new JFreeCandlestickChart(simulatorHistoryService,
				simulatorChartAnalyzerService, candleMapper);
		jfreeCandlestickChart.showChart();
	}
}
