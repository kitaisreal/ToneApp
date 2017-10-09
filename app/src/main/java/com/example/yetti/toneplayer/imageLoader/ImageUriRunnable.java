package com.example.yetti.toneplayer.imageLoader;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;

import java.io.IOException;

public class ImageUriRunnable implements Runnable{
    private IImageDownloadListener mListener;
    private Handler mHandler;
    private ContentResolver mContentResolver;
    private Uri mUri;
    public ImageUriRunnable(Handler handler, IImageDownloadListener iListener, ContentResolver mContentResolver, Uri uri){
        this.mListener = iListener;
        this.mHandler=handler;
        this.mContentResolver=mContentResolver;
        this.mUri=uri;
    }
    @Override
    public void run() {
        try{
            final Uri getImageUri = Uri.parse("content://media/external/audio/albumart");
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContentResolver, mUri);
                mHandler.post(postHandlerRunnableResult(bitmap));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Runnable postHandlerRunnableResult(final Bitmap bitmap){
        return new Runnable() {
            @Override
            public void run() {
                System.out.println("IMAGE DOWNLOADED LETS VIEW BITMAP");
                mListener.onImageDownload(bitmap);
            }
        };
    }
}
