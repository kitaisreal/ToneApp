package com.example.yetti.toneplayer.imageLoader;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ImageProcessingImpl implements IImageProcessing {
    @Override
    public byte[] getBytesFromUrl(String urlLink) {
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(urlLink);
            httpURLConnection = (HttpsURLConnection) url.openConnection();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream in = httpURLConnection.getInputStream();
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (httpURLConnection!=null) {
                httpURLConnection.disconnect();
            }
        }
    }

    @Override
    public Bitmap getBitmapFromBytes(final byte[] bytes) {
        final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        return bitmap;
    }
    // TODO RESIZE BITMAP
    @Override
    public Bitmap resizeBitmap(Bitmap bitmap) {
        return bitmap;
    }

    @Override
    public Bitmap getFinalBitmap(String urlLink) {
        return resizeBitmap(getBitmapFromBytes(getBytesFromUrl(urlLink)));
    }
}
