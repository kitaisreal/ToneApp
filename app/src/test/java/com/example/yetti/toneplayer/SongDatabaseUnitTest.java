package com.example.yetti.toneplayer;

import android.database.sqlite.SQLiteDatabase;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.database.DBToneContract;
import com.example.yetti.toneplayer.database.DBToneHelper;
import com.example.yetti.toneplayer.database.DatabaseManager;
import com.example.yetti.toneplayer.database.impl.SongServiceImpl;
import com.example.yetti.toneplayer.model.Playlist;
import com.example.yetti.toneplayer.model.Song;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;


@Config(constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class SongDatabaseUnitTest {
    private MainActivity activity;
    private SongServiceImpl songService;
    private List<Song> mTestSongList;
    @Before
    public void setup() {
        mTestSongList= TestModule.songList;
        activity = Robolectric.setupActivity(MainActivity.class);
        DatabaseManager.initializeInstance(new DBToneHelper(activity));
        songService = new SongServiceImpl();
    }
    @Test
    public void testAddGetSongs() throws Exception {
        songService.addSongs(mTestSongList, new ICallbackResult<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                System.out.println("ADDED");
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        });
        Robolectric.flushBackgroundThreadScheduler();
        songService.getAllSongs(new ICallbackResult<List<Song>>() {
            @Override
            public void onSuccess(List<Song> songs) {
                assertEquals(mTestSongList.size(),songs.size());
                for (int i = 0; i< mTestSongList.size(); i++) {
                    System.out.println(mTestSongList.get(i).getSong_name()+" "+ mTestSongList.get(i).getSong_artist()+" ");
                    assertEquals(TestModule.comparator.compare(mTestSongList.get(i), songs.get(i)),1);
                }
            }

            @Override
            public void onFail(Exception e) {

            }
        });
        Robolectric.flushBackgroundThreadScheduler();
        clearDBAfterTest();
    }
    @Test
    public void testDeleteSongs() throws Exception{
        final int firstIndex=0;
        final int secondIndex=5;
        final ICallbackResult<List<Song>> testICallBackResult = new ICallbackResult<List<Song>>() {
            @Override
            public void onSuccess(List<Song> songs) {
                List<Song> testSubList = mTestSongList.subList(secondIndex,mTestSongList.size());
                System.out.println("TEST DELETE SONGS");
                for (Song s: songs){
                    System.out.println(s.getSong_id() + " " + s.getSong_artist() + " " + s.getSong_name());
                }
                System.out.println("OUR TEST SUBLIST");
                for (Song s:testSubList){
                    System.out.println(s.getSong_id() + " " + s.getSong_artist() + " " + s.getSong_name());
                }
                assertEquals(testSubList.size(),songs.size());
                for (int i = 0; i<testSubList.size(); i++) {
                    assertEquals(TestModule.comparator.compare(testSubList.get(i), songs.get(i)),1);
                }
            }
            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        };
        songService.addSongs(mTestSongList, new ICallbackResult<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                songService.deleteSongs(mTestSongList.subList(firstIndex, secondIndex), new ICallbackResult<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        songService.getAllSongs(testICallBackResult);
                    }

                    @Override
                    public void onFail(Exception e) {

                    }
                });
            }
            @Override
            public void onFail(Exception e) {

            }
        });
        Robolectric.flushBackgroundThreadScheduler();
        clearDBAfterTest();
    }
    private void printList(List<Song>songs){
        for (Song song : songs) {
            System.out.println(song.getSong_id() + " " + song.getSong_artist());
        }
    }
    private Song cloneSongForUpdateTest(Song song,String testSongArtist){
        return new Song(song.getSong_id(),testSongArtist, song.getSong_name(), song.getSong_weight(), song.getSong_playlist());
    }
    @Test
    public void testUpdateSongs() throws Exception {
        final String testSongArtist="TESTARTIST";
        final int firstIndex=0;
        final int secondIndex=5;
        final List<Song> ourTestSublist = new ArrayList<>();
        for (Song song : mTestSongList.subList(firstIndex,secondIndex)) {
            ourTestSublist.add(cloneSongForUpdateTest(song,testSongArtist));
        }
        System.out.println("TEST SUBLISt");
        printList(ourTestSublist);
        System.out.println("OUT LIST");
        printList(mTestSongList);
        final ICallbackResult<List<Song>> testCallBackUpdateSongs = new ICallbackResult<List<Song>>() {
            @Override
            public void onSuccess(List<Song> songs) {
                List<Song> getFromDbUpdateSongs = new ArrayList<>();
                for (Song song : songs) {
                    if (Objects.equals(song.getSong_artist(), testSongArtist)){
                        getFromDbUpdateSongs.add(song);
                    }
                }
                assertEquals(getFromDbUpdateSongs.size(),ourTestSublist.size());
                for (int i = 0; i<ourTestSublist.size(); i++) {
                    assertEquals(TestModule.comparator.compare(ourTestSublist.get(i), songs.get(i)),1);
                }

            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        };
        songService.addSongs(mTestSongList, new ICallbackResult<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                songService.updateSongs(ourTestSublist, new ICallbackResult<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        songService.getAllSongs(testCallBackUpdateSongs);
                    }

                    @Override
                    public void onFail(Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        });
        Robolectric.flushBackgroundThreadScheduler();
        clearDBAfterTest();
    }
    @Test
    public void testGetByPlaylistSongs() throws Exception{
        int firstIndex=0;
        int secondIndex=5;
        final int testPlayListId = 123;
        Playlist playlist = new Playlist(testPlayListId,"TESTPLAYLIST");
        final List<Song> ourTestSubList = mTestSongList.subList(firstIndex,secondIndex);
        for (Song song : ourTestSubList) {
            song.setSong_playlist(playlist.getPlaylist_id());
        }
        songService.addSongs(mTestSongList, new ICallbackResult<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                songService.getSongsByPlaylist(testPlayListId, new ICallbackResult<List<Song>>() {
                    @Override
                    public void onSuccess(List<Song> songs) {
                        assertEquals(songs.size(),ourTestSubList.size());
                        for (int i = 0; i<ourTestSubList.size(); i++) {
                            assertEquals(TestModule.comparator.compare(ourTestSubList.get(i), songs.get(i)),1);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        });
        Robolectric.flushBackgroundThreadScheduler();
        clearDBAfterTest();

    }
    private void clearDBAfterTest(){
        SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        sqLiteDatabase.execSQL("delete from "+ DBToneContract.SongEntry.TABLE_NAME);
        sqLiteDatabase.close();
        DatabaseManager.getInstance().closeDatabase();
    }
}
