package com.elenaneacsu.healthmate.model;

import java.util.Date;

public class WaterRecord {
    private long quantity;
    private Date date;

    public WaterRecord(long quantity, Date date) {
        this.quantity = quantity;
        this.date = date;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
