package com.elenaneacsu.healthmate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("quantity")
    @Expose
    private Double quantity;
    @SerializedName("measureURI")
    @Expose
    private String measureURI;
    @SerializedName("foodId")
    @Expose
    private String foodId;

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasureURI() {
        return measureURI;
    }

    public void setMeasureURI(String measureURI) {
        this.measureURI = measureURI;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }
}
