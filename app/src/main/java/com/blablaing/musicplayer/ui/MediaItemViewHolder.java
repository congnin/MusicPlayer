package com.blablaing.musicplayer.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.blablaing.musicplayer.utils.MediaIDHelper;

/**
 * Created by Linh on 3/23/2017.
 */

public class MediaItemViewHolder {
    public static final int STATE_INVALID = -1;
    public static final int STATE_NONE = 0;
    public static final int STATE_PLAYABLE = 1;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_PLAYING = 3;

    private static ColorStateList sColorStatePlaying;
    private static ColorStateList sColorStateNotPlaying;

    ImageView mImageView;
    TextView mTitleView;
    TextView mDescriptionView;

    public static Drawable getDrawableByState(Context context, int state){
        
    }

    public static int getMediaItemState(Context context, MediaBrowserCompat.MediaItem mediaItem) {
        int state = STATE_NONE;

        if (mediaItem.isPlayable()) {
            state = STATE_PLAYABLE;
            if (MediaIDHelper.isMediaItemPlaying(context, mediaItem)) {
                state = getStateFromController(context);
            }
        }
        return state;
    }

    public static int getStateFromController(Context context) {
        MediaControllerCompat controller = ((FragmentActivity) context).getSupportMediaController();
        PlaybackStateCompat pbState = controller.getPlaybackState();
        if (pbState == null ||
                pbState.getState() == PlaybackStateCompat.STATE_ERROR) {
            return MediaItemViewHolder.STATE_NONE;
        } else if (pbState.getState() == PlaybackStateCompat.STATE_PLAYING) {
            return MediaItemViewHolder.STATE_PLAYING;
        } else {
            return MediaItemViewHolder.STATE_PAUSED;
        }
    }
}
