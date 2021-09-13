package pl.bartoszbulaj.moonrock.service;

import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.entity.InstrumentHistoryEntity;

import java.util.List;

public interface HistoryService {

	List<InstrumentHistoryDto> getInstrumentHistory(String candleSize, String intrument, String count, String reverse);

	boolean analyzeInstrumentHistory();

	void sendEmailWithSignals();

	void deleteInstrumentHistory();

	List<InstrumentHistoryEntity> saveInstrumentHistory();

	boolean sendEmailWIthSignal(String emailText);
}
