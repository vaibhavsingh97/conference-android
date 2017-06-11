package com.systers.conference.model;

import android.os.Parcel;
import android.os.Parcelable;


public class User implements Parcelable {
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            User mUser = new User();
            mUser.firstName = in.readString();
            mUser.lastName = in.readString();
            mUser.email = in.readString();
            mUser.companyName = in.readString();
            mUser.role = in.readString();
            return mUser;
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    private String firstName;
    private String lastName;
    private String email;
    private String companyName;
    private String role;

    public User() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(companyName);
        dest.writeString(role);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
