package pl.bartoszbulaj.moonrock.service;

import java.io.IOException;

import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;

public interface EmailClient {

	void sendEmail(InstrumentHistoryDto instrumentHistoryDto, String signalDirection) throws IOException;
}
