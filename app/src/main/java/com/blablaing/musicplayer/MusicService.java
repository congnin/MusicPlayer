package com.blablaing.musicplayer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.media.MediaRouter;

import com.blablaing.musicplayer.model.MusicProvider;
import com.blablaing.musicplayer.playback.CastPlayback;
import com.blablaing.musicplayer.playback.LocalPlayback;
import com.blablaing.musicplayer.playback.Playback;
import com.blablaing.musicplayer.playback.PlaybackManager;
import com.blablaing.musicplayer.utils.LogHelper;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Delayed;

/**
 * Created by Linh on 3/22/2017.
 */

public class MusicService extends MediaBrowserServiceCompat implements
        PlaybackManager.PlaybackServiceCallback {
    private static final String TAG = LogHelper.makeLogTag(MusicService.class);

    public static final String EXTRA_CONNECTED_CAST = "com.blablaing.musicplayer.CAST_NAME";
    public static final String ACTION_CMD = "com.blablaing.musicplayer.ACTION_CMD";
    public static final String CMD_NAME = "CMD_NAME";
    public static final String CMD_PAUSE = "CMD_PAUSE";
    public static final String CMD_STOP_CASTING = "CMD_STOP_CASTING";
    public static final int STOP_DELAY = 30000;

    private MusicProvider mMusicProvider;
    private PlaybackManager mPlaybackManager;

    private MediaSessionCompat mSession;
    private MediaNotificationManager mMediaNotificationManager;
    private Bundle mSessionExtras;
    private final DelayedStopHandler mDelayedStopHandler = new DelayedStopHandler(this);
    private MediaRouter mMediaRouter;
    private Pack
    private MediaRouter mMediaRouter;
    @Nullable
    @Override

    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return null;
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

    }

    @Override
    public void onPlaybackStart() {

    }

    @Override
    public void onNotificationRequired() {

    }

    @Override
    public void onPlaybackStop() {

    }

    @Override
    public void onPlaybackStateUpdated(PlaybackStateCompat newState) {

    }

    private void unregisterCarConnectionReceiver(){
        unregisterReceiver(mC);
    }

    private static class DelayedStopHandler extends Handler{
        private final WeakReference<MusicService> mWeakReference;

        private DelayedStopHandler(MusicService service){
            mWeakReference = new WeakReference<MusicService>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            MusicService service = mWeakReference.get();
            if(service != null && service.mPlaybackManager.getPlayback() != null){
                if(service.mPlaybackManager.getPlayback().isPlaying()){
                    LogHelper.d(TAG, "Ignoring delayed stop since the media player is in use.");
                    return;
                }
                LogHelper.d(TAG, "Stopping service with delay handler.");
                service.stopSelf();
            }
        }
    }

    private class CastSessionManagerListener implements SessionManagerListener<CastSession> {
        @Override
        public void onSessionEnded(CastSession session, int error) {
            LogHelper.d(TAG, "onSessionEnded");
            mSessionExtras.remove(EXTRA_CONNECTED_CAST);
            mSession.setExtras(mSessionExtras);
            Playback playback = new LocalPlayback(MusicService.this, mMusicProvider);
            mMediaRouter.setMediaSessionCompat(null);
            mPlaybackManager.switchToPlayback(playback, false);
        }

        @Override
        public void onSessionResuming(CastSession castSession, String s) {

        }

        @Override
        public void onSessionResumed(CastSession castSession, boolean b) {

        }

        @Override
        public void onSessionResumeFailed(CastSession castSession, int i) {

        }

        @Override
        public void onSessionSuspended(CastSession castSession, int i) {

        }

        @Override
        public void onSessionStarted(CastSession session, String sessionId) {
            // In case we are casting, send the device name as an extra on MediaSession metadata.
            mSessionExtras.putString(EXTRA_CONNECTED_CAST,
                    session.getCastDevice().getFriendlyName());
            mSession.setExtras(mSessionExtras);
            // Now we can switch to CastPlayback
            Playback playback = new CastPlayback(mMusicProvider, MusicService.this);
            mMediaRouter.setMediaSessionCompat(mSession);
            mPlaybackManager.switchToPlayback(playback, true);
        }

        @Override
        public void onSessionStartFailed(CastSession castSession, int i) {

        }

        @Override
        public void onSessionStarting(CastSession castSession) {

        }

        @Override
        public void onSessionEnding(CastSession castSession) {
            mPlaybackManager.getPlayback().updateLastKnowStreamPosition();
        }
    }
}
