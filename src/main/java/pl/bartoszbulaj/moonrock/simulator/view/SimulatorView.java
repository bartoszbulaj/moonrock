package pl.bartoszbulaj.moonrock.simulator.view;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import pl.bartoszbulaj.moonrock.gui.JFreeCandlestickChart;
import pl.bartoszbulaj.moonrock.service.HistoryService;
import pl.bartoszbulaj.moonrock.simulator.mapper.CandleMapper;
import pl.bartoszbulaj.moonrock.simulator.model.Candle;
import pl.bartoszbulaj.moonrock.simulator.service.SimulatorChartAnalyzerService;

@Route("/simulator")
@PageTitle("Moonrock simulator")
public class SimulatorView extends VerticalLayout {

	private final SimulatorChartAnalyzerService simulatorChartAnalyzerService;
	private final CandleMapper candleMapper;
	private final HistoryService historyService;

	SimulatorView(SimulatorChartAnalyzerService simulatorChartAnalyzerService, CandleMapper candleMapper,
			HistoryService historyService) {

		this.simulatorChartAnalyzerService = simulatorChartAnalyzerService;
		this.candleMapper = candleMapper;
		this.historyService = historyService;

		Grid<Candle> gridCandleList = new Grid<>(Candle.class);

		List<Candle> historyCandleList = historyService.collectCandleHistoryForGivenInstrument("XBTUSD", "15m", "15",
				"false");
		gridCandleList.setItems(historyCandleList); // TODO remove this gui table test
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
		JFreeCandlestickChart jfreeCandlestickChart = new JFreeCandlestickChart(simulatorChartAnalyzerService,
				candleMapper, historyService);
		jfreeCandlestickChart.showChart();
	}
}
