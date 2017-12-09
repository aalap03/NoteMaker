package com.app.aalap.notes.Utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.app.aalap.notes.MyService.Receiver;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Aalap on 2017-08-15.
 */

public class AppUtils {

    private static final String TAG = "AppUtils";

    static public void setUpAlarm(Activity activity, long alarmTimeInMilis, int requestCode) {
        AlarmManager manager = (AlarmManager) activity.getSystemService(activity.ALARM_SERVICE);
        Intent alarmIntent = new Intent(activity, Receiver.class);
        alarmIntent.putExtra("reminderTime", alarmTimeInMilis);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, requestCode, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.set(AlarmManager.RTC_WAKEUP, alarmTimeInMilis, pendingIntent);
    }

    static public void cancelAlarm(Activity activity, int requestCode) {
        AlarmManager manager = (AlarmManager) activity.getSystemService(activity.ALARM_SERVICE);
        Intent alarmIntent = new Intent(activity, Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, requestCode, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
    }
}
