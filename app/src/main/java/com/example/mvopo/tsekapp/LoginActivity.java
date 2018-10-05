package com.example.mvopo.tsekapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mvopo.tsekapp.Helper.DBHelper;
import com.example.mvopo.tsekapp.Helper.JSONApi;
import com.example.mvopo.tsekapp.Model.Constants;
import com.example.mvopo.tsekapp.Model.User;

/**
 * Created by mvopo on 10/19/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final int state_storage_code = 100;

    public static ProgressDialog pd;

    EditText txtId, txtPass;
    Button mSignInBtn;
    DBHelper db;

    String loginId, loginPass;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DBHelper(this);


        try {
            user = db.getUser();
            if (user != null) {
                showPinDialog(true, user);
            }
        } catch (Exception e) {
        }

        txtId = (EditText) findViewById(R.id.login_id);
        txtPass = (EditText) findViewById(R.id.login_pass);
        mSignInBtn = (Button) findViewById(R.id.signInBtn);
        mSignInBtn.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    showDialog();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, state_storage_code);
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.signInBtn:
                if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    db.deleteUser();
                    db.deleteProfiles();
                    db.deleteServiceStatus();

                    loginId = txtId.getText().toString().trim();
                    loginPass = txtPass.getText().toString().trim();

                    if (loginId.isEmpty()) {
                        txtId.setError("Required");
                        txtId.requestFocus();
                    } else if (loginPass.isEmpty()) {
                        txtPass.setError("Required");
                        txtPass.requestFocus();
                    } else {
//                        showPinDialog(false);
                        pd = ProgressDialog.show(LoginActivity.this, "Loading", "Please wait...", false, false);
                        String url = Constants.url + "r=login" + "&user=" + loginId + "&pass=" + loginPass;
                        JSONApi.getInstance(LoginActivity.this).login(url);
                    }
                } else {
                    showDialog();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case state_storage_code:
                if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){

                }else{
                    showDialog();
                }
                break;
        }
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please allow all permissions required for this app." +
                "\n\nPhone - Reading phone number for upcoming development in user services management (i.e Request, Complaints, Reports, etc)." +
                "\n\nStorage - Writing Storage for Application updates inline to version checker.");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.show();
    }

    public void showPinDialog(final boolean hasPin, User loggedUser) {
        user = loggedUser;

        View view = getLayoutInflater().inflate(R.layout.pin_dialog, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        TextView tvPinBtn, tvPinHeader;

        tvPinBtn = view.findViewById(R.id.pin_btn);
        tvPinHeader = view.findViewById(R.id.pin_header);

        final EditText txtPin = view.findViewById(R.id.pin_txt);

        if (hasPin) tvPinHeader.setText("Please enter pin");

        final AlertDialog dialog = builder.create();
        dialog.show();

        tvPinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pin = txtPin.getText().toString().trim();

                if (!hasPin) {
                    if (pin.isEmpty() || pin.length() < 4) {
                        txtPin.setError("Please provide a 4-digit pin.");
                        txtPin.requestFocus();

                        return;
                    } else {
                        SharedPreferences.Editor editor = getSharedPreferences("CheckAppPin", MODE_PRIVATE).edit();
                        editor.putString("pin", pin);
                        editor.apply();

                        dialog.dismiss();
                    }
                } else {
                    SharedPreferences prefs = getSharedPreferences("CheckAppPin", MODE_PRIVATE);
                    String prefPin = prefs.getString("pin", "No pin defined");//"No name defined" is the default value.

                    if (!prefPin.equals(pin)) {
                        txtPin.setError("Incorrect Pin");
                        txtPin.requestFocus();

                        return;
                    }
                }

                if(user == null) user = db.getUser();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("user", user);
                LoginActivity.this.startActivity(intent);
                dialog.dismiss();
                LoginActivity.this.finish();
            }
        });
    }

//    public void showPinDialog(final boolean hasPin) {
//        View view = getLayoutInflater().inflate(R.layout.pin_dialog, null, false);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(view);
//
//        TextView tvPinBtn, tvPinHeader;
//
//        tvPinBtn = view.findViewById(R.id.pin_btn);
//        tvPinHeader = view.findViewById(R.id.pin_header);
//
//        final EditText txtPin = view.findViewById(R.id.pin_txt);
//
//        if (hasPin) tvPinHeader.setText("Please enter pin");
//
//        final AlertDialog dialog = builder.create();
//        dialog.show();
//
//        tvPinBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String pin = txtPin.getText().toString().trim();
//
//                if (!hasPin) {
//                    if (pin.isEmpty() || pin.length() < 4) {
//                        txtPin.setError("Please provide a 4-digit pin.");
//                        txtPin.requestFocus();
//                    } else {
//                        SharedPreferences.Editor editor = getSharedPreferences("CheckAppPin", MODE_PRIVATE).edit();
//                        editor.putString("pin", pin);
//                        editor.apply();
//
//                        dialog.dismiss();
//                        pd = ProgressDialog.show(LoginActivity.this, "Loading", "Please wait...", false, false);
//                        String url = Constants.url + "r=login" + "&user=" + loginId + "&pass=" + loginPass;
//                        JSONApi.getInstance(LoginActivity.this).login(url);
//                    }
//                } else {
//                    SharedPreferences prefs = getSharedPreferences("CheckAppPin", MODE_PRIVATE);
//                    String prefPin = prefs.getString("pin", "No pin defined");//"No name defined" is the default value.
//
//                    if (prefPin.equals(pin)) {
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        intent.putExtra("user", user);
//                        LoginActivity.this.startActivity(intent);
//                        dialog.dismiss();
//                        LoginActivity.this.finish();
//                    }else{
//                        txtPin.setError("Incorrect Pin");
//                        txtPin.requestFocus();
//                    }
//                }
//            }
//        });
//    }
}
