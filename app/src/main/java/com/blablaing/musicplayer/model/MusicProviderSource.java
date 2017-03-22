package com.blablaing.musicplayer.model;

import android.support.v4.media.MediaMetadataCompat;

import java.util.Iterator;

/**
 * Created by Linh on 3/22/2017.
 */

public interface MusicProviderSource {
    String CUSTOM_METADATA_TRACK_SOURCE = "__SOURCE__";

    Iterator<MediaMetadataCompat> iterator();
}
