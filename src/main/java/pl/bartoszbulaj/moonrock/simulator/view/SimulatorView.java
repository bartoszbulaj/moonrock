package pl.bartoszbulaj.moonrock.simulator.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.bartoszbulaj.moonrock.simulator.model.Candle;
import pl.bartoszbulaj.moonrock.simulator.model.CandleOHLC;
import pl.bartoszbulaj.moonrock.simulator.service.HistoryService;

import java.util.List;

@Route("/simulator")
@PageTitle("Moonrock simulator")
public class SimulatorView extends VerticalLayout {

	private final HistoryService historyService;

	SimulatorView(HistoryService historyService) {
		this.historyService = historyService;

		List<CandleOHLC> historyCandleOhlcList = historyService.getHistoryCandleOhlcList("1h", "XBTUSD", "5", "false");
		List<Candle> historyCandleList = historyService.getHistoryCandleList("1h", "XBTUSD", "5", "false");

		Grid<CandleOHLC> gridCandleOhlcList = new Grid<>(CandleOHLC.class);
		gridCandleOhlcList.setItems(historyCandleOhlcList);
		add(gridCandleOhlcList);

		Grid<Candle> gridCandleList = new Grid<>(Candle.class);
		gridCandleList.setItems(historyCandleList);
		add(gridCandleList);
	}
}
