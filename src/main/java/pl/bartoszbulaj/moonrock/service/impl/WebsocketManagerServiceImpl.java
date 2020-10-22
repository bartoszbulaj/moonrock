package pl.bartoszbulaj.moonrock.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.DeploymentException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.bartoszbulaj.moonrock.service.WebsocketManagerService;
import pl.bartoszbulaj.moonrock.websocket.InstrumentWebsocket;

@Service
@Transactional
public class WebsocketManagerServiceImpl implements WebsocketManagerService {

	private List<InstrumentWebsocket> websocketList;

	public WebsocketManagerServiceImpl() {
		this.websocketList = new ArrayList<>();
	}

	@Override
	public void addWebsocket(String instrumentSymbol) {
		String subscribeString = "{\"op\": \"subscribe\", \"args\": [\"trade:" + instrumentSymbol + "\"]}";
		websocketList.add(new InstrumentWebsocket(instrumentSymbol, subscribeString));
	}

	@Override
	public void connectAllWebsockets() {
		for (InstrumentWebsocket instrumentWebsocket : websocketList) {
			try {
				instrumentWebsocket.connect();
			} catch (DeploymentException | IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void startCommunicaton() {
		for (InstrumentWebsocket instrumentWebsocket : websocketList) {
			instrumentWebsocket.sendMessage();
		}
	}

	@Override
	public void stopCommunication() {
		for (InstrumentWebsocket instrumentWebsocket : websocketList) {
			instrumentWebsocket.closeConnection();
		}
	}

	@Override
	public List<InstrumentWebsocket> getInstrumentWebsocketList() {
		return websocketList;
	}
}
