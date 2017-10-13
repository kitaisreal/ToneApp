package com.example.yetti.toneplayer;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.media.session.MediaControllerCompat;
import android.util.Log;

import com.example.yetti.toneplayer.database.DBToneHelper;
import com.example.yetti.toneplayer.database.DatabaseManager;
import com.example.yetti.toneplayer.database.impl.SongDBServiceImpl;
import com.example.yetti.toneplayer.imageLoader.ImageLoader;
import com.example.yetti.toneplayer.service.MediaService;
import com.example.yetti.toneplayer.service.SongService;

public class CoreApplication extends Application {
    private SongService.myBinder mMyBinder;
    private Intent bindIntent;
    private boolean boundToService=false;
    private ServiceConnection sConn;
    private SongDBServiceImpl mSongDBService;
    private MediaService.MediaServiceBinder mPlayerServiceBinder;
    private MediaControllerCompat mMediaControllerCompat;
    private final String TAG="APPLICATION";

    public CoreApplication() {
        super();
        Log.d(TAG,"START APPLICATION CREATED");
        Log.d(TAG,"APPLICATION CREATED");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"START APPLICATION CREATED");
        ImageLoader.getInstance(this);
        DatabaseManager.initializeInstance(new DBToneHelper(this));
        bindIntent = new Intent(this, MediaService.class);
        bindServiceToApplication();
        mSongDBService = new SongDBServiceImpl();
        Log.d(TAG,"APPLICATION CREATED");
    }

    private void bindServiceToApplication(){
        sConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mPlayerServiceBinder = (MediaService.MediaServiceBinder) service;
                try{
                    boundToService=true;
                    mMediaControllerCompat = new MediaControllerCompat(CoreApplication.this, mPlayerServiceBinder.getMediaSessionToken());
                    Log.d(TAG,"CONTROLLER COMPAT " + mMediaControllerCompat);
                } catch (RemoteException ex){
                    mMediaControllerCompat=null;
                }
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mPlayerServiceBinder=null;
                mMediaControllerCompat=null;
            }
        };
        bindService(bindIntent, sConn, BIND_AUTO_CREATE);
    }
    public MediaControllerCompat getMediaControllerCompat(){
        if (boundToService){
            return mMediaControllerCompat;
        }
        return null;
    }
    public boolean getMediaServiceBound(){
        return boundToService;
    }

    public void unbindServiceOfApplication(){
        if (boundToService){
            unbindService(sConn);
            Log.d("OUR PROBLEM", "UNBIND SERVICE FROM APPLICATION");
        }
        boundToService=false;
    }
    public SongDBServiceImpl getSongDBService(){
        return mSongDBService;
    }
}
