package pl.bartoszbulaj.moonrock.service.impl;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.entity.InstrumentHistoryEntity;
import pl.bartoszbulaj.moonrock.repository.InstrumentHistoryRepository;
import pl.bartoszbulaj.moonrock.service.InstrumentService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class InstrumentServiceTest {

	@Autowired
	private InstrumentService instrumentService;
	@Autowired
	private InstrumentHistoryRepository instrumentHistoryRepository;

	@Test
	public void shouldDeleteAllHistory() throws Exception {
		// given
		instrumentHistoryRepository.save(new InstrumentHistoryEntity());
		// when
		instrumentService.deleteInstrumentHistory();
		// then
		assertEquals(0, instrumentHistoryRepository.findAll().size());
	}

	@Test
	public void shouldReturnInstrumentHistoryListFromGivenArguments() {
		// given
		String candleSize = "1h";
		String instrument = "XBTUSD";
		String count = "5";
		String reverse = "false";
		int expectedListSize = 5;
		// when
		List<InstrumentHistoryDto> result = instrumentService.getInstrumentHistory(candleSize, instrument, count,
				reverse);
		// then
		assertEquals(expectedListSize, result.size());
		assertEquals(instrument, result.get(0).getSymbol());
		assertEquals(candleSize, result.get(0).getCandleSize());
	}

	@Test
	public void shouldReturnEmptyListWhenArgumentIsNull() {
		// given
		String candleSize = "1h";
		String instrument = "xbtusd";
		String count = null;
		String reverse = "false";
		int expectedListSize = 0;
		// when
		List<InstrumentHistoryDto> result = instrumentService.getInstrumentHistory(candleSize, instrument, count,
				reverse);
		// then
		assertEquals(expectedListSize, result.size());
	}

	@Test
	public void shouldReturnEmptyListWhenArgumentIsEmpty() {
		// given
		String candleSize = "1h";
		String instrument = "xbtusd";
		String count = "";
		String reverse = "false";
		int expectedListSize = 0;
		// when
		List<InstrumentHistoryDto> result = instrumentService.getInstrumentHistory(candleSize, instrument, count,
				reverse);
		// then
		assertEquals(expectedListSize, result.size());
	}

	@Test
	public void shouldReturnEmptyListWhenSymbolIsIncorrect() {
		// given
		String candleSize = "1h";
		String instrument = "112233";
		String count = "5";
		String reverse = "false";
		int expectedListSize = 0;
		// when
		List<InstrumentHistoryDto> result = instrumentService.getInstrumentHistory(candleSize, instrument, count,
				reverse);
		// then
		assertEquals(expectedListSize, result.size());
	}

	@Test
	public void shouldReturnEmptyListWhenInstrumentSymbolIsNotValid() {
		// given
		String instrument = "112233";
		int expectedListSize = 0;
		// when
		List<InstrumentHistoryDto> result = instrumentService.getInstrumentHistory(instrument);
		// then
		assertEquals(expectedListSize, result.size());
	}

	@Test
	public void shouldReturnListWith5ItemsWhenInstrumentSymbolIsValid() {
		// given
		String instrument = "ethusd";
		int expectedListSize = 5;
		// when
		List<InstrumentHistoryDto> result = instrumentService.getInstrumentHistory(instrument);
		// then
		assertEquals(expectedListSize, result.size());
		assertEquals("ETHUSD", result.get(0).getSymbol());
	}

	@Test
	public void shouldReturnEmptyListWhenInstrumentSymbolIsNull() {
		// given
		String instrument = null;
		int expectedListSize = 0;
		// when
		List<InstrumentHistoryDto> result = instrumentService.getInstrumentHistory(instrument);
		// then
		assertEquals(expectedListSize, result.size());
	}

	@Test
	public void shouldReturnTrueWhenArgumentIsNotEmpty() {
		// given
		String emailText = "email text in Junit test";
		boolean expected = true;
		// when
		boolean result = instrumentService.sendEmailWIthSignal(emailText);
		// then
		assertEquals(expected, result);
	}

	@Test
	public void shouldReturnFalseWhenArgumentIsEmpty() {
		// given
		String emailText = "";
		boolean expected = false;
		// when
		boolean result = instrumentService.sendEmailWIthSignal(emailText);
		// then
		assertEquals(expected, result);
	}

	@Test
	public void shouldReturnFalseWhenArgumentIsNull() {
		// given
		String emailText = null;
		boolean expected = false;
		// when
		boolean result = instrumentService.sendEmailWIthSignal(emailText);
		// then
		assertEquals(expected, result);
	}
}
