package com.example.yetti.toneplayer.database;

import com.example.yetti.toneplayer.model.Song;

import java.util.List;

public interface SongService {
    void createSong(Song song);
    void deleteSong(Long id);
    void updateSong(Song song);
    Song getSongByID(Long id);
    List<Song> getAllSongs();
    List<Song> getSongsByPlaylist();
}
