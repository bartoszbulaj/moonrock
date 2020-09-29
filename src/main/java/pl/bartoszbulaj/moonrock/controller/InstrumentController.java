package pl.bartoszbulaj.moonrock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.bartoszbulaj.moonrock.dto.InstrumentHistoryDto;
import pl.bartoszbulaj.moonrock.service.InstrumentService;

@RestController
@RequestMapping("/instrument")
public class InstrumentController {

	private InstrumentService instrumentService;

	@Autowired
	public InstrumentController(InstrumentService instrumentService) {
		this.instrumentService = instrumentService;
	}

	@GetMapping("/history")
	public ResponseEntity<List<InstrumentHistoryDto>> getInstrumentHistory(
			@RequestParam(defaultValue = "1h") String candleSize,
			@RequestParam(defaultValue = "xbt:perpetual") String symbol, @RequestParam(defaultValue = "5") String count,
			@RequestParam(defaultValue = "false") String reverse) {
		List<InstrumentHistoryDto> instrumentHistoryDtoList = instrumentService.getInstrumentHistory(candleSize, symbol,
				count, reverse);
		if (instrumentHistoryDtoList != null) {
			return new ResponseEntity<>(instrumentHistoryDtoList, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
