package com.example.mvopo.tsekapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.mvopo.tsekapp.Fragments.MessageThreadFragment;
import com.example.mvopo.tsekapp.Model.User;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.Query;

/**
 * Created by mvopo on 1/30/2018.
 */

public class ChatActivity extends AppCompatActivity {

    public static Toolbar toolbar;
    public static User user;

    MessageThreadFragment mtf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back_arrow);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle b = getIntent().getExtras();
        user = b.getParcelable("user");

        getSupportActionBar().setTitle(b.getString("messageTo"));

        mtf = new MessageThreadFragment();
        mtf.setArguments(b);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, mtf).commit();

        FloatingActionMenu fabMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);
        fabMenu.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            this.finish();
            mtf.removeRegisteredListener();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
