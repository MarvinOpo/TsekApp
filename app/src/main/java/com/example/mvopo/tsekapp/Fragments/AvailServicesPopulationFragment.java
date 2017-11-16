package com.example.mvopo.tsekapp.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvopo.tsekapp.Helper.ListAdapter;
import com.example.mvopo.tsekapp.MainActivity;
import com.example.mvopo.tsekapp.Model.FamilyProfile;
import com.example.mvopo.tsekapp.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by mvopo on 10/19/2017.
 */

public class AvailServicesPopulationFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    ListView lv;
    ArrayList<FamilyProfile> familyProfiles = new ArrayList<>();
    ListAdapter adapter;

    EditText txtSearch, txtDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout, container, false);

        lv = view.findViewById(R.id.lv);
        txtSearch = view.findViewById(R.id.list_searchTxt);
        txtDate = view.findViewById(R.id.avail_date);
        txtDate.setVisibility(View.VISIBLE);

        familyProfiles.clear();
        familyProfiles = MainActivity.db.getFamilyProfiles("");

        adapter = new ListAdapter(getContext(), R.layout.population_item, familyProfiles);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String date = txtDate.getText().toString().trim();

                if(!date.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("profile", familyProfiles.get(position));
                    bundle.putString("date", date);

                    AvailServicesFragment asf = new AvailServicesFragment();
                    asf.setArguments(bundle);
                    MainActivity.ft = MainActivity.fm.beginTransaction();
                    MainActivity.ft.replace(R.id.fragment_container, asf).addToBackStack("").commit();
                }else Toast.makeText(getContext(), "Date required", Toast.LENGTH_SHORT).show();
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

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AvailServicesPopulationFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });

        return view;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        txtDate.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
    }
}
