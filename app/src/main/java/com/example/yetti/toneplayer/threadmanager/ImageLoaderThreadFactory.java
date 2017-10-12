package com.example.yetti.toneplayer.threadmanager;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.ThreadFactory;

public class ImageLoaderThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(@NonNull Runnable r) {
        Log.d("THREAD FACTORY", "CREATE IMAGE LOADER NETWORK THREAD");
        return new ImageLoaderThread(r);
    }
}
