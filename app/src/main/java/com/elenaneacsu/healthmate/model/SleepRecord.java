package com.elenaneacsu.healthmate.model;

import java.util.Date;

public class SleepRecord {
    private long hours;
    private Date date;

    public SleepRecord(long hours, Date date) {
        this.hours = hours;
        this.date = date;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
