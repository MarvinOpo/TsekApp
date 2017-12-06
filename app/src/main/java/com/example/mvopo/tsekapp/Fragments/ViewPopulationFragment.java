package com.example.mvopo.tsekapp.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvopo.tsekapp.Helper.ListAdapter;
import com.example.mvopo.tsekapp.MainActivity;
import com.example.mvopo.tsekapp.Model.Constants;
import com.example.mvopo.tsekapp.Model.FamilyProfile;
import com.example.mvopo.tsekapp.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by mvopo on 10/19/2017.
 */

public class ViewPopulationFragment extends Fragment {

    ListView lv;
    ArrayList<FamilyProfile> familyProfiles = new ArrayList<>();
    ListAdapter adapter;

    TextView tvId, tvName, tvAge, tvSex, tvBarangay;
    Button btnUpdate, btnAdd;
    EditText txtSearch;
    ImageView btnSearch;

    Bundle bundle = new Bundle();
    ManagePopulationFragment mpf = new ManagePopulationFragment();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout, container, false);

        lv = view.findViewById(R.id.lv);
        txtSearch = view.findViewById(R.id.list_searchTxt);
        btnSearch = view.findViewById(R.id.list_searchBtn);

        familyProfiles.clear();
        familyProfiles = MainActivity.db.getFamilyProfiles("");
//        familyProfiles.add(new FamilyProfile("06062017-1203-2099362", "", "", "Johnny Boy", "Abapo", "Basd", "", "1/1/11", "Male", "Cubacub", "", "", "", "", "", true));true
//        familyProfiles.add(new FamilyProfile("06082017-1203-2391263", "", "", "Nacario", "Abelgas", "Basd", "", "1/1/11", "Male", "Cubacub", "", "", "", "", "", true));
//        familyProfiles.add(new FamilyProfile("06022017-1203-1759539", "", "", "Alexander James", "Abenaza", "Basd", "", "1/1/11", "Male", "Cubacub", "", "", "", "", "", true));

        adapter = new ListAdapter(getContext(), R.layout.population_item, familyProfiles, null);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                View focusedView = getActivity().getCurrentFocus();
                if (focusedView != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.population_dialog, null);
                tvId = dialogView.findViewById(R.id.population_id);
                tvName = dialogView.findViewById(R.id.population_name);
                tvAge = dialogView.findViewById(R.id.population_age);
                tvSex = dialogView.findViewById(R.id.population_sex);
                tvBarangay = dialogView.findViewById(R.id.population_barangay);
                btnUpdate = dialogView.findViewById(R.id.population_updateBtn);
                btnAdd = dialogView.findViewById(R.id.population_addBtn);

                String fullName = familyProfiles.get(position).lname + ", " + familyProfiles.get(position).fname + " " +
                        familyProfiles.get(position).mname + " " + familyProfiles.get(position).suffix;

                tvId.setText("Profile ID: " + familyProfiles.get(position).familyId);
                tvName.setText("Name: " + fullName);
                tvAge.setText("Age: " + Constants.getAge(familyProfiles.get(position).dob));
                tvSex.setText("Sex: " + familyProfiles.get(position).sex);
                tvBarangay.setText("Barangay: " + Constants.getBrgyName(familyProfiles.get(position).barangayId));

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialogView);

                final AlertDialog dialog = builder.create();
                dialog.show();

                bundle.putParcelable("familyProfile", familyProfiles.get(position));

                mpf = new ManagePopulationFragment();
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bundle.putBoolean("toUpdate", true);
                        bundle.putBoolean("addHead", false);
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
                        bundle.putBoolean("addHead", false);
                        mpf.setArguments(bundle);
                        MainActivity.ft = MainActivity.fm.beginTransaction();
                        MainActivity.ft.replace(R.id.fragment_container, mpf).addToBackStack("").commit();
                        dialog.dismiss();
                    }
                });


            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String search = txtSearch.getText().toString().trim();
                familyProfiles.clear();

                if (!search.isEmpty()) {
                    familyProfiles.addAll(MainActivity.db.getFamilyProfiles(search));
                }else{
                    familyProfiles.addAll(MainActivity.db.getFamilyProfiles(""));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        btnSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String search = txtSearch.getText().toString().trim();
//                if (!search.isEmpty()) {
//                    familyProfiles.clear();
//                    familyProfiles.addAll(MainActivity.db.getFamilyProfiles(search));
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_head, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add_member:
                Calendar c = Calendar.getInstance();
                String famId = String.format("%02d", (c.get(Calendar.MONTH) + 1)) +  String.format("%02d", (c.get(Calendar.DAY_OF_MONTH))) +
                        String.format("%02d", (c.get(Calendar.YEAR))) + "-" + String.format("%04d", Integer.parseInt(MainActivity.user.id)) + "-" +
                        String.format("%02d", (c.get(Calendar.HOUR))) + String.format("%02d", (c.get(Calendar.MINUTE))) +
                String.format("%02d", (c.get(Calendar.SECOND)));

                FamilyProfile familyProfile = new FamilyProfile("", "", famId, "", "", "", "", "", "", "", "", "",
                        "", "", "", "", "", "", "","", "", "1");

                mpf = new ManagePopulationFragment();
                bundle.putParcelable("familyProfile", familyProfile);
                bundle.putBoolean("toUpdate", false);
                bundle.putBoolean("addHead", true);
                mpf.setArguments(bundle);
                MainActivity.ft = MainActivity.fm.beginTransaction();
                MainActivity.ft.replace(R.id.fragment_container, mpf).addToBackStack("").commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
