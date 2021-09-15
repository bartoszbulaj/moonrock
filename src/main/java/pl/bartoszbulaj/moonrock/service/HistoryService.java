package pl.bartoszbulaj.moonrock.service;

import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.entity.InstrumentHistoryEntity;

import java.util.List;

public interface HistoryService {

	List<InstrumentHistoryDto> collectHistoryForGivenInstrument(String instrument, String candleSize, String count,
			String reverse);

	boolean analyzeInstrumentHistory();

	void sendEmailWithSignals();

	void deleteInstrumentHistory();

	List<InstrumentHistoryEntity> saveInstrumentHistory();

	boolean sendEmailWithGivenMessage(String emailText);
}
