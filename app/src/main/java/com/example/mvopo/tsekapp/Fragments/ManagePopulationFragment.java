package com.example.mvopo.tsekapp.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mvopo.tsekapp.MainActivity;
import com.example.mvopo.tsekapp.Model.Constants;
import com.example.mvopo.tsekapp.Model.FamilyProfile;
import com.example.mvopo.tsekapp.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;

/**
 * Created by mvopo on 10/20/2017.
 */

public class ManagePopulationFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    LinearLayout updateFields, optionHolder;
    EditText txtFamilyId, txtPhilHealthId, txtNhtsId, txtFname, txtMname, txtLname, txtBday, txtBrgy,
            txtHead, txtEducation, txtSuffix, txtSex, txtIncome, txtUnmet, txtSupply, txtToilet;
    Button manageBtn, optionBtn;

    FamilyProfile familyProfile;
    boolean toUpdate, addHead;

    String famId, philId, nhtsId, fname, mname, lname, bday, brgy, head, relation, education,
            suffix, sex, income, unmet, supply, toilet;

    String[] brgys;
    String[] brgyIds;
    boolean brgyFieldClicked = false;
    int position;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_member, container, false);
        toUpdate = getArguments().getBoolean("toUpdate");
        addHead = getArguments().getBoolean("addHead");
        familyProfile = getArguments().getParcelable("familyProfile");

        //Toast.makeText(getContext(), addHead+"", Toast.LENGTH_SHORT).show();
        try {
            JSONArray array = new JSONArray(MainActivity.user.barangay);

            brgys = new String[array.length()];
            brgyIds = new String[array.length()];

            for (int i = 0; i < array.length(); i++) {
                brgys[i] = array.getJSONObject(i).getString("description");
                brgyIds[i] = array.getJSONObject(i).getString("barangay_id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        txtFamilyId = view.findViewById(R.id.manage_familyId);
        txtPhilHealthId = view.findViewById(R.id.manage_philhealthId);
        txtNhtsId = view.findViewById(R.id.manage_nhtsId);
        txtFname = view.findViewById(R.id.manage_fname);
        txtMname = view.findViewById(R.id.manage_mname);
        txtLname = view.findViewById(R.id.manage_lname);
        txtBday = view.findViewById(R.id.manage_bday);
        txtBrgy = view.findViewById(R.id.manage_barangay);
        txtEducation = view.findViewById(R.id.manage_education);
        txtSex = view.findViewById(R.id.manage_sex);
        txtSuffix = view.findViewById(R.id.manage_suffix);
        txtIncome = view.findViewById(R.id.manage_income);
        txtUnmet = view.findViewById(R.id.manage_unmet);
        txtSupply = view.findViewById(R.id.manage_supply);
        txtToilet = view.findViewById(R.id.manage_toilet);
        updateFields = view.findViewById(R.id.updateFields_holder);
        manageBtn = view.findViewById(R.id.manageBtn);

        txtFamilyId.setText(familyProfile.familyId);
        if (!toUpdate) {
            view.findViewById(R.id.layout_head).setVisibility(View.GONE);
            if (!addHead) {
                updateFields.setVisibility(View.GONE);
                view.findViewById(R.id.layout_relation).setVisibility(View.VISIBLE);
            }
            txtHead = view.findViewById(R.id.manage_relation);
            manageBtn.setText("Add");
        } else {
            txtHead = view.findViewById(R.id.manage_head);
            setFieldTexts();
        }

        txtBrgy.setOnClickListener(this);
        txtBday.setOnClickListener(this);
        txtEducation.setOnClickListener(this);

        if(!addHead) txtHead.setOnClickListener(this);

        txtSex.setOnClickListener(this);
        txtSuffix.setOnClickListener(this);
        txtIncome.setOnClickListener(this);
        txtUnmet.setOnClickListener(this);
        txtSupply.setOnClickListener(this);
        txtToilet.setOnClickListener(this);
        manageBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.manage_education:
                showOptionDialog(R.array.educational_attainment, txtEducation);
                break;
            case R.id.manage_head:
                showOptionDialog(R.array.is_family_head, txtHead);
                break;
            case R.id.manage_relation:
                showOptionDialog(R.array.realation_to_head, txtHead);
                break;
            case R.id.manage_sex:
                showOptionDialog(R.array.sex, txtSex);
                break;
            case R.id.manage_suffix:
                showOptionDialog(R.array.suffix, txtSuffix);
                break;
            case R.id.manage_bday:
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ManagePopulationFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setMaxDate(now);
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.manage_income:
                showOptionDialog(R.array.monthly_income, txtIncome);
                break;
            case R.id.manage_unmet:
                showOptionDialog(R.array.unmet_needs, txtUnmet);
                break;
            case R.id.manage_supply:
                showOptionDialog(R.array.water_supply, txtSupply);
                break;
            case R.id.manage_toilet:
                showOptionDialog(R.array.sanitary_toilet, txtToilet);
                break;
            case R.id.manage_barangay:
                brgyFieldClicked = true;
                showOptionDialog(0, txtBrgy);
                break;
            case R.id.manageBtn:
                famId = txtFamilyId.getText().toString().trim();
                philId = txtPhilHealthId.getText().toString().trim();
                nhtsId = txtNhtsId.getText().toString().trim();
                fname = txtFname.getText().toString().trim();
                mname = txtMname.getText().toString().trim();
                lname = txtLname.getText().toString().trim();
                bday = txtBday.getText().toString().trim();
                brgy = txtBrgy.getText().toString().trim();
                head = txtHead.getText().toString().trim();
                suffix = txtSuffix.getText().toString().trim();
                sex = txtSex.getText().toString().trim();
                income = txtIncome.getText().toString().trim();
                unmet = txtUnmet.getText().toString().trim();
                supply = txtSupply.getText().toString().trim();
                toilet = txtToilet.getText().toString().trim();
                education = txtEducation.getText().toString().trim();

                if (fname.isEmpty()) {
                    txtFname.setError("Required");
                    txtFname.requestFocus();
                } else if (mname.isEmpty()) {
                    txtMname.setError("Required");
                    txtMname.requestFocus();
                } else if (lname.isEmpty()) {
                    txtLname.setError("Required");
                    txtLname.requestFocus();
                } else if (bday.isEmpty()) {
                    Toast.makeText(getContext(), "Birthdate required", Toast.LENGTH_SHORT).show();
                } else if (sex.isEmpty()) {
                    Toast.makeText(getContext(), "Gender required", Toast.LENGTH_SHORT).show();
                } else if (brgy.isEmpty()) {
                    Toast.makeText(getContext(), "Barangay required", Toast.LENGTH_SHORT).show();
                } else {
                    if(brgyFieldClicked) brgy = txtBrgy.getTag().toString();
                    else brgy = familyProfile.barangayId;

                    //Toast.makeText(getContext(), brgy, Toast.LENGTH_SHORT).show();
                    if (manageBtn.getText().toString().equalsIgnoreCase("ADD")) {
                        String isHead = "NO";
                        if (addHead) isHead = "YES";

                        FamilyProfile newFamilyProfile = new FamilyProfile("", fname+lname+suffix+famId, famId, philId, nhtsId, isHead,
                                head, fname, lname, mname, suffix, bday, sex, brgy, "", "", income, unmet, supply, toilet, education, "1");
                        MainActivity.db.addProfile(newFamilyProfile);
                        Toast.makeText(getContext(), "Successfully added", Toast.LENGTH_SHORT).show();
                    } else {

                        FamilyProfile updatedFamilyProfile = new FamilyProfile(familyProfile.id, familyProfile.uniqueId, famId, philId, nhtsId, head,
                                "Head", fname, lname, mname, suffix, bday, sex, brgy, familyProfile.muncityId, familyProfile.provinceId, income, unmet,
                                supply, toilet, education, "1");
                        MainActivity.db.updateProfile(updatedFamilyProfile);
                        Toast.makeText(getContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
                    }

                    MainActivity.fm.popBackStack();
                }
                break;

        }
    }

    public void setFieldTexts() {
        txtFamilyId.setText(familyProfile.familyId);
        txtPhilHealthId.setText(familyProfile.philId);
        txtNhtsId.setText(familyProfile.nhtsId);
        txtFname.setText(familyProfile.fname);
        txtMname.setText(familyProfile.mname);
        txtLname.setText(familyProfile.lname);
        txtBday.setText(familyProfile.dob);
        txtBrgy.setText(Constants.getBrgyName(familyProfile.barangayId));

        if (toUpdate) txtHead.setText(familyProfile.isHead);
        else txtHead.setText(familyProfile.relation);

        txtEducation.setText(familyProfile.educationalAttainment);
        txtSuffix.setText(familyProfile.suffix);
        txtSex.setText(familyProfile.sex);
        txtIncome.setText(familyProfile.income);
        txtUnmet.setText(familyProfile.unmetNeed);
        txtSupply.setText(familyProfile.waterSupply);
        txtToilet.setText(familyProfile.sanitaryToilet);
    }

    public void showOptionDialog(final int id, final EditText txtView) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.options_dialog, null);
        optionHolder = view.findViewById(R.id.option_holder);
        optionBtn = view.findViewById(R.id.optionBtn);

        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        params.bottomMargin = 5;
        params.topMargin = 5;

        String[] labels;

        if (id != 0) labels = getResources().getStringArray(id);
        else labels = brgys;

        final RadioGroup radioGroup = new RadioGroup(getContext());

        for (int i = 0; i < labels.length; i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(labels[i]);

            if (txtView.getId() == R.id.manage_barangay) radioButton.setId(Integer.parseInt(brgyIds[i]));

            View lineView = new View(getContext());
            lineView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            lineView.setLayoutParams(params);

            radioGroup.addView(radioButton);
            radioGroup.addView(lineView);
        }

        ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
        optionHolder.addView(radioGroup);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        final AlertDialog optionDialog = builder.create();
        optionDialog.show();

        optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                txtView.setText(radioButton.getText());

                if (txtView.getId() == R.id.manage_barangay) {
                    txtView.setTag(radioButton.getId());
                    txtBrgy = txtView;
                } else if (id == R.id.manage_head) {
                    if (txtView.getText().toString().equalsIgnoreCase("Yes") ||
                            txtView.getText().toString().equalsIgnoreCase("No")) {
                        txtView.setHint("Relation to Head");
                    } else {
                        txtView.setHint("Family Head?");
                    }
                }

                optionDialog.dismiss();
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        txtBday.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
    }
}
