package com.blablaing.musicplayer.ui;

import android.support.v4.media.MediaBrowserCompat;

/**
 * Created by Linh on 3/23/2017.
 */

public class MusicPlayerActivity extends ActionBarCastActivity implements MediaBrowserProvider{
    @Override
    public MediaBrowserCompat getMediaBrowser() {
        return null;
    }
}
