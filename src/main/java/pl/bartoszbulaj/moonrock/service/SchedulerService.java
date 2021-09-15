package pl.bartoszbulaj.moonrock.service;

public interface SchedulerService {

	Runnable deleteHistory();

	Runnable saveHistory();

	Runnable analyzeHistory();

	Runnable sendHeartbeat();

	boolean isHeartbeatActive();

	void enableHeartbeat();

	void disableHeartbeat();

	void configTasks(String interval);

	void deleteTasks();
}
