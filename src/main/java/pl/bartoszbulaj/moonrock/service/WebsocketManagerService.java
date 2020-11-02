package pl.bartoszbulaj.moonrock.service;

import java.util.List;
import java.util.Set;

import pl.bartoszbulaj.moonrock.websocket.InstrumentWebsocket;

public interface WebsocketManagerService {

	void addAllWebsockets(List<String> instrumentList);

	void startCommunicaton();

	void stopCommunication();

	void connectAllWebsockets();

	Set<InstrumentWebsocket> getInstrumentWebsocketSet();

	void showStatus();
}
