package pl.bartoszbulaj.moonrock.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.bartoszbulaj.moonrock.service.AppConfigurationService;
import pl.bartoszbulaj.moonrock.service.InstrumentService;
import pl.bartoszbulaj.moonrock.service.SchedulerService;
import pl.bartoszbulaj.moonrock.service.WebsocketManagerService;

@Service
@Transactional
public class SchedulerServiceImpl implements SchedulerService {

	private InstrumentService instrumentService;
	private AppConfigurationService appConfigurationService;
	private WebsocketManagerService websocketManagerService;

	private boolean heartbeatStatus;
	private boolean historyAnalyzerStatus;
	private boolean emailSenderStatus;

	@Autowired
	public SchedulerServiceImpl(InstrumentService instrumentService, AppConfigurationService appConfigurationService,
			WebsocketManagerService websocketManagerService) {
		this.instrumentService = instrumentService;
		this.appConfigurationService = appConfigurationService;
		this.websocketManagerService = websocketManagerService;
		this.heartbeatStatus = false;

		if (this.appConfigurationService.isAnyEmailSender()) {
			emailSenderStatus = appConfigurationService.setEmailSenderEnabled(true);
			historyAnalyzerStatus = appConfigurationService.setHistoryAnalyzerEnabled(true);
		} else {
			emailSenderStatus = appConfigurationService.setEmailSenderEnabled(false);
			historyAnalyzerStatus = appConfigurationService.setHistoryAnalyzerEnabled(false);
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
		boolean isSignal = false;
		if (historyAnalyzerStatus) {
			isSignal = instrumentService.analyzeInstrumentHistory();
		}
		if (emailSenderStatus && isSignal) {
			instrumentService.sendEmailWithSignals();
		}
	}

	@Override
	@Scheduled(cron = "5 * * * * *")
	public void sendHeartbeat() {
		if (heartbeatStatus) {
			websocketManagerService.pingServer();
		}
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
