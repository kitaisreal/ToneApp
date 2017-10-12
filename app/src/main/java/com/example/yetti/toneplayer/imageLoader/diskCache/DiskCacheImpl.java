package com.example.yetti.toneplayer.imageLoader.diskCache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.yetti.toneplayer.imageLoader.diskCache.lrudiskcache.DiskLruCache;
import com.example.yetti.toneplayer.utils.Utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class DiskCacheImpl implements IDiskCache {

    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10;
    private static final Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.PNG;
    private DiskLruCache mDiskLruCache;
    private final String TAG = "DiskCacheImpl";

    @Override
    public void initDiskCache(File cacheDir, int diskCacheSize) {
        try {
            mDiskLruCache = DiskLruCache.open(cacheDir, 1, 1, DISK_CACHE_SIZE, Integer.MAX_VALUE);
            Log.d(TAG, "INIT CACHE SIZE " + mDiskLruCache.size());
        } catch (final IOException Ex) {
            Log.d(TAG, "DISK CACHE MEMORY INIT EXCEPTION");
        }
    }

    @Override
    public Bitmap get(final String pImageUri) throws IOException {
        DiskLruCache.Snapshot snapshot = null;
        Bitmap bitmap = null;
        try {
            snapshot = mDiskLruCache.get(pImageUri);
            if (snapshot != null) {
                snapshot.getInputStream(0);
                final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                Utils.copyStream(snapshot.getInputStream(0), outputStream);
                bitmap = BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.toByteArray().length);
            }
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }
        return bitmap;
    }

    @Override
    public boolean put(final String pImageUri, final Bitmap pBitmap) throws IOException {
        final DiskLruCache.Editor editor = mDiskLruCache.edit(pImageUri);
        if (editor == null) {
            return false;
        }
        boolean savedDiskCache = false;
        try (BufferedOutputStream os = new BufferedOutputStream(editor.newOutputStream(0), Utils.DEFAULT_BUFFER_SIZE)) {
            savedDiskCache = pBitmap.compress(mCompressFormat, 100, os);
        }
        if (savedDiskCache) {
            editor.commit();
        } else {
            editor.abort();
        }
        return savedDiskCache;
    }

    @Override
    public void close() throws IOException {
        mDiskLruCache.close();
    }

    @Override
    public void clear() throws IOException {
        mDiskLruCache.delete();
    }

    @Override
    public long size() {
        return mDiskLruCache.size();
    }

    @Override
    public long maxSize() {
        return mDiskLruCache.getMaxSize();
    }
}
