
package com.example.yetti.toneplayer.imageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.example.yetti.toneplayer.threadmanager.ThreadsManager;

//TODO ADD FILE CACHE
public class ImageLoader {

    private final ThreadsManager mThreadsManager;
    private final Handler mHandler;
    private final ImageProcessingImpl mImageProcessing;
    private ImageMemoryCache mImageMemoryCache;
    private final Object mLockMemoryCacheObject;
    private static volatile ImageLoader sInstance;

    public static ImageLoader getInstance() {
        ImageLoader localInstance = sInstance;
        if (localInstance == null) {
            synchronized (ThreadsManager.class) {
                localInstance = sInstance;
                if (localInstance == null) {
                    sInstance = localInstance = new ImageLoader();
                }
            }
        }
        return localInstance;
    }

    private ImageLoader() {
        //TODO CACHE MEMORY FROM FILE AND LIFO QUEUE
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mLockMemoryCacheObject = new Object();
        mImageMemoryCache = new ImageMemoryCache(cacheSize);
        mHandler = new Handler(Looper.getMainLooper());
        mThreadsManager = ThreadsManager.getInstance();
        mImageProcessing = new ImageProcessingImpl();
    }

    public void displayImageByUrl(final ImageView imageView, final String urlToDownload) {
        IImageDownloadListener listener = new IImageDownloadListener() {

            @Override
            public void onImageDownload(Bitmap image) {
                synchronized (mLockMemoryCacheObject) {
                    System.out.println("PUT IN CACHE");
                    mImageMemoryCache.put(urlToDownload, image);
                }
                if (imageView != null && image != null) {
                    System.out.println("STOP WORKING ON IMAGE");
                    imageView.setImageBitmap(image);
                }
            }
        };
        Bitmap bitmapToDraw = null;
        synchronized (mLockMemoryCacheObject) {
            bitmapToDraw = mImageMemoryCache.get(urlToDownload);
        }
        if (bitmapToDraw != null) {
            imageView.setImageBitmap(bitmapToDraw);
            System.out.println("GET FROM CACHE");
            return;
        }
        mThreadsManager.getExecutorImageLoaderTasks().execute(new ImageNetworkRunnable(mHandler, listener, mImageProcessing, urlToDownload));
    }

    public void displayImageByUri(final ImageView imageView, final Uri uri, Context context) {
        IImageDownloadListener listener = new IImageDownloadListener() {

            @Override
            public void onImageDownload(Bitmap image) {
                synchronized (mLockMemoryCacheObject) {
                    System.out.println("PUT IN CACHE");
                    mImageMemoryCache.put(uri.toString(), image);
                }
                if (imageView != null && image != null) {
                    System.out.println("STOP WORKING ON IMAGE");
                    imageView.setImageBitmap(image);
                }
            }
        };
        Bitmap bitmapToDraw = null;
        synchronized (mLockMemoryCacheObject) {
            bitmapToDraw = mImageMemoryCache.get(uri.toString());
        }
        if (bitmapToDraw != null) {
            imageView.setImageBitmap(bitmapToDraw);
            System.out.println("GET FROM CACHE");
            return;
        }
        mThreadsManager.getExecutorImageLoaderTasks().execute(new ImageUriRunnable(mHandler, listener, context.getContentResolver(), uri));

    }
}
