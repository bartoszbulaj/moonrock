package pl.bartoszbulaj.moonrock.service;

import java.util.List;

import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.entity.InstrumentHistoryEntity;

public interface InstrumentService {

	List<InstrumentHistoryDto> getInstrumentHistory(String candleSize, String symbol, String count, String reverse);

	void analyzeInstrumentHistory();

	void deleteInstrumentHistory();

	List<InstrumentHistoryEntity> saveInstrumentHistory();

}
