package pl.bartoszbulaj.moonrock.service.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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

}
