package com.example.yetti.toneplayer;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.json.JsonParserImpl;
import com.example.yetti.toneplayer.model.Song;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertEquals;

@Config(constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class JsonHandlerUnitTest {

    private List<Song> mTestSongList;

    @Before
    public void setup() {
        mTestSongList = TestModule.songList;
    }

    @Test
    public void jsonTest() throws Exception {
        JsonParserImpl jsonHandler = new JsonParserImpl();
        final String[] responseJson = {""};
        ICallbackResult<String> ConvertSongsToJsonCallback = new ICallbackResult<String>() {

            @Override
            public void onSuccess(String s) {
                responseJson[0] = s;
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        };
        jsonHandler.asyncConvertSongsToJson(mTestSongList, ConvertSongsToJsonCallback);
        Robolectric.flushBackgroundThreadScheduler();
        jsonHandler.asyncConvertJsonToSongs(responseJson[0], new ICallbackResult<List<Song>>() {

            @Override
            public void onSuccess(List<Song> songs) {
                assertEquals(mTestSongList.size(), songs.size());
                for (int i = 0; i < mTestSongList.size(); i++) {
                    assertEquals(TestModule.songComparator.compare(mTestSongList.get(i), songs.get(i)), 1);
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

}