package com.example.yetti.toneplayer.database.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.yetti.toneplayer.database.DBToneContract;
import com.example.yetti.toneplayer.database.DatabaseManager;
import com.example.yetti.toneplayer.database.SongService;
import com.example.yetti.toneplayer.model.Song;

import java.util.List;

public class SongServiceImpl implements SongService{
    @Override
    public void createSong(Song song) {
        SQLiteDatabase sqLiteDatabase= DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID,song.getSong_id());
        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST,song.getSong_artist());
        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE,song.getSong_name());
        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT,song.getSong_weight());
        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST,song.getSong_playlist());
        sqLiteDatabase.insert(DBToneContract.SongEntry.TABLE_NAME,null,values);
        sqLiteDatabase.close();
        DatabaseManager.getInstance().closeDatabase();
    }

    @Override
    public  void deleteSong(Long id) {

    }

    @Override
    public void updateSong(Song song) {
        SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST,song.getSong_artist());
        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE,song.getSong_name());
        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT,song.getSong_weight());
        values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST,song.getSong_playlist());
        sqLiteDatabase.update(DBToneContract.SongEntry.TABLE_NAME, values, DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID+"=?", new String[]{String.valueOf(song.getSong_id())});
        sqLiteDatabase.close();
        DatabaseManager.getInstance().closeDatabase();
    }

    @Override
    public Song getSongByID(Long id) {
        return null;
    }

    @Override
    public List<Song> getAllSongs() {
        SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        Cursor c = sqLiteDatabase.query(DBToneContract.SongEntry.TABLE_NAME, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID);
            int songArtist = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST);
            int songTitle = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE);
            int songWeight = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT);
            int songPlaylist = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST);
            System.out.println("READ FROM DATABASE ");
            do {
                Log.d("READ FROM DATABASE", "ID = " + c.getInt(idColIndex) + " song Artist " + c.getString(songArtist) + " song weight " + c.getInt(songWeight));
            } while (c.moveToNext());
        }
        c.close();
        sqLiteDatabase.close();
        DatabaseManager.getInstance().closeDatabase();
        return null;
    }

    @Override
    public List<Song> getSongsByPlaylist() {
        return null;
    }
}
