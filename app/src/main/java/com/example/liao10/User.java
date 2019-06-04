package com.example.liao10;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    public static final String KEY_USERNAME = "username";

    private String username;

    public User(String username) {
        this.username = username;
    }

    protected User(Parcel in) {
        username = in.readString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    // Implement parcelable
    public static final Creator<User> CREATOR = new Creator<User>() {
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
    }
}
