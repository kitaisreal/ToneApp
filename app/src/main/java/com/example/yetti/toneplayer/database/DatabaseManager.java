package com.example.yetti.toneplayer.database;


import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.yetti.toneplayer.database.impl.AsyncDBServiceImpl;
import com.example.yetti.toneplayer.database.impl.DBServiceImpl;

public class DatabaseManager {
    private int mOpenCounter;
    private static DBToneHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    private static AsyncDBServiceImpl mAsyncDBService;
    private static volatile DatabaseManager sInstance;

    public static synchronized void initializeInstance(final DBToneHelper helper) {
        Log.d("DBMANAGER","INITIALIZEINSTANCe");
        if (sInstance == null) {
            sInstance = new DatabaseManager();
            mDatabaseHelper = helper;
            mAsyncDBService = new AsyncDBServiceImpl(new DBServiceImpl());
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized");
        }
        return sInstance;
    }

    public AsyncDBServiceImpl getAsyncDBService(){
        return mAsyncDBService;
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