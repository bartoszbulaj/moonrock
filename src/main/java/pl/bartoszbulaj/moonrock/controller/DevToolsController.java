package pl.bartoszbulaj.moonrock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.bartoszbulaj.moonrock.config.BitmexClientConfig;
import pl.bartoszbulaj.moonrock.service.WebsocketManagerService;

@RestController
@RequestMapping("/")
public class DevToolsController {

	@Autowired
	private WebsocketManagerService websocketManagerService;

	@GetMapping
	public String showOverview() {
		return "index.html";
	}

	@GetMapping("/init")
	public ResponseEntity<String> initWS() {
		List<String> instrumentList = BitmexClientConfig.getActiveInstruments();
		websocketManagerService.addAllWebsockets(instrumentList);
		websocketManagerService.connectAllWebsockets();
		return new ResponseEntity<>("Websockets created and connected", HttpStatus.OK);
	}

	@GetMapping("/start")
	public ResponseEntity<String> sendMessage() {
		websocketManagerService.startCommunicaton();
		return new ResponseEntity<>("Websockets started communication", HttpStatus.OK);
	}

	@GetMapping("/stop")
	public ResponseEntity<String> stopMessage() {
		websocketManagerService.stopCommunication();
		return new ResponseEntity<>("Websockets stoped communication", HttpStatus.OK);
	}

	@GetMapping("/status")
	public ResponseEntity<String> websocketStatus() {
		websocketManagerService.showStatus();
		return new ResponseEntity<>("Websocket status showed", HttpStatus.OK);
	}

}
