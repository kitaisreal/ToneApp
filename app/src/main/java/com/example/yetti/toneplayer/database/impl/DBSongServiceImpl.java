package com.example.yetti.toneplayer.database.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yetti.toneplayer.database.DBToneContract;
import com.example.yetti.toneplayer.database.DatabaseManager;
import com.example.yetti.toneplayer.database.IDBSongService;
import com.example.yetti.toneplayer.model.Artist;
import com.example.yetti.toneplayer.model.Song;

import java.util.ArrayList;
import java.util.List;

public class DBSongServiceImpl implements IDBSongService {

    @Override
    public boolean addSongs(final List<Song> pSongList) {
        try {
            final SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            for (final Song song : pSongList) {
                final ContentValues values = new ContentValues();
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
                final Cursor c = sqLiteDatabase.rawQuery(sqlToExecute, null);
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
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateSongs(final List<Song> pSongList) {
        try {
            final SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            for (final Song song : pSongList) {
                final ContentValues values = new ContentValues();
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
        } catch (final Exception ex) {
            return false;
        }
    }

    @Override
    public boolean deleteSongs(final List<Song> pSongList) {
        try {
            final SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            for (final Song song : pSongList) {
                final long id = song.getSongId();
                final String sqlToExecute = String.format(DBToneContract.SQLTemplates.SQL_SELECT_WHERE,
                        "*",
                        DBToneContract.SongEntry.TABLE_NAME,
                        DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID,
                        song.getSongId());
                final Cursor c = sqLiteDatabase.rawQuery(sqlToExecute, null);
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
        } catch (final Exception ex) {
            return false;
        }
    }

    @Override
    public boolean deleteSong(final Song pSong) {
        try {
            final SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            final long id = pSong.getSongId();
            final String sqlToExecute = String.format(DBToneContract.SQLTemplates.SQL_SELECT_WHERE,
                    "*",
                    DBToneContract.SongEntry.TABLE_NAME,
                    DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID,
                    pSong.getSongId());
            final Cursor c = sqLiteDatabase.rawQuery(sqlToExecute, null);
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
        } catch (final Exception ex) {
            return false;
        }
    }

    @Override
    public boolean updateSong(final Song pSong) {
        try {
            final SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            final ContentValues values = new ContentValues();
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
        } catch (final Exception ex) {
            return false;
        }
    }

    @Override
    public Song getSongByID(final Long pID) {
        try {
            final SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            final String sqlToExecute = String.format(DBToneContract.SQLTemplates.SQL_SELECT_WHERE,
                    "*",
                    DBToneContract.SongEntry.TABLE_NAME,
                    DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID,
                    pID);
            final Cursor c = sqLiteDatabase.rawQuery(sqlToExecute, null);
            if (c != null) {
                c.moveToFirst();
                final int idColIndex = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID);
                final int songArtist = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST);
                final int songTitle = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE);
                final int songAlbum = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM);
                final int songAlbumId = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM_ID);
                final int songWeight = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT);
                final int songPlaylist = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST);
                final int songFavourite = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_FAVOURITE);
                final int songDuration = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_DURATION);
                sqLiteDatabase.close();
                DatabaseManager.getInstance().closeDatabase();
                final Song song = new Song(c.getInt(idColIndex), c.getString(songArtist), c.getString(songTitle), c.getString(songAlbum), c.getInt(songAlbumId),
                        c.getInt(songWeight), c.getInt(songPlaylist), c.getInt(songFavourite), c.getInt(songDuration));
                c.close();
                return song;
            }
            sqLiteDatabase.close();
        } catch (final Exception ex) {
            return null;
        }
        return null;
    }

    @Override
    public List<Song> getAllSongs() {
        try {
            final SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            final Cursor c = sqLiteDatabase.query(DBToneContract.SongEntry.TABLE_NAME, null, null, null, null, null, null);
            final List<Song> songs = new ArrayList<>();
            if (c.moveToFirst()) {
                final int idColIndex = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID);
                final int songArtist = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST);
                final int songTitle = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE);
                final int songAlbum = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM);
                final int songAlbumId = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM_ID);
                final int songWeight = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT);
                final int songPlaylist = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST);
                final int songFavourite = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_FAVOURITE);
                final int songDuration = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_DURATION);
                do {
                    songs.add(new Song(c.getInt(idColIndex), c.getString(songArtist), c.getString(songTitle), c.getString(songAlbum), c.getInt(songAlbumId),
                            c.getInt(songWeight), c.getInt(songPlaylist), c.getInt(songFavourite), c.getInt(songDuration)));
                } while (c.moveToNext());
                c.close();
                sqLiteDatabase.close();
                DatabaseManager.getInstance().closeDatabase();
                return songs;
            }
            c.close();
        } catch (final Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public List<Song> getSongsByPlaylist(final int pSongPlaylist) {
        try {
            final SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            final String sqlToExecute = String.format(DBToneContract.SQLTemplates.SQL_SELECT_WHERE,
                    "*",
                    DBToneContract.SongEntry.TABLE_NAME,
                    DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST,
                    pSongPlaylist);
            final Cursor c = sqLiteDatabase.rawQuery(sqlToExecute, null);
            final List<Song> songs = new ArrayList<>();
            if (c.moveToFirst()) {
                final int idColIndex = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID);
                final int songArtist = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST);
                final int songTitle = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE);
                final int songAlbum = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM);
                final int songAlbumId = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM_ID);
                final int songWeight = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT);
                final int songPlaylist = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST);
                final int songFavourite = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_FAVOURITE);
                final int songDuration = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_DURATION);
                do {
                    songs.add(new Song(c.getInt(idColIndex), c.getString(songArtist), c.getString(songTitle), c.getString(songAlbum), c.getInt(songAlbumId),
                            c.getInt(songWeight), c.getInt(songPlaylist), c.getInt(songFavourite), c.getInt(songDuration)));
                } while (c.moveToNext());
                c.close();
                sqLiteDatabase.close();
                DatabaseManager.getInstance().closeDatabase();
                return songs;
            }
        } catch (final Exception ex) {
            return null;
        }
        return null;
    }
    @Override
    public List<Artist> getArtists() {
        try {
            final List<Artist> artistList = new ArrayList<>();
            final SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            final String sqlToExecute = String.format(DBToneContract.SQLTemplates.SQL_DISTINCT, DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST,
                    DBToneContract.SongEntry.TABLE_NAME);
            final Cursor c = sqLiteDatabase.rawQuery(sqlToExecute, null);
            final int artistColIndex = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST);
            if (c.moveToFirst()){
                do {
                    final Artist artist = new Artist();
                    artist.setArtistName(c.getString(artistColIndex));
                    artistList.add(artist);
                } while (c.moveToNext());
            }
            c.close();
            sqLiteDatabase.close();
            DatabaseManager.getInstance().closeDatabase();
            for (int i = 0; i < artistList.size(); i++) {
                artistList.get(i).setSongCount(getSongsByArtist(artistList.get(i).getArtistName()).size());
            }
            return artistList;
        } catch (final Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Song> getSongsByArtist(final String pArtistTitle) {
        try {
            System.out.println("PARTIST TO SELECT " + pArtistTitle);
            final SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            final String sqlToExecute = String.format(DBToneContract.SQLTemplates.SQL_SELECT_WHERE,
                    "*",
                    DBToneContract.SongEntry.TABLE_NAME,
                    DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST,
                    pArtistTitle);
            System.out.println("STRING TO EXECUTE " + sqlToExecute);
            final Cursor c = sqLiteDatabase.rawQuery(sqlToExecute, null);
            final List<Song> songs = new ArrayList<>();
            if (c.moveToFirst()) {
                final int idColIndex = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID);
                final int songArtist = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST);
                final int songTitle = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE);
                final int songAlbum = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM);
                final int songAlbumId = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM_ID);
                final int songWeight = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT);
                final int songPlaylist = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST);
                final int songFavourite = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_FAVOURITE);
                final int songDuration = c.getColumnIndex(DBToneContract.SongEntry.COLUMN_NAME_SONG_DURATION);
                do {
                    songs.add(new Song(c.getInt(idColIndex), c.getString(songArtist), c.getString(songTitle), c.getString(songAlbum), c.getInt(songAlbumId),
                            c.getInt(songWeight), c.getInt(songPlaylist), c.getInt(songFavourite), c.getInt(songDuration)));
                } while (c.moveToNext());
                for (Song s: songs){
                    System.out.println(s.getSongName() + " " + s.getSongAlbum());
                }
                c.close();
                sqLiteDatabase.close();
                DatabaseManager.getInstance().closeDatabase();
                return songs;
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return null;
    }
}
