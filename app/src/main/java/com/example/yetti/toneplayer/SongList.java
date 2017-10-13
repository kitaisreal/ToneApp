package com.example.yetti.toneplayer;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.MediaController;

import com.example.yetti.toneplayer.adapter.Adapter;
import com.example.yetti.toneplayer.controller.MusicController;
import com.example.yetti.toneplayer.model.Song;
import com.example.yetti.toneplayer.musicrepository.MusicRepository;
import com.example.yetti.toneplayer.service.SongService;

import java.util.ArrayList;
import java.util.List;

public class SongList extends Fragment implements MediaController.MediaPlayerControl {
    List<Song> list;
    SongService.myBinder songServiceBinder;
    MusicController mc;

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
        mc = new MusicController(getActivity());
        mc.setMediaPlayer(this);
        mc.setAnchorView(v.findViewById(R.id.mediaController));
        mc.setEnabled(false);
        listView.setAdapter(adapter);
        mc.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("LETS GO NEXT");
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("LETS GO PREVIOUS");
            }
        });
        listView.setAdapter(adapter);
        MusicRepository.getInstance().setCurrentSongList(list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(((MainActivity)getActivity()).getMusicServiceBound()){
                    MusicRepository.getInstance().setCurrentPosition(position);
                    ((MainActivity)getActivity()).getMediaControllerCompat().getTransportControls().play();
                    mc.show();
                }

            }
        });
        return v;
    }

    @Override
    public void start() {
        if (songServiceBinder != null) {
            songServiceBinder.getService().unpausePlayer();
        }
    }

    @Override
    public void pause() {
        if (songServiceBinder != null) {
            songServiceBinder.getService().pausePlayer();
        }
    }

    @Override
    public int getDuration() {
        if (songServiceBinder != null) {
            return songServiceBinder.getService().getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (songServiceBinder != null) {
            return songServiceBinder.getService().getPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        if (songServiceBinder != null) {
            songServiceBinder.getService().seek(pos);
        }
    }

    @Override
    public boolean isPlaying() {
        if (songServiceBinder != null) {
            return songServiceBinder.getService().isPlaying();
        }
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
