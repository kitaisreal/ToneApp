package com.example.yetti.toneplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yetti.toneplayer.R;
import com.example.yetti.toneplayer.imageLoader.ImageLoader;
import com.example.yetti.toneplayer.model.Artist;
import com.example.yetti.toneplayer.network.HttpContract;

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
        System.out.println("ARTIST NAME " + artist.getArtistName());
        System.out.println("URL " + HttpContract.GET_ARTIST +artist.getArtistName());
        ImageLoader.getInstance(mContext).displayImage("http://localhost:8080/api/getArtistImages/Eminem",holder.mArtistImage);
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
