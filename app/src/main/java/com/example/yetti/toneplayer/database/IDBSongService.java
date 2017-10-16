package com.example.yetti.toneplayer.database;

import com.example.yetti.toneplayer.model.Artist;
import com.example.yetti.toneplayer.model.Song;

import java.util.List;

public interface IDBSongService {
    boolean addSongs(final List<Song> pSongList);

    boolean updateSongs(final List<Song> pSongList);

    boolean deleteSongs(final List<Song> pSongList);

    boolean deleteSong(final Song pSong);

    boolean updateSong(final Song pSong);

    Song getSongByID(final Long pID);

    List<Song> getAllSongs();

    List<Song> getSongsByPlaylist(final int pSongPlaylist);

    List<Artist> getArtists();

    List<Song> getSongsByArtist(final String pArtistTitle);
}
