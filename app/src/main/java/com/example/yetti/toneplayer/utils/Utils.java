package com.example.yetti.toneplayer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class Utils {
    public static final int DEFAULT_BUFFER_SIZE=1024;
    private Utils(){}
    public static void copyInputStreamInOutputStream(final InputStream pInputStream, final OutputStream pOutputStream) throws IOException {
        int bytesRead;
        final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while ((bytesRead = pInputStream.read(buffer)) > 0) {
            pOutputStream.write(buffer, 0, bytesRead);
        }
        pOutputStream.close();
    }
    public static String getImageUrlHash(final String imageUri) {
        return String.valueOf(imageUri.hashCode());
    }
    public static int getMemoryCacheSize(){
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        return cacheSize;
    }
}
