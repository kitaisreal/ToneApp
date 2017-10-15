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

public class HttpClient {
    private final String TAG = "HTTP CLIENT";
    public void createAsyncRequest(final Request pRequest, final ICallbackResult<String> pICallbackResult) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    final String response = createRequest(pRequest);
                    pICallbackResult.onSuccess(response);
                } catch (final Exception ex) {
                    Log.d(TAG, "CREATE ASYNC REQUEST EXCEPTION");
                    if (pICallbackResult != null) {
                        final Exception exception = new Exception(TAG + "CREATE ASYNC REQUEST EXCEPTION");
                        pICallbackResult.onError(exception);
                    }
                }
            }
        }).start();
    }

    public String createRequest(final Request pRequest) {
        HttpURLConnection URLConnection = null;
        final URL url;
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
                final InputStream in = new BufferedInputStream(URLConnection.getInputStream());
                final ByteArrayOutputStream result = new ByteArrayOutputStream();
                final OutputStream outputStream = new BufferedOutputStream(result);
                Utils.copyInputStreamInOutputStream(in, outputStream);
                return result.toString(HttpContract.UTF_8);
            }
        } catch (final Exception ex) {
            Log.d(TAG, "CREATE REQUEST EXCEPTION");
        } finally {
            if (URLConnection != null) {
                URLConnection.disconnect();
            }
        }
        return null;
    }

    private void setConnectionProperties(final HttpURLConnection pHttpURLConnection, final Request pRequest) {
        try {
            pHttpURLConnection.setRequestMethod(pRequest.getMethod());
            if (pRequest.getHeaders() != null) {
                for (final String s : pRequest.getHeaders().keySet()) {
                    pHttpURLConnection.setRequestProperty(s, pRequest.getHeaders().get(s));
                }
            }
            if (pRequest.getBody() != null) {
                pHttpURLConnection.setDoOutput(true);
                pHttpURLConnection.setDoInput(true);
                final OutputStream stream = pHttpURLConnection.getOutputStream();
                final InputStream inputStream = new ByteArrayInputStream(pRequest.getBody().getBytes(HttpContract.UTF_8));
                Utils.copyInputStreamInOutputStream(inputStream, stream);
            }
        } catch (final Exception ex) {
            Log.d(TAG, "SET CONNECTION PROPERTIES EXCEPTION");
        }
    }
}
