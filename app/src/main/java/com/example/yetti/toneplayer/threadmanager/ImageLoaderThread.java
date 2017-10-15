package com.example.yetti.toneplayer.threadmanager;

import android.os.Process;

public class ImageLoaderThread extends Thread {

    ImageLoaderThread(final Runnable pRunnable) {
        super(pRunnable);
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        super.run();
    }
}
