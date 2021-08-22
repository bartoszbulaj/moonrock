package pl.bartoszbulaj.moonrock.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.instrument.util.StringUtils;
import pl.bartoszbulaj.moonrock.config.AppConfiguration;
import pl.bartoszbulaj.moonrock.config.BitmexClientConfig;
import pl.bartoszbulaj.moonrock.dto.PositionDto;
import pl.bartoszbulaj.moonrock.service.PositionManagerService;
import pl.bartoszbulaj.moonrock.service.WebsocketManagerService;

@RestController
@RequestMapping("/dev")
public class DevToolsController {

	@Autowired
	private WebsocketManagerService websocketManagerService;

	@Autowired
	private PositionManagerService positionManager;

	@Autowired
	private AppConfiguration appConfiguration;

	// TODO mark endpoints @needAuth @notAuth add flag

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

	@GetMapping("/market-close")
	public ResponseEntity<String> closePositionWithMarket(@RequestParam String owner, @RequestParam String symbol)
			throws IOException {
		if (StringUtils.isBlank(symbol) || !BitmexClientConfig.getActiveInstruments().contains(symbol.toUpperCase())) {
			throw new IllegalArgumentException();
		}

		List<PositionDto> positionListFiltered = positionManager.getPositions(owner).stream()
				.filter(p -> p.getSymbol().equalsIgnoreCase(symbol)).collect(Collectors.toList());
		if (!positionListFiltered.isEmpty()) {
			String result = positionManager.closePositionWithMarketOrder(positionListFiltered.get(0), owner);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("There is no open position with given symbol: " + symbol,
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/runall")
	public ResponseEntity<String> runAllWebsockets() {
		String initResponse = initWS().getBody();
		String sendMessage = sendMessage().getBody();
		appConfiguration.setHistoryAnalyzerEnabled(true);
		return new ResponseEntity<>(initResponse + " + " + sendMessage, HttpStatus.OK);
	}

	@GetMapping("/showflags")
	public ResponseEntity<String> showFlags() {
		String emailSender = appConfiguration.isEmailSenderEnabled() ? "Email Sender OK" : "Email Sender WRONG";
		String historyAnalyzer = appConfiguration.isHistoryAnalyzerEnabled()
				? "History Analyzer Enabled"
				: "History Analyzer Disabled";
		return new ResponseEntity<>(emailSender + " + " + historyAnalyzer, HttpStatus.OK);
	}

}
