package com.example.yetti.toneplayer;

import android.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.MediaController;

import com.example.yetti.toneplayer.adapter.Adapter;
import com.example.yetti.toneplayer.controller.MusicController;
import com.example.yetti.toneplayer.model.Song;
import com.example.yetti.toneplayer.service.TestService;

import java.util.ArrayList;
import java.util.List;

public class SongList extends Fragment implements MediaController.MediaPlayerControl {
    //TODO CHANGE FRAGMENT
    List<Song> list;
    TestService.myBinder songServiceBinder;
    MusicController mc;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list=new ArrayList<>();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.song_list,null);

        ListView listView= (ListView) v.findViewById(R.id.listSongs);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            System.out.println(bundle.size());
            songServiceBinder= (TestService.myBinder) bundle.getBinder("songServiceBinder");
            System.out.println(songServiceBinder);
            list = bundle.getParcelableArrayList("songs");
        }
        Adapter adapter = new Adapter(getContext(),list);
        mc = new MusicController(getContext());
        mc.setMediaPlayer(this);
        mc.setAnchorView(v.findViewById(R.id.mediaController));
        mc.setEnabled(false);
        mc.setOnClickListener(l->{
            System.out.println("CLICK ON MEDIA PLAYER");
        });
        mc.setPrevNextListeners(v1 -> {
            System.out.println("LETS GO NEXT");
        }, v12 -> {
            System.out.println("LETS GO PREVIOUS");
        });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {songServiceBinder.getService().playSong(id);mc.show(0);mc.setEnabled(true);});

        return v;
    }
    @Override
    public void start() {
        if (songServiceBinder!=null) {
            songServiceBinder.getService().unpausePlayer();
        }
    }

    @Override
    public void pause() {
        if (songServiceBinder!=null) {
            songServiceBinder.getService().pausePlayer();
        }
    }

    @Override
    public int getDuration() {
        if (songServiceBinder!=null) {
            return songServiceBinder.getService().getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (songServiceBinder!=null) {
            return songServiceBinder.getService().getPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        if (songServiceBinder!=null) {
            songServiceBinder.getService().seek(pos);
        }
    }

    @Override
    public boolean isPlaying() {
        if (songServiceBinder!=null) {
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
