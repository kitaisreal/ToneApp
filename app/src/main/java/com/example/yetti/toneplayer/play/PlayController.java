package com.example.yetti.toneplayer.play;

import com.example.yetti.toneplayer.model.Song;

import java.util.List;

public class PlayController {
    private PlayMode playMode;
    List<Song> currentSongList;
    List<Song> playedSongList;
    public Song getNextSong(){
        switch (playMode){
            case NORMAL:
                break;
            case SHUFFLE:
                break;
            case TONE:
                break;
        };
        return new Song(1,"ASD","RRE",4,5);
    }
    public Song getPrevSong(){
        switch (playMode){
            case NORMAL:
                break;
            case SHUFFLE:
                break;
            case TONE:
                break;
        };
        return new Song(1,"ASD","RRE",4,5);
    };
    public PlayMode getPlayMode() {
        return playMode;
    }

    public void setPlayMode(PlayMode playMode) {
        this.playMode = playMode;
    }
}
