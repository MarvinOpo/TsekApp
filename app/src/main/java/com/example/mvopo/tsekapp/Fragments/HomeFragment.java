package com.example.mvopo.tsekapp.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mvopo.tsekapp.Helper.JSONApi;
import com.example.mvopo.tsekapp.MainActivity;
import com.example.mvopo.tsekapp.Model.Constants;
import com.example.mvopo.tsekapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mvopo on 10/19/2017.
 */

public class HomeFragment extends Fragment{

    public static String brgyName = "";

    TextView brgyCount, targetCount, profiledCount, availedCount, completionCount, moreInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        brgyCount = view.findViewById(R.id.barangay_count);
        targetCount = view.findViewById(R.id.target_count);
        profiledCount = view.findViewById(R.id.profiled_count);
        availedCount = view.findViewById(R.id.availed_count);
        completionCount = view.findViewById(R.id.goal_count);
        moreInfo = view.findViewById(R.id.target_more);

        try {
            JSONArray arrayBrgy = new JSONArray(MainActivity.user.barangay);
            brgyCount.setText(arrayBrgy.length()+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int profCount = MainActivity.db.getProfilesCount();
        targetCount.setText(MainActivity.user.target);
        profiledCount.setText(profCount + "");

        float completion = profCount / Integer.parseInt(MainActivity.user.target) * 100;
        completionCount.setText(completion + "% Goal Completion");

        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.fabMenu.setVisibility(View.GONE);
                MainActivity.ft = MainActivity.fm.beginTransaction();
                MainActivity.ft.replace(R.id.fragment_container, MainActivity.vpf).commit();
            }
        });

        return view;
    }
}
