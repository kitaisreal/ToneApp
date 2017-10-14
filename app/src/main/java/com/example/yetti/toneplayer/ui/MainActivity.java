package com.example.yetti.toneplayer.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.yetti.toneplayer.CoreApplication;
import com.example.yetti.toneplayer.R;
import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.content.ContentHelper;
import com.example.yetti.toneplayer.database.impl.SongDBServiceImpl;
import com.example.yetti.toneplayer.model.Song;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    boolean bound = false;
    android.app.FragmentTransaction mFragmentTransaction;
    SongListFragment mSongList;
    ArtistListFragment mArtistListFragment;
    private DrawerLayout mDrawerLayout;
    private ContentHelper mContentHelper;
    private CoreApplication mMainApplication;
    private SongDBServiceImpl mSongDBService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainApplication = (CoreApplication) getApplication();
        setContentView(R.layout.activity_main);
        mContentHelper = new ContentHelper(this);
        setupNavBarToolbar();
        mArtistListFragment = new ArtistListFragment();
        mSongDBService = mMainApplication.getSongDBService();
        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            mContentHelper.getAsyncSongsFromDevice(new ICallbackResult<List<Song>>() {
                @Override
                public void onSuccess(final List<Song> songs) {
                    mSongDBService.addSongs(songs, new ICallbackResult<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            setSongListFragment(songs);
                            replaceSongListFragment();
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
    private void setSongListFragment(List<Song> pSongList){
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("songs", (ArrayList<? extends Parcelable>) pSongList);
        mSongList = new SongListFragment();
        mSongList.setArguments(bundle);
    }
    private void replaceSongListFragment() {
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.frgmCont, mSongList);
        mFragmentTransaction.commit();
    }
    private void setupNavBarToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    //TODO FIX BUG
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainApplication.getMediaControllerCompat().getTransportControls().stop();
        mMainApplication.unbindServiceOfApplication();
        Log.d("OUR PROBLEM", "UNBIND SERVICE FROM MAIN ACTIVITY");
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
    public boolean getMusicServiceBound(){
        return mMainApplication.getMediaServiceBound();
    }
    public MediaControllerCompat getMediaControllerCompat(){
        Log.d("ACTIVITY",""+mMainApplication.getMediaControllerCompat());
        return mMainApplication.getMediaControllerCompat();
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
                } else {
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
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
            mSongDBService.getAllSongs(new ICallbackResult<List<Song>>() {

                @Override
                public void onSuccess(List<Song> pSongList) {
                    setSongListFragment(pSongList);
                    replaceSongListFragment();
                }

                @Override
                public void onError(Exception e) {
                    Log.d("TAG","ERROR");
                }
            });
        } else if (id == R.id.songs_artists) {
            mFragmentTransaction = getFragmentManager().beginTransaction();
            mFragmentTransaction.replace(R.id.frgmCont, mArtistListFragment);
            mFragmentTransaction.commit();
        } else if (id == R.id.songs_playlists) {

        } else if (id == R.id.about) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
