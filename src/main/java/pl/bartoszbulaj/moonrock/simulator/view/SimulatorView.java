package pl.bartoszbulaj.moonrock.simulator.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.bartoszbulaj.moonrock.config.CandleSize;
import pl.bartoszbulaj.moonrock.config.CryptoPair;
import pl.bartoszbulaj.moonrock.service.HistoryService;
import pl.bartoszbulaj.moonrock.simulator.mapper.CandleMapper;
import pl.bartoszbulaj.moonrock.simulator.model.Candle;
import pl.bartoszbulaj.moonrock.simulator.service.SimulatorChartAnalyzerService;

import java.util.List;

@Route("/simulator")
@PageTitle("Moonrock simulator")
public class SimulatorView extends VerticalLayout {

	public static final String NUMBER_OF_CANDLES = "15";
	public static final String REVERSE_FALSE = "false";
	private final SimulatorChartAnalyzerService simulatorChartAnalyzerService;
	private final CandleMapper candleMapper;
	private final HistoryService historyService;

	SimulatorView(SimulatorChartAnalyzerService simulatorChartAnalyzerService, CandleMapper candleMapper,
			HistoryService historyService) {

		this.simulatorChartAnalyzerService = simulatorChartAnalyzerService;
		this.candleMapper = candleMapper;
		this.historyService = historyService;

		Grid<Candle> gridCandleList = new Grid<>(Candle.class);
		List<Candle> historyCandleList = historyService.collectCandleHistoryForGivenInstrument(CryptoPair.XBTUSD, CandleSize.CANDLE_SIZE_15M, NUMBER_OF_CANDLES,
				REVERSE_FALSE);
		gridCandleList.setItems(historyCandleList); // TODO remove this gui table test
		gridCandleList.removeColumnByKey("lastSize");
		gridCandleList.removeColumnByKey("foreignNotional");
		gridCandleList.removeColumnByKey("vwap");
		gridCandleList.removeColumnByKey("turnover");
		gridCandleList.removeColumnByKey("homeNotional");
		gridCandleList.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		add(gridCandleList);
	}
}
