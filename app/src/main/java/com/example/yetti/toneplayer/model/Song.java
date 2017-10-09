package com.example.yetti.toneplayer.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable{
    private long song_id;
    private String song_artist;
    private String song_name;
    private String song_album;
    private long song_album_id;
    private int song_weight;
    private long song_playlist_id;
    private int song_favourite;
    private long song_duration;
    public Song(){

    }
    protected Song(Parcel in) {
        song_id = in.readLong();
        song_artist = in.readString();
        song_name = in.readString();
        song_album = in.readString();
        song_album_id = in.readLong();
        song_weight = in.readInt();
        song_playlist_id=in.readLong();
        song_favourite=in.readInt();
        song_duration= in.readLong();
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

    public Song(long song_id, String song_artist, String song_name, String song_album, long song_album_id, int song_weight, long song_playlist_id, int song_favourite, long song_duration) {
        this.song_id = song_id;
        this.song_artist = song_artist;
        this.song_name = song_name;
        this.song_album = song_album;
        this.song_album_id = song_album_id;
        this.song_weight = song_weight;
        this.song_playlist_id = song_playlist_id;
        this.song_favourite=song_favourite;
        this.song_duration=song_duration;
    }

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
        dest.writeString(song_album);
        dest.writeLong(song_album_id);
        dest.writeInt(song_weight);
        dest.writeLong(song_playlist_id);
        dest.writeInt(song_favourite);
        dest.writeLong(song_duration);
    }

    public long getSong_playlist() {
        return song_playlist_id;
    }

    public void setSong_playlist(long song_playlist) {
        this.song_playlist_id = song_playlist;
    }

    public String getSong_album() {
        return song_album;
    }

    public void setSong_album(String song_album) {
        this.song_album = song_album;
    }

    public long getSong_album_id() {
        return song_album_id;
    }

    public void setSong_album_id(long song_album_id) {
        this.song_album_id = song_album_id;
    }

    public int getSong_favourite() {
        return song_favourite;
    }

    public void setSong_favourite(int song_favourite) {
        this.song_favourite = song_favourite;
    }

    public long getSong_duration() {
        return song_duration;
    }

    public void setSong_duration(long song_duration) {
        this.song_duration = song_duration;
    }
}
