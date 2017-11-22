package com.example.mvopo.tsekapp.Helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.LruCache;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.mvopo.tsekapp.Fragments.HomeFragment;
import com.example.mvopo.tsekapp.LoginActivity;
import com.example.mvopo.tsekapp.MainActivity;
import com.example.mvopo.tsekapp.Model.Constants;
import com.example.mvopo.tsekapp.Model.FamilyProfile;
import com.example.mvopo.tsekapp.Model.ServiceAvailed;
import com.example.mvopo.tsekapp.Model.User;
import com.example.mvopo.tsekapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mvopo on 10/30/2017.
 */

public class JSONApi {

    private static JSONApi parser;
    static Context context;
    static DBHelper db;

    RequestQueue mRequestQueue;
    private ImageLoader imageLoader;
    String TAG = "JSONApi";

    public JSONApi(Context context) {
        this.context = context;
        this.mRequestQueue = getRequestQueue();

        imageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized JSONApi getInstance(Context context) {
        JSONApi.context = context;
        if (parser == null) {
            parser = new JSONApi(context);
            db = new DBHelper(context);
        }
        return parser;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(context.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public void login(final String url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");

                            if(status.equalsIgnoreCase("success")){
                                JSONObject data = response.getJSONObject("data");

                                String id = data.getString("id");
                                String fname = data.getString("fname");
                                String mname = data.getString("mname");
                                String lname = data.getString("lname");
                                String muncity = data.getString("muncity");
                                String contact = data.getString("contact");
                                String userBrgy = response.getJSONArray("userBrgy").toString();
                                String target = response.getString("target");

                                User user = new User(id, fname, mname, lname, muncity, contact, userBrgy, target);

                                db.addUser(user);
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.putExtra("user", user);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                            }else{
                                Toast.makeText(context, "Invalid credentials.", Toast.LENGTH_SHORT).show();
                            }

                            LoginActivity.pd.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("LOGIN", error.getMessage());
                LoginActivity.pd.dismiss();
                Log.e("url", url);
                Toast.makeText(context, "Login failed! Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(jsonObjectRequest);
    }

    public void getCount(String url, final String brgyId, final int brgyCount){
        Log.e(TAG, url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            final int currentCount = db.getProfilesCount(brgyId);
                            final int totalCount = Integer.parseInt(response.getString("count"));

                            if(currentCount >= totalCount){
                                JSONArray arrayBrgy = new JSONArray(MainActivity.user.barangay);
                                if(Integer.parseInt(arrayBrgy.length()+"") > Integer.parseInt((brgyCount + 1)+"")) {
                                    JSONObject assignedBrgy = arrayBrgy.getJSONObject(brgyCount+1);
                                    String barangayId = assignedBrgy.getString("barangay_id");
                                    MainActivity.hf.brgyName = assignedBrgy.getString("description");
                                    String url = Constants.url + "r=countProfile" + "&brgy=" + barangayId;
                                    getCount(url, barangayId, brgyCount + 1);
                                }else{
                                    Toast.makeText(context, "Nothing to download", Toast.LENGTH_SHORT).show();
                                    MainActivity.pd.dismiss();
                                }
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage((totalCount - currentCount) + " Profiles downloadable for "+ MainActivity.hf.brgyName +", tap PROCEED to start download.");
                                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        MainActivity.pd.setTitle("Downloading " + currentCount + "/" + totalCount);
                                        String url = Constants.url + "r=profile" + "&brgy=" + brgyId + "&offset=" + currentCount;
                                        getProfile(url, totalCount, currentCount, brgyCount, brgyId);
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        MainActivity.pd.dismiss();
                                    }
                                });
                                builder.show();

                            }
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("GETCOUNT", error.getMessage());
                Log.e(TAG, error.toString());
                MainActivity.pd.dismiss();
                Toast.makeText(context, "Unable to connect to server.", Toast.LENGTH_SHORT).show();
            }
        });

