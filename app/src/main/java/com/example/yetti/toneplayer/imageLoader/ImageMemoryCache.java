package com.example.yetti.toneplayer.imageLoader;

import android.graphics.Bitmap;
import android.util.LruCache;

//TODO SOFT AND STRONG CACHE REFERENCE
public class ImageMemoryCache extends LruCache<String, Bitmap> {

    public ImageMemoryCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getByteCount() / 1024;
    }
}
