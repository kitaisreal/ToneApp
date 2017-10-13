package com.example.yetti.toneplayer.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.yetti.toneplayer.MainActivity;
import com.example.yetti.toneplayer.R;
import com.example.yetti.toneplayer.imageLoader.ImageProcessing;
import com.example.yetti.toneplayer.model.Song;
import com.example.yetti.toneplayer.model.SongContract;
import com.example.yetti.toneplayer.musicrepository.MusicRepository;

import java.io.IOException;

public class MediaService extends Service implements  MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener{
    private final int NOTIFICATION_ID=100;
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
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMediaSessionCompat = new MediaSessionCompat(this,TAG);
        mMediaSessionCompat.setCallback(mMediaSessionCallback);
        mMediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mImageProcessing= new ImageProcessing(mContext);
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
        stopTHISSHIT();
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
    private void stopTHISSHIT(){
        mMediaSessionCompat.setActive(false);
        mMediaSessionCompat.setPlaybackState(mPlaybackStateCompat.setState(PlaybackStateCompat.STATE_STOPPED, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 1).build());
        int currentState = PlaybackStateCompat.STATE_STOPPED;
        refreshNotificationAndForegroundStatus(currentState);
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
            if (mMediaPlayer.getCurrentPosition()>1){
                mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition());
            }
            playSong((int) song.getSongId());
            updateMetadataFromTrack(song);
            mMediaSessionCompat.setPlaybackState(mPlaybackStateCompat.setState(PlaybackStateCompat.STATE_PLAYING,PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,1).build());
            refreshNotificationAndForegroundStatus(PlaybackStateCompat.STATE_PLAYING);
            /*
            startService(new Intent(getApplicationContext(),MediaService.class));
            Song song = MusicRepository.getInstance().getCurrentSong();
            updateMetadataFromTrack(song);
            int audioFocusResult = mAudioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            if (audioFocusResult != AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                return;
            }
            mMediaSessionCompat.setActive(true);
            mMediaSessionCompat.setPlaybackState(mPlaybackStateCompat.setState(PlaybackStateCompat.STATE_PLAYING, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 1).build());
            registerReceiver(becomingNoisyReceiver, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));
            playSong((int) song.getSongId());
            currentState=PlaybackStateCompat.STATE_PLAYING;
            refreshNotificationAndForegroundStatus(currentState);*/
        }
        @Override
        public void onPause(){
            Log.d(TAG, "MEDIA SESSION TRY TO PAUSE");
            pausePlayer();
            mMediaSessionCompat.setPlaybackState(mPlaybackStateCompat.setState(PlaybackStateCompat.STATE_PAUSED, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,1).build());
            refreshNotificationAndForegroundStatus(PlaybackStateCompat.STATE_PAUSED);
            /*
            mMediaPlayer.pause();
            unregisterReceiver(becomingNoisyReceiver);
            mMediaSessionCompat.setPlaybackState(mPlaybackStateCompat.setState(PlaybackStateCompat.STATE_PAUSED,PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,1).build());*/
        }
        @Override
        public void onStop(){
            Log.d("OUR PROBLEM", "MEDIA SESSION TRY TO STOP");
            mMediaPlayer.stop();
            mMediaSessionCompat.setPlaybackState(mPlaybackStateCompat.setState(PlaybackStateCompat.STATE_STOPPED,PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,1).build());
            /*
            mMediaPlayer.stop();
            Log.d("OUR PROBLEM", "PLAYER TRY TO STOP");
            unregisterReceiver(becomingNoisyReceiver);
            Log.d("OUR PROBLEM", "UNREGISTER RECEIVER");
            mMediaSessionCompat.setPlaybackState(mPlaybackStateCompat.setState(PlaybackStateCompat.STATE_STOPPED, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,1).build());
            Log.d("OUR PROBLEM", "SET PLAYBACK STATE");
            currentState=PlaybackStateCompat.STATE_STOPPED;
            Log.d("OUR PROBLEM", "CURRENT STATE STOP");
            refreshNotificationAndForegroundStatus(currentState);
            Log.d("OUR PROBLEM","REFRESH NOT AND FOREGROUND STATUS");
            stopSelf();
            Log.d("OUR PROBLEM", "STOP SELF");*/
        }
        @Override
        public void onSkipToNext() {
            Log.d(TAG, "MEDIA SESSION TRY TO SKIP TO NEXT");
            /*
            Song song = MusicRepository.getInstance().getNextSong();
            updateMetadataFromTrack(song);
            refreshNotificationAndForegroundStatus(currentState);
            playSong((int) song.getSongId());*/
        }

        @Override
        public void onSkipToPrevious() {
            Log.d(TAG, "MEDIA SESSION TRY TO SKIP TO PREVIOUS");
            /*
            Song song = MusicRepository.getInstance().getNextSong();
            updateMetadataFromTrack(song);
            refreshNotificationAndForegroundStatus(currentState);
            playSong((int) song.getSongId());*/
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
    private void refreshNotificationAndForegroundStatus(int playbackState) {
        Log.d(TAG,"REFRESH NOTIFICATION AND FOREGROUND STATUS");
        switch (playbackState) {
            case PlaybackStateCompat.STATE_PLAYING: {
                NotificationManagerCompat.from(mContext).notify(2,getNotification(playbackState));
                stopForeground(false);
                break;
            }
            case PlaybackStateCompat.STATE_PAUSED: {
                NotificationManagerCompat.from(mContext).notify(2,getNotification(playbackState));
                stopForeground(false);
                break;
            }
            default: {
                stopForeground(true);
                break;
            }
        }
    }
    private Notification getNotification(int playbackState) {
        Log.d(TAG,"TRY TO GET NOTIFICATION");
        NotificationCompat.Builder builder = MediaStyleHelper.from(this, mMediaSessionCompat);
        builder.addAction(new NotificationCompat.Action(android.R.drawable.ic_media_previous, "PREVIOUS",
                MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)));

        if (playbackState == PlaybackStateCompat.STATE_PLAYING) {
            builder.addAction(new NotificationCompat.Action(android.R.drawable.ic_media_pause, "PAUSE",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PLAY_PAUSE)));
        }
        if (playbackState==PlaybackStateCompat.STATE_PAUSED){
            builder.addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play, "PLAY",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PLAY_PAUSE)));
        }
        builder.addAction(new NotificationCompat.Action(android.R.drawable.ic_media_next, "NEXT",
                MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_SKIP_TO_NEXT)));
        builder.setStyle(new NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(1)
                .setShowCancelButton(true)
                .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_STOP))
                .setMediaSession(mMediaSessionCompat.getSessionToken()));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        builder.setShowWhen(false);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setOnlyAlertOnce(true);

        return builder.build();
    }
}
