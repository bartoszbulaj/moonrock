package pl.bartoszbulaj.moonrock.gui;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.extern.slf4j.Slf4j;

@Route("/help")
@PageTitle("Help Moonrock")
@Slf4j
public class HelpView extends VerticalLayout {
	private H1 helpMoonrockTitle;
	private NativeButton mainPageButton;

	public HelpView() {
		helpMoonrockTitle = new H1("Help");
		helpMoonrockTitle.setHeight(30, Unit.POINTS);
		add(helpMoonrockTitle);

		showDescription();

		mainPageButton = new NativeButton("Main Page");
		mainPageButton.addClickListener(event -> mainPageButton.getUI().ifPresent(ui -> ui.navigate(MainView.class)));

		add(mainPageButton);
	}

	private void showDescription() {
		add(new Label("/init - create and connect websockets"));
		add(new Label("/start - start websockets communication"));
		add(new Label("/stop - stop websockets communication"));
		add(new Label("/status - show websocket status"));
		add(new Label("/market-close?owner=admin&symbol=% - close position with given symbol"));
	}
}
