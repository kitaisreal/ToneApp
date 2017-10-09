package com.example.yetti.toneplayer.threadmanager;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadsManager {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private final ThreadPoolExecutor mForDBTasks;
    private final ThreadPoolExecutor mForImageLoaderTasks;
    private static volatile ThreadsManager sInstance;

    public static ThreadsManager getInstance() {
        ThreadsManager localInstance = sInstance;
        if (localInstance == null) {
            synchronized (ThreadsManager.class) {
                localInstance = sInstance;
                if (localInstance == null) {
                    sInstance = localInstance = new ThreadsManager();
                }
            }
        }
        return localInstance;
    }

    private ThreadsManager() {
        mForDBTasks = new ThreadPoolExecutor(
                2,
                2,
                Long.MAX_VALUE,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()
        );
        mForImageLoaderTasks = new ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                Long.MAX_VALUE,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()
        );
    }

    public ThreadPoolExecutor getExecutorForDBTasks() {
        return mForDBTasks;
    }

    public ThreadPoolExecutor getExecutorImageLoaderTasks() {
        return mForImageLoaderTasks;
    }

}
