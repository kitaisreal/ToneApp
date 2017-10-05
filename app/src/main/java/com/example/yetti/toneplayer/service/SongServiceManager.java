package com.example.yetti.toneplayer.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.yetti.toneplayer.R;
import com.example.yetti.toneplayer.SongList;
import com.example.yetti.toneplayer.callback.ICallbackResult;

import static android.content.Context.BIND_AUTO_CREATE;

public class SongServiceManager {
    private boolean bound = false;
    public SongService.myBinder songServiceBinder;
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
