package com.example.yetti.toneplayer;

import android.os.Build;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.database.DBToneHelper;
import com.example.yetti.toneplayer.database.DatabaseManager;
import com.example.yetti.toneplayer.database.impl.SongServiceImpl;
import com.example.yetti.toneplayer.json.JsonHandler;
import com.example.yetti.toneplayer.model.Song;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@Config(constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class ExampleUnitTest {
    private MainActivity activity;
    private SongServiceImpl songService;
    @Before
    public void setup() {
        activity = Robolectric.setupActivity(MainActivity.class);
        songService = new SongServiceImpl();

    }
    @Test
    public void jsonTest() throws Exception{
        JsonHandler jsonHandler = new JsonHandler();
        Song s = new Song(1,"test1Artist","test1Name",1,1);
        Song s1 = new Song(2,"test2Artist","test2Name",2,2);
        final List<Song> list = new ArrayList<>();
        list.add(s);
        list.add(s1);
        final String[] responseJson = {""};
        ICallbackResult<String> ConvertSongsToJsonCallback = new ICallbackResult<String>() {
            @Override
            public void onSuccess(String s) {
                responseJson[0] =s;
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        };
        jsonHandler.ConvertSongsToJson(list,ConvertSongsToJsonCallback);
        Robolectric.flushBackgroundThreadScheduler();
        jsonHandler.ConvertJsonToSongs(responseJson[0], new ICallbackResult<List<Song>>() {
            @Override
            public void onSuccess(List<Song> songs) {
                Comparator<Song> comparator = new Comparator<Song>() {
                    @Override
                    public int compare(Song o1, Song o2) {
                        boolean test = o1.getSong_id()==o2.getSong_id() && o1.getSong_artist().equals(o2.getSong_artist()) && o1.getSong_name().equals(o2.getSong_name())
                                && o1.getSong_weight()==o2.getSong_weight() && o1.getSong_playlist()== o2.getSong_playlist();
                        if (test){
                            return 1;
                        } else{
                            return -1;
                        }
                    }
                };
                assertEquals(list.size(),songs.size());
                for (int i=0;i<list.size();i++) {
                    assertEquals(comparator.compare(list.get(i), songs.get(i)),1);
                }
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println(responseJson[0]);
    }
    @Test
    public void addition_isCorrect() throws Exception {
        DatabaseManager.initializeInstance(new DBToneHelper(activity));
        final SongServiceImpl songService = new SongServiceImpl();
        Song s = new Song(1,"Test","Test",1,1);
        List<Song> list = new ArrayList<>();
        list.add(s);
        songService.addSongs((ArrayList<Song>) list, new ICallbackResult<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {

            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        });
        Robolectric.flushBackgroundThreadScheduler();
    }
}