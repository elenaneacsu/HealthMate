package com.elenaneacsu.healthmate.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FirestoreFood implements Parcelable {
    private String label;
    private String description;
    private long calories;
    private long fat;
    private long carbs;
    private long protein;

    public FirestoreFood(){}

    public FirestoreFood(String label, String description, long calories, long fat, long carbs, long protein) {
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

    public long getCalories() {
        return calories;
    }

    public void setCalories(long calories) {
        this.calories = calories;
    }

    public long getFat() {
        return fat;
    }

    public void setFat(long fat) {
        this.fat = fat;
    }

    public long getCarbs() {
        return carbs;
    }

    public void setCarbs(long carbs) {
        this.carbs = carbs;
    }

    public long getProtein() {
        return protein;
    }

    public void setProtein(long protein) {
        this.protein = protein;
    }

    protected FirestoreFood(Parcel in) {
        label = in.readString();
        description = in.readString();
        calories = in.readLong();
        fat = in.readLong();
        carbs = in.readLong();
        protein = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(description);
        dest.writeLong(calories);
        dest.writeLong(fat);
        dest.writeLong(carbs);
        dest.writeLong(protein);
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
