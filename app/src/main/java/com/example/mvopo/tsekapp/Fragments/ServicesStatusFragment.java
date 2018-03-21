package com.example.mvopo.tsekapp.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mvopo.tsekapp.Helper.JSONApi;
import com.example.mvopo.tsekapp.Helper.ListAdapter;
import com.example.mvopo.tsekapp.MainActivity;
import com.example.mvopo.tsekapp.Model.Constants;
import com.example.mvopo.tsekapp.Model.FamilyProfile;
import com.example.mvopo.tsekapp.Model.ServicesStatus;
import com.example.mvopo.tsekapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mvopo on 10/20/2017.
 */

public class ServicesStatusFragment extends Fragment {

    ListView lv;
    ArrayList<ServicesStatus> servicesStatuses = new ArrayList<>();
    ListAdapter adapter;
    EditText txtSearch;

    View filter;
    CheckBox cbPhysical, cbLab, cbOthers;
    Spinner spnrGender, spnrBrgy;
    Button btnFilter, btnCancel;
    AlertDialog dialog;

    String filterText = "";

    String[] gender = new String[]{"Both", "Male", "Female"}, brgys;
    JSONArray arrayBrgy;

    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_must_services, container, false);
        filter = inflater.inflate(R.layout.service_status_filter, null, false);

        lv = view.findViewById(R.id.lv);
        txtSearch = view.findViewById(R.id.search);

        cbPhysical = filter.findViewById(R.id.physical);
        cbLab = filter.findViewById(R.id.lab);
        cbOthers = filter.findViewById(R.id.others);
        spnrGender = filter.findViewById(R.id.gender);
        spnrBrgy = filter.findViewById(R.id.brgy);
        btnFilter = filter.findViewById(R.id.filterBtn);
        btnCancel = filter.findViewById(R.id.cancelBtn);

        try {
            arrayBrgy = new JSONArray(MainActivity.user.barangay);
            brgys = new String[arrayBrgy.length()+1];
            brgys[0] = "All";
            for(int i = 0; i < arrayBrgy.length(); i++){
                brgys[i+1] = arrayBrgy.getJSONObject(i).getString("description");
            }

            ArrayAdapter brgyAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, brgys);
            spnrBrgy.setAdapter(brgyAdapter);
            brgyAdapter.notifyDataSetChanged();

            ArrayAdapter genderAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, gender);
            spnrGender.setAdapter(genderAdapter);
            genderAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        servicesStatuses.clear();
        servicesStatuses = MainActivity.db.getServicesStatus(null);

        adapter = new ListAdapter(getContext(), R.layout.services_item, null, servicesStatuses, null);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String search = txtSearch.getText().toString().trim();
                servicesStatuses.clear();


                if (!search.isEmpty()) {
                    String additionalFilter = "";
                    if(!filterText.isEmpty()) additionalFilter = " and " + filterText;
                    servicesStatuses.addAll(MainActivity.db.getServicesStatus("name LIKE '" + search + "%'" + additionalFilter));
                } else {
                    servicesStatuses.addAll(MainActivity.db.getServicesStatus(filterText));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        initiateFilter();

        showTutorial();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.download, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_download:
                int serviceStatusCount = MainActivity.db.getServiceStatusCount("");
                if (serviceStatusCount == 0) {
                    downloadServices();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Downloading data from server will clear current services status, Do you wish to proceed downloading?");
                    builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.db.deleteServiceStatus();
                            downloadServices();
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    builder.show();
                }
                break;
            case R.id.action_filter:
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void downloadServices() {
        try {
            MainActivity.pd = ProgressDialog.show(getContext(), "Loading", "Please wait...", false, false);
            JSONArray arrayBrgy = new JSONArray(MainActivity.user.barangay);
            JSONObject assignedBrgy = arrayBrgy.getJSONObject(0);

            String barangayId = assignedBrgy.getString("barangay_id");

            String url = Constants.url + "r=countmustservices" + "&brgy=" + barangayId;
            JSONApi.getInstance(getContext()).getServiceStatusCount(url, barangayId, 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initiateFilter(){
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterText = "";
                if(cbPhysical.isChecked()) filterText += "group1 = '1'";
                if(cbLab.isChecked()){
                    if(!filterText.isEmpty()) filterText += " and ";
                    filterText += "group2 = '1'";
                }
                if(cbOthers.isChecked()){
                    if(!filterText.isEmpty()) filterText += " and ";
                    filterText += "group3 = '1'";
                }

                if(!spnrBrgy.getSelectedItem().toString().equals("All")) {
                    try {
                        String brgyId = arrayBrgy.getJSONObject(spnrBrgy.getSelectedItemPosition() - 1).getString("barangay_id");
                        if (!filterText.isEmpty()) filterText += " and ";
                        filterText += "barangayId = '" + brgyId + "'";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                String nameSearch = txtSearch.getText().toString().trim();
                String finalFilter = filterText;
                if(!nameSearch.isEmpty()) finalFilter = "name LIKE '" + nameSearch + "%' and " + filterText;

                servicesStatuses.clear();
                servicesStatuses.addAll(MainActivity.db.getServicesStatus(finalFilter));
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(filter);
        dialog = builder.create();
    }

    public void showTutorial(){
//        new FancyShowCaseView.Builder(getActivity())
//                .focusOn(txtSearch)
//                .title("This section is for searching specific profile")
//                .titleSize(20, TypedValue.COMPLEX_UNIT_DIP)
//                .focusShape(FocusShape.ROUNDED_RECTANGLE)
//                .roundRectRadius(15)
//                .showOnce("serviceStatus")
//                .dismissListener(new DismissListener() {
//                    @Override
//                    public void onDismiss(String id) {
//                        new FancyShowCaseView.Builder(getActivity())
//                                .focusOn(lv)
//                                .title("This portion shows list of profiles and status indicator for services")
//                                .titleGravity(Gravity.TOP)
//                                .titleSize(20, TypedValue.COMPLEX_UNIT_DIP)
//                                .focusShape(FocusShape.ROUNDED_RECTANGLE)
//                                .roundRectRadius(15)
//                                .dismissListener(new DismissListener() {
//                                    @Override
//                                    public void onDismiss(String id) {
//                                        new FancyShowCaseView.Builder(getActivity())
//                                                .focusOn(MainActivity.toolbar.getChildAt(2).findViewById(R.id.action_download))
//                                                .title("This downloads Services status from web server")
//                                                .titleSize(20, TypedValue.COMPLEX_UNIT_DIP)
//                                                .dismissListener(new DismissListener() {
//                                                    @Override
//                                                    public void onDismiss(String id) {
//                                                        new FancyShowCaseView.Builder(getActivity())
//                                                                .focusOn(MainActivity.toolbar.getChildAt(2).findViewById(R.id.action_filter))
//                                                                .titleSize(20, TypedValue.COMPLEX_UNIT_DIP)
//                                                                .title("And this filters the profiles shown in the list")
//                                                                .build()
//                                                                .show();
//                                                    }
//
//                                                    @Override
//                                                    public void onSkipped(String id) {
//
//                                                    }
//                                                })
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
//                .build()
//                .show();

        MainActivity.queue.clear();
        MainActivity.queue.add(MainActivity.makeSpotlightView(view.findViewById(R.id.spotlight_search_focus),
                "Looking for Someone?",
                "Well, well, well! Just type their LAST NAME and i'll find them for yah.",
                "ServiceStatusSearch"));

        MainActivity.queue.add(MainActivity.makeSpotlightView(lv,
                "Eyes Here!",
                "These are list of services status",
                "ServiceStatusList"));

        MainActivity.queue.add(MainActivity.makeSpotlightView(MainActivity.toolbar.getChildAt(2),
                "Do Something",
                "Arrow Down Button: Click me if you want to download 3 must services status. " +
                        "I suggest you upload first before clicking me to count your newly added services\n\n" +
                        "Filter Button: Just click me if you want advance filter on your list. Now get/89 off!",
                "ServiceStatusOption"));

        MainActivity.startSequence();
    }
}
