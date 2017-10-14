package com.example.yetti.toneplayer.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yetti.toneplayer.R;
import com.example.yetti.toneplayer.adapter.SongListAdapter;
import com.example.yetti.toneplayer.model.Song;
import com.example.yetti.toneplayer.musicrepository.MusicRepository;

import java.util.ArrayList;
import java.util.List;

public class SongListFragment extends Fragment {
    List<Song> mSongList;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SongListAdapter mSongListAdapter;
    private View.OnClickListener mOnClickListener;

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
    }

}
