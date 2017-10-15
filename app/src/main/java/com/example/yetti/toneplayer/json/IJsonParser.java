package com.example.yetti.toneplayer.json;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.model.Artist;
import com.example.yetti.toneplayer.model.Song;

import java.util.List;

public interface IJsonParser {

    void asyncConvertSongsToJson(final List<Song> pSongs, final ICallbackResult<String> pStringICallbackResult);

    void asyncConvertJsonToSongs(final String pJson, final ICallbackResult<List<Song>> pListICallbackResult);

    void asyncConvertArtistToJson(final Artist pArtist, final ICallbackResult<String> pArtistICallbackResult);

    void asyncConvertJsonToArtist(final String pJson, final ICallbackResult<Artist> pArtistICallbackResult);

    String convertSongsToJson(final List<Song> pSongList);

    List<Song> convertJsonToSongs(final String pJSONSongList);

    String convertArtistToJson(final Artist pArtistList);

    Artist convertJsonToArtist(final String pJSONArtist);
}
