package com.example.mvopo.tsekapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.example.mvopo.tsekapp.Fragments.AvailServicesPopulationFragment;
import com.example.mvopo.tsekapp.Fragments.ChangePassFragment;
import com.example.mvopo.tsekapp.Fragments.FeedbackFragment;
import com.example.mvopo.tsekapp.Fragments.HomeFragment;
import com.example.mvopo.tsekapp.Fragments.ServicesStatusFragment;
import com.example.mvopo.tsekapp.Fragments.ViewPopulationFragment;
import com.example.mvopo.tsekapp.Helper.ConnectionChecker;
import com.example.mvopo.tsekapp.Helper.DBHelper;
import com.example.mvopo.tsekapp.Helper.JSONApi;
import com.example.mvopo.tsekapp.Model.Constants;
import com.example.mvopo.tsekapp.Model.ServiceAvailed;
import com.example.mvopo.tsekapp.Model.User;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import me.toptas.fancyshowcase.DismissListener;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;

public class MainActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener {

    public static FragmentManager fm;
    public static FragmentTransaction ft;
    public static User user;
    public static DBHelper db;
    public static ProgressDialog pd;
    public static String rowID = "";
    public static FloatingActionMenu fabMenu;
    public static Toolbar toolbar;

    int[] location = new int[2];

    NavigationView navigationView;
    FloatingActionButton fabDownload, fabUpload;
    String TAG = "MainActivity";

    public static HomeFragment hf = new HomeFragment();
    public static ViewPopulationFragment vpf = new ViewPopulationFragment();
    public static ServicesStatusFragment ssf = new ServicesStatusFragment();
    FeedbackFragment ff = new FeedbackFragment();
    AvailServicesPopulationFragment aspf = new AvailServicesPopulationFragment();
    ChangePassFragment cpf = new ChangePassFragment();

