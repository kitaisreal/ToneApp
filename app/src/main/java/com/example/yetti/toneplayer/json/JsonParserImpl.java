package com.example.yetti.toneplayer.json;

import android.util.Log;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.database.DBToneContract;
import com.example.yetti.toneplayer.model.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//TODO LIST<SONG>-> JSON JSON 0> LIST<SONG> simpleJSON or gson
//TODO ASYNC
public class JsonParserImpl implements IJsonParser {
    private final String TAG="JSON PARSER";
    @Override
    public void asyncConvertSongsToJson(final List<Song> pSongs, final ICallbackResult<String> pICallbackResult) {
        new Runnable() {

            @Override
            public void run() {
                try {
                    pICallbackResult.onSuccess(convertSongsToJson(pSongs));
                } catch (Exception ex){
                    Log.d(TAG, "ASYNC CONVERT SONGS TO JSON EX");
                    Exception exception = new Exception("ASYNC CONVERT SONGS TO JSON EX");
                    pICallbackResult.onError(exception);
                }
            }
        }.run();
    }
    @Override
    public void asyncConvertJsonToSongs(final String pJson, final ICallbackResult<List<Song>> pICallbackResult) {
        new Runnable() {

            @Override
            public void run() {
                try{
                    pICallbackResult.onSuccess(convertJsonToSongs(pJson));
                } catch (Exception ex){
                    Log.d(TAG, "ASYNC CONVERT JSON TO SONGS EX");
                    Exception exception = new Exception("ASYNC CONVERT JSON TO SONGS EX");
                    pICallbackResult.onError(exception);
                }
            }
        }.run();
    }
    @Override
    public String convertSongsToJson(List<Song> pSongList){
        JSONArray jsonArray = new JSONArray();
        for (Song s : pSongList) {
            jsonArray.put(convertSongToJson(s));
        }
        return jsonArray.toString();
    }
    @Override
    public List<Song> convertJsonToSongs(String pJSONSongList){
        List<Song> songs = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(pJSONSongList);
            for (int i=0;i<jsonArray.length();i++){
                songs.add(convertJsonToSong(jsonArray.getJSONObject(i)));
            }
        }
        catch (JSONException ex){
            Log.d(TAG,"CONVERT SONGS TO JSON EXCEPTION");
        }
        return songs;
    };
    private JSONObject convertSongToJson(Song pSong) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE, pSong.getSongName());
            jsonObject.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST, pSong.getSongArtist());
            jsonObject.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM, pSong.getSongAlbum());
            jsonObject.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM_ID, pSong.getSongAlbumId());
            jsonObject.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT, pSong.getSongWeight());
            jsonObject.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST, pSong.getSongPlaylist());
            jsonObject.put(DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID, pSong.getSongId());
            jsonObject.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_FAVOURITE, pSong.getSongFavourite());
            jsonObject.put(DBToneContract.SongEntry.COLUMN_NAME_SONG_DURATION, pSong.getSongDuration());
            return jsonObject;
        } catch (JSONException e) {
            Log.d(TAG, "CONVERT SONG TO JSON EXCEPTION");
        }
        return new JSONObject();
    }

    private Song convertJsonToSong(JSONObject pSong) {
        Song response = new Song();
        try {
            response.setSongArtist(String.valueOf(pSong.get(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST)));
            response.setSongName(String.valueOf(pSong.get(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE)));
            response.setSongAlbum(String.valueOf(pSong.get(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM)));
            response.setSongAlbumId((Integer) (pSong.get(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM_ID)));
            response.setSongId((Integer) pSong.get(DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID));
            response.setSongPlaylist((Integer) pSong.get(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST));
            response.setSongWeight((Integer) pSong.get( DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT));
            response.setSongFavourite((Integer) pSong.get(DBToneContract.SongEntry.COLUMN_NAME_SONG_FAVOURITE));
            response.setSongDuration((Integer) pSong.get(DBToneContract.SongEntry.COLUMN_NAME_SONG_DURATION));
            return response;
        } catch (JSONException e) {
            Log.d(TAG, "CONVERT JSON TO SONG EXCEPTION");
        }
        return null;
    }
}
