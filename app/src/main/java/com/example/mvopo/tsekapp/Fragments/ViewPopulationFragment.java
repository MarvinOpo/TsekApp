package com.example.mvopo.tsekapp.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvopo.tsekapp.Helper.ListAdapter;
import com.example.mvopo.tsekapp.MainActivity;
import com.example.mvopo.tsekapp.Model.FamilyProfile;
import com.example.mvopo.tsekapp.R;

import java.util.ArrayList;

/**
 * Created by mvopo on 10/19/2017.
 */

public class ViewPopulationFragment extends Fragment {

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
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.population_dialog, null);
                tvId = dialogView.findViewById(R.id.population_id);
                tvName = dialogView.findViewById(R.id.population_name);
                tvAge = dialogView.findViewById(R.id.population_age);
                tvSex = dialogView.findViewById(R.id.population_sex);
                tvBarangay = dialogView.findViewById(R.id.population_barangay);
                btnUpdate = dialogView.findViewById(R.id.population_updateBtn);
                btnAdd = dialogView.findViewById(R.id.population_addBtn);

                String fullName = familyProfiles.get(position).lName + ", " + familyProfiles.get(position).fName + " " +
                        familyProfiles.get(position).mName + " " + familyProfiles.get(position).suffix;

                tvId.setText("Profile ID: " + familyProfiles.get(position).profileId);
                tvName.setText("Name: " + fullName);
                tvAge.setText("Age: 13");
                tvSex.setText("Sex: " + familyProfiles.get(position).sex);
                tvBarangay.setText("Barangay: " + familyProfiles.get(position).barangay);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialogView);

                final AlertDialog dialog = builder.create();
                dialog.show();

                final ManagePopulationFragment mpf = new ManagePopulationFragment();
                final Bundle bundle = new Bundle();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bundle.putBoolean("toUpdate", true);
                        mpf.setArguments(bundle);
                        MainActivity.ft = MainActivity.fm.beginTransaction();
                        MainActivity.ft.replace(R.id.fragment_container, mpf).addToBackStack("").commit();
                        dialog.dismiss();
                    }
                });

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bundle.putBoolean("toUpdate", false);
                        mpf.setArguments(bundle);
                        MainActivity.ft = MainActivity.fm.beginTransaction();
                        MainActivity.ft.replace(R.id.fragment_container, mpf).addToBackStack("").commit();
                        dialog.dismiss();
                    }
                });


            }
        });

        return view;
    }
}
