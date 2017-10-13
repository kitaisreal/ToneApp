package com.example.yetti.toneplayer.json;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.model.Song;

import java.util.List;

public interface IJsonParser {

    void asyncConvertSongsToJson(final List<Song> pSongs, final ICallbackResult<String> pICallbackResult);

    void asyncConvertJsonToSongs(final String pJson, final ICallbackResult<List<Song>> pICallbackResult);

    String convertSongsToJson(List<Song> pSongList);

    List<Song> convertJsonToSongs(String pJSONSongList);
}
