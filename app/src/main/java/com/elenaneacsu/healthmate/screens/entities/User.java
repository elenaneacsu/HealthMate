package com.elenaneacsu.healthmate.screens.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class User implements Parcelable {
    private String name;
    private String email;
    private String password;
    private String goal;
    private String activityLevel;
    private String gender;
    private Date birthdate;
    private float currentWeight;
    private float desiredWeight;
    private int height;

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

    public Date getBirthdate() {
        return birthdate;
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

    public User(String email, String password, String goal, String activityLevel, String gender, Date birthdate, float currentWeight, float desiredWeight, int height) {
        this.email = email;
        this.password = password;
        this.goal = goal;
        this.activityLevel = activityLevel;
        this.gender = gender;
        this.birthdate = birthdate;
        this.currentWeight = currentWeight;
        this.desiredWeight = desiredWeight;
        this.height = height;
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

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
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
        long tmpBirthdate = in.readLong();
        birthdate = tmpBirthdate != -1 ? new Date(tmpBirthdate) : null;
        currentWeight = in.readFloat();
        desiredWeight = in.readFloat();
        height = in.readInt();
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
        dest.writeLong(birthdate != null ? birthdate.getTime() : -1L);
        dest.writeFloat(currentWeight);
        dest.writeFloat(desiredWeight);
        dest.writeInt(height);
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
                ", birthdate=" + birthdate +
                ", currentWeight=" + currentWeight +
                ", desiredWeight=" + desiredWeight +
                ", height=" + height +
                '}';
    }
}
