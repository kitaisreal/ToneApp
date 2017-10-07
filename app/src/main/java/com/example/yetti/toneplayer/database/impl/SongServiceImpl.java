package com.example.yetti.toneplayer.database.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.database.DBToneContract;
import com.example.yetti.toneplayer.database.DatabaseManager;
import com.example.yetti.toneplayer.database.ISongService;
import com.example.yetti.toneplayer.model.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SongServiceImpl implements ISongService {
    @Override
    public void addSongs(final List<Song> songs, final ICallbackResult<Boolean> iCallbackResult) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try{
                    SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
                    for (Song song:songs){
                        ContentValues values = new ContentValues();
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID, song.getSong_id());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST, song.getSong_artist());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE, song.getSong_name());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT, song.getSong_weight());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST, song.getSong_playlist());
                        Cursor c = sqLiteDatabase.rawQuery("select * from " + DBToneContract.SongEntry.TABLE_NAME + " where " + DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID
                                + "='" + song.getSong_id() + "'", null);
                        if (c.moveToFirst()) {
                            c.close();
                        } else{
                            c.close();
                            sqLiteDatabase.insert(DBToneContract.SongEntry.TABLE_NAME, null, values);
                        }
                    }
                    sqLiteDatabase.close();
                    DatabaseManager.getInstance().closeDatabase();
                    return true;
                }
                catch (Exception e){
                    e.printStackTrace();
                    if (iCallbackResult!=null) {
                        final Exception addSongsEx = new Exception("DB ADD SONGS EXCEPTION");
                        iCallbackResult.onFail(addSongsEx);
                    }
                    return null;
                }
            }
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (iCallbackResult!=null){
                    iCallbackResult.onSuccess(aBoolean);
                }
            }
        }.execute();
    }

    @Override
    public void updateSongs(final List<Song> songs,final ICallbackResult<Boolean> iCallbackResult) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try{
                    SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
                    for (Song song:songs) {
                        ContentValues values = new ContentValues();
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST, song.getSong_artist());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE, song.getSong_name());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT, song.getSong_weight());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST, song.getSong_playlist());
                        sqLiteDatabase.update(DBToneContract.SongEntry.TABLE_NAME, values, DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID + "=?", new String[]{String.valueOf(song.getSong_id())});
                    }

                    sqLiteDatabase.close();
                    DatabaseManager.getInstance().closeDatabase();
                    return true;
                }
                catch (Exception e){
                    if (iCallbackResult!=null) {
                        final Exception updateSong= new Exception("DB UPDATE SONGS EXCEPTION");
                        iCallbackResult.onFail(updateSong);
                    }
                    return null;
                }
            }
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (iCallbackResult!=null){
                    iCallbackResult.onSuccess(aBoolean);
                }
            }
        }.execute();
    }

    @Override
    public void deleteSongs(final List<Song> songs, final ICallbackResult<Boolean> iCallbackResult) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try{
                    SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
                    for (Song song : songs) {
                        long id = song.getSong_id();
                        Cursor c = sqLiteDatabase.rawQuery("select * from " + DBToneContract.SongEntry.TABLE_NAME + " where " + DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID
                                + "='" + id + "'", null);
                        if (c.moveToFirst()) {
                            sqLiteDatabase.delete(DBToneContract.SongEntry.TABLE_NAME, DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                                    new String[]{Long.toString(id)});
                            c.close();
                        } else{
                            c.close();
                        }
                    }
                    sqLiteDatabase.close();
                    DatabaseManager.getInstance().closeDatabase();
                    return true;
                }
                catch (Exception e){
                    if (iCallbackResult!=null) {
                        final Exception deleteSongEx = new Exception("DB DELETE SONGS EXCEPTION");
                        iCallbackResult.onFail(deleteSongEx);
                    }
                    return null;
                }
            }
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (iCallbackResult!=null){
                    iCallbackResult.onSuccess(aBoolean);
                }
            }
        }.execute();
    }

    @Override
    public void deleteSong(final Song song, final ICallbackResult<Boolean> iCallbackResult) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try{
                    SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
                    long id = song.getSong_id();
                    Cursor c = sqLiteDatabase.rawQuery("select * from " + DBToneContract.SongEntry.TABLE_NAME + " where " + DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID
                            + "='" + id + "'", null);
                    if (c.moveToFirst()) {
                        c.close();
                    } else{
                        sqLiteDatabase.delete(DBToneContract.SongEntry.TABLE_NAME, DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                                new String[]{Long.toString(id)});
                        c.close();
                    }
                    sqLiteDatabase.close();
                    DatabaseManager.getInstance().closeDatabase();
                    return true;
                }
                catch (Exception e){
                    if (iCallbackResult!=null) {
                        final Exception deleteSongEx = new Exception("DB DELETE SONG EXCEPTION");
                        iCallbackResult.onFail(deleteSongEx);
                    }
                    return null;
                }
            }
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (iCallbackResult!=null){
                    iCallbackResult.onSuccess(aBoolean);
                }
            }
        }.execute();
    }

    @Override
    public void updateSong(final Song song, final ICallbackResult<Boolean> iCallbackResult) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try{
                    SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
                    ContentValues values = new ContentValues();
                    values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST, song.getSong_artist());
                    values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE, song.getSong_name());
                    values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT, song.getSong_weight());
                    values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST, song.getSong_playlist());
                    sqLiteDatabase.update(DBToneContract.SongEntry.TABLE_NAME, values, DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID + "=?", new String[]{String.valueOf(song.getSong_id())});
                    sqLiteDatabase.close();
                    DatabaseManager.getInstance().closeDatabase();
                    return true;
                }
                catch (Exception e){
                    if (iCallbackResult!=null) {
                        final Exception updateSong= new Exception("DB UPDATE SONG EXCEPTION");
                        iCallbackResult.onFail(updateSong);
                    }
                    return null;
                }
            }
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (iCallbackResult!=null){
                    iCallbackResult.onSuccess(aBoolean);
                }
            }
        }.execute();
    }

    @Override
    public void  getSongByID(final Long id, final ICallbackResult<Song> iCallbackResult) {
        new AsyncTask<Void, Void, Song>() {
            @Override
            protected Song doInBackground(Void... params) {
                try {
                    SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
                    Cursor c = sqLiteDatabase.rawQuery("select * from " + DBToneContract.SongEntry.TABLE_NAME + " where " + DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID
                            + "='" + id + "'", null);
                    if (c != null) {
                        c.moveToFirst();
                        int idColIndex = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID);
                        int songArtist = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST);
                        int songTitle = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE);
                        int songWeight = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT);
                        int songPlaylist = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST);
                        sqLiteDatabase.close();
                        DatabaseManager.getInstance().closeDatabase();
                        Song song = new Song(c.getInt(idColIndex), c.getString(songArtist), c.getString(songTitle), c.getInt(songWeight), c.getInt(songPlaylist));
                        c.close();
                        return song;
                    }
                    sqLiteDatabase.close();
                }
                catch (Exception e){
                    final Exception getSongByIdEx = new Exception("DB GET SONG BY ID EXCEPTION");
                    iCallbackResult.onFail(getSongByIdEx);
                    return null;
                }
                return null;
            }
            @Override
            protected void onPostExecute(Song song){
                if (iCallbackResult!=null) {
                    iCallbackResult.onSuccess(song);
                }
            }
        }.execute();
    }
    @Override
    public void getAllSongs(final ICallbackResult<List<Song>> iCallbackResult) {
        new AsyncTask<Void, Void, List<Song>>() {
            @Override
            protected List<Song> doInBackground(Void... params) {
                try {
                    SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
                    Cursor c = sqLiteDatabase.query(DBToneContract.SongEntry.TABLE_NAME, null, null, null, null, null, null);
                    List<Song> songs = new ArrayList<>();
                    if (c.moveToFirst()) {
                        int idColIndex = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID);
                        int songArtist = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST);
                        int songTitle = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE);
                        int songWeight = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT);
                        int songPlaylist = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST);
                        do {
                            songs.add(new Song(c.getInt(idColIndex), c.getString(songArtist), c.getString(songTitle), c.getInt(songWeight), c.getInt(songPlaylist)));
                        } while (c.moveToNext());
                        c.close();
                        sqLiteDatabase.close();
                        DatabaseManager.getInstance().closeDatabase();
                        return songs;
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
            protected void onPostExecute(List<Song> songs){
                if (iCallbackResult!=null && songs!=null) {
                    iCallbackResult.onSuccess(songs);
                }
            }
        }.execute();
    }

    @Override
    public void getSongsByPlaylist(final int SongPlaylist, final ICallbackResult<List<Song>> iCallbackResult) {
        new AsyncTask<Void, Void, List<Song>>() {
            @Override
            protected List<Song> doInBackground(Void... params) {
                try {SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
                    Cursor c = sqLiteDatabase.query(DBToneContract.SongEntry.TABLE_NAME, null, null, null, null, null, null);
                    List<Song> songs = new ArrayList<>();
                    if (c.moveToFirst()) {
                        int idColIndex = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID);
                        int songArtist = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST);
                        int songTitle = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE);
                        int songWeight = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT);
                        int songPlaylist = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST);
                        do {
                            if (Objects.equals(c.getInt(songPlaylist), SongPlaylist)) {
                                songs.add(new Song(c.getInt(idColIndex), c.getString(songArtist), c.getString(songTitle), c.getInt(songWeight), c.getInt(songPlaylist)));
                            }
                        } while (c.moveToNext());
                        c.close();
                        sqLiteDatabase.close();
                        DatabaseManager.getInstance().closeDatabase();
                        return songs;
                    }
                }
                catch (Exception e){
                    final Exception getSongByIdEx = new Exception("DB GET SONGS BY PLAYLIST EXCEPTION");
                    if (iCallbackResult!=null) {
                        iCallbackResult.onFail(getSongByIdEx);
                    }
                    return null;
                }
                return null;
            }
            @Override
            protected void onPostExecute(List<Song> songs){
                if (iCallbackResult!=null) {
                    iCallbackResult.onSuccess(songs);
                }
            }
        }.execute();
    }
}
