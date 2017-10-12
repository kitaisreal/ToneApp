package com.example.yetti.toneplayer.imageLoader.memoryCache;

import android.graphics.Bitmap;

public interface IImageCache {
    Bitmap get(final String pImageUri);
    void put(final String pImageUri, final Bitmap pBitmap);
    void clear();
    long size();
    long maxSize();
}
