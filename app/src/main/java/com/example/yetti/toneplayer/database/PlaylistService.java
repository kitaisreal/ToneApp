package com.example.yetti.toneplayer.database;

import com.example.yetti.toneplayer.model.Playlist;
import com.example.yetti.toneplayer.model.Song;

import java.util.List;

public interface PlaylistService {
    void createPlaylist(Playlist song);
    void deletePlaylist(Long id);
    void updatePlaylist(Playlist song);
    List<Playlist> getAllPlaylists();
}
