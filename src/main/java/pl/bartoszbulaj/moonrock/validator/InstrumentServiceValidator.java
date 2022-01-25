package pl.bartoszbulaj.moonrock.validator;

public interface InstrumentServiceValidator {

	boolean isAllArgumentsValid(String instrumentSymbol, String candleSize, String count, String reverse);

	boolean isInstrumentSymbolValid(String instrumentSymbol);
}
