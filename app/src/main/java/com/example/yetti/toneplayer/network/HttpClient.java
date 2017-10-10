package com.example.yetti.toneplayer.network;

import android.os.AsyncTask;

import com.example.yetti.toneplayer.callback.ICallbackResult;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    //TODO CHANGE ASYNC
    public void createRequest(final Request pRequest, final ICallbackResult<String> pICallbackResult) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... pParams) {
                try {
                    String response = executeRequest(pRequest);
                    System.out.println("RESPONCE " + response);
                    return response;
                } catch (Exception e) {
                    System.out.println("CATCH EXCEPTION");
                    if (pICallbackResult != null) {
                        Exception exception = new Exception("REQUEST EXEPTION");
                        pICallbackResult.onError(exception);
                    }
                }
                return null;
            }
            @Override
            protected void onPostExecute(String s) {
                if (pICallbackResult != null && s != null) {
                    pICallbackResult.onSuccess(s);
                }
            }
        }.execute();
    }

    private String executeRequest(final Request pRequest) {
        HttpURLConnection URLConnection = null;
        URL url;
        System.out.println("EXECUTING REQUEST");
        try {
            url = new URL(pRequest.getUrl());
            System.out.println("URL PROTOCOL HTTP CLIENT " + url);
            URLConnection = (HttpURLConnection) url.openConnection();
            setConnectionProperties(URLConnection, pRequest);
            InputStream in = new BufferedInputStream(URLConnection.getInputStream());
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
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
            pHttpURLConnection.setRequestProperty("Content-Type", "application/json");
            if (pRequest.getBody() != null) {
                pHttpURLConnection.setDoOutput(true);
                pHttpURLConnection.setDoInput(true);
                final OutputStream stream;
                stream = pHttpURLConnection.getOutputStream();
                String requestBody = pRequest.getBody();
                final byte[] body = requestBody.getBytes("UTF-8");
                stream.write(body);
                stream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
