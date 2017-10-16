package com.example.yetti.toneplayer.content;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.database.DBToneContract;
import com.example.yetti.toneplayer.database.DatabaseManager;
import com.example.yetti.toneplayer.json.JsonParserImpl;
import com.example.yetti.toneplayer.model.Artist;
import com.example.yetti.toneplayer.model.Song;
import com.example.yetti.toneplayer.network.HttpClient;
import com.example.yetti.toneplayer.network.HttpContract;
import com.example.yetti.toneplayer.network.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ContentHelper {

    private Context mContext;
    private JsonParserImpl mJsonParserImpl;
    private HttpClient mHttpClient;
    private List<Artist> mArtistList;
    private final String TAG = "CONTENT HELPER";

    public ContentHelper(final Context pContext) {
        mContext = pContext;
        mJsonParserImpl = new JsonParserImpl();
        mHttpClient = new HttpClient();
    }
    public void initApp(final ICallbackResult<Boolean> pBooleanICallbackResult){
        new Thread(new Runnable() {

            @Override
            public void run() {
                List<Song> songList;
                List<Artist> artistList;
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                try {
                    songList = getSongsFromDevice(mContext);
                    DatabaseManager.getInstance().getDBSongService().addSongs(songList);
                    artistList = DatabaseManager.getInstance().getDBSongService().getArtists();
                    DatabaseManager.getInstance().getDBArtistService().addArtists(artistList);
                    List<Artist> artistListFromDatabase = DatabaseManager.getInstance().getDBArtistService().getArtists();
                    List<Artist> artistToUpdateList = new ArrayList<>();
                    System.out.println(TAG+"ARTIST FROM DATABASE");
                    for (Artist artist : artistListFromDatabase) {
                        System.out.println(artist.toString());
                        if (artist.getArtistGenre()==null && artist.getArtistArtUrl()==null){
                            artistToUpdateList.add(artist);
                        }
                    }
                    System.out.println(TAG + "ARTIST BEFORE UPDATE");
                    for (Artist artist:artistToUpdateList){
                        String result;
                        if (!Objects.equals(artist.getArtistName(), "<unknown>")) {
                            final Request request = new Request.RequestBuilder(HttpContract.GET_ARTIST + artist.getArtistName()).
                                    headers(headers).
                                    method(HttpContract.GET_METHOD).build();
                            result = mHttpClient.createRequest(request);
                            Artist convertedFromResultArtist = mJsonParserImpl.convertJsonToArtist(result);
                            if (convertedFromResultArtist!=null) {
                                System.out.println("CONVERTED FROM RESULT ARTIST " + convertedFromResultArtist.toString());
                                artist.setArtistArtUrl(convertedFromResultArtist.getArtistArtUrl());
                                artist.setArtistGenre(convertedFromResultArtist.getArtistGenre());
                                DatabaseManager.getInstance().getDBArtistService().updateArtist(artist);
                            }
                        }
                    }
                    System.out.println(TAG + " ARTIST AFTER UPDATE");
                    for (Artist artist: DatabaseManager.getInstance().getDBArtistService().getArtists()){
                        System.out.println(artist.toString());
                    }
                    pBooleanICallbackResult.onSuccess(true);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    Exception exception = new Exception("INIT APP EXCEPTION");
                    pBooleanICallbackResult.onError(ex);
                }
            }
        }).start();
        /*
        initSongs(new ICallbackResult<Boolean>() {

            @Override
            public void onSuccess(Boolean pBoolean) {
                pBooleanICallbackResult.onSuccess(true);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        initArtists(new ICallbackResult<Boolean>() {

            @Override
            public void onSuccess(Boolean pBoolean) {
            }

            @Override
            public void onError(Exception e) {

            }
        });*/
    }
    public void initArtists(final ICallbackResult<Boolean> pBooleanICallbackResult) {
        DatabaseManager.getInstance().getAsyncDBService().getArtistsFromSongs(new ICallbackResult<List<Artist>>() {

            @Override
            public void onSuccess(List<Artist> pArtists) {
                getInitialArtistList(pArtists, new ICallbackResult<List<Artist>>() {

                    @Override
                    public void onSuccess(List<Artist> pArtists) {
                        mArtistList=pArtists;
                        pBooleanICallbackResult.onSuccess(true);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        pBooleanICallbackResult.onError(e);
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public List<Artist> getArtistList() {
        return mArtistList;
    }

    private void getInitialArtistList(final List<Artist> pArtistList, final ICallbackResult<List<Artist>> pListICallbackResult){

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                for (int i = 0; i < pArtistList.size(); i++) {
                    final Request request = new Request.RequestBuilder(HttpContract.GET_ARTIST + pArtistList.get(i).getArtistName()).
                            headers(headers).
                            method(HttpContract.GET_METHOD).build();
                    String result = mHttpClient.createRequest(request);
                    Artist convertedFromResultArtist = mJsonParserImpl.convertJsonToArtist(result);
                    if (convertedFromResultArtist!=null && Objects.equals(convertedFromResultArtist.getArtistName(), pArtistList.get(i).getArtistName())){
                        pArtistList.get(i).setArtistGenre(convertedFromResultArtist.getArtistGenre());
                        pArtistList.get(i).setArtistArtUrl(convertedFromResultArtist.getArtistArtUrl());
                    }

                }
                System.out.println("FINAL ARTIST LIST");
                for (Artist artist:pArtistList){
                    System.out.println(artist.getArtistName() + " " + artist.getSongCount() + " " + artist.getArtistGenre() + " " + artist.getArtistArtUrl());
                }
                pListICallbackResult.onSuccess(pArtistList);
            }
        };
        new Thread(runnable).start();

    }
    public void initSongs(final ICallbackResult<Boolean> pBooleanICallbackResult) {
        getAsyncSongsFromDevice(new ICallbackResult<List<Song>>() {

            @Override
            public void onSuccess(List<Song> pSongList) {

                DatabaseManager.getInstance().getAsyncDBService().addSongs(pSongList, new ICallbackResult<Boolean>() {

                    @Override
                    public void onSuccess(Boolean pBoolean) {
                        pBooleanICallbackResult.onSuccess(pBoolean);
                    }

                    @Override
                    public void onError(Exception e) {
                        pBooleanICallbackResult.onError(e);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void getAsyncSongsFromDevice(final ICallbackResult<List<Song>> pListICallbackResult) {
        new Runnable() {

            @Override
            public void run() {
                try {
                    for (Song song : getSongsFromDevice(mContext)) {
                        System.out.println(song.getSongId() + " " + song.getSongAlbum());
                    }
                    pListICallbackResult.onSuccess(getSongsFromDevice(mContext));
                } catch (Exception ex) {
                    Exception exception = new Exception(TAG + "ASYNC GET SONGS FROM DEVICE EX");
                    pListICallbackResult.onError(exception);
                    Log.d(TAG, "ASYNC GET SONGS FROM DEVICE EX");
                }
            }
        }.run();
    }

    ;

    private List<Song> getSongsFromDevice(Context pContext) {
        System.out.println("GET SONGS FROM DEVICE");
        List<Song> songsFromDeviceList = new ArrayList<>();
        ContentResolver musicResolver = pContext.getContentResolver();
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
                songsFromDeviceList.add(new Song(thisId, thisArtist, thisTitle, thisALbum, thisAlbumId, 0, -1, 0, thisSongDuration));
            }
            while (musicCursor.moveToNext());
        }
        if (musicCursor != null) {
            musicCursor.close();
        }
        return songsFromDeviceList;
    }
}
