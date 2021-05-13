package pl.bartoszbulaj.moonrock.service;

public interface SchedulerService {

	void deleteHistory();

	void saveHistory();

	void analyzeHistory();

	boolean isHeartbeatActive();

	void enableHeartbeat();

	void disableHeartbeat();

	void sendHeartbeat();
}
