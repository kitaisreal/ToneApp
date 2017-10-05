package com.example.yetti.toneplayer.database;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.model.Song;

import java.util.ArrayList;
import java.util.List;

public interface SongService {
    void addSongs(final ArrayList<Song> songs, final ICallbackResult<Boolean> iResultCallback);
    void updateSongs(List<Song> songs,final ICallbackResult<Boolean> iResultCallback);
    void deleteSong(final Song song, final ICallbackResult<Boolean> iResultCallback);
    void updateSong(final Song song, final ICallbackResult<Boolean> iResultCallback);
    void getSongByID(final Long id, final ICallbackResult<Song> iResultCallback);
    void getAllSongs(final ICallbackResult<List<Song>> iResultCallback);
    void getSongsByPlaylist(final String SongPlaylist, final ICallbackResult<List<Song>> iResultCallback);
}
