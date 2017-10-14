package com.example.yetti.toneplayer.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yetti.toneplayer.R;
import com.example.yetti.toneplayer.adapter.ArtistListAdapter;
import com.example.yetti.toneplayer.adapter.SongListAdapter;
import com.example.yetti.toneplayer.model.Artist;

import java.util.ArrayList;
import java.util.List;

public class ArtistListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArtistListAdapter mArtistListAdapter;
    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_song_list, container,false);
        mSongList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mSongList = bundle.getParcelableArrayList("songs");
        }
        mRecyclerView = (RecyclerView) v.findViewById(R.id.list_songs);
        if (mRecyclerView!=null) {
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mOnClickListener = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    MusicRepository.getInstance().setCurrentSongList(mSongList);
                    int itemPosition=mRecyclerView.getChildLayoutPosition(v);
                    MusicRepository.getInstance().setCurrentPosition(itemPosition);
                    ((MainActivity)getActivity()).getMediaControllerCompat().getTransportControls().play();
                }
            };
            mSongListAdapter = new SongListAdapter(mSongList, getActivity().getApplicationContext(),mOnClickListener);
            mRecyclerView.setAdapter(mSongListAdapter);
        }
        return v;

     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_list, container, false);
        List<Artist> artistList = new ArrayList<>();
        artistList.add(new Artist("ARTISTNAME","ALBUMNAME",10));
        artistList.add(new Artist("ARTISTNAME","ALBUMNAME",10));
        artistList.add(new Artist("ARTISTNAME","ALBUMNAME",10));
        artistList.add(new Artist("ARTISTNAME","ALBUMNAME",10));
        artistList.add(new Artist("ARTISTNAME","ALBUMNAME",10));
        artistList.add(new Artist("ARTISTNAME","ALBUMNAME",10));
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.artist_list);
        if (mRecyclerView!=null){
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mArtistListAdapter = new ArtistListAdapter( getActivity().getApplicationContext(), artistList);
            mRecyclerView.setAdapter(mArtistListAdapter);
        }
        return view;
    }
}
