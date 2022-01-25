package pl.bartoszbulaj.moonrock.service;

import java.util.List;

import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.entity.InstrumentHistoryEntity;
import pl.bartoszbulaj.moonrock.simulator.model.Candle;
import pl.bartoszbulaj.moonrock.simulator.model.CandleOHLC;

public interface HistoryService {

	List<InstrumentHistoryDto> collectHistoryForGivenInstrument(String instrument, String candleSize, String count,
			String reverse);

	List<Candle> collectCandleHistoryForGivenInstrument(String instrument, String candleSize, String count,
			String reverse);

	List<CandleOHLC> collectCandleOHLCHistoryForGivenInstrument(String instrument, String candleSize, String count,
			String reverse);

	boolean analyzeInstrumentHistory();

	void sendEmailWithSignals();

	void deleteInstrumentHistory();

	List<InstrumentHistoryEntity> saveInstrumentHistory();

	boolean sendEmailWithGivenMessage(String emailText);
}
