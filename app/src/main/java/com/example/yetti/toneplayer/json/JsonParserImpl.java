package com.example.yetti.toneplayer.json;

import android.util.Log;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.database.DBToneContract;
import com.example.yetti.toneplayer.model.Artist;
import com.example.yetti.toneplayer.model.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParserImpl implements IJsonParser {

    private final String TAG = "JSON PARSER";

    @Override
    public void asyncConvertSongsToJson(final List<Song> pSongs, final ICallbackResult<String> pStringICallbackResult) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    pStringICallbackResult.onSuccess(convertSongsToJson(pSongs));
                } catch (final Exception ex) {
                    Log.d(TAG, "ASYNC CONVERT SONGS TO JSON EX");
                    final Exception exception = new Exception("ASYNC CONVERT SONGS TO JSON EX");
                    pStringICallbackResult.onError(exception);
                }
            }
        }).start();
    }

    @Override
    public void asyncConvertJsonToSongs(final String pJson, final ICallbackResult<List<Song>> pListICallbackResult) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    pListICallbackResult.onSuccess(convertJsonToSongs(pJson));
                } catch (final Exception ex) {
                    Log.d(TAG, "ASYNC CONVERT JSON TO SONGS EX");
                    final Exception exception = new Exception("ASYNC CONVERT JSON TO SONGS EX");
                    pListICallbackResult.onError(exception);
                }
            }
        }).start();
    }

    @Override
    public void asyncConvertArtistToJson(final Artist pArtist, final ICallbackResult<String> pArtistICallbackResult) {

    }

    @Override
    public void asyncConvertJsonToArtist(final String pJson, final ICallbackResult<Artist> pArtistICallbackResult) {

    }

    @Override
    public String convertSongsToJson(final List<Song> pSongList) {
        final JSONArray jsonArray = new JSONArray();
        for (final Song s : pSongList) {
            jsonArray.put(convertSongToJson(s));
        }
        return jsonArray.toString();
    }

    @Override
    public List<Song> convertJsonToSongs(final String pJSONSongList) {
        final List<Song> songs = new ArrayList<>();
        try {
            final JSONArray jsonArray = new JSONArray(pJSONSongList);
            for (int i = 0; i < jsonArray.length(); i++) {
                songs.add(convertJsonToSong(jsonArray.getJSONObject(i)));
            }
        } catch (final JSONException ex) {
            Log.d(TAG, "CONVERT SONGS TO JSON EXCEPTION");
        }
        return songs;
    }

    @Override
    public String convertArtistToJson(final Artist pArtistList) {
        return null;
    }

    @Override
    public Artist convertJsonToArtist(final String pJSONArtist) {
        final Artist artist = new Artist();
        if (pJSONArtist != null){
            try {
                final JSONObject artistJSON = new JSONObject(pJSONArtist);
                artist.setArtistName((String) artistJSON.get(DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_NAME));
                artist.setArtistArtUrl((String) artistJSON.get(DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_ARTWORKURL));
                artist.setArtistGenre((String) artistJSON.get(DBToneContract.ArtistEntry.COLUMN_NAME_ARTIST_GENRE));
                return artist;

            } catch (final JSONException ex) {
                ex.printStackTrace();
                Log.d(TAG, "CONVERT ARTIST TO JSON EXCEPTION");
            }
        }
        return null;
    }

    private JSONObject convertSongToJson(final Song pSong) {
        final JSONObject jsonObject = new JSONObject();
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
        } catch (final JSONException ex) {
            Log.d(TAG, "CONVERT SONG TO JSON EXCEPTION");
        }
        return new JSONObject();
    }

    private Song convertJsonToSong(final JSONObject pSong) {
        final Song response = new Song();
        try {
            response.setSongArtist(String.valueOf(pSong.get(DBToneContract.SongEntry.COLUMN_NAME_SONG_ARTIST)));
            response.setSongName(String.valueOf(pSong.get(DBToneContract.SongEntry.COLUMN_NAME_SONG_TITLE)));
            response.setSongAlbum(String.valueOf(pSong.get(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM)));
            response.setSongAlbumId((Integer) (pSong.get(DBToneContract.SongEntry.COLUMN_NAME_SONG_ALBUM_ID)));
            response.setSongId((Integer) pSong.get(DBToneContract.SongEntry.COLUMN_NAME_ENTRY_ID));
            response.setSongPlaylist((Integer) pSong.get(DBToneContract.SongEntry.COLUMN_NAME_SONG_PLAYLIST));
            response.setSongWeight((Integer) pSong.get(DBToneContract.SongEntry.COLUMN_NAME_SONG_WEIGHT));
            response.setSongFavourite((Integer) pSong.get(DBToneContract.SongEntry.COLUMN_NAME_SONG_FAVOURITE));
            response.setSongDuration((Integer) pSong.get(DBToneContract.SongEntry.COLUMN_NAME_SONG_DURATION));
            return response;
        } catch (final JSONException e) {
            Log.d(TAG, "CONVERT JSON TO SONG EXCEPTION");
        }
        return null;
    }
}
