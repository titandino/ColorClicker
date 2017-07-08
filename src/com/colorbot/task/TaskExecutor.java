package com.colorbot.task;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class TaskExecutor {

    private static ScheduledExecutorService eventExecutor;
    private static ScheduledExecutorService logicExecutor;
    private static ThreadPoolExecutor threadPool;

    static {
        eventExecutor = Executors.newSingleThreadScheduledExecutor();
        logicExecutor = Executors.newSingleThreadScheduledExecutor();
        threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    public static void initializeEventExecutor() {
    	eventExecutor = Executors.newSingleThreadScheduledExecutor();
    }
    
    public static void initializeLogicExecutor() {
    	logicExecutor = Executors.newSingleThreadScheduledExecutor();
    }
    
    public static ScheduledExecutorService getEventExecutor() {
        return eventExecutor;
    }

    public static ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }

	public static ScheduledExecutorService getLogicExecutor() {
		return logicExecutor;
	}
}