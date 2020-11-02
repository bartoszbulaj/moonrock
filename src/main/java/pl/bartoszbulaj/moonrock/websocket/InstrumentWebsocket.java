package pl.bartoszbulaj.moonrock.websocket;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.springframework.beans.factory.annotation.Autowired;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import pl.bartoszbulaj.moonrock.config.BitmexClientConfig;
import pl.bartoszbulaj.moonrock.service.EmailClient;

@ClientEndpoint
@Slf4j
@ToString
public class InstrumentWebsocket {

	private static final String URL = BitmexClientConfig.getWebsocketMainNetUrl();
	private static final int MAX_TEXT_MESSAGE_BUFFER_SIZE = 200000;
	private Session session;
	private String instrumentSymbol;
	private String subscribeString;
	private WebSocketContainer webSocketContainer;

	private static int limitRemaining;

	@Autowired
	private EmailClient emailClient;

	public InstrumentWebsocket(String instrumentSymbol, String subscribeString) {
		this.instrumentSymbol = instrumentSymbol;
		this.subscribeString = subscribeString;
		this.webSocketContainer = ContainerProvider.getWebSocketContainer();
	}

	public void connect() {
		try {
			this.webSocketContainer.connectToServer(this, new URI(URL));
		} catch (DeploymentException | IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		log.info("Connected InstrumentWebsocket: " + this.getInstrumentSymbol());
	}

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		this.session.setMaxTextMessageBufferSize(MAX_TEXT_MESSAGE_BUFFER_SIZE);
		log.info("Session opened. " + this.instrumentSymbol);
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		updateLimitRemaining(message);
	}

	private synchronized void updateLimitRemaining(String message) {
		int beginIndex = message.indexOf("\"limit\":{\"remaining\":");
		if (beginIndex != -1) {
			String remainingString = message.substring(beginIndex + 21, beginIndex + 23);
			try {
				InstrumentWebsocket.limitRemaining = Integer.valueOf(remainingString);
			} catch (NumberFormatException e) {
				InstrumentWebsocket.limitRemaining = Integer.valueOf(remainingString.substring(0, 1));
			}
		}
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		log.info("Session closed. " + this.instrumentSymbol);
		log.info("REASON: " + reason.getReasonPhrase());
		log.info("CLOSE CODE: " + reason.getCloseCode());

		if (reason.getCloseCode().equals(CloseCodes.CLOSED_ABNORMALLY)) {
			log.info("trying reconnect...");
			sendMessage();
		}
	}

	public void sendMessage() {
		if (InstrumentWebsocket.limitRemaining < 1) {
			return;
		}
		if (this.session.isOpen()) {
			getBasicRemoteAndSendText();
		} else {
			try {
				reconnect();
				log.info("Reconnected InstrumentWebsocket " + this.getInstrumentSymbol());
				getBasicRemoteAndSendText();
			} catch (DeploymentException | IOException | URISyntaxException e) {
				log.info("Cant reconnect, cant send message." + e.getCause() + " " + e.getMessage());
				e.printStackTrace();
			}
		}
		log.info("Websocket subscribed.");
	}

	private void reconnect() throws DeploymentException, IOException, URISyntaxException {
		this.webSocketContainer = ContainerProvider.getWebSocketContainer();
		this.webSocketContainer.connectToServer(this, new URI(URL));
	}

	private void getBasicRemoteAndSendText() {
		if (!StringUtils.isBlank(this.subscribeString)) {
			try {
				this.session.getBasicRemote().sendText(this.subscribeString);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void closeConnection() {
		if (this.session != null && this.session.isOpen()) {
			try {
				session.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void unsubscribe() {
		if (this.session != null && this.session.isOpen()) {
			try {
				session.getBasicRemote().sendText(this.generateUnsubscribeString(this.subscribeString));
			} catch (IOException e) {
				e.printStackTrace();
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((instrumentSymbol == null) ? 0 : instrumentSymbol.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstrumentWebsocket other = (InstrumentWebsocket) obj;
		if (instrumentSymbol == null) {
			if (other.instrumentSymbol != null)
				return false;
		} else if (!instrumentSymbol.equals(other.instrumentSymbol))
			return false;
		return true;
	}

	public void showStatus() {
		if (this.session != null) {
			log.info("--------STATUS-----------");
			log.info("instrumentSymbol: " + this.instrumentSymbol);
			log.info("Session id: " + this.session.getId());
			log.info("session isOpen: " + this.session.isOpen());
			if (this.session.isOpen()) {
				log.info("sessionContainer:   " + this.session.getContainer());
			}
			log.info("webSocketContainer: " + this.webSocketContainer);
		}
	}

	public Session getSession() {
		return session;
	}

	public WebSocketContainer getWebSocketContainer() {
		return webSocketContainer;
	}

}
