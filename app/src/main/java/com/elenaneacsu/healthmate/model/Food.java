package com.elenaneacsu.healthmate.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.elenaneacsu.healthmate.model.Nutrients;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Food implements Serializable, Parcelable {
    @SerializedName("foodId")
    @Expose
    private String foodId;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("nutrients")
    @Expose
    private Nutrients nutrients;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("categoryLabel")
    @Expose
    private String categoryLabel;

    public Food(){}

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Nutrients getNutrients() {
        return nutrients;
    }

    public void setNutrients(Nutrients nutrients) {
        this.nutrients = nutrients;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(String categoryLabel) {
        this.categoryLabel = categoryLabel;
    }

    protected Food(Parcel in) {
        foodId = in.readString();
        label = in.readString();
        nutrients = (Nutrients) in.readValue(Nutrients.class.getClassLoader());
        brand = in.readString();
        category = in.readString();
        categoryLabel = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(foodId);
        dest.writeString(label);
        dest.writeValue(nutrients);
        dest.writeString(brand);
        dest.writeString(category);
        dest.writeString(categoryLabel);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Food> CREATOR = new Parcelable.Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };
}