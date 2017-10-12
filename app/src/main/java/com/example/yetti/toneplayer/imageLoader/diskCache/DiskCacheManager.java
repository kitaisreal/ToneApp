package com.example.yetti.toneplayer.imageLoader.diskCache;

import android.graphics.Bitmap;

public interface DiskCacheManager {
    Runnable initializeDiskCache();
    Runnable getFileFromDiskCache(final String imageUri, final IImageCallbackResult<Bitmap> pBitmapICallbackResult);
    Runnable saveFileToDiskCache(final String imageUri, final Bitmap pBitmap, final IImageCallbackResult<Boolean> pBooleanICallbackResult);
}
