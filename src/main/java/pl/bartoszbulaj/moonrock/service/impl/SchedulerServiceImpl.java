package pl.bartoszbulaj.moonrock.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.bartoszbulaj.moonrock.service.AppConfigurationService;
import pl.bartoszbulaj.moonrock.service.HistoryService;
import pl.bartoszbulaj.moonrock.service.SchedulerService;
import pl.bartoszbulaj.moonrock.service.WebsocketManagerService;

@Service
@Transactional
public class SchedulerServiceImpl implements SchedulerService {

	private HistoryService historyService;
	private AppConfigurationService appConfigurationService;
	private WebsocketManagerService websocketManagerService;

	private boolean heartbeatActive;
	private boolean emailSenderActive;

	@Autowired
	public SchedulerServiceImpl(HistoryService historyService, AppConfigurationService appConfigurationService,
                                WebsocketManagerService websocketManagerService) {
		this.historyService = historyService;
		this.appConfigurationService = appConfigurationService;
		this.websocketManagerService = websocketManagerService;
		this.heartbeatActive = false;

		appConfigurationService.setHistoryAnalyzerEnabled(true);

		if (this.appConfigurationService.isAnyEmailSender()) {
			appConfigurationService.setEmailSenderEnabled(true);
			emailSenderActive = true;
		} else {
			appConfigurationService.setEmailSenderEnabled(false);
			emailSenderActive = false;
		}
	}

	@Override
	@Scheduled(cron = "5 0 * * * *")
	public void deleteHistory() {
		if (appConfigurationService.isHistoryAnalyzerEnabled()) {
			historyService.deleteInstrumentHistory();
		}
	}

	@Override
	@Scheduled(cron = "35 0 * * * *")
	public void saveHistory() {
		if (appConfigurationService.isHistoryAnalyzerEnabled()) {
			historyService.saveInstrumentHistory();
		}
	}

	@Override
	@Scheduled(cron = "45 0 * * * *")
	public void analyzeHistory() {
		boolean isSignal = false;
		if (appConfigurationService.isHistoryAnalyzerEnabled()) {
			isSignal = historyService.analyzeInstrumentHistory();
		}
		if (emailSenderActive && isSignal) {
			historyService.sendEmailWithSignals();
		}
	}

	@Override
	@Scheduled(cron = "5 * * * * *")
	public void sendHeartbeat() {
		if (heartbeatActive) {
			websocketManagerService.pingServer();
		}
	}

	@Override
	public boolean isHeartbeatActive() {
		return this.heartbeatActive;
	}

	@Override
	public void enableHeartbeat() {
		this.heartbeatActive = true;
	}

	@Override
	public void disableHeartbeat() {
		this.heartbeatActive = false;
	}
}
