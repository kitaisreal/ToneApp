package com.example.yetti.toneplayer.database;


import android.provider.BaseColumns;

public final class DBToneContract {
    public static final  int    DATABASE_VERSION   = 1;
    public static final  String DATABASE_NAME      = "tone.db";
    private static final String TEXT_TYPE          = " TEXT";
    private static final String INTEGER_TYPE       = " INTEGER";
    private static final String COMMA_SEP          = ",";

    private DBToneContract() {}

    public static abstract class SongEntry implements BaseColumns {
        public static final String TABLE_NAME = "songs";
        public static final String COLUMN_NAME_ENTRY_ID = "song_id";
        public static final String COLUMN_NAME_SONG_TITLE = "song_title";
        public static final String COLUMN_NAME_SONG_ARTIST="song_artist";
        public static final String COLUMN_NAME_SONG_WEIGHT = "song_weight";
        public static final String COLUMN_NAME_SONG_PLAYLIST="song_playlist";
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + SongEntry.TABLE_NAME + " (" +
                        SongEntry._ID + " INTEGER PRIMARY KEY," +
                        SongEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                        SongEntry.COLUMN_NAME_SONG_TITLE + TEXT_TYPE + COMMA_SEP +
                        SongEntry.COLUMN_NAME_SONG_ARTIST+ TEXT_TYPE + COMMA_SEP +
                        SongEntry.COLUMN_NAME_SONG_WEIGHT + INTEGER_TYPE + COMMA_SEP+
                        SongEntry.COLUMN_NAME_SONG_PLAYLIST + INTEGER_TYPE+" )";
        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + SongEntry.TABLE_NAME;
    }
    public static abstract class PlaylistEntry implements BaseColumns{
        public static final String TABLE_NAME ="playlist";
        public static final String COLUMN_NAME_ENTRY_ID="playlist_id";
        public static final String COLUMN_NAME_PLAYLIST_NAME="playlist_name";
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + PlaylistEntry.TABLE_NAME + " (" +
                        PlaylistEntry._ID + " INTEGER PRIMARY KEY," +
                        PlaylistEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                        PlaylistEntry.COLUMN_NAME_PLAYLIST_NAME + TEXT_TYPE + COMMA_SEP +" )";
        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + PlaylistEntry.TABLE_NAME;
    }
}
