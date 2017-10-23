package com.example.mvopo.tsekapp.Model;

/**
 * Created by mvopo on 10/19/2017.
 */

public class FamilyProfile {
    public String profileId, philHealthId, nhtsId, fName, lName, mName, suffix, bday,
            sex, barangay, income, unmetNeed, waterSupply, sanitaryToilet, educationalAttainment;

    public Boolean isFamilyHead;

    public FamilyProfile(String profileId, String philHealthId, String nhtsId, String fName, String lName,
                         String mName, String suffix, String bday, String sex, String barangay, String income,
                         String unmetNeed, String waterSupply, String sanitaryToilet, String educationalAttainment,
                         Boolean isFamilyHead) {
        this.profileId = profileId;
        this.philHealthId = philHealthId;
        this.nhtsId = nhtsId;
        this.fName = fName;
        this.lName = lName;
        this.mName = mName;
        this.suffix = suffix;
        this.bday = bday;
        this.sex = sex;
        this.barangay = barangay;
        this.income = income;
        this.unmetNeed = unmetNeed;
        this.waterSupply = waterSupply;
        this.sanitaryToilet = sanitaryToilet;
        this.educationalAttainment = educationalAttainment;
        this.isFamilyHead = isFamilyHead;
    }
}
