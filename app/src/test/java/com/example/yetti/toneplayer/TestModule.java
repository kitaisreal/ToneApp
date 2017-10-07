package com.example.yetti.toneplayer;


import com.example.yetti.toneplayer.model.Playlist;
import com.example.yetti.toneplayer.model.Song;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class TestModule {
    static Comparator<Song> songComparator = new Comparator<Song>() {
        @Override
        public int compare(Song o1, Song o2) {
            boolean test = o1.getSong_id()==o2.getSong_id() && o1.getSong_artist().equals(o2.getSong_artist()) && o1.getSong_name().equals(o2.getSong_name())
                    && o1.getSong_weight()==o2.getSong_weight() && o1.getSong_playlist()== o2.getSong_playlist();
            if (test){
                return 1;
            } else{
                return -1;
            }
        }
    };
    static Comparator<Playlist> playlistComparator = new Comparator<Playlist>() {
        @Override
        public int compare(Playlist o1, Playlist o2) {
            boolean test = o1.getPlaylist_id()==o2.getPlaylist_id() && o1.getPlaylist_name().equals(o2.getPlaylist_name());
            if (test){
                return 1;
            } else{
                return -1;
            }
        }
    };
    static String GET_TEST_METHOD_RESULT = "GET_TEST_TONE";
    static String POST_STRING = "POST";
    static String POST_TEST_METHOD_RESULT="POST_TEST_TONE";
    static List<Song> songList = new ArrayList<Song>() {{
        add(new Song(1,"testSongArtist1","testSongArtist1",1,1));
        add(new Song(2,"testSongArtist2","testSongArtist2",2,2));
        add(new Song(3,"testSongArtist3","testSongArtist3",3,3));
        add(new Song(4,"testSongArtist4","testSongArtist4",4,4));
        add(new Song(5,"testSongArtist5","testSongArtist5",5,5));
        add(new Song(6,"testSongArtist6","testSongArtist6",6,6));
        add(new Song(7,"testSongArtist7","testSongArtist7",7,7));
        add(new Song(8,"testSongArtist8","testSongArtist8",8,8));
        add(new Song(9,"testSongArtist9","testSongArtist9",9,9));
        add(new Song(10,"testSongArtist10","testSongArtist10",10,10));
    }};
    static List<Playlist> playLists = new ArrayList<Playlist>() {{
        add(new Playlist(1,"testPlaylist1"));
        add(new Playlist(2,"testPlaylist2"));
        add(new Playlist(3,"testPlaylist3"));
        add(new Playlist(4,"testPlaylist4"));
        add(new Playlist(5,"testPlaylist5"));
        add(new Playlist(6,"testPlaylist6"));
        add(new Playlist(7,"testPlaylist7"));
        add(new Playlist(8,"testPlaylist8"));
        add(new Playlist(9,"testPlaylist9"));
        add(new Playlist(10,"testPlaylist10"));

    }};
}
