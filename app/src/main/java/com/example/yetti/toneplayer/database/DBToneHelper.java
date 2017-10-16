package com.example.yetti.toneplayer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBToneHelper extends SQLiteOpenHelper {

    public DBToneHelper(final Context context) {
        super(context, DBToneContract.Database.DATABASE_NAME, null, DBToneContract.Database.DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(DBToneContract.SongEntry.SQL_CREATE_ENTRIES);
        db.execSQL(DBToneContract.ArtistEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL(DBToneContract.SongEntry.SQL_DELETE_ENTRIES);
        db.execSQL(DBToneContract.ArtistEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
