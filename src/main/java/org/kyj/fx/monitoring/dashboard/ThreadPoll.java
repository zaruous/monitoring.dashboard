package org.kyj.fx.monitoring.dashboard;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoll {
	
	static ExecutorService newVirtualThreadPerTaskExecutor;
	private static ThreadPoll INSTANCE = null;
	private ThreadPoll() {
		newVirtualThreadPerTaskExecutor = Executors.newVirtualThreadPerTaskExecutor();
	}
	
	public synchronized static ThreadPoll getInstance() {
		if(	INSTANCE == null )
			return INSTANCE = new ThreadPoll();
		return INSTANCE;
	}
	
	public void execute(Runnable command) {
		newVirtualThreadPerTaskExecutor.execute(command);
	}
	
}
