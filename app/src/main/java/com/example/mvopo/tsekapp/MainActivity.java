package com.example.mvopo.tsekapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvopo.tsekapp.Fragments.AvailServicesFragment;
import com.example.mvopo.tsekapp.Fragments.AvailServicesPopulationFragment;
import com.example.mvopo.tsekapp.Fragments.ChangePassFragment;
import com.example.mvopo.tsekapp.Fragments.HomeFragment;
import com.example.mvopo.tsekapp.Fragments.ServicesStatusFragment;
import com.example.mvopo.tsekapp.Fragments.ViewPopulationFragment;
import com.example.mvopo.tsekapp.Helper.DBHelper;
import com.example.mvopo.tsekapp.Helper.JSONApi;
import com.example.mvopo.tsekapp.Model.Constants;
import com.example.mvopo.tsekapp.Model.FamilyProfile;
import com.example.mvopo.tsekapp.Model.User;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static FragmentManager fm;
    public static FragmentTransaction ft;
    public static User user;
    public static DBHelper db;
    public static ProgressDialog pd;
    public static String token = "", rowID = "";

    NavigationView navigationView;
    FloatingActionMenu fabMenu;
    FloatingActionButton fabDownload, fabUpload;
    TextView tvUserName, tvUserContact;
    String TAG = "MainActivity";

    public static HomeFragment hf = new HomeFragment();
    ViewPopulationFragment vpf = new ViewPopulationFragment();
    ServicesStatusFragment ssf = new ServicesStatusFragment();
    AvailServicesPopulationFragment aspf = new AvailServicesPopulationFragment();
    ChangePassFragment cpf = new ChangePassFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DBHelper(this);

        try {
            Bundle bundle = getIntent().getExtras();
            user = bundle.getParcelable("user");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, hf).commit();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setUpHeader();

        fabMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);
        fabDownload = (FloatingActionButton) findViewById(R.id.download);
        fabUpload = (FloatingActionButton) findViewById(R.id.upload);;

        fabDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int uploadableCount = db.getUploadableCount();
                int profileCount = db.getProfilesCount();
                if (profileCount == 0) {
                    downloadProfiles();
                } else if (uploadableCount > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Please sync profiles before downloading from server.\n\nUnsync Profile(s): " + uploadableCount);
                    builder.setPositiveButton("Ok", null);
                    builder.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Downloading data from server will clear all records, Do you wish to proceed downloading?");
                    builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.deleteProfiles();
                            downloadProfiles();
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    builder.show();
                }
            }
        });

        fabUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int count = db.getUploadableCount();

                if (count > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(count + " Profiles needs to be uploaded");
                    builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            int count = db.getUploadableCount();
                            MainActivity.pd = ProgressDialog.show(MainActivity.this, "Uploading 1/" + count, "Please wait...", false, false);

                            String url = Constants.url.replace("?", "/syncprofile");
                            JSONApi.getInstance(MainActivity.this).uploadProfile(url, Constants.getProfileJson(), count, 1);
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    builder.show();
                } else {
                    Toast.makeText(MainActivity.this, "Nothing to upload", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setUpHeader() {
        View view = getLayoutInflater().inflate(R.layout.nav_header_main, null);

        TextView name = view.findViewById(R.id.user_name);
        TextView contact = view.findViewById(R.id.user_contact);

        name.setText(user.fname + " " + user.lname);
        contact.setText(user.contact.replace(" ", ""));

        navigationView.addHeaderView(view);
    }

    public void downloadProfiles() {
        try {
            MainActivity.pd = ProgressDialog.show(MainActivity.this, "Loading", "Please wait...", false, false);
            JSONArray arrayBrgy = new JSONArray(MainActivity.user.barangay);
            JSONObject assignedBrgy = arrayBrgy.getJSONObject(0);

            String barangayId = assignedBrgy.getString("barangay_id");
            hf.brgyName = assignedBrgy.getString("description");
            String url = Constants.url + "r=countProfile" + "&brgy=" + barangayId;
            JSONApi.getInstance(MainActivity.this).getCount(url, barangayId, 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fm.getBackStackEntryCount() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to exit app?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.super.onBackPressed();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            } else {
                fm.popBackStackImmediate();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.add_head, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        fabMenu.setVisibility(View.GONE);
        ft = fm.beginTransaction();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if (id == R.id.nav_home) {
            // Handle the camera action
            fabMenu.setVisibility(View.VISIBLE);
            ft.replace(R.id.fragment_container, hf).commit();
        } else if (id == R.id.nav_services) {
            ft.replace(R.id.fragment_container, aspf).commit();
        } else if (id == R.id.nav_manage_population) {
            ft.replace(R.id.fragment_container, vpf).commit();
        } else if (id == R.id.nav_services_status) {
            ft.replace(R.id.fragment_container, ssf).commit();
//        } else if (id == R.id.nav_services_report) {
//
//        } else if (id == R.id.nav_case_referred) {
//
//        } else if (id == R.id.nav_change_pass) {
//            ft.replace(R.id.fragment_container, cpf).commit();
//        } else if (id == R.id.nav_logout) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("Are you sure you want to log out?");
//            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    MainActivity.this.finish();
//                }
//            });
//            builder.setNegativeButton("Cancel", null);
//            builder.show();
//
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
