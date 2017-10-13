package com.example.yetti.toneplayer;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.media.session.MediaControllerCompat;
import android.util.Log;
import android.widget.MediaController;

import com.example.yetti.toneplayer.database.DBToneHelper;
import com.example.yetti.toneplayer.database.DatabaseManager;
import com.example.yetti.toneplayer.database.impl.SongDBServiceImpl;
import com.example.yetti.toneplayer.imageLoader.ImageLoader;
import com.example.yetti.toneplayer.service.MediaService;
import com.example.yetti.toneplayer.service.SongService;

public class MainApplication extends Application {
    private SongService.myBinder mMyBinder;
    private Intent bindIntent;
    private boolean boundToService=false;
    private ServiceConnection sConn;
    private SongDBServiceImpl mSongDBService;
    private MediaService.MediaServiceBinder mPlayerServiceBinder;
    private MediaControllerCompat mMediaControllerCompat;
    private final String TAG="APPLICATION";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"START APPLICATION CREATED");
        ImageLoader.getInstance(this);
        DatabaseManager.initializeInstance(new DBToneHelper(this));
        bindIntent = new Intent(this, MediaService.class);
        //bindServiceToApplication();
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
                    mMediaControllerCompat = new MediaControllerCompat(MainApplication.this, mPlayerServiceBinder.getMediaSessionToken());
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
    };
    public boolean getMediaServiceBound(){
        return boundToService;
    }
    /*
    private void bindServiceToApplication() {
        sConn = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder binder) {
                Log.d(TAG, TAG+"onServiceConnected");
                mMyBinder = (SongService.myBinder) binder;
                boundToService = true;
                System.out.println(mMyBinder.getService());
            }
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, TAG+"MainActivity onServiceDisconnected");
                boundToService = false;
            }
        };
        bindService(bindIntent, sConn, BIND_AUTO_CREATE);
    }
    public void unbindServiceOfApplication(){
        if (boundToService){
            unbindService(sConn);
        }
        boundToService=false;
    }
    public boolean getSongServiceBound(){
        return boundToService;
    }
    public SongService.myBinder getSongServiceBinder(){
        if (boundToService) {
            return mMyBinder;
        }
        return null;
    }*/
    public SongDBServiceImpl getSongDBService(){
        return mSongDBService;
    }
}
