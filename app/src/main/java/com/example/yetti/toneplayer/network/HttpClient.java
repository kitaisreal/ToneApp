package com.example.yetti.toneplayer.network;

import android.util.Log;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.utils.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class HttpClient {

    public void createAsyncRequest(final Request pRequest, final ICallbackResult<String> pICallbackResult) {
        new Runnable() {

            @Override
            public void run() {
                try {
                    String response = createRequest(pRequest);
                    pICallbackResult.onSuccess(response);
                } catch (Exception e) {
                    Log.d(TAG, "CREATE ASYNC REQUEST EXCEPTION");
                    if (pICallbackResult != null) {
                        Exception exception = new Exception("CREATE ASYNC REQUEST EXCEPTION");
                        pICallbackResult.onError(exception);
                    }
                }
            }
        }.run();
    }

    private String createRequest(final Request pRequest) {
        HttpURLConnection URLConnection = null;
        URL url;
        try {
            url = new URL(pRequest.getUrl());
            if (url.toString().toLowerCase().contains(HttpContract.HTTP)) {
                URLConnection = (HttpURLConnection) url.openConnection();
            }
            if (url.toString().toLowerCase().contains(HttpContract.HTTPS)) {
                URLConnection = (HttpsURLConnection) url.openConnection();
            }
            if (URLConnection != null) {
                setConnectionProperties(URLConnection, pRequest);
                InputStream in = new BufferedInputStream(URLConnection.getInputStream());
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                OutputStream outputStream = new BufferedOutputStream(result);
                Utils.copyInputStreamInOutputStream(in, outputStream);
                return result.toString(HttpContract.UTF_8);
            }
        } catch (Exception e) {
            Log.d(TAG, "CREATE REQUEST EXCEPTION");
        } finally {
            if (URLConnection != null) {
                URLConnection.disconnect();
            }
        }
        return null;
    }

    private void setConnectionProperties(HttpURLConnection pHttpURLConnection, Request pRequest) {
        try {
            pHttpURLConnection.setRequestMethod(pRequest.getMethod());
            if (pRequest.getHeaders() != null) {
                for (String s : pRequest.getHeaders().keySet()) {
                    pHttpURLConnection.setRequestProperty(s, pRequest.getHeaders().get(s));
                }
            }
            if (pRequest.getBody() != null) {
                pHttpURLConnection.setDoOutput(true);
                pHttpURLConnection.setDoInput(true);
                final OutputStream stream = pHttpURLConnection.getOutputStream();
                InputStream inputStream = new ByteArrayInputStream(pRequest.getBody().getBytes(HttpContract.UTF_8));
                Utils.copyInputStreamInOutputStream(inputStream, stream);
            }
        } catch (Exception e) {
            Log.d(TAG, "SET CONNECTION PROPERTIES EXCEPTION");
        }
    }
}
