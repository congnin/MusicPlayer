package com.blablaing.musicplayer.utils;

import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

/**
 * Created by Linh on 3/23/2017.
 */

public class CarHelper {
    private static final String TAG = LogHelper.makeLogTag(CarHelper.class);

    private static final String AUTO_APP_PACKAGE_NAME = "com.google.android.projection.gearhead";
    private static final String SLOT_RESERVATION_SKIP_TO_NEXT =
            "com.google.android.gms.car.media.ALWAYS_RESERVE_SPACE_FOR.ACTION_SKIP_TO_NEXT";
    private static final String SLOT_RESERVATION_SKIP_TO_PREV =
            "com.google.android.gms.car.media.ALWAYS_RESERVE_SPACE_FOR.ACTION_SKIP_TO_PREVIOUS";
    private static final String SLOT_RESERVATION_QUEUE =
            "com.google.android.gms.car.media.ALWAYS_RESERVE_SPACE_FOR.ACTION_QUEUE";

    public static final String ACTION_MEDIA_STATUS = "com.google.android.gms.car.media.STATUS";

    public static final String MEDIA_CONNECTION_STATUS = "media_connection_status";

    public static final String MEDIA_CONNECTED = "media_connected";

    public static final String MEDIA_DISCONNECTED = "media_disconnected";

    public static boolean isValidCarPackage(String packageName) {
        return AUTO_APP_PACKAGE_NAME.equals(packageName);
    }

    public static void setSlotReservationFlags(Bundle extras, boolean reservePlayingQueueSlot,
                                               boolean reserveSkipToNextSlot, boolean reserveSkipToPrevSlot) {
        if (reservePlayingQueueSlot) {
            extras.putBoolean(SLOT_RESERVATION_QUEUE, true);
        } else {
            extras.remove(SLOT_RESERVATION_QUEUE);
        }
        if (reserveSkipToPrevSlot) {
            extras.putBoolean(SLOT_RESERVATION_SKIP_TO_PREV, true);
        } else {
            extras.remove(SLOT_RESERVATION_SKIP_TO_PREV);
        }
        if (reserveSkipToNextSlot) {
            extras.putBoolean(SLOT_RESERVATION_SKIP_TO_NEXT, true);
        } else {
            extras.remove(SLOT_RESERVATION_SKIP_TO_NEXT);
        }
    }

    public static boolean isCarUiMode(Context c) {
        UiModeManager uiModeManager = (UiModeManager) c.getSystemService(Context.UI_MODE_SERVICE);
        if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_CAR) {
            LogHelper.d(TAG, "Running in Car mode");
            return true;
        } else {
            LogHelper.d(TAG, "Running on a non-Car mode");
            return false;
        }
    }

}
