package com.example.mvopo.tsekapp.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
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
    Calendar c;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_layout, container, false);

        c = Calendar.getInstance();

        lv = view.findViewById(R.id.lv);
        txtSearch = view.findViewById(R.id.list_searchTxt);
        txtSearch.requestFocus();
        txtDate = view.findViewById(R.id.avail_date);
        txtDate.setVisibility(View.VISIBLE);

        familyProfiles.clear();
        familyProfiles = MainActivity.db.getFamilyProfiles("");

        adapter = new ListAdapter(getContext(), R.layout.population_item, familyProfiles, null, null);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String date = txtDate.getText().toString().trim();

                if(!date.isEmpty()) {
                    if(date.length() == 10) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("profile", familyProfiles.get(position));
                        bundle.putString("date", date);

                        AvailServicesFragment asf = new AvailServicesFragment();
                        asf.setArguments(bundle);
                        MainActivity.ft = MainActivity.fm.beginTransaction();
                        MainActivity.ft.replace(R.id.fragment_container, asf).addToBackStack("").commit();
                    }else{
                        Toast.makeText(getContext(), "Invalid date, please follow YYYY-MM-DD format", Toast.LENGTH_LONG).show();
                        txtSearch.requestFocus();
                    }
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

        txtDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Log.e("QWEQWE", i + " " + i1 + " " + i2);
                String date = txtDate.getText().toString();

                if(i1 == 0) {
                    if(date.length() == 7){
                        if(Integer.parseInt(date.substring(5,7)) > 12){
                            Toast.makeText(getContext(), "Maximum month is 12", Toast.LENGTH_SHORT).show();
                            date = date.replace(date.substring(5,7), "12");
                        }
                    }else if(date.length() == 10){
                        c.set(Integer.parseInt(date.substring(0,4)), Integer.parseInt(date.substring(5,7)) - 1, 1);
                        int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);

                        if(Integer.parseInt(date.substring(8,10)) > maxDay){
                            Toast.makeText(getContext(), "Maximum day for " + date.substring(0, date.length()-3) + " is " + maxDay, Toast.LENGTH_LONG).show();
                            date = date.replace(date.substring(8,10), maxDay + "");
                            txtDate.setText(date);
                            txtDate.setSelection(txtDate.getText().length());
                        }
                    }

                    if ((date.length() == 4 || date.length() == 7)) {
                        txtDate.setText(date += "-");
                        txtDate.setSelection(txtDate.getText().length());
                    }
                }else if(i1 == 1){
                    if(date.length() == 4 || date.length() == 7){
                        txtDate.setText(date.substring(0, date.length()-1));
                        txtDate.setSelection(txtDate.getText().length());
                    }
                }
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
                txtSearch.requestFocus();
            }
        });

        showTutorial();
        return view;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        txtDate.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
    }

    public void showTutorial(){
//        new FancyShowCaseView.Builder(getActivity())
//                .focusOn(txtDate)
//                .title("Tap once to manually set date. Tap again to show date picker")
//                .titleSize(20, TypedValue.COMPLEX_UNIT_DIP)
//                .focusShape(FocusShape.ROUNDED_RECTANGLE)
//                .showOnce("availPopulation")
//                .roundRectRadius(15)
//                .dismissListener(new DismissListener() {
//                    @Override
//                    public void onDismiss(String id) {
//                        new FancyShowCaseView.Builder(getActivity())
//                                .focusOn(txtSearch)
//                                .title("This section is for searching specific profile")
//                                .titleSize(20, TypedValue.COMPLEX_UNIT_DIP)
//                                .focusShape(FocusShape.ROUNDED_RECTANGLE)
//                                .roundRectRadius(15)
//                                .dismissListener(new DismissListener() {
//                                    @Override
//                                    public void onDismiss(String id) {
//                                        new FancyShowCaseView.Builder(getActivity())
//                                                .focusOn(lv)
//                                                .title("This shows list of family profiles")
//                                                .titleGravity(Gravity.TOP)
//                                                .focusShape(FocusShape.ROUNDED_RECTANGLE)
//                                                .roundRectRadius(15)
//                                                .build()
//                                                .show();
//                                    }
//
//                                    @Override
//                                    public void onSkipped(String id) {
//
//                                    }
//                                })
//                                .build()
//                                .show();
//                    }
//
//                    @Override
//                    public void onSkipped(String id) {
//
//                    }
//                })
//                 .build()
//                .show();

        MainActivity.queue.clear();
        MainActivity.queue.add(MainActivity.makeSpotlightView(view.findViewById(R.id.spotlight_date_focus),
                "Must Read!",
                "Tap once to manually set date. Tap again to show date picker",
                "AvailServiceDate"));

        MainActivity.queue.add(MainActivity.makeSpotlightView(view.findViewById(R.id.spotlight_search_focus),
                "Looking for Someone?",
                "Well, well, well! Just type their NAME and i'll find them for yah.",
                "AvailServiceSearch"));

        MainActivity.queue.add(MainActivity.makeSpotlightView(lv,
                "Eyes Here!",
                "These are list of family profiles",
                "AvailServicesList"));

        MainActivity.startSequence();
    }
}