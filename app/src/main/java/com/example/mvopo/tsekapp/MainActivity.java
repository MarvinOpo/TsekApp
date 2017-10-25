package com.example.mvopo.tsekapp;

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
import android.view.Menu;
import android.view.MenuItem;

import com.example.mvopo.tsekapp.Fragments.AvailServicesFragment;
import com.example.mvopo.tsekapp.Fragments.AvailServicesPopulationFragment;
import com.example.mvopo.tsekapp.Fragments.ChangePassFragment;
import com.example.mvopo.tsekapp.Fragments.HomeFragment;
import com.example.mvopo.tsekapp.Fragments.ServicesStatusFragment;
import com.example.mvopo.tsekapp.Fragments.ViewPopulationFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static FragmentManager fm;
    public static FragmentTransaction ft;

    HomeFragment hf = new HomeFragment();
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(fm.getBackStackEntryCount() == 0) {
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
            }else{
                fm.popBackStackImmediate();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
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

        ft = fm.beginTransaction();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if (id == R.id.nav_home) {
            // Handle the camera action
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

        } else if (id == R.id.nav_change_pass) {
            ft.replace(R.id.fragment_container, cpf).commit();
        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to log out?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
