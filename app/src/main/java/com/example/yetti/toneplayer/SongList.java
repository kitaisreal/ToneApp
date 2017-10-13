package com.example.yetti.toneplayer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.yetti.toneplayer.adapter.Adapter;
import com.example.yetti.toneplayer.model.Song;
import com.example.yetti.toneplayer.musicrepository.MusicRepository;
import com.example.yetti.toneplayer.service.SongService;

import java.util.ArrayList;
import java.util.List;

public class SongList extends Fragment {
    List<Song> list;
    SongService.myBinder songServiceBinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.song_list, null);
        ListView listView = (ListView) v.findViewById(R.id.listSongs);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            list = bundle.getParcelableArrayList("songs");
        }
        Adapter adapter = new Adapter(getActivity(), list);
        listView.setAdapter(adapter);
        listView.setAdapter(adapter);
        MusicRepository.getInstance().setCurrentSongList(list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(((MainActivity)getActivity()).getMusicServiceBound()){
                    MusicRepository.getInstance().setCurrentPosition(position);
                    ((MainActivity)getActivity()).getMediaControllerCompat().getTransportControls().play();
                }

            }
        });
        return v;
    }

}
