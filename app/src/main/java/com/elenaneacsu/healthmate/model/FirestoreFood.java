package com.elenaneacsu.healthmate.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FirestoreFood implements Parcelable {
    private String label;
    private String description;
    private double calories;
    private double fat;
    private double carbs;
    private double protein;

    public FirestoreFood(){}

    public FirestoreFood(String label, String description, double calories, double fat, double carbs, double protein) {
        this.label = label;
        this.description = description;
        this.calories = calories;
        this.fat = fat;
        this.carbs = carbs;
        this.protein = protein;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    protected FirestoreFood(Parcel in) {
        label = in.readString();
        description = in.readString();
        calories = in.readDouble();
        fat = in.readDouble();
        carbs = in.readDouble();
        protein = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(description);
        dest.writeDouble(calories);
        dest.writeDouble(fat);
        dest.writeDouble(carbs);
        dest.writeDouble(protein);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FirestoreFood> CREATOR = new Parcelable.Creator<FirestoreFood>() {
        @Override
        public FirestoreFood createFromParcel(Parcel in) {
            return new FirestoreFood(in);
        }

        @Override
        public FirestoreFood[] newArray(int size) {
            return new FirestoreFood[size];
        }
    };
}
