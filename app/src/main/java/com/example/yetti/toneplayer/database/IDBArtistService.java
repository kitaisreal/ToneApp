package com.example.yetti.toneplayer.database;

import com.example.yetti.toneplayer.model.Artist;
import com.example.yetti.toneplayer.model.Song;

import java.util.List;

public interface IDBArtistService {
    boolean addArtists(final List<Artist> pArtistList);

    boolean deleteArtists(final List<Artist> pArtistList);

    boolean updateArtist(final Artist pArtist);

    Artist getArtistByName(final String pArtistName);

    List<Artist> getArtists();
}
