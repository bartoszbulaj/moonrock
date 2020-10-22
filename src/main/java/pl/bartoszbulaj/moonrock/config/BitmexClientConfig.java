package pl.bartoszbulaj.moonrock.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class BitmexClientConfig {

	private static final String WEBSOCKET_TEST_NET = "wss://testnet.bitmex.com/realtime";
	private static final String BITMEX_TEST_NET_BASE_URL = "https://testnet.bitmex.com/api/v1";

	private static final String WEBSOCKET_MAIN_NET = "wss://www.bitmex.com/realtime";
	private static final String BITMEX_API_URL = "https://www.bitmex.com/api/v1";

	private static final List<String> ACTIVE_INSTRUMENTS = Arrays.asList("XBTUSD", "BCHUSD", "ETHUSD", "LTCUSD",
			"XRPUSD");

	private BitmexClientConfig() {
	}

	public static String getBitmexTestNetBaseUrl() {
		return BITMEX_TEST_NET_BASE_URL;
	}

	public static String getWebsocketTestNetUrl() {
		return WEBSOCKET_TEST_NET;
	}

	public static String getBitmexApiUrl() {
		return BITMEX_API_URL;
	}

	public static String getWebsocketMainNetUrl() {
		return WEBSOCKET_MAIN_NET;
	}

	public static List<String> getActiveInstruments() {
		return ACTIVE_INSTRUMENTS;
	}
}
