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
import com.example.yetti.toneplayer.model.Artist;
import com.example.yetti.toneplayer.model.Song;
import com.example.yetti.toneplayer.model.SongContract;

import java.util.List;

public class ArtistListAdapter extends  RecyclerView.Adapter<ArtistListAdapter.ViewHolder>{
    private List<Artist> mArtistList;
    private Context mContext;
    //private View.OnClickListener mOnClickListener;
    public ArtistListAdapter(Context pContext, List<Artist> pArtistList){
        this.mContext=pContext;
        this.mArtistList=pArtistList;
        //this.mOnClickListener = pOnClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.artist_card,parent,false);
        //v.setOnClickListener(mOnClickListener);
        return new ArtistListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Artist artist = mArtistList.get(position);
        ImageLoader.getInstance(mContext).displayImage("https://i.scdn.co/image/eb266625dab075341e8c4378a177a27370f91903",holder.mArtistImage);
        holder.mArtistTitle.setText(artist.getArtistName());
        holder.mSongCount.setText(String.valueOf("SONGS:"+artist.getSongCount()));
    }

    @Override
    public int getItemCount() {
        return mArtistList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mArtistImage;
        private TextView mArtistTitle;
        private TextView mSongCount;
        public ViewHolder(View itemView) {
            super(itemView);
            mArtistImage = (ImageView) itemView.findViewById(R.id.artist_image);
            mArtistTitle = (TextView) itemView.findViewById(R.id.artist_title);
            mSongCount = (TextView) itemView.findViewById(R.id.song_count);
        }
    }
}
