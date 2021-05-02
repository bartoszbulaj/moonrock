package pl.bartoszbulaj.moonrock.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.service.HistoryAnalyzerService;

@Service
@Transactional
public class HistoryAnalyzerServiceImpl implements HistoryAnalyzerService {

	private static final Logger LOG = LogManager.getLogger(HistoryAnalyzerServiceImpl.class);

	@Override
	public String checkForSignal(List<InstrumentHistoryDto> instrumentHistoryDtoList) {
		if (instrumentHistoryDtoList == null) {
			return "";
		}
		String signal = "";
		if (!instrumentHistoryDtoList.isEmpty()) {
			LOG.info("[Analyzer] Searching for {} signal.", instrumentHistoryDtoList.get(0).getSymbol());
			if (isSignalToBuy(instrumentHistoryDtoList)) {
				signal += "Buy";
				LOG.info("[Signal] {} signal to {} {}", instrumentHistoryDtoList.get(0).getCandleSize(), signal,
						instrumentHistoryDtoList.get(0).getSymbol());
			}
			if (isSignalToSell(instrumentHistoryDtoList)) {
				signal += "Sell";
				LOG.info("[Signal] {} signal to {} {}", instrumentHistoryDtoList.get(0).getCandleSize(), signal,
						instrumentHistoryDtoList.get(0).getSymbol());
			}
		}
		return signal;
	}

	private boolean isSignalToBuy(List<InstrumentHistoryDto> instrumentHistoryDtoList) {
		int listSize = instrumentHistoryDtoList.size();
		return !isCandleGreen(instrumentHistoryDtoList.get(listSize - 4))
				&& !isCandleGreen(instrumentHistoryDtoList.get(listSize - 3))
				&& !isCandleGreen(instrumentHistoryDtoList.get(listSize - 2))
				&& isCandleGreen(instrumentHistoryDtoList.get(listSize - 1));
	}

	private boolean isSignalToSell(List<InstrumentHistoryDto> instrumentHistoryDtoList) {
		int listSize = instrumentHistoryDtoList.size();
		return isCandleGreen(instrumentHistoryDtoList.get(listSize - 4))
				&& isCandleGreen(instrumentHistoryDtoList.get(listSize - 3))
				&& isCandleGreen(instrumentHistoryDtoList.get(listSize - 2))
				&& !isCandleGreen(instrumentHistoryDtoList.get(listSize - 1));
	}

	private boolean isCandleGreen(InstrumentHistoryDto instrumentHistoryDto) {
		return (instrumentHistoryDto.getOpen() <= instrumentHistoryDto.getClose());
	}

}
