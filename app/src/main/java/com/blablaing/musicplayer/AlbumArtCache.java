package com.blablaing.musicplayer;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;

import com.blablaing.musicplayer.utils.BitmapHelper;
import com.blablaing.musicplayer.utils.LogHelper;

import java.io.IOException;

/**
 * Created by Linh on 3/22/2017.
 */

public class AlbumArtCache {
    private static final String TAG = LogHelper.makeLogTag(AlbumArtCache.class);

    private static final int MAX_ALBUM_ART_CACHE_SIZE = 12 * 1024 * 1024;
    private static final int MAX_ART_WIDTH = 800;
    private static final int MAX_ART_HEIGHT = 480;

    private static final int MAX_ART_WIDTH_ICON = 128;
    private static final int MAX_ART_HEIGHT_ICON = 128;

    private static final int BIG_BITMAP_INDEX = 0;
    private static final int ICON_BITMAP_INDEX = 1;

    private final LruCache<String, Bitmap[]> mCache;

    private static final AlbumArtCache sInstance = new AlbumArtCache();

    public static AlbumArtCache getsInstance() {
        return sInstance;
    }

    private AlbumArtCache() {
        int maxSize = Math.min(MAX_ALBUM_ART_CACHE_SIZE,
                (int) (Math.min(Integer.MAX_VALUE, Runtime.getRuntime().maxMemory() / 4)));
        mCache = new LruCache<String, Bitmap[]>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap[] value) {
                return value[BIG_BITMAP_INDEX].getByteCount()
                        + value[ICON_BITMAP_INDEX].getByteCount();
            }
        };
    }

    public Bitmap getBitImage(String artUrl) {
        Bitmap[] result = mCache.get(artUrl);
        return result == null ? null : result[BIG_BITMAP_INDEX];
    }

    public Bitmap getIconImage(String artUrl) {
        Bitmap[] result = mCache.get(artUrl);
        return result == null ? null : result[ICON_BITMAP_INDEX];
    }

    public void fetch(final String artUrl, final FetchListener listener) {
        Bitmap[] bitmap = mCache.get(artUrl);
        if (bitmap != null) {
            LogHelper.d(TAG, "getOrFetch: album art is in cache, using it", artUrl);
            listener.onFetched(artUrl, bitmap[BIG_BITMAP_INDEX], bitmap[ICON_BITMAP_INDEX]);
            return;
        }
        LogHelper.d(TAG, "getOrFetch: starting asynctask to fetch ", artUrl);

        new AsyncTask<Void, Void, Bitmap[]>() {

            @Override
            protected Bitmap[] doInBackground(Void... params) {
                Bitmap[] bitmaps;
                try {
                    Bitmap bitmap = BitmapHelper.fetchAndRescaleBitmap(artUrl,
                            MAX_ART_WIDTH, MAX_ART_HEIGHT);
                    Bitmap icon = BitmapHelper.scaleBitmap(bitmap,
                            MAX_ART_WIDTH_ICON, MAX_ART_HEIGHT_ICON);
                    bitmaps = new Bitmap[]{bitmap, icon};
                    mCache.put(artUrl, bitmaps);
                } catch (IOException e) {
                    return null;
                }
                LogHelper.d(TAG, "doInBackground: putting bitmap in cache, cache size=" + mCache.size());
                return bitmaps;
            }

            @Override
            protected void onPostExecute(Bitmap[] bitmaps) {
                if (bitmaps == null) {
                    listener.onError(artUrl, new IllegalArgumentException("got null bitmaps"));
                } else {
                    listener.onFetched(artUrl,
                            bitmaps[BIG_BITMAP_INDEX], bitmaps[ICON_BITMAP_INDEX]);
                }
            }
        }.execute();
    }

    public static abstract class FetchListener {
        public abstract void onFetched(String artUrl, Bitmap bigImage, Bitmap iconImage);

        public void onError(String artUrl, Exception e) {
            LogHelper.e(TAG, e, "AlbumArtFetchListener: error while downloading " + artUrl);
        }
    }
}
