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
    protected Song(final Parcel pIn) {
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
        public Song createFromParcel(final Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(final int size) {
            return new Song[size];
        }
    };

    public Song(final long song_id, final String song_artist, final String song_name, final String song_album, final long song_album_id,
                final int song_weight, final long song_playlist_id, final int song_favourite, final long song_duration) {
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

    public void setSongId(final long pSongId) {
        this.mSongId = pSongId;
    }

    public int getSongWeight() {
        return mSongWeight;
    }

    public void setSongWeight(final int pSongWeight) {
        this.mSongWeight = pSongWeight;
    }

    public String getSongArtist() {
        return mSongArtist;
    }

    public void setSongArtist(final String pSongArtist) {
        this.mSongArtist = pSongArtist;
    }

    public String getSongName() {
        return mSongName;
    }

    public void setSongName(final String pSongName) {
        this.mSongName = pSongName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel pDest, final int pFlags) {
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

    public void setSongPlaylist(final long pSongPlaylist) {
        this.mSongPlaylistId = pSongPlaylist;
    }

    public String getSongAlbum() {
        return mSongAlbum;
    }

    public void setSongAlbum(final String pSongAlbum) {
        this.mSongAlbum = pSongAlbum;
    }

    public long getSongAlbumId() {
        return mSongAlbumId;
    }

    public void setSongAlbumId(final long pSongAlbumId) {
        this.mSongAlbumId = pSongAlbumId;
    }

    public int getSongFavourite() {
        return mSongFavourite;
    }

    public void setSongFavourite(final int pSongFavourite) {
        this.mSongFavourite = pSongFavourite;
    }

    public long getSongDuration() {
        return mSongDuration;
    }

    public void setSongDuration(final long pSongDuration) {
        this.mSongDuration = pSongDuration;
    }
}
