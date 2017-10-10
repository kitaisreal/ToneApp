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
//TODO ASYNC
public class JsonHandler implements IJsonHandler {

    @Override
    public void ConvertSongsToJson(final List<Song> pSongs, final ICallbackResult<String> pICallbackResult) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    System.out.println("CONVERT SONGS TO JSON");
                    JSONArray jsonArray = new JSONArray();
                    for (Song s : pSongs) {
                        jsonArray.put(ConvertSongToJson(s));
                    }
                    return jsonArray.toString();
                } catch (Exception e) {
                    Exception exception = new Exception("CONVERT SONGS TO JSON EXCEPTION");
                    if (pICallbackResult != null) {
                        pICallbackResult.onError(exception);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(String songsJson) {
                if (pICallbackResult != null) {
                    pICallbackResult.onSuccess(songsJson);
                }
            }
        }.execute();
    }

    @Override
    public void ConvertJsonToSongs(final String pJson, final ICallbackResult<List<Song>> pICallbackResult) {
        new AsyncTask<Void, Void, List<Song>>() {

            @Override
            protected List<Song> doInBackground(Void... params) {
                try {
                    List<Song> songs = new ArrayList<>();
                    JSONArray reader = new JSONArray(pJson);
                    for (int i = 0; i < reader.length(); i++) {
                        songs.add(ConvertJsonToSong(reader.getJSONObject(i)));
                    }
                    return songs;
                } catch (JSONException e) {
                    Exception exception = new Exception("CONVERT JSON TO SONGS EXCEPTION");
                    pICallbackResult.onError(exception);
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Song> songs) {
                if (pICallbackResult != null) {
                    pICallbackResult.onSuccess(songs);
                }
            }
        }.execute();
    }

    private JSONObject ConvertSongToJson(Song pSong) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("song_name", pSong.getSongName());
            jsonObject.put("song_artist", pSong.getSongArtist());
            jsonObject.put("song_album", pSong.getSongAlbum());
            jsonObject.put("song_album_id", pSong.getSongAlbumId());
            jsonObject.put("song_weight", pSong.getSongWeight());
            jsonObject.put("song_playlist", pSong.getSongPlaylist());
            jsonObject.put("song_id", pSong.getSongId());
            jsonObject.put("song_favourite", pSong.getSongFavourite());
            jsonObject.put("song_duration", pSong.getSongDuration());
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    private Song ConvertJsonToSong(JSONObject song) {
        Song responce = new Song();
        try {
            responce.setSongArtist(String.valueOf(song.get("song_artist")));
            responce.setSongName(String.valueOf(song.get("song_name")));
            responce.setSongAlbum(String.valueOf(song.get("song_album")));
            responce.setSongAlbumId((Integer) (song.get("song_album_id")));
            responce.setSongId((Integer) song.get("song_id"));
            responce.setSongPlaylist((Integer) song.get("song_playlist"));
            responce.setSongWeight((Integer) song.get("song_weight"));
            responce.setSongFavourite((Integer) song.get("song_favourite"));
            responce.setSongDuration((Integer) song.get("song_duration"));
            return responce;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
