package com.example.mvopo.tsekapp.Model;

import android.util.Log;

import com.example.mvopo.tsekapp.Helper.DBHelper;
import com.example.mvopo.tsekapp.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by mvopo on 10/30/2017.
 */

public class Constants {

    public static String url = "http://203.177.67.124/tsekap/vii/api?";
    public static String apkUrl = "http://203.177.67.124/tsekap/vii/resources/apk/PHA%20Check-App.apk";

//    public static String url = "http://203.177.67.124/tsekap/dummy/api?";
//    public static String apkUrl = "http://203.177.67.124/tsekap/dummy/resources/apk/PHA%20Check-App.apk";

    public static JSONObject getProfileJson() {

        FamilyProfile profile = MainActivity.db.getProfileForSync();

        JSONObject request = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            data.accumulate("unique_id", profile.uniqueId);
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
            data.accumulate("user_id", MainActivity.user.id);

            String toilet = profile.sanitaryToilet;

            if(!toilet.isEmpty()) {
                if(toilet.equals("1")) toilet = "non";
                else if(toilet.equals("2")) toilet = "comm";
                else if(toilet.equals("3")) toilet = "indi";
            }

            data.accumulate("toilet",toilet);
            data.accumulate("education", profile.educationalAttainment);

            request.accumulate("data", data);
            //request.accumulate("_token", MainActivity.user.token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return request;
    }

    public static String getAge(String date) {
        int year, month, day;
        String ageString = "";

        try {
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            Calendar dob = Calendar.getInstance();
            dob.setTime(myFormat.parse(date));

            year = c.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);

            if (month >= 0) {
                ageString = month + " m/o";

                if(day > 0)ageString = day + " d/o";
                else if(day < 0){
                    if(year > 0){
                        year--;
                        month += 11;
                    }
                    else month--;
                }

            } else if(month < 0) year--;


            if(year > 0) ageString = year + "";
            else if(month > 0) ageString = month + " m/o";
            else{
                if(day > 0){
                    ageString = day + " d/o";
                }else{
                    if(day < 0) month--;

                    if(month <= 0) {
                        String now = c.get(Calendar.YEAR) + "-" + String.format("%02d", (c.get(Calendar.MONTH) + 1)) +
                                "-" + String.format("%02d", (c.get(Calendar.DAY_OF_MONTH)));

                        Date date1 = myFormat.parse(date);
                        Date date2 = myFormat.parse(now);
                        long diff = date2.getTime() - date1.getTime();

                        ageString = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " d/o";
                    }
                }
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ageString;
    }

    public static String getBrgyName(String id) {
        String name = "";
        try {
            JSONArray arrayBrgy = new JSONArray(MainActivity.user.barangay);
            for (int i = 0; i < arrayBrgy.length(); i++) {
                JSONObject assignedBrgy = arrayBrgy.getJSONObject(i);
                String barangayId = assignedBrgy.getString("barangay_id");
                if (id.equalsIgnoreCase(barangayId)) {
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
