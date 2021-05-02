package pl.bartoszbulaj.moonrock.service;

public interface SchedulerService {

	void deleteHistory();

	void saveHistory();

	void analyzeHistory();

	boolean isHeartbeatEnabled();

	void enableHeartbeat();

	void disableHeartbeat();

	void sendHeartbeat();
}
