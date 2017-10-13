package com.example.yetti.toneplayer.threadmanager;


import com.example.yetti.toneplayer.imageLoader.ImageLoader;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadsManager {
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private final ThreadPoolExecutor mForDiskLoaderTasks;
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
        mForDiskLoaderTasks = new ThreadPoolExecutor(
                1,
                1,
                Long.MAX_VALUE,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ImageLoderDiskThreadFactory()
        );
        mForImageLoaderTasks = new ThreadPoolExecutor(
                3,
                3,
                Long.MAX_VALUE,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ImageLoaderThreadFactory()
        );
    }

    public ThreadPoolExecutor getThreadsForDiskCache() {
        return mForDiskLoaderTasks;
    }

    public ThreadPoolExecutor getForImageLoaderTasks() {
        return mForImageLoaderTasks;
    }
}
