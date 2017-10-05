package com.example.yetti.toneplayer.model;


import android.content.ContentResolver;
import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable{
    private long song_id;
    private String song_artist;
    private String song_name;
    private int song_weight;
    private String song_playlist;

    public Song(long song_id, String song_artist, String song_name, int song_weight, String song_playlist) {
        this.song_id = song_id;
        this.song_artist = song_artist;
        this.song_name = song_name;
        this.song_weight = song_weight;
        this.song_playlist=song_playlist;
    }
    public Song(){

    }
    protected Song(Parcel in) {
        song_id = in.readLong();
        song_artist = in.readString();
        song_name = in.readString();
        song_weight = in.readInt();
        song_playlist=in.readString();
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

    public long getSong_id() {
        return song_id;
    }

    public void setSong_id(long song_id) {
        this.song_id = song_id;
    }

    public int getSong_weight() {
        return song_weight;
    }

    public void setSong_weight(int song_weight) {
        this.song_weight = song_weight;
    }

    public String getSong_artist() {
        return song_artist;
    }

    public void setSong_artist(String song_artist) {
        this.song_artist = song_artist;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(song_id);
        dest.writeString(song_name);
        dest.writeString(song_artist);
        dest.writeInt(song_weight);
        dest.writeString(song_playlist);
    }

    public String getSong_playlist() {
        return song_playlist;
    }

    public void setSong_playlist(String song_playlist) {
        this.song_playlist = song_playlist;
    }
}
