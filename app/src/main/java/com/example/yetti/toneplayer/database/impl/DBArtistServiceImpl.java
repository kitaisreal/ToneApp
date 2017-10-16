package com.example.yetti.toneplayer.database.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yetti.toneplayer.database.DBToneContract;
import com.example.yetti.toneplayer.database.DatabaseManager;
import com.example.yetti.toneplayer.database.IDBArtistService;
import com.example.yetti.toneplayer.model.Artist;

import java.util.ArrayList;
import java.util.List;

public class DBArtistServiceImpl implements IDBArtistService {

    @Override
    public boolean addArtists(List<Artist> pArtistList) {
        try {
            final SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            for (final Artist artist : pArtistList) {
                final ContentValues values = new ContentValues();
                values.put(DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_NAME, artist.getArtistName());
                values.put(DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_SONG_COUNT, artist.getSongCount());
                values.put(DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_GENRE, artist.getArtistGenre());
                values.put(DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_ARTWORKURL, artist.getArtistArtUrl());
                final String sqlToExecute = String.format(DBToneContract.SQLTemplates.SQL_SELECT_WHERE,
                        "*",
                        DBToneContract.ArtistEntry.TABLE_NAME,
                        DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_NAME,
                        artist.getArtistName());
                final Cursor c = sqLiteDatabase.rawQuery(sqlToExecute, null);
                if (c.moveToFirst()) {
                    c.close();
                } else {
                    c.close();
                    sqLiteDatabase.insert(DBToneContract.ArtistEntry.TABLE_NAME, null, values);
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
    public boolean deleteArtists(List<Artist> pArtistList) {
        final SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        for (final Artist artist : pArtistList) {
            final String artistName = artist.getArtistName();
            final String sqlToExecute = String.format(DBToneContract.SQLTemplates.SQL_SELECT_WHERE,
                    "*",
                    DBToneContract.SongEntry.TABLE_NAME,
                    DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID,
                    artist.getArtistName());
            final Cursor c = sqLiteDatabase.rawQuery(sqlToExecute, null);
            if (c.moveToFirst()) {
                sqLiteDatabase.delete(DBToneContract.SongEntry.TABLE_NAME, DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_NAME + " = ?",
                        new String[]{artistName});
                c.close();
            } else {
                c.close();
            }
        }
        sqLiteDatabase.close();
        DatabaseManager.getInstance().closeDatabase();
        return true;
    }

    @Override
    public boolean updateArtist(Artist pArtist) {
        final SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        try {
            System.out.println("DATABASE " + pArtist.toString());
            final ContentValues values = new ContentValues();
            values.put(DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_NAME, pArtist.getArtistName());
            values.put(DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_SONG_COUNT, pArtist.getSongCount());
            values.put(DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_GENRE, pArtist.getArtistGenre());
            values.put(DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_ARTWORKURL, pArtist.getArtistArtUrl());
            sqLiteDatabase.update(DBToneContract.ArtistEntry.TABLE_NAME, values, DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_NAME + "=?",
                    new String[]{pArtist.getArtistName()});
            sqLiteDatabase.close();
            DatabaseManager.getInstance().closeDatabase();
            return true;
        } catch (final Exception ex) {
            sqLiteDatabase.close();
            DatabaseManager.getInstance().closeDatabase();
            return false;
        }
    }

    @Override
    public Artist getArtistByName(String pArtistName) {
        try {
            final SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            final String sqlToExecute = String.format(DBToneContract.SQLTemplates.SQL_SELECT_WHERE,
                    "*",
                    DBToneContract.ArtistEntry.TABLE_NAME,
                    DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_NAME,
                    pArtistName);
            final Cursor c = sqLiteDatabase.rawQuery(sqlToExecute, null);
            if (c != null) {
                c.moveToFirst();
                final int artistNameIndex = c.getColumnIndex(DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_NAME);
                final int artistSongCount = c.getColumnIndex(DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_SONG_COUNT);
                final int artistGenre = c.getColumnIndex(DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_GENRE);
                final int artistArtworkUrl = c.getColumnIndex(DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_ARTWORKURL);
                sqLiteDatabase.close();
                DatabaseManager.getInstance().closeDatabase();
                final Artist artist = new Artist(c.getString(artistNameIndex), c.getInt(artistSongCount), c.getString(artistGenre), c.getString(artistArtworkUrl));
                c.close();
                return artist;
            }
            sqLiteDatabase.close();
        } catch (final Exception ex) {
            return null;
        }
        return null;
    }

    @Override
    public List<Artist> getArtists() {
        try {
            final SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
            final Cursor c = sqLiteDatabase.query(DBToneContract.ArtistEntry.TABLE_NAME, null, null, null, null, null, null);
            final List<Artist> artistList = new ArrayList<>();
            if (c.moveToFirst()) {
                final int artistNameIndex = c.getColumnIndex(DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_NAME);
                final int artistSongCount = c.getColumnIndex(DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_SONG_COUNT);
                final int artistGenre = c.getColumnIndex(DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_GENRE);
                final int artistArtworkUrl = c.getColumnIndex(DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_ARTWORKURL);
                do {
                    artistList.add(new Artist(c.getString(artistNameIndex), c.getInt(artistSongCount), c.getString(artistArtworkUrl), c.getString(artistGenre)));
                } while (c.moveToNext());
                c.close();
                sqLiteDatabase.close();
                DatabaseManager.getInstance().closeDatabase();
                return artistList;
            }
            c.close();
        } catch (final Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return null;
    }
}
