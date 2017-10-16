package com.example.yetti.toneplayer.ui;

import android.app.Activity;
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
import com.example.yetti.toneplayer.model.Artist;
import com.example.yetti.toneplayer.musicrepository.MusicRepository;

import java.util.ArrayList;
import java.util.List;

public class ArtistListFragment extends Fragment {
    IOnArtistSelectedListener mOnArtistSelectedListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mOnArtistSelectedListener = (IOnArtistSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnArtistSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
        System.out.println("ARTIST LIST FRAGMENT CREATE VIEW");
        final View view = inflater.inflate(R.layout.fragment_artist_list, container, false);
        List<Artist> artistList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.artist_list);
        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            artistList = bundle.getParcelableArrayList("artists");
            System.out.println("ARTIST LIST " + artistList.size());
        }
        if (recyclerView !=null){
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);

            final List<Artist> finalArtistList = artistList;
            final View.OnClickListener onClickListener = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final int itemPosition = recyclerView.getChildLayoutPosition(v);
                    mOnArtistSelectedListener.onArtistSelected(finalArtistList.get(itemPosition).getArtistName());
                }
            };
            ArtistListAdapter artistListAdapter = new ArtistListAdapter(getActivity().getApplicationContext(), artistList, onClickListener);
            recyclerView.setAdapter(artistListAdapter);
        }
        return view;
    }
}
