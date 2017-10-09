package com.example.yetti.toneplayer.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yetti.toneplayer.R;
import com.example.yetti.toneplayer.imageLoader.ImageLoader;
import com.example.yetti.toneplayer.model.Song;
import com.example.yetti.toneplayer.model.SongContract;

import java.io.IOException;
import java.util.List;

public class Adapter extends BaseAdapter {
    private List<Song> songList;
    private LayoutInflater layoutInflater;
    private Context mContext;
    public Adapter(Context context, List<Song> songs){
        songList=songs;
        mContext = context;
        layoutInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return songList.size();
    }
    @Override
    public Object getItem(int position) {
        return songList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return songList.get(position).getSong_id();
    }
    static class ViewHolder{
        TextView songName;
        TextView artistName;
        int song_id;
        ImageView song_album;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Song song = (Song) getItem(position);
        if (convertView==null){
            convertView = layoutInflater.inflate(R.layout.song,parent, false);
            holder= new ViewHolder();
            holder.songName= (TextView) convertView.findViewById(R.id.songName);
            holder.artistName= (TextView) convertView.findViewById(R.id.artistName);
            holder.song_album=(ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else{
            holder=(ViewHolder) convertView.getTag();
        }
        System.out.println("CONTENT URIS FOR SONG " + ((Song) getItem(position)).getSong_artist() + " " + ((Song) getItem(position)).getSong_name() + " ");
        holder.artistName.setText(((Song) getItem(position)).getSong_artist());
        holder.songName.setText(((Song) getItem(position)).getSong_name());
        final Uri sArtworkUri = Uri.parse(SongContract.SONG_ALBUM_ARTWORK_URI+((Song) getItem(position)).getSong_album_id());
        ImageLoader.getInstance().displayImageByUri(holder.song_album, sArtworkUri, mContext );
        return convertView;
    }
}
