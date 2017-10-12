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
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.database.DBToneContract;
import com.example.yetti.toneplayer.database.DBToneHelper;
import com.example.yetti.toneplayer.database.DatabaseManager;
import com.example.yetti.toneplayer.database.impl.SongServiceImpl;
import com.example.yetti.toneplayer.json.JsonHandler;
import com.example.yetti.toneplayer.model.Song;
import com.example.yetti.toneplayer.network.HttpClient;
import com.example.yetti.toneplayer.service.SongService;
import com.example.yetti.toneplayer.service.SongServiceManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //TODO ADD NAVIGATION DRAWLER PLAYLIST FRAGMENT (ON REQUEST ADD) CONTROLS
    ArrayList<Song> list;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    boolean bound = false;
    Intent intent;
    android.app.FragmentTransaction ft;
    SongList fragment;
    SongServiceManager songServiceManager;
    public SongService.myBinder songServiceBinder;
    HttpClient httpClient;
    JsonHandler jsonHandler;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.deleteDatabase(DBToneContract.DATABASE_NAME);
        songServiceManager = new SongServiceManager();
        httpClient = new HttpClient();
        jsonHandler = new JsonHandler();
        list = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        intent = new Intent(this, SongService.class);
        DatabaseManager.initializeInstance(new DBToneHelper(this));
        final SongServiceImpl songService = new SongServiceImpl();
        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            getSongList(new ICallbackResult<List<Song>>() {
                @Override
                public void onSuccess(List<Song> songs) {
                    songService.addSongs((ArrayList<Song>) songs, new ICallbackResult<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            bindService(intent, sConn, BIND_AUTO_CREATE);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }

    private ServiceConnection sConn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d("SERVICE", "MainActivity onServiceConnected");
            songServiceBinder = (SongService.myBinder) binder;
            System.out.println("SONG SERVICE BINDER" + songServiceBinder);
            System.out.println("SONG SERVICE " + songServiceBinder.getService());
            bound = true;
            Bundle bundle = new Bundle();
            bundle.putBinder("songServiceBinder", songServiceBinder);
            bundle.putParcelableArrayList("songs", list);
            fragment = new SongList();
            fragment.setArguments(bundle);
            ft = getFragmentManager().beginTransaction();
            ft.add(R.id.frgmCont, fragment);
            ft.commit();

        }

        public void onServiceDisconnected(ComponentName name) {
            Log.d("SERVICE", "MainActivity onServiceDisconnected");
            bound = false;
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        if (!bound) return;
        unbindService(sConn);
        bound = false;
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
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
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
                                new String[]{permission},
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

    private void getSongList(final ICallbackResult<List<Song>> callbackResult) {
        new AsyncTask<Void, Void, List<Song>>() {
            @Override
            protected List<Song> doInBackground(Void... params) {
                try {
                    ContentResolver musicResolver = getContentResolver();
                    Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
                    if (musicCursor != null && musicCursor.moveToFirst()) {
                        int titleColumn = musicCursor.getColumnIndex
                                (MediaStore.Audio.Media.TITLE);
                        int idColumn = musicCursor.getColumnIndex
                                (MediaStore.Audio.Media._ID);
                        int artistColumn = musicCursor.getColumnIndex
                                (MediaStore.Audio.Media.ARTIST);
                        int albumId = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                        int songAlbum = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                        int songDuration = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
                        do {
                            long thisId = musicCursor.getLong(idColumn);
                            String thisTitle = musicCursor.getString(titleColumn);
                            String thisArtist = musicCursor.getString(artistColumn);
                            long thisAlbumId = musicCursor.getLong(albumId);
                            String thisALbum = musicCursor.getString(songAlbum);
                            long thisSongDuration = musicCursor.getLong(songDuration);
                            System.out.println("SONG");
                            System.out.println("THIS ID " + thisId + " THIS TITLE " + thisTitle + " THIS ARTIST " + thisArtist + " THIS ALBUM " + thisALbum +
                                    " THIS ALBUM ID " + thisAlbumId + " THIS SONG DURATION " + thisSongDuration);
                            list.add(new Song(thisId, thisArtist, thisTitle,thisALbum,thisAlbumId,0, -1,0,thisSongDuration));
                            /*
                            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                            Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri,musicCursor.getLong(albumId));
                            Bitmap bitmap = null;
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), albumArtUri);
                                bitmap = Bitmap.createScaledBitmap(bitmap,30,30,true);
                            } catch (FileNotFoundException e){
                                e.printStackTrace();
                                continue;
                            };
                            System.out.println("BITMAP " + bitmap.getHeight());
                            */
                        }
                        while (musicCursor.moveToNext());
                    }
                    return list;
                } catch (Exception e) {
                    if (callbackResult != null) {
                        final Exception exception = new Exception("GET SONG FROM DEVICE EXCEPTION");
                        callbackResult.onError(exception);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Song> songs) {
                if (callbackResult != null) {
                    callbackResult.onSuccess(list);
                }
            }
        }.execute();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.all_songs) {

        } else if (id == R.id.songs_artists) {

        } else if (id == R.id.songs_playlists) {

        } else if (id == R.id.about) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
