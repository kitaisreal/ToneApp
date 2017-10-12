package com.example.yetti.toneplayer.imageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.example.yetti.toneplayer.imageLoader.diskCache.DiskCacheManagerImpl;
import com.example.yetti.toneplayer.imageLoader.diskCache.IImageCallbackResult;
import com.example.yetti.toneplayer.imageLoader.memoryCache.ImageCacheImpl;
import com.example.yetti.toneplayer.threadmanager.ThreadsManager;
import com.example.yetti.toneplayer.utils.Utils;

public class ImageLoader {

    private final String TAG = "IMAGE LOADER";
    private final ThreadsManager mThreadsManager;
    private final Handler mHandler;
    private final Object mLockMemoryCacheObject;
    private final DiskCacheManagerImpl mDiskCacheMemoryManager;
    private final ImageCacheImpl mImageMemoryCacheManager;
    private final ImageProcessing mImageProcessingImpl;
    private static volatile ImageLoader sInstance;

    private ImageLoader(final Context pContext) {
        mLockMemoryCacheObject = new Object();
        mHandler = new Handler(Looper.getMainLooper());
        mThreadsManager = ThreadsManager.getInstance();
        mImageProcessingImpl = new ImageProcessing(pContext);
        mImageMemoryCacheManager = new ImageCacheImpl();
        mDiskCacheMemoryManager = new DiskCacheManagerImpl(pContext);
        ThreadsManager.getInstance().getThreadsForDiskCache().execute(mDiskCacheMemoryManager.initializeDiskCache());
    }

    public static ImageLoader getInstance(Context pContext) {
        ImageLoader localInstance = sInstance;
        if (localInstance == null) {
            synchronized (ThreadsManager.class) {
                localInstance = sInstance;
                if (localInstance == null) {
                    sInstance = localInstance = new ImageLoader(pContext);
                }
            }
        }
        return localInstance;
    }

    public void displayImage(final String pUrlToDownload, final ImageView pImageView){
        final String imageUriInDiskCache = Utils.getImageUrlHash(pUrlToDownload);
        final String imageUriInMemCache = Utils.getImageUrlHash(pUrlToDownload);
        final IImageCallbackResult<Boolean> inDiskCacheCheck = new IImageCallbackResult<Boolean>() {
            @Override
            public void onSuccess(final Boolean pBoolean) {
                Log.d(TAG, "GET BITMAP FROM DISK CACHE");
            }
            @Override
            public void onError(final Boolean pBoolean) {
                Log.d(TAG, "NO BITMAP IN DISK CACHE");
                processImageDownload(imageUriInDiskCache, imageUriInMemCache, pUrlToDownload, pImageView);
            }
        };
        final IImageCallbackResult<Boolean> inMemoryCacheCheck = new IImageCallbackResult<Boolean>() {
            @Override
            public void onSuccess(final Boolean pBoolean) {
                Log.d(TAG, "GET BITMAP FROM MEMORY CACHE");
            }
            @Override
            public void onError(final Boolean pBoolean) {
                Log.d(TAG, "NO BITMAP IN MEMORY CACHE");
                checkInDiskCache(imageUriInDiskCache, pImageView,inDiskCacheCheck);
            }
        };
        checkInMemoryCache(imageUriInMemCache, pImageView, inMemoryCacheCheck);
    }
    private void checkInMemoryCache(final String pImageUri, final ImageView pImageView, final IImageCallbackResult<Boolean> pBooleanICallbackResult){
        final Bitmap bitmap;
        Log.d(TAG, "CHECK IN MEMORY CACHE STARTED");
        synchronized (mLockMemoryCacheObject){
            bitmap = mImageMemoryCacheManager.get(pImageUri);
        }
        if (bitmap!=null){
            updateMainThreadImageView(pImageView, bitmap);
            pBooleanICallbackResult.onSuccess(true);
        } else{
            pBooleanICallbackResult.onError(false);
        }
    }
    private void checkInDiskCache(final String pImageUri, final ImageView pImageView, final IImageCallbackResult<Boolean> pBooleanICallbackResult){
        Log.d(TAG, "CHECK IN DISK CACHE STARTED");
        mThreadsManager.getThreadsForDiskCache().execute(mDiskCacheMemoryManager.getFileFromDiskCache(pImageUri, new IImageCallbackResult<Bitmap>() {
            @Override
            public void onSuccess(final Bitmap pBitmap) {
                if (pBitmap!=null) {
                    synchronized (mLockMemoryCacheObject) {
                        mImageMemoryCacheManager.put(pImageUri, pBitmap);
                    }
                }
                updateMainThreadImageView(pImageView, pBitmap);
                pBooleanICallbackResult.onSuccess(true);
            }
            @Override
            public void onError(final Bitmap pBitmap) {
                pBooleanICallbackResult.onError(false);
            }
        }));
    }
    private void processImageDownload(final String pImageDiskUri, final String pImageUriInMemCache, final String pImageUrl, final ImageView pImageView){
        Log.d(TAG,"PROCESS IMAGE DOWNLOAD STARTED");
        final IImageDownloadListener iImageDownloadListener= new IImageDownloadListener() {
            @Override
            public void onImageDownload(final Bitmap pBitmap) {
                pImageView.setImageBitmap(pBitmap);
                ThreadsManager.getInstance().getThreadsForDiskCache().execute(mDiskCacheMemoryManager.saveFileToDiskCache(pImageDiskUri,pBitmap,null));
                synchronized (mLockMemoryCacheObject){
                    mImageMemoryCacheManager.put(pImageUriInMemCache, pBitmap);
                }
            }
        };
        mThreadsManager.getForImageLoaderTasks().
                execute(new ImageRunnable(new UIHandler(mHandler,iImageDownloadListener),mImageProcessingImpl,pImageUrl));
    }
    private void updateMainThreadImageView(final ImageView pImageView, final Bitmap pBitmap){
        new UIHandler(mHandler, new IImageDownloadListener() {
            @Override
            public void onImageDownload(final Bitmap image) {
                pImageView.setImageBitmap(image);
            }
        }).postHandlerResult(pBitmap);
    }
}
