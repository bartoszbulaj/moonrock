package pl.bartoszbulaj.moonrock.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.bartoszbulaj.moonrock.service.InstrumentService;
import pl.bartoszbulaj.moonrock.service.SchedulerService;

@Service
@Transactional
public class SchedulerServiceImpl implements SchedulerService {

	private boolean historyAnalyzer;
	private InstrumentService instrumentService;

	@Autowired
	public SchedulerServiceImpl(InstrumentService instrumentService) {
		this.instrumentService = instrumentService;
		this.setHistoryAnalyzerDisabled();
	}

	@Override
	@Scheduled(cron = "5 0 * * * *")
	public void deleteHistory() {
		if (historyAnalyzer) {
			instrumentService.deleteInstrumentHistory();
		}
	}

	@Override
	@Scheduled(cron = "35 0 * * * *")
	public void saveHistory() {
		if (historyAnalyzer) {
			instrumentService.saveInstrumentHistory();
		}
	}

	@Override
	@Scheduled(cron = "45 0 * * * *")
	public void analyzeHistory() {
		if (historyAnalyzer) {
			instrumentService.analyzeInstrumentHistoryAndSendEmailWithSignals();
		}
	}

	public boolean isHistoryAnalyzerEnabled() {
		return historyAnalyzer;
	}

	public void setHistoryAnalyzerDisabled() {
		this.historyAnalyzer = false;
	}

	public void setHistoryAnalyzerEnabled() {
		this.historyAnalyzer = true;
	}
}
