package pl.bartoszbulaj.moonrock.entity;

import java.io.IOException;
import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import pl.bartoszbulaj.moonrock.config.BitmexClientConfig;

@Component
@ClientEndpoint
@Slf4j
public class OverviewWebsocket {

	private Session session;
	private static boolean consolePrinter = false;

	public OverviewWebsocket() {
		String URL = BitmexClientConfig.getWebsocketMainNetUrl();
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, new URI(URL));
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
	}

	@OnMessage
	public void onMessage(String message) {
		if (consolePrinter)
			log.warn(message);
	}

	public void sendMessage(String message) {
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException ex) {
			log.error(ex.getMessage());
		}
	}

	public void setOnConsolePrinter() {
		consolePrinter = true;
	}

	public void setOffConsolePrinter() {
		consolePrinter = false;
	}
}
