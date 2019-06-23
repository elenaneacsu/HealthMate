package com.elenaneacsu.healthmate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalNutrients {

    @SerializedName("ENERC_KCAL")
    @Expose
    private ENERCKCAL enerckal;
    @SerializedName("FAT")
    @Expose
    private FAT fat;
    @SerializedName("CHOCDF")
    @Expose
    private CHOCDF chocdf;
    @SerializedName("SUGAR")
    @Expose
    private SUGAR sugar;
    @SerializedName("PROCNT")
    @Expose
    private PROCNT procnt;
    @SerializedName("NA")
    @Expose
    private NA na;

    public ENERCKCAL getEnerckal() {
        return enerckal;
    }

    public void setEnerckal(ENERCKCAL enerckal) {
        this.enerckal = enerckal;
    }

    public FAT getFat() {
        return fat;
    }

    public void setFat(FAT fat) {
        this.fat = fat;
    }

    public CHOCDF getChocdf() {
        return chocdf;
    }

    public void setChocdf(CHOCDF chocdf) {
        this.chocdf = chocdf;
    }

    public SUGAR getSugar() {
        return sugar;
    }

    public void setSugar(SUGAR sugar) {
        this.sugar = sugar;
    }

    public PROCNT getProcnt() {
        return procnt;
    }

    public void setProcnt(PROCNT procnt) {
        this.procnt = procnt;
    }

    public NA getNa() {
        return na;
    }

    public void setNa(NA na) {
        this.na = na;
    }
}