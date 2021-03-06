package com.example.yetti.toneplayer.threadmanager;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ThreadsManager {

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
        Log.d("ThreadsManager", "INITIALIZATION");
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
