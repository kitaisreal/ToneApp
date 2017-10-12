package com.example.yetti.toneplayer.imageLoader;

import android.graphics.Bitmap;
import android.os.Handler;

public class UIHandler {
    private final Handler mHandler;
    private final IImageDownloadListener mListener;

    public UIHandler(final Handler pHandler, final IImageDownloadListener pIImageDownloadListener){
        this.mHandler=pHandler;
        this.mListener = pIImageDownloadListener;
    }
    public void postHandlerResult(final Bitmap pBitmap){
        mHandler.post(postHandlerRunnableResult(pBitmap));
    }
    private Runnable postHandlerRunnableResult(final Bitmap bitmap){
        return new Runnable() {
            @Override
            public void run() {
                mListener.onImageDownload(bitmap);
            }
        };
    }
}
