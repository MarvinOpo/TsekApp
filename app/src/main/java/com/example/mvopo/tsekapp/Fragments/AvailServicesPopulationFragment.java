package com.example.mvopo.tsekapp.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mvopo.tsekapp.Helper.ListAdapter;
import com.example.mvopo.tsekapp.MainActivity;
import com.example.mvopo.tsekapp.Model.FamilyProfile;
import com.example.mvopo.tsekapp.R;

import java.util.ArrayList;

/**
 * Created by mvopo on 10/19/2017.
 */

public class AvailServicesPopulationFragment extends Fragment {

    ListView lv;
    ArrayList<FamilyProfile> familyProfiles = new ArrayList<>();
    ListAdapter adapter;

    TextView tvId, tvName, tvAge, tvSex, tvBarangay;
    Button btnUpdate, btnAdd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout, container, false);

        lv = view.findViewById(R.id.lv);
        familyProfiles.clear();
        familyProfiles.add(new FamilyProfile("06062017-1203-2099362", "", "", "Johnny Boy", "Abapo", "Basd", "", "1/1/11", "Male", "Cubacub", "", "", "", "", "", true));
        familyProfiles.add(new FamilyProfile("06082017-1203-2391263", "", "", "Nacario", "Abelgas", "Basd", "", "1/1/11", "Male", "Cubacub", "", "", "", "", "", true));
        familyProfiles.add(new FamilyProfile("06022017-1203-1759539", "", "", "Alexander James", "Abenaza", "Basd", "", "1/1/11", "Male", "Cubacub", "", "", "", "", "", true));

        adapter = new ListAdapter(getContext(), R.layout.population_item, familyProfiles);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = familyProfiles.get(position).fName + " "  + familyProfiles.get(position).lName;

                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("age", "13");

                AvailServicesFragment asf = new AvailServicesFragment();
                asf.setArguments(bundle);
                MainActivity.ft = MainActivity.fm.beginTransaction();
                MainActivity.ft.replace(R.id.fragment_container, asf).addToBackStack("").commit();
            }
        });

        return view;
    }
}
