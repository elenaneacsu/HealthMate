package com.elenaneacsu.healthmate.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Nutrients implements Serializable, Parcelable {
    @SerializedName("ENERC_KCAL")
    @Expose
    private double ENERC_KCAL;
    @SerializedName("PROCNT")
    @Expose
    private double PROCNT;
    @SerializedName("FAT")
    @Expose
    private double FAT;
    @SerializedName("CHOCDF")
    @Expose
    private double CHOCDF;

    public double getENER_KCAL() {
        return ENERC_KCAL;
    }

    public void setENER_KCAL(double ENER_KCAL) {
        this.ENERC_KCAL = ENER_KCAL;
    }

    public double getPROCNT() {
        return PROCNT;
    }

    public void setPROCNT(double PROCNT) {
        this.PROCNT = PROCNT;
    }

    public double getFAT() {
        return FAT;
    }

    public void setFAT(double FAT) {
        this.FAT = FAT;
    }

    public double getCHOCDF() {
        return CHOCDF;
    }

    public void setCHOCDF(double CHOCDF) {
        this.CHOCDF = CHOCDF;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        return  df.format(ENERC_KCAL) + " kcal" + " | " +
                df.format(PROCNT) + " protein" + " | " +
                df.format(FAT) + " fat" + " | " +
                df.format(CHOCDF) + " carbs";
    }

    protected Nutrients(Parcel in) {
        ENERC_KCAL = in.readDouble();
        PROCNT = in.readDouble();
        FAT = in.readDouble();
        CHOCDF = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(ENERC_KCAL);
        dest.writeDouble(PROCNT);
        dest.writeDouble(FAT);
        dest.writeDouble(CHOCDF);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Nutrients> CREATOR = new Parcelable.Creator<Nutrients>() {
        @Override
        public Nutrients createFromParcel(Parcel in) {
            return new Nutrients(in);
        }

        @Override
        public Nutrients[] newArray(int size) {
            return new Nutrients[size];
        }
    };
}
