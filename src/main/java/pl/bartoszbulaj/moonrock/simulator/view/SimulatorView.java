package pl.bartoszbulaj.moonrock.simulator.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import pl.bartoszbulaj.moonrock.simulator.model.Candle;
import pl.bartoszbulaj.moonrock.simulator.service.SimulatorHistoryService;

import java.util.List;

@Route("/simulator")
@PageTitle("Moonrock simulator")
public class SimulatorView extends VerticalLayout {

	private final SimulatorHistoryService simulatorHistoryService;

	SimulatorView(SimulatorHistoryService simulatorHistoryService) {
		this.simulatorHistoryService = simulatorHistoryService;

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
	}
}
