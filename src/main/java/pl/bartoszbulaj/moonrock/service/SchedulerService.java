package pl.bartoszbulaj.moonrock.service;

public interface SchedulerService {

	void deleteHistory();

	void saveHistory();

	void analyzeHistory();

	boolean isHistoryAnalyzerEnabled();

	void setHistoryAnalyzerDisabled();

	void setHistoryAnalyzerEnabled();

	boolean isHeartbeatEnabled();

	void enableHeartbeat();

	void disableHeartbeat();

	void sendHeartbeat();
}
