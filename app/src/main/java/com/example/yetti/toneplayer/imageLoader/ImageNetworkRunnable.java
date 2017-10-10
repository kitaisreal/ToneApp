package com.example.yetti.toneplayer.imageLoader;

import android.graphics.Bitmap;
import android.os.Handler;

public class ImageNetworkRunnable implements Runnable {

    private Handler mHandler;
    private IImageDownloadListener mListener;
    private ImageProcessingImpl mImageProcessing;
    private String mUrlToDownload;

    public ImageNetworkRunnable(Handler mainHandler, IImageDownloadListener myListener, ImageProcessingImpl imageProcessing, String UrlToDownload) {
        this.mHandler = mainHandler;
        this.mListener = myListener;
        this.mImageProcessing = imageProcessing;
        this.mUrlToDownload = UrlToDownload;
    }

    @Override
    public void run() {
        System.out.println("DOWNLOAD IMAGE");
        System.out.println("WORKING");
        try {
            mHandler.post(postHandlerRunnableResult(mImageProcessing.getFinalBitmap(mUrlToDownload)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Runnable postHandlerRunnableResult(final Bitmap bitmap) {
        return new Runnable() {

            @Override
            public void run() {
                System.out.println("IMAGE DOWNLOADED LETS VIEW BITMAP");
                mListener.onImageDownload(bitmap);
            }
        };
    }
}
