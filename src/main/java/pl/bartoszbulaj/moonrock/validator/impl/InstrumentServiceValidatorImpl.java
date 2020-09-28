package pl.bartoszbulaj.moonrock.validator.impl;

import org.springframework.stereotype.Component;

import pl.bartoszbulaj.moonrock.service.impl.InstrumentServiceImpl;
import pl.bartoszbulaj.moonrock.validator.InstrumentServiceValidator;

@Component
public class InstrumentServiceValidatorImpl implements InstrumentServiceValidator {

	@Override
	public boolean isAllArgumentsValid(String candleSize, String instrumentSymbol, String count, String reverse) {
		return isInstrumentSymbolValid(instrumentSymbol) && isCandleSizeValid(candleSize) && isReverseValid(reverse)
				&& isCountValid(count);
	}

	@Override
	public boolean isInstrumentSymbolValid(String instrumentSymbol) {
		if (instrumentSymbol == null) {
			return false;
		} else {
			return InstrumentServiceImpl.activeInstruments.contains(instrumentSymbol);
		}
	}

	private boolean isCandleSizeValid(String candleSize) {
		// TODO implement for 5m
		if (candleSize == null) {
			return false;
		} else {
			return candleSize == "1h";
		}
	}

	private boolean isReverseValid(String reverse) {
		if (reverse == null) {
			return false;
		} else {
			return (reverse == "true" || reverse == "false") ? true : false;
		}
	}

	private boolean isCountValid(String count) {
		if (count == null) {
			return false;
		} else {
			return count == "5";
		}
	}

}
