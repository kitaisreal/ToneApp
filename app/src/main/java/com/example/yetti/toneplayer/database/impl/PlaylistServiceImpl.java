package com.example.yetti.toneplayer.database.impl;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.yetti.toneplayer.database.DBToneContract;
import com.example.yetti.toneplayer.database.DatabaseManager;
import com.example.yetti.toneplayer.database.PlaylistService;
import com.example.yetti.toneplayer.model.Playlist;

import java.util.List;

public class PlaylistServiceImpl implements PlaylistService{

    @Override
    public void createPlaylist(Playlist playlist) {
    }

    @Override
    public void deletePlaylist(Long id) {

    }

    @Override
    public void updatePlaylist(Playlist playlist) {

    }

    @Override
    public List<Playlist> getAllPlaylist() {
        return null;
    }
}
