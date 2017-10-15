package com.example.yetti.toneplayer.database.impl;

import android.os.AsyncTask;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.database.IAsyncDBService;
import com.example.yetti.toneplayer.model.Artist;
import com.example.yetti.toneplayer.model.Song;

import java.util.List;

public class AsyncDBServiceImpl implements IAsyncDBService {

    private final DBServiceImpl mDBService;

    public AsyncDBServiceImpl(final DBServiceImpl pDBService) {
        this.mDBService = pDBService;
    }

    @Override
    public void addSongs(final List<Song> pSongList, final ICallbackResult<Boolean> pBooleanICallbackResult) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(final Void... params) {
                return mDBService.addSongs(pSongList);
            }

            @Override
            protected void onPostExecute(final Boolean pBoolean) {
                if (pBooleanICallbackResult != null && pBoolean) {
                    pBooleanICallbackResult.onSuccess(true);
                }
                else if (pBooleanICallbackResult != null) {
                    final Exception addSongsEx = new Exception("DB ADD SONGS EXCEPTION");
                    pBooleanICallbackResult.onError(addSongsEx);
                }
            }
        }.execute();
    }

    @Override
    public void updateSongs(final List<Song> pSongList, final ICallbackResult<Boolean> pBooleanICallbackResult) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(final Void... params) {
                return mDBService.updateSongs(pSongList);
            }

            @Override
            protected void onPostExecute(final Boolean aBoolean) {
                if (pBooleanICallbackResult!=null && aBoolean) {
                    pBooleanICallbackResult.onSuccess(true);
                }
                else if (pBooleanICallbackResult!=null){
                    final Exception ex = new Exception("UPDATE SONGS EX");
                    pBooleanICallbackResult.onError(ex);
                }
            }
        }.execute();
    }

    @Override
    public void deleteSongs(final List<Song> pSongList, final ICallbackResult<Boolean> pBooleanICallbackResult) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(final Void... params) {
                return mDBService.deleteSongs(pSongList);
            }

            @Override
            protected void onPostExecute(final Boolean aBoolean) {
                if (pBooleanICallbackResult != null && aBoolean) {
                    pBooleanICallbackResult.onSuccess(true);
                }
                else if (pBooleanICallbackResult!=null){
                    final Exception exception = new Exception("DELETE SONGS EXCEPTION");
                    pBooleanICallbackResult.onError(exception);
                }
            }
        }.execute();
    }

    @Override
    public void deleteSong(final Song pSong, final ICallbackResult<Boolean> pBooleanICallbackResult) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(final Void... params) {
                return mDBService.deleteSong(pSong);
            }

            @Override
            protected void onPostExecute(final Boolean aBoolean) {
                if (pBooleanICallbackResult != null &&aBoolean) {
                    pBooleanICallbackResult.onSuccess(true);
                }
                else if (pBooleanICallbackResult!=null){
                    final Exception exception = new Exception("DELETE SONG");
                    pBooleanICallbackResult.onError(exception);
                }
            }
        }.execute();
    }

    @Override
    public void updateSong(final Song pSong, final ICallbackResult<Boolean> pBooleanICallbackResult) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(final Void... params) {
                return mDBService.updateSong(pSong);
            }

            @Override
            protected void onPostExecute(final Boolean aBoolean) {
                if (pBooleanICallbackResult != null && aBoolean) {
                    pBooleanICallbackResult.onSuccess(true);
                }
                else if(pBooleanICallbackResult!=null){
                    final Exception exception = new Exception("UPDATE SONG EXCEPTION");
                    pBooleanICallbackResult.onError(exception);
                }
            }
        }.execute();
    }

    @Override
    public void getSongByID(final Long pID, final ICallbackResult<Song> pSongICallbackResult) {
        new AsyncTask<Void, Void, Song>() {

            @Override
            protected Song doInBackground(final Void... params) {
                return mDBService.getSongByID(pID);
            }

            @Override
            protected void onPostExecute(final Song song) {
                if (pSongICallbackResult != null && song!=null) {
                    pSongICallbackResult.onSuccess(song);
                }
                else if(pSongICallbackResult!=null){
                    final Exception exception = new Exception("GET SONG BY ID");
                    pSongICallbackResult.onError(exception);
                }
            }
        }.execute();
    }

    @Override
    public void getAllSongs(final ICallbackResult<List<Song>> pSongListICallbackResult) {
        new AsyncTask<Void, Void, List<Song>>() {

            @Override
            protected List<Song> doInBackground(final Void... params) {
                return mDBService.getAllSongs();
            }

            @Override
            protected void onPostExecute(final List<Song> songs) {
                if (pSongListICallbackResult != null && songs != null) {
                    pSongListICallbackResult.onSuccess(songs);
                }
                else if(pSongListICallbackResult!=null){
                    final Exception exception = new Exception("GET ALL SONG FROM DB EXCEPTION");
                    pSongListICallbackResult.onError(exception);
                }
            }
        }.execute();
    }

    @Override
    public void getSongsByPlaylist(final int pSongPlaylist, final ICallbackResult<List<Song>> pSongListICallbackResult) {
        new AsyncTask<Void, Void, List<Song>>() {

            @Override
            protected List<Song> doInBackground(final Void... params) {
                return mDBService.getSongsByPlaylist(pSongPlaylist);
            }

            @Override
            protected void onPostExecute(final List<Song> songs) {
                if (pSongListICallbackResult != null && songs!=null) {
                    pSongListICallbackResult.onSuccess(songs);
                }
                else if(pSongListICallbackResult!=null){
                    final Exception exception= new Exception("GET SONG BY PLAYLIST EXCEPTION");
                    pSongListICallbackResult.onError(exception);
                }
            }
        }.execute();
    }
    @Override
    public void getArtists(final ICallbackResult<List<Artist>> pArtistListICallbackResult) {
        new AsyncTask<Void, Void, List<Artist>>() {
            @Override
            protected List<Artist> doInBackground(final Void... params) {
                return mDBService.getArtists();
            }

            @Override
            protected void onPostExecute(final List<Artist> pArtists) {
                if (pArtistListICallbackResult != null && pArtists!=null) {
                    pArtistListICallbackResult.onSuccess(pArtists);
                }
                else if(pArtistListICallbackResult!=null){
                    final Exception exception= new Exception("GET SONG BY ARTIST EXCEPTION");
                    pArtistListICallbackResult.onError(exception);
                }
            }
        }.execute();
    }

    @Override
    public void getSongsByArtist(final String pArtistTitle, final ICallbackResult<List<Song>> pSongListICallbackResult) {
        new AsyncTask<Void, Void, List<Song>>() {

            @Override
            protected List<Song> doInBackground(final Void... params) {
                return mDBService.getSongsByArtist(pArtistTitle);
            }

            @Override
            protected void onPostExecute(final List<Song> songs) {
                if (pSongListICallbackResult != null && songs!=null) {
                    pSongListICallbackResult.onSuccess(songs);
                }
                else if(pSongListICallbackResult!=null){
                    final Exception exception= new Exception("GET SONG BY ARTIST EXCEPTION");
                    pSongListICallbackResult.onError(exception);
                }
            }
        }.execute();
    }
}
