package com.app.aalap.notes.MyService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Aalap on 2017-08-08.
 */

public class Receiver extends BroadcastReceiver {
    private static final String TAG = "Receiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "alarmTest: "+intent);
        Intent intent1 = new Intent (context, ReminderService.class);
        intent1.putExtra("reminderTime", intent.getLongExtra("reminderTime", 0));
        context.startService(intent1);
    }
}
