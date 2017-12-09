package com.app.aalap.notes.MyService;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.app.aalap.notes.Activities.NewNoteActivity;
import com.app.aalap.notes.Activities.NewReminderActivity;
import com.app.aalap.notes.R;
import com.app.aalap.notes.Utils.Reminder;

import java.util.Date;

import br.com.goncalves.pugnotification.notification.PugNotification;
import io.realm.Realm;
import io.realm.RealmResults;

public class ReminderService extends IntentService {

    private static final String TAG = "ReminderService";

    public ReminderService() {
        super("ReminderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "alarmTest: "+intent);

        Log.d(TAG, "onHandleIntent: "+intent.getLongExtra("reminderTime", 0));
        RealmResults<Reminder> id = Realm.getDefaultInstance()
                .where(Reminder.class)
                .equalTo("reminderTime", new Date(intent.getLongExtra("reminderTime", 0)))
                .findAll();

        Log.d(TAG, "onHandleIntent: "+id.size());
        for (Reminder reminder : id) {

            Bundle bundle = new Bundle();
            bundle.putLong("id", reminder.getId());

            PugNotification.with(getApplicationContext())
                    .load()
                    .autoCancel(true)
                    .title(reminder.getTitle())
                    .message(reminder.getDetails())
                    .smallIcon(R.mipmap.ic_launcher)
                    .largeIcon(R.mipmap.ic_launcher)
                    .flags(Notification.DEFAULT_ALL)
                    .click(NewReminderActivity.class, bundle)
                    .simple()
                    .build();
        }


    }
}
