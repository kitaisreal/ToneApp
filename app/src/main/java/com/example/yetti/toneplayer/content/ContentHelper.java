package com.example.yetti.toneplayer.content;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.yetti.toneplayer.callback.ICallbackResult;
import com.example.yetti.toneplayer.model.Song;

import java.util.ArrayList;
import java.util.List;

public class ContentHelper {
    private Context mContext;
    private final String TAG = "CONTENT HELPER";
    public ContentHelper(Context pContext){
        mContext=pContext;
    }
    public void getAsyncSongsFromDevice(final ICallbackResult<List<Song>> pListICallbackResult){
        new Runnable() {
            @Override
            public void run() {
                try {
                    for (Song song:getSongsFromDevice(mContext)){
                        System.out.println(song.getSongId() + " " + song.getSongAlbum());
                    }
                    pListICallbackResult.onSuccess(getSongsFromDevice(mContext));
                }
                catch (Exception ex){
                    Exception exception = new Exception(TAG + "ASYNC GET SONGS FROM DEVICE EX");
                    pListICallbackResult.onError(exception);
                    Log.d(TAG, "ASYNC GET SONGS FROM DEVICE EX");
                }
            }
        }.run();
    };
    public List<Song> getSongsFromDevice(Context pContext){
        System.out.println("GET SONGS FROM DEVICE");
        List<Song> songsFromDeviceList = new ArrayList<>();
        ContentResolver musicResolver = pContext.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            int albumId = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int songAlbum = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int songDuration = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                long thisAlbumId = musicCursor.getLong(albumId);
                String thisALbum = musicCursor.getString(songAlbum);
                long thisSongDuration = musicCursor.getLong(songDuration);
                songsFromDeviceList.add(new Song(thisId, thisArtist, thisTitle,thisALbum,thisAlbumId,0, -1,0,thisSongDuration));
            }
            while (musicCursor.moveToNext());
        }
        musicCursor.close();
        return songsFromDeviceList;
    }
}
