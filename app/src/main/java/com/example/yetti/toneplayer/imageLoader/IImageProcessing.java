package com.example.yetti.toneplayer.imageLoader;

import android.graphics.Bitmap;

public interface IImageProcessing {
    byte[] getBytesFromUrl(final String urlLink) throws Exception;
    Bitmap getBitmapFromBytes(final byte[] bytes);
    Bitmap resizeBitmap(final Bitmap bitmap);
    Bitmap getFinalBitmap(final String urlLink);
}
