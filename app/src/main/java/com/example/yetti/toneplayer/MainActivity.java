package com.example.yetti.toneplayer;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.database.DBToneHelper;
import com.example.yetti.toneplayer.database.DatabaseManager;
import com.example.yetti.toneplayer.database.SongService;
import com.example.yetti.toneplayer.database.impl.SongServiceImpl;
import com.example.yetti.toneplayer.model.Song;
import com.example.yetti.toneplayer.service.TestService;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //TODO ADD NAVIGATION DRAWLER PLAYLIST FRAGMENT (ON REQUEST ADD) CONTROLS
    ArrayList<Song> list;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    boolean bound = false;
    ServiceConnection sConn;
    Intent intent;
    android.app.FragmentTransaction ft;
    SongList fragment;
    public TestService.myBinder songServiceBinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();
        intent = new Intent(this,TestService.class);
        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            getSongList();
        }
        DatabaseManager.initializeInstance(new DBToneHelper(this));
        SongServiceImpl songService = new SongServiceImpl();
        songService.addSongs(list, new ICallbackResult<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                System.out.println("SONGS ADDED");
            }

            @Override
            public void onFail(Exception e) {
                System.out.println("SONGS DONT ADDED");
            }
        });
        sConn = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder binder) {
                Log.d("SERVICE", "MainActivity onServiceConnected");
                songServiceBinder = (TestService.myBinder) binder;
                System.out.println("SONG SERVICE BINDER" + songServiceBinder);
                bound = true;
                Bundle bundle = new Bundle();
                bundle.putBinder("songServiceBinder",songServiceBinder);
                bundle.putParcelableArrayList("songs", list);
                System.out.println(bundle.size());
                System.out.println("BUNDLE SIZE " + bundle.size());
                fragment= new SongList();
                fragment.setArguments(bundle);
                ft=getFragmentManager().beginTransaction();
                ft.add(R.id.frgmCont,fragment);
                ft.commit();
            }
            public void onServiceDisconnected(ComponentName name) {
                Log.d("SERVICE", "MainActivity onServiceDisconnected");
                bound = false;
            }
        };
        bindService(intent,sConn,BIND_AUTO_CREATE);
    }
    protected void onDestroy() {
        super.onDestroy();
        if (!bound) return;
        unbindService(sConn);
        bound=false;
    }
    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }
    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }
    public void getSongList(){
            ContentResolver musicResolver = getContentResolver();
            Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
            if (musicCursor != null && musicCursor.moveToFirst()) {
                int titleColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.TITLE);
                int idColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media._ID);
                int artistColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.ARTIST);
                do {
                    long thisId = musicCursor.getLong(idColumn);
                    String thisTitle = musicCursor.getString(titleColumn);
                    String thisArtist = musicCursor.getString(artistColumn);
                    list.add(new Song(thisId, thisTitle, thisArtist, 0,""));
                }
                while (musicCursor.moveToNext());
            }
        }

}
