/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.android.deskclock;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.net.Uri;

/**
 * Manages the {@link android.app.NotificationChannel}s for Telecom.
 */
public class NotificationChannelManager {
    public static final String CHANNEL_ID_MISSED_ALARM = "missed_alarms";
    static final String NOTIFICATION_MISSED_ALARM = "Missed alarms";
    public static final String CHANNEL_ID_UPCOMING_ALARM = "upcoming_alarms";
    static final String NOTIFICATION_UPCOMING_ALARM = "Upcoming Alarms";
    public static final String CHANNEL_ID_STOPWATCH = "stopwatch";
    static final String NOTIFICATION_STOPWATCH = "Stopwatch";
    public static final String CHANNEL_ID_TIMER = "timers";
    static final String NOTIFICATION_TIMER = "Timers";

    public void createChannels(Context context) {
        createSingleChannel(context, CHANNEL_ID_MISSED_ALARM);
        createSingleChannel(context, CHANNEL_ID_STOPWATCH);
        createSingleChannel(context, CHANNEL_ID_TIMER);
        createSingleChannel(context, CHANNEL_ID_UPCOMING_ALARM);
    }

    private void createSingleChannel(Context context, String channelId) {
        NotificationChannel channel = createChannel(context, channelId);
        getNotificationManager(context).createNotificationChannel(channel);
    }

    private NotificationChannel createChannel(Context context, String channelId) {
        Uri silentRingtone = Uri.parse("");

        CharSequence name = "";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        boolean canShowBadge = false;
        boolean lights = false;
        boolean vibration = false;
        Uri sound = null;
        switch (channelId) {
            case CHANNEL_ID_MISSED_ALARM:
                name = NOTIFICATION_MISSED_ALARM;
                break;
            case CHANNEL_ID_UPCOMING_ALARM:
                name = NOTIFICATION_UPCOMING_ALARM;
                break;
            case CHANNEL_ID_STOPWATCH:
                name = NOTIFICATION_STOPWATCH;
                break;
            case CHANNEL_ID_TIMER:
                name = NOTIFICATION_TIMER;
                break;
        }

        NotificationChannel channel = new NotificationChannel(channelId, name, importance);
        channel.setShowBadge(canShowBadge);
        if (sound != null) {
            channel.setSound(
                    sound,
                    new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build());
        }
        channel.enableLights(lights);
        channel.enableVibration(vibration);
        return channel;
    }

    private NotificationManager getNotificationManager(Context context) {
        return context.getSystemService(NotificationManager.class);
    }
}
