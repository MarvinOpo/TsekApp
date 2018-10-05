package com.example.mvopo.tsekapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by mvopo on 10/30/2017.
 */

public class User implements Parcelable{
    public String id, fname, mname, lname, muncity, contact, barangay, target, image;

    public User(String id, String fname, String mname, String lname, String muncity,
                String contact, String barangay, String target, String image) {
        this.id = id;
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.muncity = muncity;
        this.contact = contact;
        this.barangay = barangay;
        this.target = target;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    protected User(Parcel in) {
        id = in.readString();
        fname = in.readString();
        mname = in.readString();
        lname = in.readString();
        muncity = in.readString();
        contact = in.readString();
        barangay = in.readString();
        target = in.readString();
        image = in.readString();
    }

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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(fname);
        parcel.writeString(mname);
        parcel.writeString(lname);
        parcel.writeString(muncity);
        parcel.writeString(contact);
        parcel.writeString(barangay);
        parcel.writeString(target);
        parcel.writeString(image);
    }
}
