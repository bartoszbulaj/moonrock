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

	@Autowired
	private InstrumentService instrumentService;

	@Override
	@Scheduled(cron = "0 0 * * * *")
	public void deleteHistory() {
		instrumentService.deleteInstrumentHistory();
	}

	@Override
	@Scheduled(cron = "15 0 * * * *")
	public void saveHistory() {
		instrumentService.saveInstrumentHistory();

	}

	@Override
	@Scheduled(cron = "0 1 * * * *")
	public void analyzeHistory() {
		instrumentService.analyzeInstrumentHistory();

	}

}
