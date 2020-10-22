package pl.bartoszbulaj.moonrock.service;

import java.util.List;

import pl.bartoszbulaj.moonrock.websocket.InstrumentWebsocket;

public interface WebsocketManagerService {

	void addWebsocket(String instrumentSymbol);

	void startCommunicaton();

	void stopCommunication();

	void connectAllWebsockets();

	List<InstrumentWebsocket> getInstrumentWebsocketList();
}
