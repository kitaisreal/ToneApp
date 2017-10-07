package com.example.yetti.toneplayer.database;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.model.Song;

import java.util.ArrayList;
import java.util.List;

public interface ISongService {
    void addSongs(final List<Song> songs, final ICallbackResult<Boolean> iCallbackResult);
    void updateSongs(final List<Song> songs,final ICallbackResult<Boolean> iCallbackResult);
    void deleteSongs(final List<Song> songs, final ICallbackResult<Boolean> iCallbackResult);
    void deleteSong(final Song song, final ICallbackResult<Boolean> iCallbackResult);
    void updateSong(final Song song, final ICallbackResult<Boolean> iCallbackResult);
    void getSongByID(final Long id, final ICallbackResult<Song> iCallbackResult);
    void getAllSongs(final ICallbackResult<List<Song>> iCallbackResult);
    void getSongsByPlaylist(final int SongPlaylist, final ICallbackResult<List<Song>> iCallbackResult);
}
