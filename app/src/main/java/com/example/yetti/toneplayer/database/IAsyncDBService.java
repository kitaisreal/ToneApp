package com.example.yetti.toneplayer.database;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.model.Artist;
import com.example.yetti.toneplayer.model.Song;

import java.util.List;

public interface IAsyncDBService {

    void addSongs(final List<Song> pSongList, final ICallbackResult<Boolean> pBooleanICallbackResult);

    void updateSongs(final List<Song> pSongList, final ICallbackResult<Boolean> pBooleanICallbackResult);

    void deleteSongs(final List<Song> pSongList, final ICallbackResult<Boolean> pBooleanICallbackResult);

    void deleteSong(final Song pSong, final ICallbackResult<Boolean> pBooleanICallbackResult);

    void updateSong(final Song pSong, final ICallbackResult<Boolean> pBooleanICallbackResult);

    void getSongByID(final Long pID, final ICallbackResult<Song> pSongICallbackResult);

    void getAllSongs(final ICallbackResult<List<Song>> pSongListICallbackResult);

    void getSongsByPlaylist(final int pSongPlaylist, final ICallbackResult<List<Song>> pSongListICallbackResult);

    //void getArtists(final ICallbackResult<List<Artist>> pArtistListICallbackResult);

    void getSongsByArtist(final String pArtistTitle,final ICallbackResult<List<Song>> pArtistListICallbackResult);
}
