package pl.bartoszbulaj.moonrock.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.bartoszbulaj.moonrock.service.SchedulerService;
import pl.bartoszbulaj.moonrock.service.WebsocketManagerService;
import pl.bartoszbulaj.moonrock.websocket.InstrumentWebsocket;

@Service
@Transactional
public class WebsocketManagerServiceImpl implements WebsocketManagerService {

	private Set<InstrumentWebsocket> websocketSet;
	private ApplicationContext context;

	@Autowired
	public WebsocketManagerServiceImpl(ApplicationContext context) {
		this.context = context;
		this.websocketSet = new HashSet<>();
	}

	@Override
	public void addAllWebsockets(List<String> instrumentList) {
		this.websocketSet.clear();
		for (String instrumentSymbol : instrumentList) {
			String subscribeString = "{\"op\": \"subscribe\", \"args\": [\"trade:" + instrumentSymbol + "\"]}";
			this.websocketSet.add(new InstrumentWebsocket(instrumentSymbol, subscribeString));
		}
	}

	@Override
	public void connectAllWebsockets() {
		for (InstrumentWebsocket instrumentWebsocket : this.websocketSet) {
			if (instrumentWebsocket.getSession() == null) {
				instrumentWebsocket.connect();

				SchedulerService scheduler = context.getBean(SchedulerService.class);
				scheduler.enableHeartbeat();
			}
		}
	}

	@Override
	public void startCommunicaton() {
		for (InstrumentWebsocket instrumentWebsocket : this.websocketSet) {
			instrumentWebsocket.sendMessage();
		}
	}

	@Override
	public void stopCommunication() {
		for (InstrumentWebsocket instrumentWebsocket : this.websocketSet) {
			instrumentWebsocket.closeConnection();

			SchedulerService scheduler = context.getBean(SchedulerService.class);
			scheduler.disableHeartbeat();
		}
	}

	@Override
	public Set<InstrumentWebsocket> getInstrumentWebsocketSet() {
		return this.websocketSet;
	}

	@Override
	public void showStatus() {
		for (InstrumentWebsocket instrumentWebsocket : this.websocketSet) {
			instrumentWebsocket.showStatus();
		}
	}

	@Override
	public void pingServer() {
		for (InstrumentWebsocket instrumentWebsocket : this.websocketSet) {
			instrumentWebsocket.sendPing();
		}

	}

}
