package com.elenaneacsu.healthmate.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class WeightRecord implements Parcelable {
    private float weight;
    private Date date;

    public WeightRecord(){}

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    protected WeightRecord(Parcel in) {
        weight = in.readFloat();
        long tmpDate = in.readLong();
        date = tmpDate != -1 ? new Date(tmpDate) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(weight);
        dest.writeLong(date != null ? date.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WeightRecord> CREATOR = new Parcelable.Creator<WeightRecord>() {
        @Override
        public WeightRecord createFromParcel(Parcel in) {
            return new WeightRecord(in);
        }

        @Override
        public WeightRecord[] newArray(int size) {
            return new WeightRecord[size];
        }
    };
}
