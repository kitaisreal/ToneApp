package com.example.yetti.toneplayer.database;

import android.provider.BaseColumns;

import com.example.yetti.toneplayer.model.Artist;

import static com.example.yetti.toneplayer.database.DBToneContract.Database.COMMA_SEP;
import static com.example.yetti.toneplayer.database.DBToneContract.Database.INTEGER_TYPE;
import static com.example.yetti.toneplayer.database.DBToneContract.Database.TEXT_TYPE;

public final class DBToneContract {

    public interface Database {

        int DATABASE_VERSION = 1;
        String DATABASE_NAME = "tone.db";
        String TEXT_TYPE = " TEXT";
        String INTEGER_TYPE = " INTEGER";
        String COMMA_SEP = ",";
    }

    private DBToneContract() {
    }
    public interface ArtistEntry extends BaseColumns{
        String TABLE_NAME ="artists";
        String COLUMN_NAME_ARTIST_NAME="artist_name";
        String COLUMN_NAME_ARTIST_SONG_COUNT="artist_song_count";
        String COLUMN_NAME_ARTIST_GENRE="artist_genre";
        String COLUMN_NAME_ARTIST_ARTWORKURL="artist_artwork_url";
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + ArtistEntry.TABLE_NAME + " (" +
                        ArtistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        ArtistEntry.COLUMN_NAME_ARTIST_NAME + TEXT_TYPE + COMMA_SEP +
                        ArtistEntry.COLUMN_NAME_ARTIST_SONG_COUNT + INTEGER_TYPE + COMMA_SEP +
                        ArtistEntry.COLUMN_NAME_ARTIST_GENRE + TEXT_TYPE + COMMA_SEP +
                        ArtistEntry.COLUMN_NAME_ARTIST_ARTWORKURL + TEXT_TYPE + " )";
        String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + SongEntry.TABLE_NAME;
    }
    public interface SongEntry extends BaseColumns {

        String TABLE_NAME = "songs";
        String COLUMN_NAME_ENTRY_ID = "song_id";
        String COLUMN_NAME_SONG_TITLE = "song_title";
        String COLUMN_NAME_SONG_ARTIST = "song_artist";
        String COLUMN_NAME_SONG_ALBUM = "song_album";
        String COLUMN_NAME_SONG_ALBUM_ID = "song_album_id";
        String COLUMN_NAME_SONG_WEIGHT = "song_weight";
        String COLUMN_NAME_SONG_PLAYLIST = "song_playlist";
        String COLUMN_NAME_SONG_FAVOURITE = "song_favourite";
        String COLUMN_NAME_SONG_DURATION = "song_duration";
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + SongEntry.TABLE_NAME + " (" +
                        SongEntry._ID + " INTEGER PRIMARY KEY," +
                        SongEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                        SongEntry.COLUMN_NAME_SONG_TITLE + TEXT_TYPE + COMMA_SEP +
                        SongEntry.COLUMN_NAME_SONG_ARTIST + TEXT_TYPE + COMMA_SEP +
                        SongEntry.COLUMN_NAME_SONG_ALBUM + TEXT_TYPE + COMMA_SEP +
                        SongEntry.COLUMN_NAME_SONG_ALBUM_ID + INTEGER_TYPE + COMMA_SEP +
                        SongEntry.COLUMN_NAME_SONG_WEIGHT + INTEGER_TYPE + COMMA_SEP +
                        SongEntry.COLUMN_NAME_SONG_PLAYLIST + INTEGER_TYPE + COMMA_SEP +
                        SongEntry.COLUMN_NAME_SONG_FAVOURITE + INTEGER_TYPE + COMMA_SEP +
                        SongEntry.COLUMN_NAME_SONG_DURATION + INTEGER_TYPE + " )";
        String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + SongEntry.TABLE_NAME;
    }

    public interface PlaylistEntry extends BaseColumns {

        String TABLE_NAME = "playlist";
        String COLUMN_NAME_ENTRY_ID = "playlist_id";
        String COLUMN_NAME_PLAYLIST_NAME = "playlist_name";
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + PlaylistEntry.TABLE_NAME + " (" +
                        PlaylistEntry._ID + " INTEGER PRIMARY KEY," +
                        PlaylistEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                        PlaylistEntry.COLUMN_NAME_PLAYLIST_NAME + TEXT_TYPE + " )";
        String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + PlaylistEntry.TABLE_NAME;
    }

    public interface SQLTemplates {

        String SQL_SELECT_WHERE = "SELECT %s FROM %s WHERE %s" + "='" + "%s" + "'";
        ;
        String SQL_DISTINCT = "SELECT DISTINCT %s FROM %s";
    }
}
