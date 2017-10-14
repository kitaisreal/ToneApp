package com.example.yetti.toneplayer.database.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yetti.toneplayer.database.DBToneContract;
import com.example.yetti.toneplayer.database.DatabaseManager;
import com.example.yetti.toneplayer.database.IDBService;
import com.example.yetti.toneplayer.model.Artist;
import com.example.yetti.toneplayer.model.Song;

import java.util.ArrayList;
import java.util.List;

public class DBServiceImpl implements IDBService {

    @Override
    public boolean addSongs(List<Song> pSongList) {
        try {
            SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            for (Song song : pSongList) {
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
                final String sqlToExecute = String.format(DBToneContract.SQLTemplates.SQL_SELECT_WHERE,
                        "*",
                        DBToneContract.SongEntry.TABLE_NAME,
                        DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID,
                        song.getSongId());
                Cursor c = sqLiteDatabase.rawQuery(sqlToExecute, null);
                if (c.moveToFirst()) {
                    c.close();
                } else {
                    c.close();
                    sqLiteDatabase.insert(DBToneContract.SongEntry.TABLE_NAME, null, values);
                }
            }
            sqLiteDatabase.close();
            DatabaseManager.getInstance().closeDatabase();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateSongs(List<Song> pSongList) {
        try {
            SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            for (Song song : pSongList) {
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
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean deleteSongs(List<Song> pSongList) {
        try {
            SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            for (Song song : pSongList) {
                long id = song.getSongId();
                final String sqlToExecute = String.format(DBToneContract.SQLTemplates.SQL_SELECT_WHERE,
                        "*",
                        DBToneContract.SongEntry.TABLE_NAME,
                        DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID,
                        song.getSongId());
                Cursor c = sqLiteDatabase.rawQuery(sqlToExecute, null);
                if (c.moveToFirst()) {
                    sqLiteDatabase.delete(DBToneContract.SongEntry.TABLE_NAME, DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                            new String[]{Long.toString(id)});
                    c.close();
                } else {
                    c.close();
                }
            }
            sqLiteDatabase.close();
            DatabaseManager.getInstance().closeDatabase();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean deleteSong(Song pSong) {
        try {
            SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            long id = pSong.getSongId();
            final String sqlToExecute = String.format(DBToneContract.SQLTemplates.SQL_SELECT_WHERE,
                    "*",
                    DBToneContract.SongEntry.TABLE_NAME,
                    DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID,
                    pSong.getSongId());
            Cursor c = sqLiteDatabase.rawQuery(sqlToExecute, null);
            if (c.moveToFirst()) {
                c.close();
            } else {
                sqLiteDatabase.delete(DBToneContract.SongEntry.TABLE_NAME, DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                        new String[]{Long.toString(id)});
                c.close();
            }
            sqLiteDatabase.close();
            DatabaseManager.getInstance().closeDatabase();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean updateSong(Song pSong) {
        try {
            SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            ContentValues values = new ContentValues();
            values.put(DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID, pSong.getSongId());
            values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST, pSong.getSongArtist());
            values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE, pSong.getSongName());
            values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM, pSong.getSongAlbum());
            values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM_ID, pSong.getSongAlbumId());
            values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT, pSong.getSongWeight());
            values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST, pSong.getSongPlaylist());
            values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_FAVOURITE, pSong.getSongFavourite());
            values.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_DURATION, pSong.getSongDuration());
            sqLiteDatabase.update(DBToneContract.SongEntry.TABLE_NAME, values, DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID + "=?", new String[]{String.valueOf(pSong.getSongId())});
            sqLiteDatabase.close();
            DatabaseManager.getInstance().closeDatabase();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Song getSongByID(Long pID) {
        try {
            SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            final String sqlToExecute = String.format(DBToneContract.SQLTemplates.SQL_SELECT_WHERE,
                    "*",
                    DBToneContract.SongEntry.TABLE_NAME,
                    DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID,
                    pID);
            Cursor c = sqLiteDatabase.rawQuery(sqlToExecute, null);
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
                int songDuration = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_DURATION);
                sqLiteDatabase.close();
                DatabaseManager.getInstance().closeDatabase();
                Song song = new Song(c.getInt(idColIndex), c.getString(songArtist), c.getString(songTitle), c.getString(songAlbum), c.getInt(songAlbumId),
                        c.getInt(songWeight), c.getInt(songPlaylist), c.getInt(songFavourite), c.getInt(songDuration));
                c.close();
                return song;
            }
            sqLiteDatabase.close();
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public List<Song> getAllSongs() {
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
                int songDuration = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_DURATION);
                do {
                    songs.add(new Song(c.getInt(idColIndex), c.getString(songArtist), c.getString(songTitle), c.getString(songAlbum), c.getInt(songAlbumId),
                            c.getInt(songWeight), c.getInt(songPlaylist), c.getInt(songFavourite), c.getInt(songDuration)));
                } while (c.moveToNext());
                c.close();
                sqLiteDatabase.close();
                DatabaseManager.getInstance().closeDatabase();
                return songs;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public List<Song> getSongsByPlaylist(int pSongPlaylist) {
        try {
            SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            final String sqlToExecute = String.format(DBToneContract.SQLTemplates.SQL_SELECT_WHERE,
                    "*",
                    DBToneContract.SongEntry.TABLE_NAME,
                    DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST,
                    pSongPlaylist);
            Cursor c = sqLiteDatabase.rawQuery(sqlToExecute, null);
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
                int songDuration = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_DURATION);
                do {
                    songs.add(new Song(c.getInt(idColIndex), c.getString(songArtist), c.getString(songTitle), c.getString(songAlbum), c.getInt(songAlbumId),
                            c.getInt(songWeight), c.getInt(songPlaylist), c.getInt(songFavourite), c.getInt(songDuration)));
                } while (c.moveToNext());
                c.close();
                sqLiteDatabase.close();
                DatabaseManager.getInstance().closeDatabase();
                return songs;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public List<Artist> getArtists() {
        return null;
    }

    @Override
    public List<Song> getSongsByArtist(String pArtistTitle) {
        try {
            SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            final String sqlToExecute = String.format(DBToneContract.SQLTemplates.SQL_SELECT_WHERE,
                    "*",
                    DBToneContract.SongEntry.TABLE_NAME,
                    DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST,
                    pArtistTitle);
            Cursor c = sqLiteDatabase.rawQuery(sqlToExecute, null);
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
                int songDuration = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_DURATION);
                do {
                    songs.add(new Song(c.getInt(idColIndex), c.getString(songArtist), c.getString(songTitle), c.getString(songAlbum), c.getInt(songAlbumId),
                            c.getInt(songWeight), c.getInt(songPlaylist), c.getInt(songFavourite), c.getInt(songDuration)));
                } while (c.moveToNext());
                for (Song song : songs) {
                    System.out.println(song.getSongAlbum()+" "+song.getSongId());
                }
                c.close();
                sqLiteDatabase.close();
                DatabaseManager.getInstance().closeDatabase();
                return songs;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
