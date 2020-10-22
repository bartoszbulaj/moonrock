package pl.bartoszbulaj.moonrock.websocket;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import pl.bartoszbulaj.moonrock.config.BitmexClientConfig;

@ClientEndpoint
@Slf4j
@ToString
public class InstrumentWebsocket {

	private static final String URL = BitmexClientConfig.getWebsocketMainNetUrl();
	private Session session;
	private String instrumentSymbol;
	private String subscribeString;
	private WebSocketContainer webSocketContainer;

	public InstrumentWebsocket(String instrumentSymbol, String subscribeString) {
		this.instrumentSymbol = instrumentSymbol;
		this.subscribeString = subscribeString;
	}

	public void connect() throws DeploymentException, IOException, URISyntaxException {
		this.webSocketContainer = ContainerProvider.getWebSocketContainer();
		webSocketContainer.connectToServer(this, new URI(URL));
		log.info("Connected");
	}

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		log.info("Session opened.");
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		log.info(message);
		if (this.session != null && this.session.isOpen()) {

		}
	}

	@OnClose
	public void onClose(Session session) {
		log.info("Session closed.");
	}

	public void sendMessage() {
		if (this.session.isOpen()) {
			getBasicRemoteAndSendText();
		} else {
			reconnect();
			getBasicRemoteAndSendText();
		}
	}

	private void reconnect() {
		try {
			this.webSocketContainer = ContainerProvider.getWebSocketContainer();
			this.webSocketContainer.connectToServer(this, new URI(URL));
			log.info("Connected again");
		} catch (DeploymentException | IOException | URISyntaxException e) {
			e.printStackTrace();
			log.info("Cant connect again, cant send message - session is closed.");
		}
	}

	private void getBasicRemoteAndSendText() {
		try {
			this.session.getBasicRemote().sendText(this.subscribeString);
		} catch (IOException ex) {
			log.error(ex.getMessage());
		}
	}

	public void closeConnection() {
		if (this.session != null && this.session.isOpen()) {
			try {
				session.getBasicRemote().sendText(this.generateUnsubscribeString(this.subscribeString));
				session.close();
			} catch (IOException ex) {
				log.error(ex.getMessage());
			}
		}

	}

	public void setSubscribeString(String subscribeString) {
		this.subscribeString = subscribeString;
	}

	public String getInstrumentSymbol() {
		return instrumentSymbol;
	}

	private String generateUnsubscribeString(String subscriptionString) {
		return subscriptionString.replace("subscribe", "unsubscribe");
	}

}
