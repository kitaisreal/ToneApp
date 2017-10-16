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
import com.example.yetti.toneplayer.database.DBToneContract;
import com.example.yetti.toneplayer.database.DatabaseManager;
import com.example.yetti.toneplayer.database.impl.AsyncDBServiceImpl;
import com.example.yetti.toneplayer.model.Artist;
import com.example.yetti.toneplayer.model.Song;
import com.example.yetti.toneplayer.model.SongContract;
import com.example.yetti.toneplayer.threadmanager.ThreadsManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IOnArtistSelectedListener {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    boolean bound = false;
    android.app.FragmentTransaction mFragmentTransaction;
    SongListFragment mSongListFragment;
    ArtistListFragment mArtistListFragment;
    private DrawerLayout mDrawerLayout;
    private ContentHelper mContentHelper;
    private CoreApplication mMainApplication;
    private AsyncDBServiceImpl mSongDBService;
    private Toolbar toolbar;
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
            mContentHelper.initApp(new ICallbackResult<Boolean>() {

                @Override
                public void onSuccess(final Boolean pBoolean) {
                    mSongDBService.getAllSongs(new ICallbackResult<List<Song>>() {

                        @Override
                        public void onSuccess(final List<Song> pSongList) {
                            setSongListFragment(pSongList);
                            replaceSongListFragment();
                            toolbar.setTitle("ALL SONGS");
                        }

                        @Override
                        public void onError(final Exception e) {
                            e.printStackTrace();
                            Log.d("MAINACTIVITY","GET SONGS FROM DB EXCEPTION");
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    Log.d("MAINACTIVITY","INIT APP EXCEPTION");
                }
            });
        }
    }
    public void onArtistSelected(final String pArtistName){
        System.out.println("MAIN ACTIVITY ON ARTIST SELECTED " + pArtistName);
        DatabaseManager.getInstance().getAsyncDBService().getSongsByArtist(pArtistName, new ICallbackResult<List<Song>>() {

            @Override
            public void onSuccess(List<Song> pSongList) {
                setSongListFragment(pSongList);
                replaceSongListFragment();
                toolbar.setTitle("SONG BY ARTIST " + pArtistName);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void setSongListFragment(final List<Song> pSongList) {
        final Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("songs", (ArrayList<? extends Parcelable>) pSongList);
        mSongListFragment = new SongListFragment();
        mSongListFragment.setArguments(bundle);
    }
    private void setArtistListFragment(final List<Artist> pArtistList){
        System.out.println("MAIN ACTIVITY SETUP BUNDLE");
        List<Artist> artistListToPut=new ArrayList<>();
        for (Artist artist:pArtistList){
            if (artist.getArtistArtUrl()!=null && artist.getArtistArtUrl()!=null){
                artistListToPut.add(artist);
            }
        }
        final Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("artists", (ArrayList<? extends Parcelable>) artistListToPut);
        mArtistListFragment = new ArtistListFragment();
        mArtistListFragment.setArguments(bundle);
    }
    private void replaceSongListFragment() {
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.frgmCont, mSongListFragment);
        mFragmentTransaction.commit();
    }

    private void setupNavBarToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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
        final int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
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

    public boolean getMusicServiceBound() {
        return mMainApplication.getMediaServiceBound();
    }

    public MediaControllerCompat getMediaControllerCompat() {
        Log.d("ACTIVITY", "" + mMainApplication.getMediaControllerCompat());
        return mMainApplication.getMediaControllerCompat();
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
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
        final AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
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
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.all_songs) {
            mSongDBService.getAllSongs(new ICallbackResult<List<Song>>() {

                @Override
                public void onSuccess(final List<Song> pSongList) {
                    setSongListFragment(pSongList);
                    replaceSongListFragment();
                    toolbar.setTitle("ALL SONGS");
                }

                @Override
                public void onError(final Exception ex) {
                    Log.d("TAG", "ERROR");
                }
            });
        } else if (id == R.id.songs_artists) {
            System.out.println("ARTIST FRAGMENT");
            DatabaseManager.getInstance().getAsyncDBService().getArtists(new ICallbackResult<List<Artist>>() {
                @Override
                public void onSuccess(List<Artist> pArtistList) {
                    if (pArtistList!=null){
                        setArtistListFragment(pArtistList);
                        mFragmentTransaction = getFragmentManager().beginTransaction();
                        mFragmentTransaction.replace(R.id.frgmCont, mArtistListFragment);
                        mFragmentTransaction.commit();
                    }
                }
                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        } else if (id == R.id.songs_playlists) {

        } else if (id == R.id.about) {

        }
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
