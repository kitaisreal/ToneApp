package com.example.yetti.toneplayer.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yetti.toneplayer.R;
import com.example.yetti.toneplayer.imageLoader.ImageLoader;
import com.example.yetti.toneplayer.model.Song;
import com.example.yetti.toneplayer.model.SongContract;

import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder>{
    private List<Song> mSongList;
    private Context mContext;
    private View.OnClickListener mOnClickListener;
    public SongListAdapter(final List<Song> pSongList, final Context pContext, final View.OnClickListener pOnClickListener){
        this.mSongList=pSongList;
        this.mContext=pContext;
        this.mOnClickListener=pOnClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.song,parent,false);
        v.setOnClickListener(mOnClickListener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = mSongList.get(position);
        final Uri sArtworkUri = Uri.parse(SongContract.SONG_ALBUM_ARTWORK_URI +  song.getSongAlbumId());
        ImageLoader.getInstance(mContext).displayImage(sArtworkUri.toString(),holder.mArtistArt);
        holder.mSongArtistName.setText(song.getSongArtist());
        holder.mSongTitle.setText(song.getSongName());
        holder.mSongDuration.setText(String.valueOf(song.getSongDuration()));
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView mArtistArt;
        public TextView mSongTitle;
        public TextView mSongArtistName;
        public TextView mSongDuration;
        public ViewHolder(View itemView) {
            super(itemView);
            mArtistArt= (ImageView) itemView.findViewById(R.id.song_artist_art);
            mSongTitle = (TextView) itemView.findViewById(R.id.song_name);
            mSongArtistName = (TextView) itemView.findViewById(R.id.artist_name);
            mSongDuration = (TextView) itemView.findViewById(R.id.song_duration);
        }
    }
}
