package com.example.yetti.toneplayer.database;


import android.provider.BaseColumns;

public final class DBToneContract {
    static final  int    DATABASE_VERSION   = 1;
    static final String DATABASE_NAME      = "tone.db";
    private static final String TEXT_TYPE          = " TEXT";
    private static final String INTEGER_TYPE       = " INTEGER";
    private static final String COMMA_SEP          = ",";

    private DBToneContract() {}

    public static interface SongEntry extends BaseColumns {
        String TABLE_NAME = "songs";
        String COLUMN_NAME_ENTRY_ID = "song_id";
        String COLUMN_NAME_SONG_TITLE = "song_title";
        String COLUMN_NAME_SONG_ARTIST="song_artist";
        String COLUMN_NAME_SONG_ALBUM="song_album";
        String COLUMN_NAME_SONG_ALBUM_ID="song_album_id";
        String COLUMN_NAME_SONG_WEIGHT = "song_weight";
        String COLUMN_NAME_SONG_PLAYLIST="song_playlist";
        String COLUMN_NAME_SONG_FAVOURITE="song_favourite";
        String COLUMN_NAME_SONG_DURATION="song_duration";
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + SongEntry.TABLE_NAME + " (" +
                        SongEntry._ID + " INTEGER PRIMARY KEY," +
                        SongEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                        SongEntry.COLUMN_NAME_SONG_TITLE + TEXT_TYPE + COMMA_SEP +
                        SongEntry.COLUMN_NAME_SONG_ARTIST+ TEXT_TYPE + COMMA_SEP +
                        SongEntry.COLUMN_NAME_SONG_ALBUM+ TEXT_TYPE + COMMA_SEP +
                        SongEntry.COLUMN_NAME_SONG_ALBUM_ID+ INTEGER_TYPE + COMMA_SEP +
                        SongEntry.COLUMN_NAME_SONG_WEIGHT + INTEGER_TYPE + COMMA_SEP+
                        SongEntry.COLUMN_NAME_SONG_PLAYLIST+ INTEGER_TYPE + COMMA_SEP +
                        SongEntry.COLUMN_NAME_SONG_FAVOURITE + INTEGER_TYPE+COMMA_SEP+
                        SongEntry.COLUMN_NAME_SONG_DURATION+ INTEGER_TYPE +" )";
        String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + SongEntry.TABLE_NAME;
    }
    public interface PlaylistEntry extends BaseColumns {
        String TABLE_NAME ="playlist";
        String COLUMN_NAME_ENTRY_ID="playlist_id";
        String COLUMN_NAME_PLAYLIST_NAME="playlist_name";
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + PlaylistEntry.TABLE_NAME + " (" +
                        PlaylistEntry._ID + " INTEGER PRIMARY KEY," +
                        PlaylistEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                        PlaylistEntry.COLUMN_NAME_PLAYLIST_NAME + TEXT_TYPE +" )";
        String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + PlaylistEntry.TABLE_NAME;
    }
    public interface SQLTemplates {
        String SQL_SELECT_WHERE="SELECT %s FROM %s WHERE %s"+"='"+"%s"+"'";;
        String SQL_DISTINCT = "SELECT DISTINCT %s FROM %s";
    }
}
