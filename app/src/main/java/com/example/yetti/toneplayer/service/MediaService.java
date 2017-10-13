package com.example.yetti.toneplayer.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.yetti.toneplayer.MainActivity;
import com.example.yetti.toneplayer.R;
import com.example.yetti.toneplayer.model.Song;
import com.example.yetti.toneplayer.musicrepository.MusicRepository;

import java.io.IOException;

public class MediaService extends Service implements  MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener{
    private final int NOTIFICATION_ID=404;
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
    @Override
    public void onCreate() {
        super.onCreate();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMediaSessionCompat = new MediaSessionCompat(this,"MediaService");
        mMediaSessionCompat.setCallback(mMediaSessionCallback);
        mMediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mContext = getApplicationContext();
        Intent activityIntent = new Intent(mContext, MainActivity.class);
        mMediaSessionCompat.setSessionActivity(PendingIntent.getActivity(mContext,0, activityIntent,0));
        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON,null, mContext, MediaButtonReceiver.class);
        mMediaSessionCompat.setMediaButtonReceiver(PendingIntent.getBroadcast(mContext,0, mediaButtonIntent,0));
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
        mMediaSessionCompat.release();
        mMediaPlayer.release();
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

    public void seek(int posn) {
        mMediaPlayer.seekTo(posn);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MediaButtonReceiver.handleIntent(mMediaSessionCompat,intent);
        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent) {
        return new MediaServiceBinder();
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
        int currentState = PlaybackStateCompat.STATE_STOPPED;
        @Override
        public void onPlay(){
            Log.d("MEDIA SESSION COMPAT ", "TRY TO PLAY");
            startService(new Intent(getApplicationContext(),MediaService.class));
            Song song = MusicRepository.getInstance().getCurrentSong();
            updateMetadataFromTrack(song);
            int audioFocusResult = mAudioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            if (audioFocusResult != AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                return;
            }
            mMediaSessionCompat.setActive(true);
            registerReceiver(becomingNoisyReceiver, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));
            playSong((int) song.getSongId());
        }
        @Override
        public void onPause(){
            Log.d("MEDIA SESSION COMPAT ", "TRY TO PLAY");
            mMediaPlayer.pause();
            unregisterReceiver(becomingNoisyReceiver);
            mMediaSessionCompat.setPlaybackState(mPlaybackStateCompat.setState(PlaybackStateCompat.STATE_PAUSED,PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,1).build());
        }
        @Override
        public void onStop(){
            Log.d("MEDIA SESSION COMPAT ", "TRY TO PLAY");
            mMediaPlayer.stop();
            unregisterReceiver(becomingNoisyReceiver);
            mMediaSessionCompat.setPlaybackState(mPlaybackStateCompat.setState(PlaybackStateCompat.STATE_STOPPED, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,1).build());
            currentState=PlaybackStateCompat.STATE_STOPPED;
            refreshNotificationAndForegroundStatus(currentState);
            stopSelf();
        }
        @Override
        public void onSkipToNext() {
            Log.d("MEDIA SESSION COMPAT ", "TRY TO PLAY");
            Song song = MusicRepository.getInstance().getNextSong();
            updateMetadataFromTrack(song);
            refreshNotificationAndForegroundStatus(currentState);
            playSong((int) song.getSongId());
        }

        @Override
        public void onSkipToPrevious() {
            Log.d("MEDIA SESSION COMPAT ", "TRY TO PLAY");
            Song song = MusicRepository.getInstance().getNextSong();
            updateMetadataFromTrack(song);
            refreshNotificationAndForegroundStatus(currentState);
            playSong((int) song.getSongId());
        }
        private void updateMetadataFromTrack(Song pSong){
            Log.d("MEDIA SESSION COMPAT ", "TRY TO PLAY");
            mMediaMetadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, pSong.getSongName());
            mMediaMetadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, pSong.getSongArtist());
            mMediaMetadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, pSong.getSongAlbum());
            mMediaMetadataBuilder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, pSong.getSongDuration());
            mMediaMetadataBuilder.build();
        }
    };

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
    private final BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                mMediaSessionCallback.onPause();
            }
        }
    };
    private void refreshNotificationAndForegroundStatus(int playbackState) {
        switch (playbackState) {
            case PlaybackStateCompat.STATE_PLAYING: {
                startForeground(404, getNotification(playbackState));
                break;
            }
            case PlaybackStateCompat.STATE_PAUSED: {
                NotificationManagerCompat.from(this).notify(404, getNotification(playbackState));
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
        NotificationCompat.Builder builder = MediaStyleHelper.from(this, mMediaSessionCompat);
        builder.addAction(new NotificationCompat.Action(android.R.drawable.ic_media_previous, "PREVIOUS", MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)));

        if (playbackState == PlaybackStateCompat.STATE_PLAYING)
            builder.addAction(new NotificationCompat.Action(android.R.drawable.ic_media_pause, "PAUSE", MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PLAY_PAUSE)));
        else
            builder.addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play, "PLAY", MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PLAY_PAUSE)));

        builder.addAction(new NotificationCompat.Action(android.R.drawable.ic_media_next, "NEXT", MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_SKIP_TO_NEXT)));
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
