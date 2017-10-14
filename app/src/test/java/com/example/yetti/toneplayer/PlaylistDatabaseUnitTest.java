package com.example.yetti.toneplayer;


import android.database.sqlite.SQLiteDatabase;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.database.DBToneContract;
import com.example.yetti.toneplayer.database.DBToneHelper;
import com.example.yetti.toneplayer.database.DatabaseManager;
import com.example.yetti.toneplayer.database.impl.PlaylistServiceImpl;
import com.example.yetti.toneplayer.model.Playlist;
import com.example.yetti.toneplayer.ui.MainActivity;

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
public class PlaylistDatabaseUnitTest {
    private MainActivity activity;
    private PlaylistServiceImpl mPlaylistService;
    private List<Playlist> mTestPlaylists;

    @Before
    public void setup() {
        mTestPlaylists = TestModule.playLists;
        activity = Robolectric.setupActivity(MainActivity.class);
        DatabaseManager.initializeInstance(new DBToneHelper(activity));
        mPlaylistService = new PlaylistServiceImpl();
    }

    @Test
    public void testAddGetPlaylist() throws Exception {
        final ICallbackResult<Boolean> testAddPlaylistICallbackResult = new ICallbackResult<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                assertEquals(aBoolean, true);
                System.out.println("TRUE");
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        };
        for (Playlist playlist : mTestPlaylists) {
            System.out.println(playlist.getPlaylist_id() + " " + playlist.getPlaylist_name());
            mPlaylistService.createPlaylist(playlist, testAddPlaylistICallbackResult);
        }
        Robolectric.flushBackgroundThreadScheduler();
        mPlaylistService.getAllPlaylist(new ICallbackResult<List<Playlist>>() {
            @Override
            public void onSuccess(List<Playlist> playlists) {
                System.out.println("PLAYLISTS SIZE");
                System.out.println(playlists.size());
                assertEquals(mTestPlaylists.size(), playlists.size());
                for (int i = 0; i < mTestPlaylists.size(); i++) {
                    System.out.println(mTestPlaylists.get(i).getPlaylist_id() + " " + mTestPlaylists.get(i).getPlaylist_id() + " ");
                    assertEquals(TestModule.playlistComparator.compare(mTestPlaylists.get(i), playlists.get(i)), 1);
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        Robolectric.flushBackgroundThreadScheduler();
        clearDBAfterTest();
    }

    @Test
    public void testDeletePlaylistDB() throws Exception {
        final ICallbackResult<Boolean> testAddPlaylistICallbackResult = new ICallbackResult<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                assertEquals(aBoolean, true);
                System.out.println("TRUE");
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        };
        for (Playlist playlist : mTestPlaylists) {
            System.out.println(playlist.getPlaylist_id() + " " + playlist.getPlaylist_name());
            mPlaylistService.createPlaylist(playlist, testAddPlaylistICallbackResult);
        }
        Robolectric.flushBackgroundThreadScheduler();
        final ICallbackResult<Boolean> deletePlaylistICallbackResult = new ICallbackResult<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                assertEquals(aBoolean, true);
                System.out.println("TRUE");
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        };
        final int firstIndex = 0;
        final int secondIndex = 5;
        for (Playlist playlist : mTestPlaylists.subList(firstIndex, secondIndex)) {
            mPlaylistService.deletePlaylist(playlist, deletePlaylistICallbackResult);
        }
        mPlaylistService.getAllPlaylist(new ICallbackResult<List<Playlist>>() {
            @Override
            public void onSuccess(List<Playlist> playlists) {
                List<Playlist> ourTestSublist = mTestPlaylists.subList(secondIndex, mTestPlaylists.size());
                assertEquals(ourTestSublist.size(), playlists.size());
                for (int i = 0; i < ourTestSublist.size(); i++) {
                    System.out.println(ourTestSublist.get(i).getPlaylist_id() + " " + ourTestSublist.get(i).getPlaylist_id() + " ");
                    assertEquals(TestModule.playlistComparator.compare(ourTestSublist.get(i), playlists.get(i)), 1);
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        clearDBAfterTest();
    }

    private void clearDBAfterTest() {
        SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        sqLiteDatabase.execSQL("delete from " + DBToneContract.PlaylistEntry.TABLE_NAME);
        sqLiteDatabase.close();
        DatabaseManager.getInstance().closeDatabase();
    }
}
