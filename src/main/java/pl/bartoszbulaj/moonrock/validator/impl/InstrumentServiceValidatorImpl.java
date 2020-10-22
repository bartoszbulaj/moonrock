package pl.bartoszbulaj.moonrock.validator.impl;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.util.StringUtils;
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
		if (StringUtils.isBlank(instrumentSymbol)) {
			return false;
		} else {
			return BitmexClientConfig.getActiveInstruments().contains(instrumentSymbol.toUpperCase());
		}
	}

	private boolean isCandleSizeValid(String candleSize) {
		if (StringUtils.isBlank(candleSize)) {
			return false;
		} else {
			return candleSize.equalsIgnoreCase("1h") || candleSize.equalsIgnoreCase("5m");
		}
	}

	private boolean isReverseStringValid(String reverse) {
		if (StringUtils.isBlank(reverse)) {
			return false;
		}
		return (reverse.equalsIgnoreCase("true") || reverse.equalsIgnoreCase("false"));
	}

	private boolean isCountValid(String count) {
		if (StringUtils.isBlank(count)) {
			return false;
		} else {
			return count.equalsIgnoreCase("5");
		}
	}

}
