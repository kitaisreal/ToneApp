package com.example.yetti.toneplayer.imageLoader;

import android.util.Log;

public class ImageRunnable implements Runnable {
    private final UIHandler mUiHandler;
    private final ImageProcessing mImageProcessing;
    private final String mUrlToDownload;
    private final String TAG = "IMAGERUNNABLE";
    public ImageRunnable(final UIHandler mainHandler, final ImageProcessing imageProcessing, final String UrlToDownload) {
        this.mUiHandler = mainHandler;
        this.mImageProcessing = imageProcessing;
        this.mUrlToDownload=UrlToDownload;
    }
    @Override
    public void run() {
        try {
            mUiHandler.postHandlerResult(mImageProcessing.decodeBitmap(mUrlToDownload));
        } catch (final Exception ex) {
            Log.d(TAG, "IMAGE RUNNABLE ON POST TO UI THREAD EXCEPTION");
        }
    }
}
