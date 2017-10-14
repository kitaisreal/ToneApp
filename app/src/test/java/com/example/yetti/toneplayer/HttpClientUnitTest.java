package com.example.yetti.toneplayer;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.network.HttpClient;
import com.example.yetti.toneplayer.network.HttpContract;
import com.example.yetti.toneplayer.network.Request;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public class HttpClientUnitTest {
    private HttpClient httpClient;
    @Before
    public void setup() {
        httpClient = new HttpClient();
    }
    @Test
    public void testGET() throws Exception{
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        Request request = new Request.RequestBuilder(HttpContract.GET_TEST).
                headers(headers).
                method(HttpContract.GET_METHOD).build();
        httpClient.createAsyncRequest(request, new ICallbackResult<String>() {
            @Override
            public void onSuccess(String s) {
                assertEquals(s,TestModule.GET_TEST_METHOD_RESULT);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        Robolectric.flushBackgroundThreadScheduler();
    }
    @Test
    public void testPOST() throws Exception{
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        Request request = new Request.RequestBuilder(HttpContract.POST_TEST).
                body(TestModule.POST_STRING).
                method(HttpContract.POST_METHOD).
                headers(headers).build();
        httpClient.createAsyncRequest(request, new ICallbackResult<String>() {

            @Override
            public void onSuccess(String pS) {
                assertEquals(pS, TestModule.POST_TEST_METHOD_RESULT);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        Robolectric.flushBackgroundThreadScheduler();
    }
}
