package com.example.yetti.toneplayer.musicrepository;

import com.example.yetti.toneplayer.model.Song;
import com.example.yetti.toneplayer.threadmanager.ThreadsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MusicRepository {
    private List<Song> mCurrentSongList;
    private List<Song> mAlreadyPlayedSongList;
    private String mSongPlayMode;
    private int currentPosition;
    private static volatile MusicRepository sInstance;

    public static MusicRepository getInstance() {
        MusicRepository localInstance = sInstance;
        if (localInstance == null) {
            synchronized (ThreadsManager.class) {
                localInstance = sInstance;
                if (localInstance == null) {
                    sInstance = localInstance = new MusicRepository();
                }
            }
        }
        return localInstance;
    }

    private MusicRepository(){
        mCurrentSongList= new ArrayList<>();
        mSongPlayMode = MusicRepositoryContract.NORMAL_PLAY_MODE;
    }
    public Song getCurrentSong(){
        return mCurrentSongList.get(currentPosition);
    }
    public Song getNextSong(){
        if (Objects.equals(mSongPlayMode, MusicRepositoryContract.NORMAL_PLAY_MODE)) {
            return getNormalPlayModeNextSong();
        }
        if (Objects.equals(mSongPlayMode, MusicRepositoryContract.SHUFFLED_PLAY_MODE)){

        }
        if (Objects.equals(mSongPlayMode, MusicRepositoryContract.TONE_PLAY_MODE)){

        }
        return new Song();
    }

    public List<Song> getCurrentSongList() {
        return mCurrentSongList;
    }

    public void setCurrentSongList(List<Song> pCurrentSongList) {
        mCurrentSongList = pCurrentSongList;
    }

    public Song getPrevSong(){
        if (currentPosition>0) {
            currentPosition--;
        }
        return mCurrentSongList.get(currentPosition);
    }

    private Song getNormalPlayModeNextSong(){
        if (currentPosition<mCurrentSongList.size()){
            currentPosition++;
        }
        return mCurrentSongList.get(currentPosition);
    }
    public void setSongPlayMode(String pSongPlayMode){
        mSongPlayMode=pSongPlayMode;
    }
    public void setCurrentPosition(int pCurrentPosition){
        this.currentPosition=pCurrentPosition;
    }
}
