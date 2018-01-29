package com.example.mvopo.tsekapp.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.mvopo.tsekapp.MainActivity;
import com.example.mvopo.tsekapp.Model.FamilyProfile;
import com.example.mvopo.tsekapp.Model.FeedBack;
import com.example.mvopo.tsekapp.Model.ServicesStatus;
import com.example.mvopo.tsekapp.Model.ServiceAvailed;
import com.example.mvopo.tsekapp.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    Context context;
    final static String DBNAME = "db_tsekap";
    final static String USERS = "tbl_user";
    final static String SERVICES = "tbl_services";
    final static String SERVICESTATUS = "tbl_must_services";
    final static String PROFILES = "tbl_profile";
    final static String FEEDBACK = "tbl_feedback";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 4);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "Create table " + USERS + " (id integer, fname varchar(50), mname varchar(50), lname varchar(50)," +
                " muncity varchar(50), contact varchar(50), barangay varchar(255), target varchar(100))";

        String sql1 = "Create table " + PROFILES + " (id integer, uniqueId varchar(100), familyId varchar(50), philId varchar(50), " +
                "nhtsId varchar(50), isHead varchar(50), relation varchar(50), fname varchar(50), mname varchar(50), lname varchar(50), " +
                "suffix varchar(50), dob varchar(50), sex varchar(50), barangayId varchar(50), muncityId varchar(50),  provinceId varchar(50), " +
                "income varchar(50), unmetNeed varchar(50), waterSupply varchar(50), sanitaryToilet varchar(50), educationalAttainment varchar(50)," +
                "status varchar(3))";

        String sql2 = "Create table " + SERVICES + " (id integer primary key autoincrement, request TEXT)";

        String sql3 = "Create table " + SERVICESTATUS + " (id integer primary key autoincrement, name varchar(100), group1 varchar(2), group2 varchar(2)," +
                " group3 varchar(2), barangayId varchar(10))";

        String sql4 = "Create table " + FEEDBACK + " (id integer primary key autoincrement, subject varchar(25), body varchar(255))";

        db.execSQL(sql);
        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "Create table IF NOT EXISTS " + SERVICESTATUS + " (id integer primary key autoincrement, name varchar(100), group1 varchar(2), group2 varchar(2)," +
                " group3 varchar(2), barangayId varchar(10))";

        String sql1 = "Create table IF NOT EXISTS " + FEEDBACK + " (id integer primary key autoincrement, subject varchar(25), body varchar(255))";

        db.execSQL(sql);
        db.execSQL(sql1);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", user.id);
        cv.put("fname", user.fname);
        cv.put("mname", user.mname);
        cv.put("lname", user.lname);
        cv.put("muncity", user.muncity);
        cv.put("contact", user.contact);
        cv.put("barangay", user.barangay);
        cv.put("target", user.target);
        db.insert(USERS, null, cv);
        db.close();
    }

    public void deleteUser() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(USERS, null, null);
    }

    public User getUser() {
        User user = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "Select * from " + USERS;
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            String id = c.getString(c.getColumnIndex("id"));
            String fname = c.getString(c.getColumnIndex("fname"));
            String mname = c.getString(c.getColumnIndex("mname"));
            String lname = c.getString(c.getColumnIndex("lname"));
            String muncity = c.getString(c.getColumnIndex("muncity"));
            String contact = c.getString(c.getColumnIndex("contact"));
            String barangay = c.getString(c.getColumnIndex("barangay"));
            String target = c.getString(c.getColumnIndex("target"));

            user = new User(id, fname, mname, lname, muncity, contact, barangay, target);
        }
        c.close();
        return user;
    }

    public void addProfile(FamilyProfile familyProfile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", familyProfile.id);
        cv.put("uniqueId", familyProfile.uniqueId);
        cv.put("familyId", familyProfile.familyId);
        cv.put("philId", familyProfile.philId);
        cv.put("nhtsId", familyProfile.nhtsId);
        cv.put("isHead", familyProfile.isHead);
        cv.put("relation", familyProfile.relation);
        cv.put("fname", familyProfile.fname);
        cv.put("mname", familyProfile.mname);
        cv.put("lname", familyProfile.lname);
        cv.put("suffix", familyProfile.suffix);
        cv.put("dob", familyProfile.dob);
        cv.put("sex", familyProfile.sex);
        cv.put("barangayId", familyProfile.barangayId);
        cv.put("muncityId", familyProfile.muncityId);
        cv.put("provinceId", familyProfile.provinceId);
        cv.put("income", familyProfile.income);
        cv.put("unmetNeed", familyProfile.unmetNeed);
        cv.put("waterSupply", familyProfile.waterSupply);
        cv.put("sanitaryToilet", familyProfile.sanitaryToilet);
        cv.put("educationalAttainment", familyProfile.educationalAttainment);
        cv.put("status", familyProfile.status);
        db.insertWithOnConflict(PROFILES, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }

    public void updateProfile(FamilyProfile familyProfile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", familyProfile.id);
        cv.put("uniqueId", familyProfile.uniqueId);
        cv.put("familyId", familyProfile.familyId);
        cv.put("philId", familyProfile.philId);
        cv.put("nhtsId", familyProfile.nhtsId);
        cv.put("isHead", familyProfile.isHead);
        cv.put("relation", familyProfile.relation);
        cv.put("fname", familyProfile.fname);
        cv.put("mname", familyProfile.mname);
        cv.put("lname", familyProfile.lname);
        cv.put("suffix", familyProfile.suffix);
        cv.put("dob", familyProfile.dob);
        cv.put("sex", familyProfile.sex);
        cv.put("barangayId", familyProfile.barangayId);
        cv.put("muncityId", familyProfile.muncityId);
        cv.put("provinceId", familyProfile.provinceId);
        cv.put("income", familyProfile.income);
        cv.put("unmetNeed", familyProfile.unmetNeed);
        cv.put("waterSupply", familyProfile.waterSupply);
        cv.put("sanitaryToilet", familyProfile.sanitaryToilet);
        cv.put("educationalAttainment", familyProfile.educationalAttainment);
        cv.put("status", familyProfile.status);
        db.update(PROFILES, cv, "uniqueId=?", new String[]{familyProfile.uniqueId});
        db.close();
    }

    public ArrayList<FamilyProfile> getFamilyProfiles(String name) {
        name += "%";
        ArrayList<FamilyProfile> profiles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(PROFILES, null, "fname LIKE ? or mname LIKE ? or lname LIKE ? or familyId LIKE ?", new String[]{name, name, name, name}, null, null, null, "10");

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                int id = c.getInt(c.getColumnIndex("id"));
                String uniqueId = c.getString(c.getColumnIndex("uniqueId"));
                String familyId = c.getString(c.getColumnIndex("familyId"));
                String philId = c.getString(c.getColumnIndex("philId"));
                String nhtsId = c.getString(c.getColumnIndex("nhtsId"));
                String isHead = c.getString(c.getColumnIndex("isHead"));
                String relation = c.getString(c.getColumnIndex("relation"));
                String fname = c.getString(c.getColumnIndex("fname"));
                String mname = c.getString(c.getColumnIndex("mname"));
                String lname = c.getString(c.getColumnIndex("lname"));
                String suffix = c.getString(c.getColumnIndex("suffix"));
                String dob = c.getString(c.getColumnIndex("dob"));
                String sex = c.getString(c.getColumnIndex("sex"));
                String barangayId = c.getString(c.getColumnIndex("barangayId"));
                String muncityId = c.getString(c.getColumnIndex("muncityId"));
                String provinceId = c.getString(c.getColumnIndex("provinceId"));
                String income = c.getString(c.getColumnIndex("income"));
                String unmetNeed = c.getString(c.getColumnIndex("unmetNeed"));
                String waterSupply = c.getString(c.getColumnIndex("waterSupply"));
                String sanitaryToilet = c.getString(c.getColumnIndex("sanitaryToilet"));
                String educationalAttainment = c.getString(c.getColumnIndex("educationalAttainment"));
                String status = c.getString(c.getColumnIndex("status"));

                FamilyProfile profile = new FamilyProfile(id + "", uniqueId, familyId, philId, nhtsId, isHead, relation, fname,
                        lname, mname, suffix, dob, sex, barangayId, muncityId, provinceId, income, unmetNeed, waterSupply,
                        sanitaryToilet, educationalAttainment, status);

                if(familyId.equals(name.substring(0, name.length()-1)) && relation.equalsIgnoreCase("Head")) profiles.add(0, profile);
                else profiles.add(profile);

                c.moveToNext();
            }
            c.close();
        }
        db.close();
        return profiles;
    }

    public FamilyProfile getProfileForSync() {
        FamilyProfile familyProfile = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(PROFILES, null, "status = 1", null, null, null, null, "1");

        if (c.moveToFirst()) {
            int id = c.getInt(c.getColumnIndex("id"));
            String uniqueId = c.getString(c.getColumnIndex("uniqueId"));
            String familyId = c.getString(c.getColumnIndex("familyId"));
            String philId = c.getString(c.getColumnIndex("philId"));
            String nhtsId = c.getString(c.getColumnIndex("nhtsId"));
            String isHead = c.getString(c.getColumnIndex("isHead"));
            String relation = c.getString(c.getColumnIndex("relation"));
            String fname = c.getString(c.getColumnIndex("fname"));
            String mname = c.getString(c.getColumnIndex("mname"));
            String lname = c.getString(c.getColumnIndex("lname"));
            String suffix = c.getString(c.getColumnIndex("suffix"));
            String dob = c.getString(c.getColumnIndex("dob"));
            String sex = c.getString(c.getColumnIndex("sex"));
            String barangayId = c.getString(c.getColumnIndex("barangayId"));
            String muncityId = c.getString(c.getColumnIndex("muncityId"));
            String provinceId = c.getString(c.getColumnIndex("provinceId"));
            String income = c.getString(c.getColumnIndex("income"));
            String unmetNeed = c.getString(c.getColumnIndex("unmetNeed"));
            String waterSupply = c.getString(c.getColumnIndex("waterSupply"));
            String sanitaryToilet = c.getString(c.getColumnIndex("sanitaryToilet"));
            String educationalAttainment = c.getString(c.getColumnIndex("educationalAttainment"));
            String status = c.getString(c.getColumnIndex("status"));

            familyProfile = new FamilyProfile(id + "", uniqueId, familyId, philId, nhtsId, isHead, relation, fname,
                    lname, mname, suffix, dob, sex, barangayId, muncityId, provinceId, income, unmetNeed, waterSupply,
                    sanitaryToilet, educationalAttainment, status);
        }
        c.close();
        db.close();
        return familyProfile;
    }

    public void updateProfileById(String uniqueId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("status", 0);

        db.update(PROFILES, cv, "uniqueId = ?", new String[]{uniqueId});
    }

    public void deleteProfiles() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(PROFILES, null, null);
    }

    public int getProfilesCount(String brgyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + PROFILES;

        if(!brgyId.equals("")) countQuery += " where barangayId = '"+ brgyId +"'";

        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();

        cursor.close();
        db.close();
        return count;
    }

    public int getUploadableCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + PROFILES + " where status = '1'";

        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();

        cursor.close();
        db.close();
        return count;
    }

    //    public void addAccoount(Accounts acc) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put("category", acc.category);
