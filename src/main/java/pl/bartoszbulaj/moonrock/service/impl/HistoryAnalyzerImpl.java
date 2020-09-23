package pl.bartoszbulaj.moonrock.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.service.HistoryAnalyzer;

@Service
@Transactional
public class HistoryAnalyzerImpl implements HistoryAnalyzer {

	private static final Logger LOG = LogManager.getLogger(HistoryAnalyzerImpl.class);

	@Override
	public String checkForSignal(List<InstrumentHistoryDto> instrumentHistoryDtoList) {
		String signal = "";
		if (instrumentHistoryDtoList.size() > 0) {
			if (isSignalToBuy(instrumentHistoryDtoList))
				signal += "Buy";

			if (isSignalToSell(instrumentHistoryDtoList))
				signal += "Sell";
		}
		if (signal == "") {
			LOG.info("No signal at this moment...");
		}
		return signal;
	}

	private boolean isSignalToBuy(List<InstrumentHistoryDto> instrumentHistoryDtoList) {
		int listSize = instrumentHistoryDtoList.size();
		LOG.info("Searching for buy signal...");
		return !isCandleGreen(instrumentHistoryDtoList.get(listSize - 4))
				&& !isCandleGreen(instrumentHistoryDtoList.get(listSize - 3))
				&& !isCandleGreen(instrumentHistoryDtoList.get(listSize - 2))
				&& isCandleGreen(instrumentHistoryDtoList.get(listSize - 1));
	}

	private boolean isSignalToSell(List<InstrumentHistoryDto> instrumentHistoryDtoList) {
		int listSize = instrumentHistoryDtoList.size();
		LOG.info("Searching for sell signal...");
		return isCandleGreen(instrumentHistoryDtoList.get(listSize - 4))
				&& isCandleGreen(instrumentHistoryDtoList.get(listSize - 3))
				&& isCandleGreen(instrumentHistoryDtoList.get(listSize - 2))
				&& !isCandleGreen(instrumentHistoryDtoList.get(listSize - 1));
	}

	private boolean isCandleGreen(InstrumentHistoryDto instrumentHistoryDto) {
		return (instrumentHistoryDto.getClose() > instrumentHistoryDto.getOpen());
	}

}
