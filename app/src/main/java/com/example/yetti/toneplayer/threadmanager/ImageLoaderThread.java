package com.example.yetti.toneplayer.threadmanager;
import android.os.Process;

public class ImageLoaderThread extends Thread {
    ImageLoaderThread(Runnable r){
        super(r);
    }
    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        super.run();
    }
}
