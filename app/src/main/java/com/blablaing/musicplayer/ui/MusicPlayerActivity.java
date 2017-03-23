package com.blablaing.musicplayer.ui;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

import com.blablaing.musicplayer.utils.LogHelper;

/**
 * Created by Linh on 3/23/2017.
 */

public class MusicPlayerActivity extends BaseActivity {
    private static final String TAG = LogHelper.makeLogTag(MusicPlayerActivity.class);
    private static final String SAVED_MEDIA_ID="com.example.android.uamp.MEDIA_ID";
    private static final String FRAGMENT_TAG = "uamp_list_container";

    public static final String EXTRA_START_FULLSCREEN =
            "com.blablaing.musicplayer.EXTRA_START_FULLSCREEN";

    public static final String EXTRA_CURRENT_MEDIA_DESCRIPTION =
            "com.blablaing.musicplayer.CURRENT_MEDIA_DESCRIPTION";

    private Bundle mVoiceSearchParams;
    @Override
    public MediaBrowserCompat getMediaBrowser() {
        return null;
    }
}
