package pl.bartoszbulaj.moonrock.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.service.ConnectionService;
import pl.bartoszbulaj.moonrock.service.HistoryService;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class HistoryServiceTest {

	@Autowired
	private HistoryService historyService;

	@MockBean
	private ConnectionService connectionService;

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIAEFromGivenArguments() {
		// given
		String candleSize = "1h";
		String instrument = "XBTUSD";
		String count = "5";
		String reverse = "false";
		int expectedListSize = 5;
		// when
		List<InstrumentHistoryDto> result = historyService.collectHistoryForGivenInstrument(candleSize, instrument,
				count, reverse);

	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIAEWhenArgumentIsNull() {
		// given
		String candleSize = "1h";
		String instrument = "xbtusd";
		String count = null;
		String reverse = "false";
		int expectedListSize = 0;
		// when
		List<InstrumentHistoryDto> result = historyService.collectHistoryForGivenInstrument(candleSize, instrument,
				count, reverse);

	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIAEWhenCountArgumentIsEmpty() {
		// given
		String candleSize = "1h";
		String instrument = "xbtusd";
		String count = "";
		String reverse = "false";
		int expectedListSize = 0;
		// when
		List<InstrumentHistoryDto> result = historyService.collectHistoryForGivenInstrument(candleSize, instrument,
				count, reverse);

	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldReturnEmptyListWhenSymbolIsIncorrect() {
		// given
		String candleSize = "1h";
		String instrument = "112233";
		String count = "5";
		String reverse = "false";
		int expectedListSize = 0;
		// when
		List<InstrumentHistoryDto> result = historyService.collectHistoryForGivenInstrument(candleSize, instrument,
				count, reverse);

	}

	@Test
	public void shouldReturnFalseWhenArgumentIsEmpty() {
		// given
		String emailText = "";
		boolean expected = false;
		// when
		boolean result = historyService.sendEmailWithGivenMessage(emailText);
		// then
		assertEquals(expected, result);
	}

	@Test
	public void shouldReturnFalseWhenArgumentIsNull() {
		// given
		String emailText = null;
		boolean expected = false;
		// when
		boolean result = historyService.sendEmailWithGivenMessage(emailText);
		// then
		assertEquals(expected, result);
	}

	@Test
	public void shouldMergeCandlesFromM5ToM15() {
		// given
		Mockito.when(connectionService.getResultFromHttpRequest(Mockito.any())).thenReturn(
				"[{\"timestamp\":\"2021-09-15T12:00:00.000Z\",\"symbol\":\"XBTUSD\",\"open\":47477,\"high\":47542.5,\"low\":47476.5,\"close\":47542.5,\"trades\":392,\"volume\":4054500,\"vwap\":47505.7126,\"lastSize\":100,\"turnover\":8534793273,\"homeNotional\":85.34793273000003,\"foreignNotional\":4054500},{\"timestamp\":\"2021-09-15T11:55:00.000Z\",\"symbol\":\"XBTUSD\",\"open\":47440,\"high\":47478.5,\"low\":47440,\"close\":47477,\"trades\":224,\"volume\":599300,\"vwap\":47456.5654,\"lastSize\":100,\"turnover\":1262839437,\"homeNotional\":12.62839437,\"foreignNotional\":599300},{\"timestamp\":\"2021-09-15T11:50:00.000Z\",\"symbol\":\"XBTUSD\",\"open\":47384.5,\"high\":47465,\"low\":47384,\"close\":47440,\"trades\":326,\"volume\":1381600,\"vwap\":47424.8316,\"lastSize\":1500,\"turnover\":2913245995,\"homeNotional\":29.132459949999994,\"foreignNotional\":1381600},{\"timestamp\":\"2021-09-15T11:45:00.000Z\",\"symbol\":\"XBTUSD\",\"open\":47401.5,\"high\":47401.5,\"low\":47340,\"close\":47384.5,\"trades\":556,\"volume\":2445500,\"vwap\":47367.5485,\"lastSize\":7500,\"turnover\":5162831483,\"homeNotional\":51.628314829999994,\"foreignNotional\":2445500},{\"timestamp\":\"2021-09-15T11:40:00.000Z\",\"symbol\":\"XBTUSD\",\"open\":47403,\"high\":47416,\"low\":47324,\"close\":47401.5,\"trades\":541,\"volume\":2649000,\"vwap\":47365.0806,\"lastSize\":1300,\"turnover\":5592743043,\"homeNotional\":55.92743042999999,\"foreignNotional\":2649000},{\"timestamp\":\"2021-09-15T11:35:00.000Z\",\"symbol\":\"XBTUSD\",\"open\":47505.5,\"high\":47497.5,\"low\":47327,\"close\":47403,\"trades\":694,\"volume\":3523400,\"vwap\":47419.2095,\"lastSize\":10000,\"turnover\":7430328723,\"homeNotional\":74.30328723,\"foreignNotional\":3523400},{\"timestamp\":\"2021-09-15T11:30:00.000Z\",\"symbol\":\"XBTUSD\",\"open\":47518,\"high\":47549,\"low\":47460.5,\"close\":47505.5,\"trades\":531,\"volume\":2177400,\"vwap\":47498.0407,\"lastSize\":100,\"turnover\":4584201320,\"homeNotional\":45.84201320000002,\"foreignNotional\":2177400},{\"timestamp\":\"2021-09-15T11:25:00.000Z\",\"symbol\":\"XBTUSD\",\"open\":47575,\"high\":47633,\"low\":47379,\"close\":47518,\"trades\":1094,\"volume\":8013200,\"vwap\":47496.6871,\"lastSize\":1300,\"turnover\":16871073856,\"homeNotional\":168.71073856000004,\"foreignNotional\":8013200},{\"timestamp\":\"2021-09-15T11:20:00.000Z\",\"symbol\":\"XBTUSD\",\"open\":47589,\"high\":47597.5,\"low\":47544,\"close\":47575,\"trades\":293,\"volume\":2029100,\"vwap\":47565.593,\"lastSize\":23500,\"turnover\":4265905252,\"homeNotional\":42.65905252,\"foreignNotional\":2029100},{\"timestamp\":\"2021-09-15T11:15:00.000Z\",\"symbol\":\"XBTUSD\",\"open\":47526,\"high\":47589,\"low\":47474.5,\"close\":47589,\"trades\":410,\"volume\":3253500,\"vwap\":47530.5503,\"lastSize\":1300,\"turnover\":6845096927,\"homeNotional\":68.45096927,\"foreignNotional\":3253500},{\"timestamp\":\"2021-09-15T11:10:00.000Z\",\"symbol\":\"XBTUSD\",\"open\":47503.5,\"high\":47541,\"low\":47495.5,\"close\":47526,\"trades\":202,\"volume\":1664800,\"vwap\":47521.967,\"lastSize\":200,\"turnover\":3503232942,\"homeNotional\":35.03232941999999,\"foreignNotional\":1664800},{\"timestamp\":\"2021-09-15T11:05:00.000Z\",\"symbol\":\"XBTUSD\",\"open\":47592.5,\"high\":47605.5,\"low\":47483,\"close\":47503.5,\"trades\":577,\"volume\":2471600,\"vwap\":47537.5547,\"lastSize\":200,\"turnover\":5199269461,\"homeNotional\":51.992694609999994,\"foreignNotional\":2471600},{\"timestamp\":\"2021-09-15T11:00:00.000Z\",\"symbol\":\"XBTUSD\",\"open\":47615,\"high\":47643.5,\"low\":47558,\"close\":47592.5,\"trades\":551,\"volume\":5117500,\"vwap\":47598.4216,\"lastSize\":1000,\"turnover\":10751440775,\"homeNotional\":107.51440775,\"foreignNotional\":5117500},{\"timestamp\":\"2021-09-15T10:55:00.000Z\",\"symbol\":\"XBTUSD\",\"open\":47565.5,\"high\":47625,\"low\":47543,\"close\":47615,\"trades\":426,\"volume\":2877800,\"vwap\":47592.5318,\"lastSize\":800,\"turnover\":6046760081,\"homeNotional\":60.46760081,\"foreignNotional\":2877800},{\"timestamp\":\"2021-09-15T10:50:00.000Z\",\"symbol\":\"XBTUSD\",\"open\":47579.5,\"high\":47588,\"low\":47531,\"close\":47565.5,\"trades\":397,\"volume\":2787000,\"vwap\":47567.1767,\"lastSize\":100,\"turnover\":5859102782,\"homeNotional\":58.59102782000001,\"foreignNotional\":2787000}]");

		// when
		List<InstrumentHistoryDto> instrumentHistoryDtoList = historyService.collectHistoryForGivenInstrument("XBTUSD",
				"15m", "5", "true");

		// then
		assertEquals(5, instrumentHistoryDtoList.size());
	}
}
