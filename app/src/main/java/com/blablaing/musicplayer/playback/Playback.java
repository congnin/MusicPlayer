package com.blablaing.musicplayer.playback;

import android.support.v4.media.session.MediaSessionCompat.QueueItem;

/**
 * Created by Linh on 3/22/2017.
 */

public interface Playback {
    void start();

    void stop(boolean notifyListeners);

    void setState(int state);

    int getState();

    boolean isConnected();

    boolean isPlaying();

    int getCurrentStreamPosition();

    void setCurrentStreamPosition(int pos);

    void updateLastKnowStreamPosition();

    void play(QueueItem item);

    void pause();

    void seekTo(int position);

    void setCurrentMediaId(String mediaId);

    String getCurrentMediaId();

    interface Callback {
        void onCompletion();

        void onPlaybackStatusChanged(int state);

        void onError(String error);

        void setCurrentMediaId(String mediaId);
    }

    void setCallback(Callback callback);
}
