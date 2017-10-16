package com.example.yetti.toneplayer.database.impl;

import android.os.AsyncTask;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.database.IAsyncDBService;
import com.example.yetti.toneplayer.model.Artist;
import com.example.yetti.toneplayer.model.Song;

import java.util.List;

public class AsyncDBServiceImpl implements IAsyncDBService {

    private final DBSongServiceImpl mDBSongService;
    private final DBArtistServiceImpl mDBArtistService;
    public AsyncDBServiceImpl(final DBSongServiceImpl pDBService, final DBArtistServiceImpl pDBArtistService) {
        this.mDBSongService = pDBService;
        this.mDBArtistService = pDBArtistService;
    }

    @Override
    public void addSongs(final List<Song> pSongList, final ICallbackResult<Boolean> pBooleanICallbackResult) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(final Void... params) {
                return mDBSongService.addSongs(pSongList);
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
                return mDBSongService.updateSongs(pSongList);
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
                return mDBSongService.deleteSongs(pSongList);
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
                return mDBSongService.deleteSong(pSong);
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
                return mDBSongService.updateSong(pSong);
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
                return mDBSongService.getSongByID(pID);
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
                return mDBSongService.getAllSongs();
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
                return mDBSongService.getSongsByPlaylist(pSongPlaylist);
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
    public void getArtistsFromSongs(final ICallbackResult<List<Artist>> pArtistListICallbackResult) {
        new AsyncTask<Void, Void, List<Artist>>() {
            @Override
            protected List<Artist> doInBackground(final Void... params) {
                return mDBSongService.getArtists();
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
                return mDBSongService.getSongsByArtist(pArtistTitle);
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

    @Override
    public void addArtists(final List<Artist> pArtistList, final ICallbackResult<Boolean> pBooleanICallbackResult) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(final Void... params) {
                return mDBArtistService.addArtists(pArtistList);
            }

            @Override
            protected void onPostExecute(final Boolean pBoolean) {
                if (pBooleanICallbackResult != null && pBoolean) {
                    pBooleanICallbackResult.onSuccess(true);
                }
                else if (pBooleanICallbackResult != null) {
                    final Exception addSongsEx = new Exception("DB ADD ARTISTS EXCEPTION");
                    pBooleanICallbackResult.onError(addSongsEx);
                }
            }
        }.execute();
    }

    @Override
    public void deleteArtists(final List<Artist> pArtistList, final ICallbackResult<Boolean> pBooleanICallbackResult) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(final Void... params) {
                return mDBArtistService.deleteArtists(pArtistList);
            }

            @Override
            protected void onPostExecute(final Boolean aBoolean) {
                if (pBooleanICallbackResult != null && aBoolean) {
                    pBooleanICallbackResult.onSuccess(true);
                }
                else if (pBooleanICallbackResult!=null){
                    final Exception exception = new Exception("DELETE ARTIST EXCEPTION");
                    pBooleanICallbackResult.onError(exception);
                }
            }
        }.execute();
    }

    @Override
    public void getArtistByName(final String pArtistName, final ICallbackResult<Artist> pArtistICallbackResult) {
        new AsyncTask<Void, Void, Artist>() {

            @Override
            protected Artist doInBackground(final Void... params) {
                return mDBArtistService.getArtistByName(pArtistName);
            }

            @Override
            protected void onPostExecute(final Artist artist) {
                if (pArtistICallbackResult != null && artist!=null) {
                    pArtistICallbackResult.onSuccess(artist);
                }
                else if(pArtistICallbackResult!=null){
                    final Exception exception = new Exception("GET ARTIST BY NAME");
                    pArtistICallbackResult.onError(exception);
                }
            }
        }.execute();
    }

    @Override
    public void getArtists(final ICallbackResult<List<Artist>> pListArtistICallbackResult) {
        new AsyncTask<Void, Void, List<Artist>>() {

            @Override
            protected List<Artist> doInBackground(final Void... params) {
                return mDBArtistService.getArtists();
            }

            @Override
            protected void onPostExecute(final List<Artist> pArtistList) {
                if (pListArtistICallbackResult != null && pArtistList != null) {
                    pListArtistICallbackResult.onSuccess(pArtistList);
                }
                else if(pListArtistICallbackResult!=null){
                    final Exception exception = new Exception("GET ALL ARTISTS FROM DB EXCEPTION");
                    pListArtistICallbackResult.onError(exception);
                }
            }
        }.execute();
    }
}
