package pl.bartoszbulaj.moonrock.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.service.HistoryAnalyzerService;

import java.util.List;

@Service
@Transactional
public class HistoryAnalyzerServiceImpl implements HistoryAnalyzerService {

	private static final Logger LOG = LogManager.getLogger(HistoryAnalyzerServiceImpl.class);

	@Override
	public String checkForSignal(List<InstrumentHistoryDto> instrumentHistoryDtoList) {
		if (instrumentHistoryDtoList == null || instrumentHistoryDtoList.isEmpty()) {
			return "";
		}
		StringBuilder signal = new StringBuilder();

		if (isSignalToBuy(instrumentHistoryDtoList)) {
			signal.append("Buy");
			logSignalInformation(signal, instrumentHistoryDtoList);
		}
		if (isSignalToSell(instrumentHistoryDtoList)) {
			signal.append("Sell");
			logSignalInformation(signal, instrumentHistoryDtoList);
		}

		return signal.toString();
	}

	private void logSignalInformation(StringBuilder signal, List<InstrumentHistoryDto> instrumentHistoryDtoList) {
		LOG.info("[Signal] {} signal to {} {}", instrumentHistoryDtoList.get(0).getCandleSize(), signal,
				instrumentHistoryDtoList.get(0).getSymbol());
	}

	private boolean isSignalToBuy(List<InstrumentHistoryDto> instrumentHistoryDtoList) {
		// int listSize = instrumentHistoryDtoList.size();
		return isCandleGreen(instrumentHistoryDtoList.get(0)) && !isCandleGreen(instrumentHistoryDtoList.get(1))
				&& !isCandleGreen(instrumentHistoryDtoList.get(2)) && !isCandleGreen(instrumentHistoryDtoList.get(3));
	}

	private boolean isSignalToSell(List<InstrumentHistoryDto> instrumentHistoryDtoList) {
		// int listSize = instrumentHistoryDtoList.size();
		return !isCandleGreen(instrumentHistoryDtoList.get(0)) && isCandleGreen(instrumentHistoryDtoList.get(1))
				&& isCandleGreen(instrumentHistoryDtoList.get(2)) && isCandleGreen(instrumentHistoryDtoList.get(3));
	}

	private boolean isCandleGreen(InstrumentHistoryDto instrumentHistoryDto) {
		return (instrumentHistoryDto.getOpen() <= instrumentHistoryDto.getClose());
	}

	private boolean isCandleGreenHammer(InstrumentHistoryDto instrumentHistoryDto) {
		return isCandleGreen(instrumentHistoryDto) && (instrumentHistoryDto.getOpen()
				- instrumentHistoryDto.getClose()) < (instrumentHistoryDto.getClose() - instrumentHistoryDto.getLow());
	}

	private boolean isCandleGreenShootingStar(InstrumentHistoryDto instrumentHistoryDto) {
		return isCandleGreen(instrumentHistoryDto) && (instrumentHistoryDto.getOpen()
				- instrumentHistoryDto.getClose()) < (instrumentHistoryDto.getHigh() - instrumentHistoryDto.getOpen());
	}
}
