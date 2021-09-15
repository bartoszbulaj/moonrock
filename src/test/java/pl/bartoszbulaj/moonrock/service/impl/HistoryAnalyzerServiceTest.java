package pl.bartoszbulaj.moonrock.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.service.HistoryAnalyzerService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class HistoryAnalyzerServiceTest {

	@Autowired
	private HistoryAnalyzerService historyAnalyzerService;

	private List<InstrumentHistoryDto> instrumentHistoryDtoList = new ArrayList<>();
	private InstrumentHistoryDto instrumentHistoryDto1;
	private InstrumentHistoryDto instrumentHistoryDto2;
	private InstrumentHistoryDto instrumentHistoryDto3;
	private InstrumentHistoryDto instrumentHistoryDto4;
	private InstrumentHistoryDto instrumentHistoryDto5;

	@Test
	public void shouldReturnSignalForSell() {
		// given
		instrumentHistoryDto1 = new InstrumentHistoryDto(1L, "", "XBT", 90D, 12D, 8D, 100D, 1D, 1D, 1D, 1D, 1D, 1D, 1D,
				"1h");
		instrumentHistoryDto2 = new InstrumentHistoryDto(2L, "", "XBT", 100D, 120D, 50D, 110D, 1D, 1D, 1D, 1D, 1D, 1D,
				1D, "1h");
		instrumentHistoryDto3 = new InstrumentHistoryDto(3L, "", "XBT", 110D, 130D, 60D, 120D, 1D, 1D, 1D, 1D, 1D, 1D,
				1D, "1h");
		instrumentHistoryDto4 = new InstrumentHistoryDto(4L, "", "XBT", 120D, 140D, 70D, 130D, 1D, 1D, 1D, 1D, 1D, 1D,
				1D, "1h");
		instrumentHistoryDto5 = new InstrumentHistoryDto(5L, "", "XBT", 130D, 150D, 30D, 50D, 1D, 1D, 1D, 1D, 1D, 1D,
				1D, "1h");

		instrumentHistoryDtoList.add(instrumentHistoryDto5);
		instrumentHistoryDtoList.add(instrumentHistoryDto4);
		instrumentHistoryDtoList.add(instrumentHistoryDto3);
		instrumentHistoryDtoList.add(instrumentHistoryDto2);
		instrumentHistoryDtoList.add(instrumentHistoryDto1);

		// when
		String result = historyAnalyzerService.checkForSignal(instrumentHistoryDtoList);
		// then
		assertEquals("Sell", result);
	}

	@Test
	public void shouldReturnSignalForBuy() {
		// given
		instrumentHistoryDto1 = new InstrumentHistoryDto(1L, "", "XBT", 10D, 12D, 8D, 5D, 1D, 1D, 1D, 1D, 1D, 1D, 1D,
				"1h");
		instrumentHistoryDto2 = new InstrumentHistoryDto(2L, "", "XBT", 150D, 120D, 50D, 140D, 1D, 1D, 1D, 1D, 1D, 1D,
				1D, "1h");
		instrumentHistoryDto3 = new InstrumentHistoryDto(3L, "", "XBT", 140D, 130D, 60D, 130D, 1D, 1D, 1D, 1D, 1D, 1D,
				1D, "1h");
		instrumentHistoryDto4 = new InstrumentHistoryDto(4L, "", "XBT", 130D, 140D, 70D, 120D, 1D, 1D, 1D, 1D, 1D, 1D,
				1D, "1h");
		instrumentHistoryDto5 = new InstrumentHistoryDto(5L, "", "XBT", 120D, 160D, 30D, 150D, 1D, 1D, 1D, 1D, 1D, 1D,
				1D, "1h");
		instrumentHistoryDtoList.add(instrumentHistoryDto5);
		instrumentHistoryDtoList.add(instrumentHistoryDto4);
		instrumentHistoryDtoList.add(instrumentHistoryDto3);
		instrumentHistoryDtoList.add(instrumentHistoryDto2);
		instrumentHistoryDtoList.add(instrumentHistoryDto1);
		// when
		String result = historyAnalyzerService.checkForSignal(instrumentHistoryDtoList);
		// then
		assertEquals("Buy", result);
	}

	@Test
	public void shouldReturnNoSignalWhenCandlesAreGreenRedGreenRed() {
		// given
		instrumentHistoryDto1 = new InstrumentHistoryDto(1L, "", "XBT", 10D, 12D, 8D, 5D, 1D, 1D, 1D, 1D, 1D, 1D, 1D,
				"1h");
		instrumentHistoryDto2 = new InstrumentHistoryDto(2L, "", "XBT", 130D, 120D, 50D, 140D, 1D, 1D, 1D, 1D, 1D, 1D,
				1D, "1h");
		instrumentHistoryDto3 = new InstrumentHistoryDto(3L, "", "XBT", 140D, 130D, 60D, 130D, 1D, 1D, 1D, 1D, 1D, 1D,
				1D, "1h");
		instrumentHistoryDto4 = new InstrumentHistoryDto(4L, "", "XBT", 130D, 140D, 70D, 140D, 1D, 1D, 1D, 1D, 1D, 1D,
				1D, "1h");
		instrumentHistoryDto5 = new InstrumentHistoryDto(5L, "", "XBT", 140D, 160D, 30D, 130D, 1D, 1D, 1D, 1D, 1D, 1D,
				1D, "1h");
		instrumentHistoryDtoList.add(instrumentHistoryDto1);
		instrumentHistoryDtoList.add(instrumentHistoryDto2);
		instrumentHistoryDtoList.add(instrumentHistoryDto3);
		instrumentHistoryDtoList.add(instrumentHistoryDto4);
		instrumentHistoryDtoList.add(instrumentHistoryDto5);
		// when
		String result = historyAnalyzerService.checkForSignal(instrumentHistoryDtoList);
		// then
		assertEquals("", result);
	}

	@Test
	public void shouldReturnNoSignalWhenArrayIsEmpty() {
		// given
		List<InstrumentHistoryDto> emptyList = new ArrayList<>();
		// when
		String result = historyAnalyzerService.checkForSignal(emptyList);
		// then
		assertEquals("", result);
	}

	@Test
	public void shouldReturnNoSignalWhenArgumentIsNull() {
		// when
		String result = historyAnalyzerService.checkForSignal(null);
		// then
		assertEquals("", result);
	}
}
