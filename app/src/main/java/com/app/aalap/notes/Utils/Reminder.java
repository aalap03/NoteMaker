package com.app.aalap.notes.Utils;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Aalap on 2017-05-15.
 */

public class Reminder extends RealmObject {

    String title;
    String details;
    Date endTime;
    Date reminderTime;

    @PrimaryKey
    long id;

    public Reminder(){}

    public Reminder(String title, String details, Date endTime, Date reminderTime, long id) {
        this.title = title;
        this.details = details;
        this.endTime = endTime;
        this.reminderTime = reminderTime;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(Date reminderTime) {
        this.reminderTime = reminderTime;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
