/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.longmaster.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.StringDef;
import android.support.v4.app.NotificationCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.longmaster.doctorlibrary.R;


/**
 * Helper class to manage notification channels, and create notifications.
 */
public final class NotificationHelper extends ContextWrapper {
    private NotificationManager manager;
    public static final String IMPORTANCE_DEFAULT = "IMPORTANCE_DEFAULT";
    public static final String IMPORTANCE_HIGH = "IMPORTANCE_HIGH";
    public static final String IMPORTANCE_LOW = "IMPORTANCE_LOW";
    @StringDef({IMPORTANCE_DEFAULT,IMPORTANCE_HIGH,IMPORTANCE_LOW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Importance {}
    /**
     * Registers notification channels, which can be used later by individual notifications.
     *
     * @param ctx The application context
     */
    public NotificationHelper(Context ctx) {
        super(ctx);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan1 = new NotificationChannel(IMPORTANCE_DEFAULT,
                    getString(R.string.noti_channel_default), NotificationManager.IMPORTANCE_DEFAULT);
            chan1.setLightColor(Color.GREEN);
            chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(chan1);

            NotificationChannel chan2 = new NotificationChannel(IMPORTANCE_HIGH,
                    getString(R.string.noti_channel_high), NotificationManager.IMPORTANCE_HIGH);
            chan2.setLightColor(Color.BLUE);
            chan2.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(chan2);

            NotificationChannel chan3 = new NotificationChannel(IMPORTANCE_LOW,
                    getString(R.string.noti_channel_low), NotificationManager.IMPORTANCE_LOW);
            chan3.setLightColor(Color.BLUE);
            chan3.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(chan3);
        }
    }

    /**
     * Get a notification of type 1
     * <p>
     * Provide the builder rather than the notification it's self as useful for making notification
     * changes.
     *
     * @return the builder as it keeps a reference to the notification (since API 24)
     */
    public NotificationCompat.Builder getNotification(@Importance String importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new NotificationCompat.Builder(getApplicationContext(), importance);
        } else {
            return new NotificationCompat.Builder(getApplicationContext());
        }
    }

    /**
     * Send a notification.
     *
     * @param id           The ID of the notification
     * @param notification The notification object
     */
    public void notify(int id, NotificationCompat.Builder notification) {
        getManager().notify(id, notification.build());
    }

    public void cancel(int notificationID) {
        getManager().cancel(notificationID);
    }
    public void cancelAll(){
        getManager().cancelAll();
    }
    /**
     * Get the notification manager.
     * <p>
     * Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }
}
