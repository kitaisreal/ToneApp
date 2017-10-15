package com.example.yetti.toneplayer.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.yetti.toneplayer.R;

public class NotificationHelper {

    public static final int ID = 404;
    private final Context mContext;
    private final String TAG="NOTIFICATION HELPER";
    private final MediaSessionCompat mMediaSessionCompat;
    private final Service mMediaService;
    public NotificationHelper(final Service pService, final Context pContext, final MediaSessionCompat pMediaSessionCompat){
        this.mContext=pContext;
        this.mMediaSessionCompat=pMediaSessionCompat;
        this.mMediaService=pService;
    }
    public void refreshNotificationAndForegroundStatus(final int playbackState) {
        Log.d(TAG,"REFRESH NOTIFICATION AND FOREGROUND STATUS");
        switch (playbackState) {
            case PlaybackStateCompat.STATE_PLAYING:
                NotificationManagerCompat.from(mContext).notify(ID,getNotification(playbackState));
                mMediaService.stopForeground(false);
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                NotificationManagerCompat.from(mContext).notify(ID,getNotification(playbackState));
                mMediaService.stopForeground(false);
                break;
            default:
                mMediaService.stopForeground(true);
                break;
        }
    }
    private Notification getNotification(final int playbackState) {
        Log.d(TAG,"TRY TO GET NOTIFICATION");
        final NotificationCompat.Builder builder = from(mContext, mMediaSessionCompat);
        builder.addAction(new NotificationCompat.Action(android.R.drawable.ic_media_previous, "PREVIOUS",
                MediaButtonReceiver.buildMediaButtonPendingIntent(mContext, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)));
        if (playbackState == PlaybackStateCompat.STATE_PLAYING) {
            builder.addAction(new NotificationCompat.Action(android.R.drawable.ic_media_pause, "PAUSE",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(mContext, PlaybackStateCompat.ACTION_PLAY_PAUSE)));
        }
        if (playbackState== PlaybackStateCompat.STATE_PAUSED){
            builder.addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play, "PLAY",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(mContext, PlaybackStateCompat.ACTION_PLAY_PAUSE)));
        }
        builder.addAction(new NotificationCompat.Action(android.R.drawable.ic_media_next, "NEXT",
                MediaButtonReceiver.buildMediaButtonPendingIntent(mContext, PlaybackStateCompat.ACTION_SKIP_TO_NEXT)));
        builder.setStyle(new NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(1)
                .setShowCancelButton(true)
                .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(mContext, PlaybackStateCompat.ACTION_STOP))
                .setMediaSession(mMediaSessionCompat.getSessionToken()));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        builder.setShowWhen(false);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setOnlyAlertOnce(true);

        return builder.build();
    }
    /**
     * Helper APIs for constructing MediaStyle notifications
     */
    /**
     * Build a notification using the information from the given media session. Makes heavy use
     * of {@link MediaMetadataCompat#getDescription()} to extract the appropriate information.
     *
     * @param context      Context used to construct the notification.
     * @param mediaSession Media session to get information.
     * @return A pre-built notification with information from the given media session.
     */
    // https://gist.github.com/ianhanniballake/47617ec3488e0257325c

    private NotificationCompat.Builder from(
            Context context, MediaSessionCompat mediaSession) {
        MediaControllerCompat controller = mediaSession.getController();
        MediaMetadataCompat mediaMetadata = controller.getMetadata();
        MediaDescriptionCompat description = mediaMetadata.getDescription();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder
                .setContentTitle(description.getTitle())
                .setContentText(description.getSubtitle())
                .setSubText(description.getDescription())
                .setLargeIcon(description.getIconBitmap())
                .setContentIntent(controller.getSessionActivity())
                .setDeleteIntent(
                        MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_STOP))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        return builder;
    }
}
