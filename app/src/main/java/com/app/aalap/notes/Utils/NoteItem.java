package com.app.aalap.notes.Utils;

import java.util.Date;

/**
 * Created by Aalap on 2016-12-09.
 */

public class NoteItem {

    String title, time, details;
    int id;
    long timeMilis;



    public NoteItem(){}

    public long getTimeMilis() {
        return timeMilis;
    }

    public void setTimeMilis(long timeMilis) {
        this.timeMilis = timeMilis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        Date date = new Date();

    }
}
