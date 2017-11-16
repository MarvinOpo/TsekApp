package com.example.mvopo.tsekapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mvopo.tsekapp.Helper.ListAdapter;
import com.example.mvopo.tsekapp.MainActivity;
import com.example.mvopo.tsekapp.Model.FamilyProfile;
import com.example.mvopo.tsekapp.R;

import java.util.ArrayList;

/**
 * Created by mvopo on 10/20/2017.
 */

public class ServicesStatusFragment extends Fragment {

    ListView lv;
    ArrayList<FamilyProfile> familyProfiles = new ArrayList<>();
    ListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_must_services, container, false);

        lv = view.findViewById(R.id.lv);
        familyProfiles.clear();
        familyProfiles = MainActivity.db.getFamilyProfiles("");

        adapter = new ListAdapter(getContext(), R.layout.services_item, familyProfiles);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }
}
