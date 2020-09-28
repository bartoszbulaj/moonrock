package pl.bartoszbulaj.moonrock.validator;

public interface InstrumentServiceValidator {

	boolean isAllArgumentsValid(String candleSize, String instrumentSymbol, String count, String reverse);

	boolean isInstrumentSymbolValid(String instrumentSymbol);
}
