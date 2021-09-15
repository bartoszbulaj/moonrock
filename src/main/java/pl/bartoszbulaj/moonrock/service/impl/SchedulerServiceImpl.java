package pl.bartoszbulaj.moonrock.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bartoszbulaj.moonrock.service.AppConfigurationService;
import pl.bartoszbulaj.moonrock.service.HistoryService;
import pl.bartoszbulaj.moonrock.service.SchedulerService;
import pl.bartoszbulaj.moonrock.service.WebsocketManagerService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

@Service
@Transactional
@Slf4j
public class SchedulerServiceImpl implements SchedulerService {

	private HistoryService historyService;
	private AppConfigurationService appConfigurationService;
	private WebsocketManagerService websocketManagerService;
	private ApplicationContext context;
	private TaskScheduler taskScheduler;
	private Set<ScheduledFuture<?>> taskSet = new HashSet<>();

	private boolean heartbeatActive;
	private boolean emailSenderActive;

	@Autowired
	public SchedulerServiceImpl(HistoryService historyService, AppConfigurationService appConfigurationService,
			WebsocketManagerService websocketManagerService, ApplicationContext context, TaskScheduler taskScheduler) {
		this.historyService = historyService;
		this.appConfigurationService = appConfigurationService;
		this.websocketManagerService = websocketManagerService;
		this.context = context;
		this.taskScheduler = taskScheduler;
		this.heartbeatActive = false;

		appConfigurationService.setHistoryAnalyzerEnabled(true);

		if (this.appConfigurationService.isAnyEmailSender()) {
			appConfigurationService.setEmailSenderEnabled(true);
			emailSenderActive = true;
		} else {
			appConfigurationService.setEmailSenderEnabled(false);
			emailSenderActive = false;
		}

		this.taskScheduler.schedule(sendHeartbeat(), new CronTrigger("5 * * * * *"));
		configTasks(appConfigurationService.getHistoryAnalyzerInterval());
	}

	@Override
	public Runnable deleteHistory() {
		return new Runnable() {
			@Override
			public void run() {
				if (appConfigurationService.isHistoryAnalyzerEnabled()) {
					historyService.deleteInstrumentHistory();
				}
			}
		};
	}

	@Override
	public Runnable saveHistory() {
		return new Runnable() {
			@Override
			public void run() {
				if (appConfigurationService.isHistoryAnalyzerEnabled()) {
					historyService.saveInstrumentHistory();
				}
			}
		};
	}

	@Override
	public Runnable analyzeHistory() {
		return new Runnable() {
			@Override
			public void run() {
				boolean isSignal = false;
				if (appConfigurationService.isHistoryAnalyzerEnabled()) {
					isSignal = historyService.analyzeInstrumentHistory();
				}
				if (emailSenderActive && isSignal) {
					historyService.sendEmailWithSignals();
				}
			}
		};
	}

	@Override
	public Runnable sendHeartbeat() {
		return new Runnable() {
			@Override
			public void run() {
				if (heartbeatActive) {
					websocketManagerService.pingServer();
				}
			}
		};
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

	@Override
	public void configTasks(String interval) {
		Map<String, String> intervalDictionary = new HashMap<>();
		intervalDictionary.put("5m", " 0/5 * * * *");
		intervalDictionary.put("15m", " 0/15 * * * *");
		intervalDictionary.put("1h", " 0 * * * *");
		intervalDictionary.put("4h", " 0 */4 * * *");

		String cronSufix = intervalDictionary.get(interval);
		ScheduledFuture<?> schedule1 = taskScheduler.schedule(deleteHistory(), new CronTrigger("5" + cronSufix));
		ScheduledFuture<?> schedule2 = taskScheduler.schedule(saveHistory(), new CronTrigger("15" + cronSufix));
		ScheduledFuture<?> schedule3 = taskScheduler.schedule(analyzeHistory(), new CronTrigger("25" + cronSufix));
		log.info(interval + " nowy interval*************** cron: " + interval.replace("m", "") + cronSufix
				+ " **********");
		taskSet.add(schedule1);
		taskSet.add(schedule2);
		taskSet.add(schedule3);
	}

	@Override
	public void deleteTasks() {
		taskSet.forEach(task -> task.cancel(true));
		taskSet.clear();
	}
}
