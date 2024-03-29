package com.elenaneacsu.healthmate.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String name;
    private String email;
    private String password;
    private String goal;
    private String activityLevel;
    private String gender;
    private int age;
    private float currentWeight;
    private float desiredWeight;
    private int height;
    private String photo;

    public User(String name, String goal, String activityLevel, String gender, int age, float currentWeight, float desiredWeight, int height) {
        this.name = name;
        this.goal = goal;
        this.activityLevel = activityLevel;
        this.gender = gender;
        this.age = age;
        this.currentWeight = currentWeight;
        this.desiredWeight = desiredWeight;
        this.height = height;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoal() {
        return goal;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public float getCurrentWeight() {
        return currentWeight;
    }

    public float getDesiredWeight() {
        return desiredWeight;
    }

    public int getHeight() {
        return height;
    }

    public User() {
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCurrentWeight(float currentWeight) {
        this.currentWeight = currentWeight;
    }

    public void setDesiredWeight(float desiredWeight) {
        this.desiredWeight = desiredWeight;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    protected User(Parcel in) {
        email = in.readString();
        password = in.readString();
        goal = in.readString();
        activityLevel = in.readString();
        gender = in.readString();
        age = in.readInt();
        currentWeight = in.readFloat();
        desiredWeight = in.readFloat();
        height = in.readInt();
        photo = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(goal);
        dest.writeString(activityLevel);
        dest.writeString(gender);
        dest.writeLong(age);
        dest.writeFloat(currentWeight);
        dest.writeFloat(desiredWeight);
        dest.writeInt(height);
        dest.writeString(photo);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", goal='" + goal + '\'' +
                ", activityLevel='" + activityLevel + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", currentWeight=" + currentWeight +
                ", desiredWeight=" + desiredWeight +
                ", height=" + height +
                '}';
    }
}
