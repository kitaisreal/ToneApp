package com.example.yetti.toneplayer.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {

    private long mSongId;
    private String mSongArtist;
    private String mSongName;
    private String mSongAlbum;
    private long mSongAlbumId;
    private int mSongWeight;
    private long mSongPlaylistId;
    private int mSongFavourite;
    private long mSongDuration;

    public Song() {

    }

    protected Song(Parcel pIn) {
        mSongId = pIn.readLong();
        mSongArtist = pIn.readString();
        mSongName = pIn.readString();
        mSongAlbum = pIn.readString();
        mSongAlbumId = pIn.readLong();
        mSongWeight = pIn.readInt();
        mSongPlaylistId = pIn.readLong();
        mSongFavourite = pIn.readInt();
        mSongDuration = pIn.readLong();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {

        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public Song(long song_id, String song_artist, String song_name, String song_album, long song_album_id,
                int song_weight, long song_playlist_id, int song_favourite, long song_duration) {
        this.mSongId = song_id;
        this.mSongArtist = song_artist;
        this.mSongName = song_name;
        this.mSongAlbum = song_album;
        this.mSongAlbumId = song_album_id;
        this.mSongWeight = song_weight;
        this.mSongPlaylistId = song_playlist_id;
        this.mSongFavourite = song_favourite;
        this.mSongDuration = song_duration;
    }

    public long getSongId() {
        return mSongId;
    }

    public void setSongId(long pSongId) {
        this.mSongId = pSongId;
    }

    public int getSongWeight() {
        return mSongWeight;
    }

    public void setSongWeight(int pSongWeight) {
        this.mSongWeight = pSongWeight;
    }

    public String getSongArtist() {
        return mSongArtist;
    }

    public void setSongArtist(String pSongArtist) {
        this.mSongArtist = pSongArtist;
    }

    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String pSongName) {
        this.mSongName = pSongName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel pDest, int pFlags) {
        pDest.writeLong(mSongId);
        pDest.writeString(mSongName);
        pDest.writeString(mSongArtist);
        pDest.writeString(mSongAlbum);
        pDest.writeLong(mSongAlbumId);
        pDest.writeInt(mSongWeight);
        pDest.writeLong(mSongPlaylistId);
        pDest.writeInt(mSongFavourite);
        pDest.writeLong(mSongDuration);
    }

    public long getSongPlaylist() {
        return mSongPlaylistId;
    }

    public void setSongPlaylist(long pSongPlaylist) {
        this.mSongPlaylistId = pSongPlaylist;
    }

    public String getSongAlbum() {
        return mSongAlbum;
    }

    public void setSongAlbum(String pSongAlbum) {
        this.mSongAlbum = pSongAlbum;
    }

    public long getSongAlbumId() {
        return mSongAlbumId;
    }

    public void setSongAlbumId(long pSongAlbumId) {
        this.mSongAlbumId = pSongAlbumId;
    }

    public int getSongFavourite() {
        return mSongFavourite;
    }

    public void setSongFavourite(int pSongFavourite) {
        this.mSongFavourite = pSongFavourite;
    }

    public long getSongDuration() {
        return mSongDuration;
    }

    public void setSongDuration(long pSongDuration) {
        this.mSongDuration = pSongDuration;
    }
}
