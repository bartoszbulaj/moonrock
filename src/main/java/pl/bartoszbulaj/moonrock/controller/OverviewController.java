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

	@Autowired
	private WebsocketManagerService websocketManagerService;

	@GetMapping
	public String showOverview() {
		return "index.html";
	}

	@GetMapping("/init")
	public String initWS() {
		List<String> instrumentList = BitmexClientConfig.getActiveInstruments();
//		for (String instrumentSymbol : instrumentList) {
//			websocketManagerService.addWebsocket(instrumentSymbol);
//		}
		websocketManagerService.addWebsocket("XBTUSD");
		websocketManagerService.connectAllWebsockets();
		return "redirect:/";
	}

	@GetMapping("/start")
	public String sendMessage() {
		websocketManagerService.startCommunicaton();
		return "redirect:/";
	}

	@GetMapping("/stop")
	public String stopMessage() {
		websocketManagerService.stopCommunication();
		return "redirect:/";
	}

}