        mRequestQueue.add(jsonObjectRequest);
    }

    public void uploadProfile(final String url, final JSONObject request, final int totalCount, final int currentCount){
        Log.e(TAG, url);
        Log.e(TAG, request.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String uniqueId = request.getJSONObject("data").getString("unique_id");
                            Log.e(TAG, uniqueId);
                            db.updateProfileById(uniqueId);
                            int count = db.getUploadableCount();
                            int serviceCount = db.getServicesCount();

                            if (count > 0) {
                                MainActivity.pd.setTitle("Uploading " + currentCount + "/" + (totalCount + serviceCount));
                                uploadProfile(url, Constants.getProfileJson(), totalCount, currentCount + 1);
                            } else {
                                //doSecretJob();
                                if(serviceCount > 0){
                                    //upload services here
                                    ServiceAvailed serviceAvailed = db.getServiceForUpload();
                                    uploadServices(Constants.url.replace("?", "/syncservices"), serviceAvailed, currentCount, totalCount + serviceCount);
                                }else{
                                    Toast.makeText(context, "Upload completed", Toast.LENGTH_SHORT).show();
                                    MainActivity.pd.dismiss();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("GETCOUNT", error.getMessage());
                Log.e(TAG, error.toString());
                MainActivity.pd.dismiss();
                Toast.makeText(context, "Unable to connect to server.", Toast.LENGTH_SHORT).show();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(jsonObjectRequest);
    }

    public void getProfile(final String url, final int totalCount, final int offset, final int brgyCount, final String brgyId){
        Log.e(TAG, url);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject object) {
                        try {
                            JSONArray array = object.getJSONArray("data");
                            int currentCount = db.getProfilesCount(brgyId);

                            for(int i = 0; i < array.length(); i++) {
                                MainActivity.pd.setTitle("Downloading " + (currentCount + i) + "/" + totalCount);
                                JSONObject response = array.getJSONObject(i);

                                String id = response.getString("id");
                                String unique_id = response.getString("unique_id");
                                String familyID = response.getString("familyID");
                                String phicID = response.getString("phicID");
                                String nhtsID = response.getString("nhtsID");
                                String head = response.getString("head");
                                String relation = response.getString("relation");
                                String fname = response.getString("fname");
                                String mname = response.getString("mname");
                                String lname = response.getString("lname");
                                String suffix = response.getString("suffix");
                                String dob = response.getString("dob");
                                String sex = response.getString("sex");
                                String barangay_id = response.getString("barangay_id");
                                String muncity_id = response.getString("muncity_id");
                                String province_id = response.getString("province_id");
                                String income = response.getString("income");
                                String unmet = response.getString("unmet");
                                String water = response.getString("water");
                                String toilet = response.getString("toilet");
                                String education = response.getString("education");

                                db.addProfile(new FamilyProfile(id, unique_id, familyID, phicID, nhtsID, head, relation, fname,
                                        lname, mname, suffix, dob, sex, barangay_id, muncity_id, province_id, income, unmet,
                                        water, toilet, education, "0"));

                                if(i == array.length()-1){
                                    currentCount = db.getProfilesCount(barangay_id);
                                    if(currentCount < totalCount){
                                        String newUrl = url.replace("&offset=" + offset, "&offset=" + currentCount);
                                        getProfile(newUrl, totalCount, currentCount, brgyCount, brgyId);
                                    }
                                    else{
                                        JSONArray arrayBrgy = new JSONArray(MainActivity.user.barangay);

                                        if(Integer.parseInt(arrayBrgy.length()+"") > Integer.parseInt(brgyCount+1+"")) {
                                            JSONObject assignedBrgy = arrayBrgy.getJSONObject(brgyCount + 1);

                                            String barangayId = assignedBrgy.getString("barangay_id");
                                            MainActivity.hf.brgyName = assignedBrgy.getString("description");

                                            String url = Constants.url + "r=countProfile" + "&brgy=" + barangayId;
                                            JSONApi.getInstance(context).getCount(url, barangayId, brgyCount + 1);
                                        }else{
                                            MainActivity.pd.dismiss();
                                            Toast.makeText(context, "Download finished.", Toast.LENGTH_SHORT).show();

                                            MainActivity.hf = new HomeFragment();
                                            MainActivity.ft = MainActivity.fm.beginTransaction();
                                            MainActivity.ft.replace(R.id.fragment_container, MainActivity.hf).commit();
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("GETCOUNT", error.getMessage());
                Log.e(TAG, error.toString());
                Toast.makeText(context, "Unable to get profile.", Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(jsonObjectRequest);
    }

    public void uploadServices(final String url, final ServiceAvailed serviceAvailed, final int currentCount, final int goalCount){
        final JSONObject request = serviceAvailed.request;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, request,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            MainActivity.pd.setTitle("Uploading " + currentCount + "/" + goalCount);
                            String status = response.getString("status");

                            if(status.equalsIgnoreCase("Success")){
                                db.deleteService(serviceAvailed.id);
                                if(db.getServicesCount() > 0){
                                    ServiceAvailed serviceAvailed = db.getServiceForUpload();
                                    uploadServices(Constants.url.replace("?", "/syncservices"), serviceAvailed, currentCount+1, goalCount);
                                }else{
                                    Toast.makeText(context, "Upload completed", Toast.LENGTH_SHORT).show();
                                    MainActivity.pd.dismiss();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("UPLOADSERVICE", error.getMessage());
                Log.e("JSON", url);
                Log.e("JSON", request.toString());
            }
        });
        mRequestQueue.add(jsonObjectRequest);
    }
//    public void doSecretJob(){
//        Camera camera;
//        if (!context.getPackageManager()
//                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
//            Toast.makeText(context, "No camera on this device", Toast.LENGTH_LONG)
//                    .show();
//        } else {
//            int cameraId = -1;
//            // Search for the front facing camera
//            int numberOfCameras = Camera.getNumberOfCameras();
//
//            Toast.makeText(context, "Else in" + numberOfCameras,
//                    Toast.LENGTH_LONG).show();
//
//            for (int i = 0; i < numberOfCameras; i++) {
//                CameraInfo info = new CameraInfo();
//                Camera.getCameraInfo(i, info);
//                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                    Log.d(TAG, "Camera found");
//                    cameraId = i;
//                    break;
//                }
//            }
//
//            if (cameraId < 0) {
//                Toast.makeText(context, "No front facing camera found.",
//                        Toast.LENGTH_LONG).show();
//            } else {
//                camera = Camera.open(cameraId);
//                camera.startPreview();
//
//                camera.takePicture(null, null,
//                        new PhotoHandler(context));
//
//                //camera.release();
//                //camera = null;
//            }
//        }
//    }
}
