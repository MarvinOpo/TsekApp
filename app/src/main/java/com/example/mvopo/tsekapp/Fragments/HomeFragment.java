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

    TextView brgyCount, targetCount, profiledCount, availedCount, completionCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        brgyCount = view.findViewById(R.id.barangay_count);
        targetCount = view.findViewById(R.id.target_count);
        profiledCount = view.findViewById(R.id.profiled_count);
        availedCount = view.findViewById(R.id.availed_count);
        completionCount = view.findViewById(R.id.goal_count);

        return view;
    }
}
