package com.example.yetti.toneplayer.json;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.model.Song;

import java.util.List;

public interface IJsonHandler {
    void ConvertSongsToJson(final List<Song> songs, final ICallbackResult<String> iCallbackResult);
    void ConvertJsonToSongs(final String json, final ICallbackResult<List<Song>> iCallbackResult);
}
