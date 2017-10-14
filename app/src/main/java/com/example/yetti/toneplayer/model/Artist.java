package com.example.yetti.toneplayer.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Artist implements Parcelable {
    private String mArtistName;
    private String mAlbumName;
    private long mSongCount;

    public Artist(String pArtistName, String pAlbumName, long pSongCount) {
        this.mArtistName = pArtistName;
        this.mSongCount = pSongCount;
        this.mAlbumName=pAlbumName;
    }

    protected Artist(Parcel in) {
        mArtistName = in.readString();
        mAlbumName = in.readString();
        mSongCount = in.readLong();
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {

        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    public String getArtistName() {
        return mArtistName;
    }

    public void setArtistName(String pArtistName) {
        mArtistName = pArtistName;
    }

    public long getSongCount() {
        return mSongCount;
    }

    public void setSongCount(long pSongCount) {
        mSongCount = pSongCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mArtistName);
        dest.writeString(mAlbumName);
        dest.writeLong(mSongCount);
    }
}
