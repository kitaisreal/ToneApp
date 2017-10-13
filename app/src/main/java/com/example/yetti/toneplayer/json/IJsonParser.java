package com.example.yetti.toneplayer.json;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.model.Song;

import java.util.List;

public interface IJsonParser {

    void AsyncConvertSongsToJson(final List<Song> pSongs, final ICallbackResult<String> pICallbackResult);

    void AsyncConvertJsonToSongs(final String pJson, final ICallbackResult<List<Song>> pICallbackResult);

    String ConvertSongsToJson(List<Song> pSongList);

    List<Song> ConvertJsonToSongs(String pJSONSongList);
}
