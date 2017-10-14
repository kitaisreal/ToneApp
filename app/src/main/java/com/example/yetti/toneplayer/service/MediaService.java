package com.example.yetti.toneplayer.service;

import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.example.yetti.toneplayer.imageLoader.ImageProcessing;
import com.example.yetti.toneplayer.model.Song;
import com.example.yetti.toneplayer.model.SongContract;
import com.example.yetti.toneplayer.musicrepository.MusicRepository;

import java.io.IOException;

public class MediaService extends Service implements  MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener{

    private final String TAG = "MediaService";
    private final MediaMetadataCompat.Builder mMediaMetadataBuilder = new MediaMetadataCompat.Builder();
    private final PlaybackStateCompat.Builder mPlaybackStateCompat = new PlaybackStateCompat.Builder().setActions(
                    PlaybackStateCompat.ACTION_PLAY
                    | PlaybackStateCompat.ACTION_STOP
                    | PlaybackStateCompat.ACTION_PAUSE
                    | PlaybackStateCompat.ACTION_PLAY_PAUSE
                    | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                    | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
    );
    private MediaSessionCompat mMediaSessionCompat;
    private AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;
    private Context mContext;
    private ImageProcessing mImageProcessing;
    private NotificationHelper mNotificationHelper;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMediaSessionCompat = new MediaSessionCompat(this,TAG);
        mMediaSessionCompat.setCallback(mMediaSessionCallback);
        mMediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mImageProcessing= new ImageProcessing(mContext);
        mNotificationHelper = new NotificationHelper(this,mContext,mMediaSessionCompat);
        initMediaPlayer();
    }
    private void initMediaPlayer(){
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setWakeMode(mContext,
                PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("OUR PROBLEM","DESTROY");
        prepareToStopService();
        mMediaSessionCompat.release();
        mMediaPlayer.release();
        Log.d("OUR PROBLEM", "DESROYED");
    }
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public void pausePlayer() {
        mMediaPlayer.pause();
    }

    public void unpausePlayer() {
        mMediaPlayer.start();
    }
    private void prepareToStopService(){
        mMediaSessionCompat.setActive(false);
        mMediaSessionCompat.setPlaybackState(mPlaybackStateCompat.setState(PlaybackStateCompat.STATE_STOPPED, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 1).build());
        mNotificationHelper.refreshNotificationAndForegroundStatus(PlaybackStateCompat.STATE_STOPPED);
    }
    public void seek(int posn) {
        mMediaPlayer.seekTo(posn);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MediaButtonReceiver.handleIntent(mMediaSessionCompat,intent);
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return new MediaServiceBinder();
    }
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("OUR PROBLEM","ON UNBIND");
        return true;
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }
    public class MediaServiceBinder extends Binder{
        public MediaSessionCompat.Token getMediaSessionToken(){
            return mMediaSessionCompat.getSessionToken();
        }
    }
    private void playSong(int id){
        mMediaPlayer.reset();
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,id);
        try {
            mMediaPlayer.setDataSource(getApplicationContext(), trackUri);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private MediaSessionCompat.Callback mMediaSessionCallback = new MediaSessionCompat.Callback() {
        @Override
        public void onPlay(){
            Log.d(TAG, "MEDIA SESSION TRY TO PLAY");
            Song song = MusicRepository.getInstance().getCurrentSong();
            Log.d(TAG,"MEDIA PLAYER GET CURRENT POSITION " + mMediaPlayer.getCurrentPosition());
            playSong((int) song.getSongId());
            updateMetadataFromTrack(song);
            mMediaSessionCompat.setPlaybackState(mPlaybackStateCompat.setState(PlaybackStateCompat.STATE_PLAYING,PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,1).build());
            mNotificationHelper.refreshNotificationAndForegroundStatus(PlaybackStateCompat.STATE_PLAYING);
        }
        @Override
        public void onPause(){
            Log.d(TAG, "MEDIA SESSION TRY TO PAUSE");
            pausePlayer();
            mMediaSessionCompat.setPlaybackState(mPlaybackStateCompat.setState(PlaybackStateCompat.STATE_PAUSED, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,1).build());
            mNotificationHelper.refreshNotificationAndForegroundStatus(PlaybackStateCompat.STATE_PAUSED);
        }
        @Override
        public void onStop(){
            Log.d("OUR PROBLEM", "MEDIA SESSION TRY TO STOP");
            mMediaPlayer.stop();
            mMediaSessionCompat.setPlaybackState(mPlaybackStateCompat.setState(PlaybackStateCompat.STATE_STOPPED,PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,1).build());
            mNotificationHelper.refreshNotificationAndForegroundStatus(PlaybackStateCompat.STATE_STOPPED);
        }
        @Override
        public void onSkipToNext() {
            Log.d(TAG, "MEDIA SESSION TRY TO SKIP TO NEXT");
            MusicRepository.getInstance().getNextSong();
            onPlay();
        }

        @Override
        public void onSkipToPrevious() {
            Log.d(TAG, "MEDIA SESSION TRY TO SKIP TO PREVIOUS");
            MusicRepository.getInstance().getPrevSong();
            onPlay();
        }
        private void updateMetadataFromTrack(Song pSong){
            final Uri sArtworkUri = Uri.parse(SongContract.SONG_ALBUM_ARTWORK_URI + pSong.getSongAlbumId());
            mMediaMetadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ART, mImageProcessing.decodeBitmap(sArtworkUri.toString()));
            mMediaMetadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, pSong.getSongName());
            mMediaMetadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, pSong.getSongArtist());
            mMediaMetadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, pSong.getSongAlbum());
            mMediaMetadataBuilder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, pSong.getSongDuration());
            mMediaSessionCompat.setMetadata(mMediaMetadataBuilder.build());
        }
    };
    //TODO ADD LISTENER FOCUS AND BECOMING NOISY
    /*
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    mMediaSessionCallback.onPlay();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    mMediaSessionCallback.onPause();
                    break;
                default:
                    mMediaSessionCallback.onPause();
                    break;
            }
        }
    };
    /*
    private final BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                mMediaSessionCallback.onPause();
            }
        }
    };*/
}
