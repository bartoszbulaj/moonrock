package pl.bartoszbulaj.moonrock.service;

import java.util.List;

import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.entity.InstrumentHistoryEntity;

public interface InstrumentService {

	List<InstrumentHistoryDto> getInstrumentHistory(String candleSize, String intrument, String count, String reverse);

	boolean analyzeInstrumentHistoryAndSendEmailWithSignals();

	void deleteInstrumentHistory();

	List<InstrumentHistoryEntity> saveInstrumentHistory();

	List<InstrumentHistoryDto> getInstrumentHistory(String instrument);

	boolean sendEmailWIthSignal(String emailText);

	void sendTestEmail();

}
