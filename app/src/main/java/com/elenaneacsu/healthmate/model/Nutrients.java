package com.elenaneacsu.healthmate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Nutrients implements Serializable {
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
}
