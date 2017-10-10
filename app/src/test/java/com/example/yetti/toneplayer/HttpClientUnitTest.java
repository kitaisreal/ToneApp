package com.example.yetti.toneplayer;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.database.DBToneHelper;
import com.example.yetti.toneplayer.database.DatabaseManager;
import com.example.yetti.toneplayer.database.impl.SongServiceImpl;
import com.example.yetti.toneplayer.model.Song;
import com.example.yetti.toneplayer.network.HttpClient;
import com.example.yetti.toneplayer.network.HttpContract;
import com.example.yetti.toneplayer.network.Request;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@Config(constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class HttpClientUnitTest {
    private HttpClient httpClient;
    @Before
    public void setup() {
        httpClient = new HttpClient();
    }
    @Test
    public void testGET() throws Exception{
        httpClient.createRequest(new Request(HttpContract.GET_TEST, HttpContract.GET_METHOD), new ICallbackResult<String>() {
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
        httpClient.createRequest(new Request(HttpContract.POST_TEST, HttpContract.POST_METHOD, TestModule.POST_STRING), new ICallbackResult<String>() {
            @Override
            public void onSuccess(String s) {
                assertEquals(s,TestModule.POST_TEST_METHOD_RESULT);
            }
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        Robolectric.flushBackgroundThreadScheduler();
    }
}
