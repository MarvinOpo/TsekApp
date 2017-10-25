package com.example.mvopo.tsekapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mvopo.tsekapp.R;

/**
 * Created by mvopo on 10/23/2017.
 */

public class AvailServicesFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    CheckBox cbBloodPressure, cbWeight, cbHeight;
    RadioGroup rgBloodPressure, rgWeight, rgHeight;
    TextView tvName, tvAge;

    String name = "", age = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        name = getArguments().getString("name");
        age = getArguments().getString("age");

        View view = inflater.inflate(R.layout.fragment_avail_services, container, false);

        tvName = view.findViewById(R.id.avail_services_name);
        tvAge = view.findViewById(R.id.avail_services_age);
        cbBloodPressure = view.findViewById(R.id.avail_blood_pressure);
        cbWeight = view.findViewById(R.id.avail_weight);
        cbHeight = view.findViewById(R.id.avail_height);
        rgBloodPressure = view.findViewById(R.id.rg_blood_pressure);
        rgWeight = view.findViewById(R.id.rg_weight);
        rgHeight = view.findViewById(R.id.rg_height);

        tvName.setText("Name: " + name);
        tvAge.setText("Age: " + age);

        cbBloodPressure.setOnCheckedChangeListener(this);
        cbWeight.setOnCheckedChangeListener(this);
        cbHeight.setOnCheckedChangeListener(this);

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        int visibility = 0;

        visibility = (isChecked)? View.VISIBLE :  View.GONE;

        switch (id){
            case R.id.avail_blood_pressure:
                rgBloodPressure.setVisibility(visibility);
                break;
            case R.id.avail_weight:
                rgWeight.setVisibility(visibility);
                break;
            case R.id.avail_height:
                rgHeight.setVisibility(visibility);
                break;
        }
    }
}
