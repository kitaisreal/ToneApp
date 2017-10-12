package com.example.yetti.toneplayer.imageLoader.memoryCache;

import android.graphics.Bitmap;
import android.util.LruCache;

//TODO SOFT AND STRONG CACHE REFERENCE
public class LruMemoryCache extends LruCache<String,Bitmap> {

    private static final int IMAGE_MEMORY_CACHE_DIVIDE_IN_MB = 1024;

    public LruMemoryCache(final int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(final String key, final Bitmap value) {
        return value.getByteCount()/ IMAGE_MEMORY_CACHE_DIVIDE_IN_MB;
    }
}
