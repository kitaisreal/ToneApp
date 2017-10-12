package com.example.yetti.toneplayer.imageLoader.diskCache;

import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;

public interface IDiskCache {
    void initDiskCache(File cacheDir, int diskCacheSize);
    Bitmap get(String pImageUri) throws IOException;
    boolean put(String pImageUri, Bitmap pBitmap) throws IOException;
    void close() throws IOException;
    void clear() throws IOException;
    long size();
    long maxSize();
}
