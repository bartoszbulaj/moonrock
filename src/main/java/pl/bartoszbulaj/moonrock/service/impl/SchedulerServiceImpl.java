package pl.bartoszbulaj.moonrock.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.bartoszbulaj.moonrock.service.EmailSenderService;
import pl.bartoszbulaj.moonrock.service.InstrumentService;
import pl.bartoszbulaj.moonrock.service.SchedulerService;
import pl.bartoszbulaj.moonrock.service.WebsocketManagerService;

@Service
@Transactional
public class SchedulerServiceImpl implements SchedulerService {

	private boolean historyAnalyzerStatus;
	private boolean heartbeatStatus;
	private InstrumentService instrumentService;
	private EmailSenderService emailSenderService;
	private WebsocketManagerService websocketManagerService;

	@Autowired
	public SchedulerServiceImpl(InstrumentService instrumentService, EmailSenderService emailSenderService,
			WebsocketManagerService websocketManagerService) {
		this.instrumentService = instrumentService;
		this.emailSenderService = emailSenderService;
		this.websocketManagerService = websocketManagerService;
		this.heartbeatStatus = false;

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

	@Override
	public boolean isHistoryAnalyzerEnabled() {
		return historyAnalyzerStatus;
	}

	@Override
	public void setHistoryAnalyzerDisabled() {
		this.historyAnalyzerStatus = false;
	}

	@Override
	@Scheduled(cron = "5 * * * * *")
	public void sendHeartbeat() {
		if (heartbeatStatus) {
			websocketManagerService.pingServer();
		}
	}

	@Override
	public void setHistoryAnalyzerEnabled() {
		this.historyAnalyzerStatus = true;
	}

	@Override
	public boolean isHeartbeatEnabled() {
		return this.heartbeatStatus;
	}

	@Override
	public void enableHeartbeat() {
		this.heartbeatStatus = true;
	}

	@Override
	public void disableHeartbeat() {
		this.heartbeatStatus = false;
	}
}
