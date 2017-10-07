package com.example.yetti.toneplayer.database.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.database.DBToneContract;
import com.example.yetti.toneplayer.database.DatabaseManager;
import com.example.yetti.toneplayer.database.IPlaylistService;
import com.example.yetti.toneplayer.model.Playlist;
import com.example.yetti.toneplayer.model.Song;

import java.util.ArrayList;
import java.util.List;

public class PlaylistServiceImpl implements IPlaylistService {

    @Override
    public void createPlaylist(final Playlist playlist, final ICallbackResult<Boolean> iCallbackResult) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    System.out.println("TRY TO CREATE PLAYLIST");
                    SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DBToneContract.PlaylistEntry.COLUMN_NAME_ENTRY_ID, playlist.getPlaylist_id());
                    values.put(DBToneContract.PlaylistEntry.COLUMN_NAME_PLAYLIST_NAME, playlist.getPlaylist_name());
                    Cursor c = sqLiteDatabase.rawQuery("select * from " + DBToneContract.PlaylistEntry.TABLE_NAME + " where " + DBToneContract.PlaylistEntry.COLUMN_NAME_ENTRY_ID
                            + "='" + playlist.getPlaylist_id() + "'", null);
                    if (c.moveToFirst()) {
                        c.close();
                    } else {
                        c.close();
                        sqLiteDatabase.insert(DBToneContract.PlaylistEntry.TABLE_NAME, null, values);
                    }
                    sqLiteDatabase.close();
                    DatabaseManager.getInstance().closeDatabase();
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    if (iCallbackResult != null) {
                        final Exception addSongsEx = new Exception("DB ADD SONGS EXCEPTION");
                        iCallbackResult.onFail(addSongsEx);
                    }
                    return null;
                }
            }
        }.execute();
    }

    @Override
    public void deletePlaylist(final Playlist playlist, final ICallbackResult<Boolean> iCallbackResult) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
                    long id = playlist.getPlaylist_id();
                    Cursor c = sqLiteDatabase.rawQuery("select * from " + DBToneContract.PlaylistEntry.TABLE_NAME + " where " + DBToneContract.PlaylistEntry.COLUMN_NAME_ENTRY_ID
                            + "='" + id + "'", null);
                    if (c.moveToFirst()) {
                        sqLiteDatabase.delete(DBToneContract.PlaylistEntry.TABLE_NAME, DBToneContract.PlaylistEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                                new String[]{Long.toString(id)});
                        c.close();
                    } else {
                        c.close();
                    }
                    sqLiteDatabase.close();
                    DatabaseManager.getInstance().closeDatabase();
                    return true;
                } catch (Exception e) {
                    if (iCallbackResult != null) {
                        final Exception deleteSongEx = new Exception("DB DELETE SONGS EXCEPTION");
                        iCallbackResult.onFail(deleteSongEx);
                    }
                    return null;
                }
            }
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (iCallbackResult != null) {
                    iCallbackResult.onSuccess(aBoolean);
                }
            }
        }.execute();
    }

    @Override
    public void getAllPlaylist(final ICallbackResult<List<Playlist>> iCallbackResult) {
        new AsyncTask<Void, Void, List<Playlist>>() {
            @Override
            protected List<Playlist> doInBackground(Void... params) {
                try {
                    SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
                    Cursor c = sqLiteDatabase.query(DBToneContract.PlaylistEntry.TABLE_NAME, null, null, null, null, null, null);
                    List<Playlist> playlists = new ArrayList<>();
                    if (c.moveToFirst()) {
                        int idColIndex = c.getColumnIndex(DBToneContract.PlaylistEntry.COLUMN_NAME_ENTRY_ID);
                        int playlistName = c.getColumnIndex(DBToneContract.PlaylistEntry.COLUMN_NAME_PLAYLIST_NAME);
                        do {
                            playlists.add(new Playlist(c.getInt(idColIndex),c.getString(playlistName)));
                        } while (c.moveToNext());
                        c.close();
                        sqLiteDatabase.close();
                        DatabaseManager.getInstance().closeDatabase();
                        return playlists;
                    }
                }
                catch (Exception e){
                    final Exception getSongByIdEx = new Exception("DB GET SONGS EXCEPTION");
                    if (iCallbackResult!=null) {
                        iCallbackResult.onFail(getSongByIdEx);
                    }
                    return null;
                }
                return null;
            }
            @Override
            protected void onPostExecute(List<Playlist> playlists){
                if (iCallbackResult!=null && playlists!=null) {
                    iCallbackResult.onSuccess(playlists);
                }
            }
        }.execute();
    }
}
