package com.example.yetti.toneplayer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBToneHelper extends SQLiteOpenHelper {

    public DBToneHelper(Context context) {
        super(context, DBToneContract.DATABASE_NAME, null, DBToneContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBToneContract.SongEntry.SQL_CREATE_ENTRIES);
        db.execSQL(DBToneContract.PlaylistEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBToneContract.SongEntry.SQL_DELETE_ENTRIES);
        db.execSQL(DBToneContract.PlaylistEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
