package com.example.yetti.toneplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yetti.toneplayer.R;
import com.example.yetti.toneplayer.model.Song;

import java.util.List;

public class Adapter extends BaseAdapter {
    private List<Song> songList;
    private LayoutInflater layoutInflater;

    public Adapter(Context context, List<Song> songs){
        songList=songs;
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
            convertView.setTag(holder);
        } else{
            holder=(ViewHolder) convertView.getTag();
        }
        holder.artistName.setText(((Song) getItem(position)).getSong_artist());
        holder.songName.setText(((Song) getItem(position)).getSong_name());
        return convertView;
    }
}
