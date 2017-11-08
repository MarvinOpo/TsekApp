package com.example.mvopo.tsekapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mvopo on 10/19/2017.
 */

public class FamilyProfile implements Parcelable {
    public String id, uniqueId, familyId, philId, nhtsId, isHead, relation, fname, lname, mname, suffix, dob,
            sex, barangayId, muncityId, provinceId, income, unmetNeed, waterSupply, sanitaryToilet, educationalAttainment,
            status;

    public FamilyProfile(String id, String uniqueId, String familyId, String philId, String nhtsId, String isHead,
                         String relation, String fname, String lname, String mname, String suffix, String dob, String sex,
                         String barangayId, String muncityId, String provinceId, String income, String unmetNeed, String waterSupply,
                         String sanitaryToilet, String educationalAttainment, String status) {
        this.id = id;
        this.uniqueId = uniqueId;
        this.familyId = familyId;
        this.philId = philId;
        this.nhtsId = nhtsId;
        this.isHead = isHead;
        this.relation = relation;
        this.fname = fname;
        this.lname = lname;
        this.mname = mname;
        this.suffix = suffix;
        this.dob = dob;
        this.sex = sex;
        this.barangayId = barangayId;
        this.muncityId = muncityId;
        this.provinceId = provinceId;
        this.income = income;
        this.unmetNeed = unmetNeed;
        this.waterSupply = waterSupply;
        this.sanitaryToilet = sanitaryToilet;
        this.educationalAttainment = educationalAttainment;
        this.status = status;
    }

    protected FamilyProfile(Parcel in) {
        id = in.readString();
        uniqueId = in.readString();
        familyId = in.readString();
        philId = in.readString();
        nhtsId = in.readString();
        isHead = in.readString();
        relation = in.readString();
        fname = in.readString();
        lname = in.readString();
        mname = in.readString();
        suffix = in.readString();
        dob = in.readString();
        sex = in.readString();
        barangayId = in.readString();
        muncityId = in.readString();
        provinceId = in.readString();
        income = in.readString();
        unmetNeed = in.readString();
        waterSupply = in.readString();
        sanitaryToilet = in.readString();
        educationalAttainment = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(uniqueId);
        dest.writeString(familyId);
        dest.writeString(philId);
        dest.writeString(nhtsId);
        dest.writeString(isHead);
        dest.writeString(relation);
        dest.writeString(fname);
        dest.writeString(lname);
        dest.writeString(mname);
        dest.writeString(suffix);
        dest.writeString(dob);
        dest.writeString(sex);
        dest.writeString(barangayId);
        dest.writeString(muncityId);
        dest.writeString(provinceId);
        dest.writeString(income);
        dest.writeString(unmetNeed);
        dest.writeString(waterSupply);
        dest.writeString(sanitaryToilet);
        dest.writeString(educationalAttainment);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FamilyProfile> CREATOR = new Creator<FamilyProfile>() {
        @Override
        public FamilyProfile createFromParcel(Parcel in) {
            return new FamilyProfile(in);
        }

        @Override
        public FamilyProfile[] newArray(int size) {
            return new FamilyProfile[size];
        }
    };
}
