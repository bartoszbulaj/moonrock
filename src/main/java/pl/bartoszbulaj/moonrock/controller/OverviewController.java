package pl.bartoszbulaj.moonrock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.bartoszbulaj.moonrock.config.BitmexClientConfig;
import pl.bartoszbulaj.moonrock.service.WebsocketManagerService;

@Controller
@RequestMapping("/")
public class OverviewController {

	private static final String REDIRECT_TO_MAIN_PAGE = "redirect:/";

	@Autowired
	private WebsocketManagerService websocketManagerService;

	@GetMapping
	public String showOverview() {
		return "index.html";
	}

	@GetMapping("/init")
	public String initWS() {
		List<String> instrumentList = BitmexClientConfig.getActiveInstruments();
		websocketManagerService.addAllWebsockets(instrumentList);
		websocketManagerService.connectAllWebsockets();

		return REDIRECT_TO_MAIN_PAGE;
	}

	@GetMapping("/start")
	public String sendMessage() {
		websocketManagerService.startCommunicaton();
		return REDIRECT_TO_MAIN_PAGE;
	}

	@GetMapping("/stop")
	public String stopMessage() {
		websocketManagerService.stopCommunication();
		return REDIRECT_TO_MAIN_PAGE;
	}

	@GetMapping("/status")
	public String websocketStatus() {
		websocketManagerService.showStatus();
		return REDIRECT_TO_MAIN_PAGE;
	}

}
