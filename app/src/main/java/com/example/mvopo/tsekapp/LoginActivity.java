package com.example.mvopo.tsekapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mvopo.tsekapp.Helper.DBHelper;
import com.example.mvopo.tsekapp.Helper.JSONApi;
import com.example.mvopo.tsekapp.Model.Constants;
import com.example.mvopo.tsekapp.Model.User;

/**
 * Created by mvopo on 10/19/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static ProgressDialog pd;
    EditText txtId, txtPass;
    Button mSignInBtn;
    DBHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DBHelper(this);


        try {
            User user = db.getUser();
            if (user != null) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("user", user);
                this.startActivity(intent);
                this.finish();
            }
        }catch (Exception e){}

        txtId = (EditText) findViewById(R.id.login_id);
        txtPass = (EditText) findViewById(R.id.login_pass);
        mSignInBtn = (Button) findViewById(R.id.signInBtn);
        mSignInBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.signInBtn:
                String loginId = txtId.getText().toString().trim();
                String loginPass = txtPass.getText().toString().trim();

                if(loginId.isEmpty()){
                    txtId.setError("Required");
                    txtId.requestFocus();
                }else if(loginPass.isEmpty()){
                    txtPass.setError("Required");
                    txtPass.requestFocus();
                }else {
                    pd = ProgressDialog.show(this, "Loading", "Please wait...", false, false);
                    String url = Constants.url + "r=login" + "&user=" + loginId + "&pass=" + loginPass;
                    JSONApi.getInstance(this).login(url);
                }

                break;
        }
    }
}
