package com.example.yetti.toneplayer.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class SongServiceManager {
    public SongService.myBinder songServiceBinder;
    private boolean bound = false;
    private ServiceConnection sConn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            songServiceBinder = (SongService.myBinder) binder;
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void setBound(boolean bound) {
        this.bound = bound;
    }

    public ServiceConnection getsConn() {
        return sConn;
    }

    public void setsConn(ServiceConnection sConn) {
        this.sConn = sConn;
    }

}