    AlertDialog dialog;
    View headerView;
    CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                showNavTutorial();
            }
        };

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setUpHeader();

        fabMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);
        fabDownload = (FloatingActionButton) findViewById(R.id.download);
        fabUpload = (FloatingActionButton) findViewById(R.id.upload);

        fabDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try{ takePicture(); }catch (Exception e){ Log.e("QWEASD", e.getMessage()); };

                int uploadableCount = db.getUploadableCount();
                int serviceCount = db.getServicesCount();
                int profileCount = db.getProfilesCount("");
                if (profileCount == 0) {
                    downloadProfiles();
                } else if (uploadableCount > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Please upload data before syncing from server.\n\nProfile(s): " + uploadableCount +
                            "\nServices Availed: " + serviceCount);
                    builder.setPositiveButton("Ok", null);
                    builder.show();
                } else {
//                    try{ takePicture(); }catch (Exception e){ Log.e("QWEASD1", e.getMessage()); };
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
                final int uploadableCount = db.getUploadableCount();
                final int serviceCount = db.getServicesCount();

                if (uploadableCount + serviceCount > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Uploadable Profile: " + uploadableCount + "\nUploadable Services: " + serviceCount);
                    builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            MainActivity.pd = ProgressDialog.show(MainActivity.this, "Uploading 1/" + (uploadableCount + serviceCount),
                                    "Please wait...", false, true);

                            if(uploadableCount > 0) {
                                String url = Constants.url.replace("?", "/syncprofile");
                                JSONApi.getInstance(MainActivity.this).uploadProfile(url, Constants.getProfileJson(), uploadableCount, 1);
                            }else if(serviceCount > 0){
                                ServiceAvailed serviceAvailed = db.getServiceForUpload();
                                JSONApi.getInstance(MainActivity.this).uploadServices(Constants.url.replace("?", "/syncservices"), serviceAvailed,
                                        1, uploadableCount+serviceCount);
                            }else{
                                //upload services here
                                Toast.makeText(MainActivity.this, "Uploading services is under development", Toast.LENGTH_SHORT).show();
                                MainActivity.pd.dismiss();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    builder.show();
                } else {
                    Toast.makeText(MainActivity.this, "Nothing to upload", Toast.LENGTH_SHORT).show();
                }
            }
        });

        showTutorial();
    }

    public void setUpHeader() {
        View view = getLayoutInflater().inflate(R.layout.nav_header_main, null);

       profile_image = view.findViewById(R.id.profile_image);
        TextView name = view.findViewById(R.id.user_name);
        TextView contact = view.findViewById(R.id.user_contact);
        TextView version = view.findViewById(R.id.version);

        name.setText(user.fname + " " + user.lname);
        contact.setText(user.contact.replace(" ", ""));
        version.setText("APP VERSION " + BuildConfig.VERSION_NAME);
//        version.setText("DUMMY VERSION " + BuildConfig.VERSION_NAME);

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);
            }
        });

        navigationView.addHeaderView(view);
        headerView = view;
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

        if(id != R.id.nav_logout) fabMenu.setVisibility(View.GONE);

        ft = fm.beginTransaction();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (id == R.id.nav_home) {
            // Handle the camera action
            fabMenu.setVisibility(View.VISIBLE);
            hf = new HomeFragment();
            ft.replace(R.id.fragment_container, hf).commit();
        } else if (id == R.id.nav_services) {
            ft.replace(R.id.fragment_container, aspf).commit();
        } else if (id == R.id.nav_manage_population) {
            ft.replace(R.id.fragment_container, vpf).commit();
        } else if (id == R.id.nav_services_status) {
            ssf = new ServicesStatusFragment();
            ft.replace(R.id.fragment_container, ssf).commit();
//            Toast.makeText(MainActivity.this, "This feature is under development", Toast.LENGTH_SHORT).show();
//        } else if (id == R.id.nav_services_report) {
//
//        } else if (id == R.id.nav_case_referred) {
//
//        } else if (id == R.id.nav_change_pass) {
//            ft.replace(R.id.fragment_container, cpf).commit();
        } else if (id == R.id.nav_feedback) {
            ft.replace(R.id.fragment_container, ff).commit();
        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            int uploadableCount = db.getUploadableCount();
            if(uploadableCount <= 0) {
                builder.setMessage("Logging out will delete all profile data. Are you sure you want to proceed?");
                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.deleteProfiles();
                        db.deleteUser();
                        db.deleteServiceStatus();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                    }
                });
                builder.setNegativeButton("Cancel", null);
            }else{
                builder.setMessage("Please upload data before logging out.\n\nProfile(s): " + uploadableCount);
                builder.setPositiveButton("Ok", null);
            }
            builder.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case 100:
                    profile_image.setImageURI(data.getData());
            }
        }
    }

    public void showTutorial(){
        new FancyShowCaseView.Builder(this)
                .focusOn(fabMenu.getMenuIconView())
                .focusCircleRadiusFactor(2.5)
                .title("This button will Open/Close the available actions in this page")
                .showOnce("homeFragment")
                .dismissListener(new DismissListener() {
                    @Override
                    public void onDismiss(String id) {
                        new FancyShowCaseView.Builder(MainActivity.this)
                                .focusOn(toolbar.getChildAt(1))
                                .title("This button will Open/Close drawer")
                                .build()
                                .show();
                    }

                    @Override
                    public void onSkipped(String id) {

                    }
                })
                .build()
                .show();

        fabMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if(fabMenu.isOpened()){
                    new FancyShowCaseView.Builder(MainActivity.this)
                            .focusOn(fabDownload)
                            .title("This button downloads profile from the web server")
                            .showOnce("fabMenu")
                            .dismissListener(new DismissListener() {
                                @Override
                                public void onDismiss(String id) {
                                    new FancyShowCaseView.Builder(MainActivity.this)
                                            .focusOn(fabUpload)
                                            .title("This button uploads profile to the web server")
                                            .build()
                                            .show();
                                }

                                @Override
                                public void onSkipped(String id) {

                                }
                            })
                            .build()
                            .show();
                }
            }
        });
    }

    public void showNavTutorial(){
        new FancyShowCaseView.Builder(MainActivity.this)
                .focusOn(headerView.findViewById(R.id.user_info))
                .title("This shows user information")
                .showOnce("navView")
                .build()
                .show();
    }
}