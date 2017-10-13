package com.example.yetti.toneplayer.database;


import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

//TODO FIELDS SERVICES
public class DatabaseManager {
    private int mOpenCounter;
    private static DatabaseManager sInstance;
    private static DBToneHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    public static synchronized void initializeInstance(DBToneHelper helper) {
        Log.d("DBMANAGER","INITIALIZEINSTANCe");
        if (sInstance == null) {
            sInstance = new DatabaseManager();
            mDatabaseHelper = helper;
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized");
        }
        return sInstance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        mOpenCounter++;
        if(mOpenCounter == 1) {
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        mOpenCounter--;
        if(mOpenCounter == 0) {
            mDatabase.close();

        }
    }

}