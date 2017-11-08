package com.example.mvopo.tsekapp.Model;

import com.example.mvopo.tsekapp.Helper.DBHelper;
import com.example.mvopo.tsekapp.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mvopo on 10/30/2017.
 */

public class Constants {

    public static String url = "http://210.4.59.4/tsekap/dummy/api?";

    public static JSONObject getProfileJson() {

        FamilyProfile profile = MainActivity.db.getProfileForSync();

        JSONObject request = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            data.accumulate("uniqueId", profile.uniqueId);
            data.accumulate("familyID", profile.familyId);
            data.accumulate("phicID", profile.philId);
            data.accumulate("nhtsID", profile.nhtsId);
            data.accumulate("head", profile.isHead);
            data.accumulate("relation", profile.relation);
            data.accumulate("fname", profile.fname);
            data.accumulate("mname", profile.mname);
            data.accumulate("lname", profile.lname);
            data.accumulate("suffix", profile.suffix);
            data.accumulate("sex", profile.sex);
            data.accumulate("dob", profile.dob);
            data.accumulate("barangay_id", profile.barangayId);
            data.accumulate("muncity_id", profile.muncityId);
            data.accumulate("province_id", profile.provinceId);
            data.accumulate("income", profile.income);
            data.accumulate("unmet", profile.unmetNeed);
            data.accumulate("water", profile.waterSupply);
            data.accumulate("toilet", profile.sanitaryToilet);
            data.accumulate("education", profile.educationalAttainment);

            request.accumulate("data", data);
            //request.accumulate("_token", MainActivity.user.token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return request;
    }

    public static int getAge(String date) {

        int age = 0;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = dateFormat.parse(date);
            Calendar now = Calendar.getInstance();
            Calendar dob = Calendar.getInstance();
            dob.setTime(date1);
            if (dob.after(now)) {
                throw new IllegalArgumentException("Can't be born in the future");
            }
            int year1 = now.get(Calendar.YEAR);
            int year2 = dob.get(Calendar.YEAR);
            age = year1 - year2;
            int month1 = now.get(Calendar.MONTH);
            int month2 = dob.get(Calendar.MONTH);
            if (month2 > month1) {
                age--;
            } else if (month1 == month2) {
                int day1 = now.get(Calendar.DAY_OF_MONTH);
                int day2 = dob.get(Calendar.DAY_OF_MONTH);
                if (day2 > day1) {
                    age--;
                }
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }

        return age ;
    }

    public static String getBrgyName(String id){
        String name = "";
        try{
            JSONArray arrayBrgy = new JSONArray(MainActivity.user.barangay);
            for(int i = 0; i < arrayBrgy.length(); i++) {
                JSONObject assignedBrgy = arrayBrgy.getJSONObject(i);
                String barangayId = assignedBrgy.getString("barangay_id");
                if(id.equalsIgnoreCase(barangayId)) {
                    name = assignedBrgy.getString("description");
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }
}
