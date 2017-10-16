package com.example.yetti.toneplayer.database;


import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.yetti.toneplayer.database.impl.AsyncDBServiceImpl;
import com.example.yetti.toneplayer.database.impl.DBArtistServiceImpl;
import com.example.yetti.toneplayer.database.impl.DBSongServiceImpl;

public class DatabaseManager {

    private int mOpenCounter;
    private static DBToneHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    private static DBArtistServiceImpl sDBArtistService;
    private static DBSongServiceImpl sDBSongService;
    private static AsyncDBServiceImpl sAsyncDBService;
    private static volatile DatabaseManager sInstance;
    private static final String TAG="DBMANAGER";
    public static synchronized void initializeInstance(final DBToneHelper helper) {
        Log.d(TAG,"INITIALIZE INSTANCE");
        if (sInstance == null) {
            sInstance = new DatabaseManager();
            mDatabaseHelper = helper;
            sDBArtistService = new DBArtistServiceImpl();
            sDBSongService = new DBSongServiceImpl();
            sAsyncDBService = new AsyncDBServiceImpl(sDBSongService,sDBArtistService);
        }
    }
    public DBSongServiceImpl getDBSongService(){
        return sDBSongService;
    }
    public DBArtistServiceImpl getDBArtistService(){
        return sDBArtistService;
    }
    public static synchronized DatabaseManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized");
        }
        return sInstance;
    }

    public AsyncDBServiceImpl getAsyncDBService(){
        return sAsyncDBService;
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