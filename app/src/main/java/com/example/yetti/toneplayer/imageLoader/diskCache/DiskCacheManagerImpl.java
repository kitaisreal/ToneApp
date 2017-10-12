package com.example.yetti.toneplayer.imageLoader.diskCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import static android.os.Environment.isExternalStorageRemovable;

public class DiskCacheManagerImpl implements DiskCacheManager {

    private static final String UNIQUE_NAME = "tone";
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10;
    private final Context mContext;
    private final String TAG = "DISKCACHE";

    private boolean diskCacheInitialized;
    private final DiskCacheImpl mDiskCacheImpl;
    public DiskCacheManagerImpl(final Context pContext) {
        mContext = pContext;
        mDiskCacheImpl = new DiskCacheImpl();
    }

    @Override
    public Runnable initializeDiskCache() {
        return new Runnable() {

            @Override
            public void run() {
                mDiskCacheImpl.initDiskCache(getDiskCacheDir(mContext, UNIQUE_NAME), DISK_CACHE_SIZE);
                diskCacheInitialized = true;

                Log.d(TAG, "DISK CACHE INITIALIZED");
            }
        };
    }
    @Override
    public Runnable getFileFromDiskCache(final String imageUri, final IImageCallbackResult<Bitmap> pBitmapICallbackResult) {
        return new Runnable() {

            @Override
            public void run() {
                try {
                    if (diskCacheInitialized) {
                        final Bitmap bitmap = mDiskCacheImpl.get(imageUri);
                        if (bitmap!=null){
                            pBitmapICallbackResult.onSuccess(bitmap);
                        } else{
                            pBitmapICallbackResult.onError(null);
                        }
                    } else {
                        pBitmapICallbackResult.onError(null);
                    }
                } catch (final Exception ex) {
                    Log.d(TAG,"GET FILE FROM DISK CACHE EXCEPTION");
                    pBitmapICallbackResult.onError(null);
                }
            }
        };
    }
    @Override
    public Runnable saveFileToDiskCache(final String imageUri, final Bitmap pBitmap, final IImageCallbackResult<Boolean> pBooleanICallbackResult) {
        return new Runnable() {

            @Override
            public void run() {
                try {
                    if (diskCacheInitialized) {
                        mDiskCacheImpl.put(imageUri, pBitmap);
                        if (pBooleanICallbackResult!=null) {
                            pBooleanICallbackResult.onSuccess(true);
                        }

                    }
                } catch (final Exception ex) {
                    Log.d(TAG,"SAVE FILE TO DISK CACHE EXCEPTION");
                    if (pBooleanICallbackResult!=null) {
                        pBooleanICallbackResult.onError(false);
                    }
                }
            }
        };
    }
    private File getDiskCacheDir(final Context pContext, final String uniqueName) {
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? pContext.getExternalCacheDir().getPath() :
                        pContext.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }
}
