package com.example.yetti.toneplayer.network;

import android.net.http.HttpResponseCache;
import android.os.AsyncTask;

import com.example.yetti.toneplayer.callback.ICallbackResult;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    public void createRequest(final Request request, final ICallbackResult<String> iCallbackResult) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    String responce = executeRequest(request);
                    System.out.println("RESPONCE " + responce);
                    return responce;
                } catch (Exception e) {
                    System.out.println("CATCH EXCEPTION");
                    if (iCallbackResult != null) {
                        Exception exception = new Exception("REQUEST EXEPTION");
                        iCallbackResult.onFail(exception);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if (iCallbackResult != null && s != null) {
                    iCallbackResult.onSuccess(s);
                }
            }
        }.execute();
    }

    private String executeRequest(final Request request) {
        HttpURLConnection httpURLConnection = null;
        URL url = null;
        System.out.println("EXECUTING REQUEST");
        try {
            url = new URL(request.getUrl());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            setConnectionProperties(httpURLConnection, request);
            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
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
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return null;
    }

    private void setConnectionProperties(HttpURLConnection urlConnection, Request request) {
        try {
            urlConnection.setRequestMethod(request.getMethod());
            urlConnection.setRequestProperty("Content-Type", "application/json");
            if (request.getBody() != null) {
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                final OutputStream stream;
                stream = urlConnection.getOutputStream();
                String requestBody = request.getBody();
                final byte[] body = requestBody.getBytes("UTF-8");
                stream.write(body);
                stream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
