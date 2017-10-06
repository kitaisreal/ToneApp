package com.example.yetti.toneplayer.json;

import android.os.AsyncTask;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.model.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//TODO LIST<SONG>-> JSON JSON 0> LIST<SONG> simpleJSON or gson
public class JsonHandler implements IJsonHandler{
    @Override
    public void ConvertSongsToJson(final List<Song> songs, final ICallbackResult<String> iCallbackResult){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    System.out.println("CONVERT SONGS TO JSON");
                    JSONArray jsonArray = new JSONArray();
                    for (Song s: songs) {
                        jsonArray.put(ConvertSongToJson(s));
                    }
                    return jsonArray.toString();
                } catch (Exception e) {
                    Exception exception = new Exception("CONVERT SONGS TO JSON EXCEPTION");
                    if (iCallbackResult!=null){
                        iCallbackResult.onFail(exception);
                    }
                }
                return null;
            }
            @Override
            protected void onPostExecute(String songsJson) {
                if (iCallbackResult!=null){
                    iCallbackResult.onSuccess(songsJson);
                }
            }
        }.execute();
    }
    @Override
    public void ConvertJsonToSongs(final String json, final ICallbackResult<List<Song>> iCallbackResult){
        new AsyncTask<Void, Void, List<Song>>() {
            @Override
            protected List<Song> doInBackground(Void... params) {
                try {
                    List<Song> songs = new ArrayList<Song>();
                    JSONArray reader = new JSONArray(json);
                    for (int i=0;i<reader.length();i++){
                        songs.add(ConvertJsonToSong(reader.getJSONObject(i)));
                    }
                    return songs;
                } catch (JSONException e) {
                    Exception exception = new Exception("CONVERT JSON TO SONGS EXCEPTION");
                    iCallbackResult.onFail(exception);
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Song> songs) {
                if (iCallbackResult!=null){
                    iCallbackResult.onSuccess(songs);
                }
            }
        }.execute();
    }
    private JSONObject ConvertSongToJson(Song song){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("song_name", song.getSong_name());
            jsonObject.put("song_artist",song.getSong_artist());
            jsonObject.put("song_weight",song.getSong_weight());
            jsonObject.put("song_playlist",song.getSong_playlist());
            jsonObject.put("song_id",song.getSong_id());
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }
    private Song ConvertJsonToSong(JSONObject song){
        Song responce = new Song();
        try {
            responce.setSong_artist(String.valueOf(song.get("song_artist")));
            responce.setSong_name(String.valueOf(song.get("song_name")));
            responce.setSong_id((Integer) song.get("song_id"));
            responce.setSong_playlist((Integer) song.get("song_playlist"));
            responce.setSong_weight((Integer) song.get("song_weight"));
            return responce;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
