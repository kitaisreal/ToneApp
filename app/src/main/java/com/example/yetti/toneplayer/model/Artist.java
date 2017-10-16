package com.example.yetti.toneplayer.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Artist implements Parcelable {
    private String mArtistName;
    private long mSongCount;
    private String mArtistArtUrl;
    private String mArtistGenre;

    public Artist(final String pArtistName, final long pSongCount, final String pArtistArtUrl, final String pArtistGenre ) {
        this.mArtistName = pArtistName;
        this.mSongCount = pSongCount;
        this.mArtistArtUrl = pArtistArtUrl;
        this.mArtistGenre = pArtistGenre;
    }
    public Artist(){

    }

    @Override
    public String toString() {
        return "ARTIST NAME " + mArtistName + " ARTIST SONG COUNT " + mSongCount + " ARTIST GENRE " + mArtistGenre + " ARTIST ARTWORK URL " + mArtistArtUrl;
    }

    protected Artist(final Parcel in) {
        mArtistName = in.readString();
        mSongCount = in.readLong();
        mArtistArtUrl = in.readString();
        mArtistGenre = in.readString();
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {

        @Override
        public Artist createFromParcel(final Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(final int size) {
            return new Artist[size];
        }
    };

    public String getArtistName() {
        return mArtistName;
    }

    public void setArtistName(final String pArtistName) {
        mArtistName = pArtistName;
    }

    public long getSongCount() {
        return mSongCount;
    }

    public void setSongCount(final long pSongCount) {
        mSongCount = pSongCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {

        dest.writeString(mArtistName);
        dest.writeLong(mSongCount);
        dest.writeString(mArtistArtUrl);
        dest.writeString(mArtistGenre);
    }


    public String getArtistArtUrl() {
        return mArtistArtUrl;
    }

    public void setArtistArtUrl(final String pArtistArtUrl) {
        mArtistArtUrl = pArtistArtUrl;
    }

    public String getArtistGenre() {
        return mArtistGenre;
    }

    public void setArtistGenre(final String pArtistGenre) {
        mArtistGenre = pArtistGenre;
    }
}
