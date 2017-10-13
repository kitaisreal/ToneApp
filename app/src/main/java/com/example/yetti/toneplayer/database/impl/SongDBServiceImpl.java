package com.example.yetti.toneplayer.database.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.database.DBToneContract;
import com.example.yetti.toneplayer.database.DatabaseManager;
import com.example.yetti.toneplayer.database.ISongDBService;
import com.example.yetti.toneplayer.model.Song;

import java.util.ArrayList;
import java.util.List;

//TODO CHANGE ASYNC -> THREAD PULL OR JUST RUNNABLE
public class SongDBServiceImpl implements ISongDBService {
    @Override
    public void addSongs(final List<Song> songs, final ICallbackResult<Boolean> iCallbackResult) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try{
                    SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();

                    for (Song song:songs){
                        ContentValues values = new ContentValues();
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID, song.getSongId());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST, song.getSongArtist());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE, song.getSongName());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM, song.getSongAlbum());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM_ID, song.getSongAlbumId());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT, song.getSongWeight());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST, song.getSongPlaylist());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_FAVOURITE, song.getSongFavourite());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_DURATION, song.getSongDuration());
                        Cursor c = sqLiteDatabase.rawQuery("select * from " + DBToneContract.SongEntry.TABLE_NAME + " where " + DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID
                                + "='" + song.getSongId() + "'", null);
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
                        iCallbackResult.onError(addSongsEx);
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
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID, song.getSongId());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST, song.getSongArtist());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE, song.getSongName());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM, song.getSongAlbum());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM_ID, song.getSongAlbumId());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT, song.getSongWeight());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST, song.getSongPlaylist());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_FAVOURITE, song.getSongFavourite());
                        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_DURATION, song.getSongDuration());
                        sqLiteDatabase.update(DBToneContract.SongEntry.TABLE_NAME, values, DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID + "=?", new String[]{String.valueOf(song.getSongId())});
                    }

                    sqLiteDatabase.close();
                    DatabaseManager.getInstance().closeDatabase();
                    return true;
                }
                catch (Exception e){
                    if (iCallbackResult!=null) {
                        final Exception updateSong= new Exception("DB UPDATE SONGS EXCEPTION");
                        iCallbackResult.onError(updateSong);
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
                        long id = song.getSongId();
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
                        iCallbackResult.onError(deleteSongEx);
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
                    long id = song.getSongId();
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
                        iCallbackResult.onError(deleteSongEx);
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
                    values.put(DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID, song.getSongId());
                    values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST, song.getSongArtist());
                    values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE, song.getSongName());
                    values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM, song.getSongAlbum());
                    values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM_ID, song.getSongAlbumId());
                    values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT, song.getSongWeight());
                    values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST, song.getSongPlaylist());
                    values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_FAVOURITE, song.getSongFavourite());
                    values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_DURATION, song.getSongDuration());
                    sqLiteDatabase.update(DBToneContract.SongEntry.TABLE_NAME, values, DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID + "=?", new String[]{String.valueOf(song.getSongId())});
                    sqLiteDatabase.close();
                    DatabaseManager.getInstance().closeDatabase();
                    return true;
                }
                catch (Exception e){
                    if (iCallbackResult!=null) {
                        final Exception updateSong= new Exception("DB UPDATE SONG EXCEPTION");
                        iCallbackResult.onError(updateSong);
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
                        int songAlbum = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM);
                        int songAlbumId = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM_ID);
                        int songWeight = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT);
                        int songPlaylist = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST);
                        int songFavourite = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_FAVOURITE);
                        int songDuration= c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_DURATION);
                        sqLiteDatabase.close();
                        DatabaseManager.getInstance().closeDatabase();
                        Song song = new Song(c.getInt(idColIndex), c.getString(songArtist), c.getString(songTitle), c.getString(songAlbum), c.getInt(songAlbumId),
                                c.getInt(songWeight), c.getInt(songPlaylist), c.getInt(songFavourite), c.getInt(songDuration));
                        c.close();
                        return song;
                    }
                    sqLiteDatabase.close();
                }
                catch (Exception e){
                    final Exception getSongByIdEx = new Exception("DB GET SONG BY ID EXCEPTION");
                    iCallbackResult.onError(getSongByIdEx);
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
                        int songAlbum = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM);
                        int songAlbumId = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM_ID);
                        int songWeight = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT);
                        int songPlaylist = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST);
                        int songFavourite = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_FAVOURITE);
                        int songDuration= c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_DURATION);
                        System.out.println("SONG ARTIST " + c.getString(songArtist));
                        System.out.println("SONG ALBUM " + c.getString(songAlbum));
                        do {
                            songs.add(new Song(c.getInt(idColIndex), c.getString(songArtist), c.getString(songTitle), c.getString(songAlbum), c.getInt(songAlbumId),
                                    c.getInt(songWeight), c.getInt(songPlaylist), c.getInt(songFavourite), c.getInt(songDuration)));
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
                        iCallbackResult.onError(getSongByIdEx);
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
                    Cursor c = sqLiteDatabase.rawQuery("select * from " + DBToneContract.SongEntry.TABLE_NAME + " where " +
                            DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST+ "='" + SongPlaylist + "'", null);
                    List<Song> songs = new ArrayList<>();
                    if (c.moveToFirst()) {
                        int idColIndex = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID);
                        int songArtist = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST);
                        int songTitle = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE);
                        int songAlbum = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM);
                        int songAlbumId = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM_ID);
                        int songWeight = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT);
                        int songPlaylist = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST);
                        int songFavourite = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_FAVOURITE);
                        int songDuration= c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_DURATION);
                        do {
                                songs.add(new Song(c.getInt(idColIndex), c.getString(songArtist), c.getString(songTitle), c.getString(songAlbum), c.getInt(songAlbumId),
                                        c.getInt(songWeight), c.getInt(songPlaylist), c.getInt(songFavourite), c.getInt(songDuration)));
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
                        iCallbackResult.onError(getSongByIdEx);
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