//        cv.put("site_name", acc.site_name);
//        cv.put("user_name", acc.user_name);
//        cv.put("password", acc.password);
//        db.insert(ACCOUNTS, null, cv);
//        db.close();
//    }
//
    public void addServicesAvail(String request) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("request", request);
        db.insert(SERVICES, null, cv);
        db.close();

        Toast.makeText(context, "Succesfully availed", Toast.LENGTH_SHORT).show();
    }

    public int getServicesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + SERVICES;

        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();

        cursor.close();
        db.close();
        return count;
    }

    public ServiceAvailed getServiceForUpload() {
        ServiceAvailed serviceAvailed = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(SERVICES, null, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            JSONObject request=null;
            String id = c.getString(c.getColumnIndex("id"));
            try {
                request = new JSONObject(c.getString(c.getColumnIndex("request")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            serviceAvailed = new ServiceAvailed(id, request);
            Log.e("DBHelper", request.toString());
        }
        c.close();
        db.close();
        return serviceAvailed;
    }

    public void deleteService(String id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SERVICES, "id=?", new String[]{id});
    }

    public void addServiceStatus(ServicesStatus ss) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", ss.name);
        cv.put("group1", ss.group1);
        cv.put("group2", ss.group2);
        cv.put("group3", ss.group3);
        cv.put("barangayId", ss.brgyId);
        db.insert(SERVICESTATUS, null, cv);
        db.close();
    }

    public int getServiceStatusCount(String brgyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + SERVICESTATUS;

        if(!brgyId.equals("")) countQuery += " where barangayId = '"+ brgyId +"'";

        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();

        cursor.close();
        db.close();
        return count;
    }

    public void deleteServiceStatus() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SERVICESTATUS, null, null);
    }

    public ArrayList<ServicesStatus> getServicesStatus(String filter) {
        ArrayList<ServicesStatus> servicesStatuses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(SERVICESTATUS, null, filter, null, null, null, null, "20");

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String name = c.getString(c.getColumnIndex("name"));
                String group1 = c.getString(c.getColumnIndex("group1"));
                String group2 = c.getString(c.getColumnIndex("group2"));
                String group3 = c.getString(c.getColumnIndex("group3"));
                String brgyId = c.getString(c.getColumnIndex("barangayId"));

                ServicesStatus servicesStatus = new ServicesStatus(name, group1, group2, group3, brgyId);

                servicesStatuses.add(servicesStatus);
                c.moveToNext();
            }
            c.close();
        }
        db.close();
        return servicesStatuses;
    }

    public void addFeedback(FeedBack fb) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("subject", fb.subject);
        cv.put("body", fb.body);
        db.insert(FEEDBACK, null, cv);
        db.close();
    }

    public void deleteFeedback(String id) {
        SQLiteDatabase db = getWritableDatabase();
        if(id.isEmpty()) db.delete(FEEDBACK, null, null);
        else db.delete(FEEDBACK, "id=?", new String[]{id});
    }

    public String getFeedbacksForUpload() {
        String feedback = "";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(FEEDBACK, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String subject = c.getString(c.getColumnIndex("subject"));
                String body = c.getString(c.getColumnIndex("body"));

                feedback +=  (c.getPosition() + 1) + ". " + subject + " - " + body + "\n\n";
                c.moveToNext();
            }
            c.close();
        }
        db.close();
        return feedback;
    }

    public ArrayList<FeedBack> getFeedbacks() {
        ArrayList<FeedBack> feedBacks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(FEEDBACK, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String id = c.getString(c.getColumnIndex("id"));
                String subject = c.getString(c.getColumnIndex("subject"));
                String body = c.getString(c.getColumnIndex("body"));

                feedBacks.add(new FeedBack(id, subject, body));
                c.moveToNext();
            }
            c.close();
        }
        db.close();
        return feedBacks;
    }

    public int getFeedbacksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + FEEDBACK;

        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();

        cursor.close();
        db.close();
        return count;
    }
}