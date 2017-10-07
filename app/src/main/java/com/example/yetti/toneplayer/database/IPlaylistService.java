package com.example.yetti.toneplayer.database;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.model.Playlist;

import java.util.List;

public interface IPlaylistService {
    void createPlaylist(final Playlist playlist, final ICallbackResult<Boolean> iCallbackResult);
    void deletePlaylist(final Playlist playlist, final ICallbackResult<Boolean> iCallbackResult);
    void getAllPlaylist(final ICallbackResult<List<Playlist>> iCallbackResult);
}
