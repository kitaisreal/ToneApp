package com.example.yetti.toneplayer.imageLoader.memoryCache;

import android.graphics.Bitmap;

import com.example.yetti.toneplayer.utils.Utils;

public class ImageCacheImpl implements IImageCache {
    private LruMemoryCache mImageMemoryCache;
    public ImageCacheImpl(){
        mImageMemoryCache = new LruMemoryCache(Utils.getMemoryCacheSize());
    }
    @Override
    public Bitmap get(final String pImageUri){
        return mImageMemoryCache.get(pImageUri);
    }
    @Override
    public void put(final String pImageUri, final Bitmap pBitmap){
        if (pBitmap!=null) {
            mImageMemoryCache.put(pImageUri, pBitmap);
        }
    }
    @Override
    public void clear(){
        mImageMemoryCache.trimToSize(0);
    }
    @Override
    public long size(){
        return mImageMemoryCache.size();
    }
    @Override
    public long maxSize(){
        return mImageMemoryCache.maxSize();
    }
}
