package pl.bartoszbulaj.moonrock.validator.impl;

import org.springframework.stereotype.Component;

import pl.bartoszbulaj.moonrock.config.BitmexClientConfig;
import pl.bartoszbulaj.moonrock.validator.InstrumentServiceValidator;

@Component
public class InstrumentServiceValidatorImpl implements InstrumentServiceValidator {

	@Override
	public boolean isAllArgumentsValid(String candleSize, String instrumentSymbol, String count, String reverse) {
		return isInstrumentSymbolValid(instrumentSymbol) && isCandleSizeValid(candleSize)
				&& isReverseStringValid(reverse) && isCountValid(count);
	}

	@Override
	public boolean isInstrumentSymbolValid(String instrumentSymbol) {
		if (BitmexClientConfig.getActiveInstruments().contains(instrumentSymbol.toUpperCase())) {
			return true;
		}
		throw new IllegalArgumentException(instrumentSymbol);
	}

	private boolean isCandleSizeValid(String candleSize) {
		if (candleSize.equalsIgnoreCase("1h") || candleSize.equalsIgnoreCase("5m")
				|| candleSize.equalsIgnoreCase("15m")) {
			return true;
		}
		throw new IllegalArgumentException(candleSize);
	}

	private boolean isReverseStringValid(String reverse) {
		if (reverse.equalsIgnoreCase("true") || reverse.equalsIgnoreCase("false")) {
			return true;
		}
		throw new IllegalArgumentException(reverse);
	}

	private boolean isCountValid(String count) {
		if (count.equalsIgnoreCase("5")) {
			return true;
		}
		throw new IllegalArgumentException(count);
	}

}
