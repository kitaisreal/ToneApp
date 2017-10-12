package com.example.yetti.toneplayer.imageLoader;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.yetti.toneplayer.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageProcessing {
    private final String TAG="IMAGE PROCESSING IMPL";
    private Context mContext;
    public ImageProcessing(Context pContext){
        this.mContext=pContext;
    }
    public Bitmap decodeBitmap(final String pUrlLink){
        return decodeBitmap(pUrlLink,-1,-1);
    }
    private Bitmap decodeBitmap(final String pUrlLink, final int pXWidth, final int pYHight){
        if (pUrlLink.toLowerCase().contains("http")|| pUrlLink.toLowerCase().contains("https")){
            return getBitmapFromBytes(getBytesFromUrl(pUrlLink));
        }
        if (pUrlLink.toLowerCase().contains("content")){
            return getBitmapFromContent(pUrlLink);
        }
        return null;
    }
    private Bitmap getBitmapFromContent(final String pUrlLink){
        Bitmap bitmap = null;
        final Uri imageUri = Uri.parse(pUrlLink);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);
        } catch (IOException ex){
            //TODO ADD DEFAULT
        }
        return bitmap;
    }
    private byte[] getBytesFromUrl(final String pUrlLink) {
        HttpURLConnection httpURLConnection = null;
        try {
            final URL url = new URL(pUrlLink);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final InputStream in = httpURLConnection.getInputStream();
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            Utils.copyStream(in,outputStream);
            return outputStream.toByteArray();
        } catch (final Exception ex) {
            Log.d(TAG, "GET BYTES FROM URL EXCEPTION");
            return null;
        } finally {
            if (httpURLConnection!=null) {
                httpURLConnection.disconnect();
            }
        }
    }
    private Bitmap getBitmapFromBytes(final byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
    //TODO CHANGE
    public Bitmap resizeBitmap(final byte[] bytes) {
        final int REQUIRED_SIZE=480;
        Bitmap bitmap = getBitmapFromBytes(bytes);
        int width=bitmap.getWidth();
        int height = bitmap.getHeight();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        int scale=1;
        while(true){
            if(width/2<REQUIRED_SIZE || height/2<REQUIRED_SIZE) {
                break;
            }
            width/=2;
            height/=2;
            scale*=2;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize=scale;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }
}
