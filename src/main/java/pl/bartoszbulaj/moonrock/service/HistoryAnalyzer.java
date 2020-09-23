package pl.bartoszbulaj.moonrock.service;

import java.util.List;

import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;

public interface HistoryAnalyzer {

	String checkForSignal(List<InstrumentHistoryDto> instrumentHistoryDtoList);

}
