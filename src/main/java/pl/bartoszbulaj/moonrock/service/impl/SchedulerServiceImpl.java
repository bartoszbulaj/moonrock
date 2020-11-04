package pl.bartoszbulaj.moonrock.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.bartoszbulaj.moonrock.service.EmailSenderService;
import pl.bartoszbulaj.moonrock.service.InstrumentService;
import pl.bartoszbulaj.moonrock.service.SchedulerService;

@Service
@Transactional
public class SchedulerServiceImpl implements SchedulerService {

	private boolean historyAnalyzerStatus;
	private InstrumentService instrumentService;
	private EmailSenderService emailSenderService;

	@Autowired
	public SchedulerServiceImpl(InstrumentService instrumentService, EmailSenderService emailSenderService) {
		this.instrumentService = instrumentService;
		this.emailSenderService = emailSenderService;

		if (this.emailSenderService.isAnyEmailSender()) {
			setHistoryAnalyzerEnabled();
		} else {
			setHistoryAnalyzerDisabled();
		}
	}

	@Override
	@Scheduled(cron = "5 0 * * * *")
	public void deleteHistory() {
		if (historyAnalyzerStatus) {
			instrumentService.deleteInstrumentHistory();
		}
	}

	@Override
	@Scheduled(cron = "35 0 * * * *")
	public void saveHistory() {
		if (historyAnalyzerStatus) {
			instrumentService.saveInstrumentHistory();
		}
	}

	@Override
	@Scheduled(cron = "45 0 * * * *")
	public void analyzeHistory() {
		if (historyAnalyzerStatus) {
			instrumentService.analyzeInstrumentHistoryAndSendEmailWithSignals();
		}
	}

	public boolean isHistoryAnalyzerEnabled() {
		return historyAnalyzerStatus;
	}

	public void setHistoryAnalyzerDisabled() {
		this.historyAnalyzerStatus = false;
	}

	public void setHistoryAnalyzerEnabled() {
		this.historyAnalyzerStatus = true;
	}
}
