package com.example.mvopo.tsekapp.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvopo.tsekapp.Helper.JSONApi;
import com.example.mvopo.tsekapp.Helper.ListAdapter;
import com.example.mvopo.tsekapp.LoginActivity;
import com.example.mvopo.tsekapp.MainActivity;
import com.example.mvopo.tsekapp.Model.Constants;
import com.example.mvopo.tsekapp.Model.DengvaxiaDetails;
import com.example.mvopo.tsekapp.Model.FamilyProfile;
import com.example.mvopo.tsekapp.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Permission;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by mvopo on 10/20/2017.
 */

public class ManagePopulationFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private final int CAMERA_CODE = 100;
    LinearLayout updateFields;
    ScrollView optionHolder;
    EditText txtFamilyId, txtPhilHealthId, txtNhtsId, txtFname, txtMname, txtLname, txtBday, txtBrgy,
            txtHead, txtEducation, txtSuffix, txtSex, txtIncome, txtUnmet, txtSupply, txtToilet, txtRelation,
            txtFacilityName, txtListNumber, txtDoseScreen, txtDoseDate, txtDoseLot, txtDoseBatch, txtDoseExpiration,
            txtDoseAefi, txtRemarks;
    TextView  tvStatus;
    Button manageBtn, optionBtn, manageDengvaxia, dengvaxiaRegisterBtn;
    TextInputLayout unmetFrame;
    View view;
    ImageView ivPatient;

    FamilyProfile familyProfile;
    List<FamilyProfile> matchingProfiles = new ArrayList<>();

    String famId, philId, nhtsId, fname, mname, lname, bday, brgy, head, relation, education,
            suffix, sex, income, unmet, supply, toilet;
    String[] brgys, value;
    String[] brgyIds;
    int age = 0;
    boolean brgyFieldClicked = false;
    boolean toUpdate, addHead;

    String males = "Son, Husband, Father, Brother, Nephew, Grandfather, Grandson, Son in Law, Brother in Law, Father in Law";
    String females = "Daughter, Wife, Mother, Sister, Niece, Grandmother, Granddaugther, Daughter in Law, Sister in Law, Mother in Law";

    Calendar now = Calendar.getInstance();
    DatePickerDialog dpd = DatePickerDialog.newInstance(
            ManagePopulationFragment.this,
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
    );

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_manage_member, container, false);
        toUpdate = getArguments().getBoolean("toUpdate");
        addHead = getArguments().getBoolean("addHead");

        familyProfile = getArguments().getParcelable("familyProfile");

        //Toast.makeText(getContext(), addHead+"", Toast.LENGTH_SHORT).show();
        dpd.setMaxDate(now);
        value = getResources().getStringArray(R.array.educational_attainment_value);
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
        txtHead = view.findViewById(R.id.manage_head);
        txtRelation = view.findViewById(R.id.manage_relation);

        updateFields = view.findViewById(R.id.updateFields_holder);
        unmetFrame = view.findViewById(R.id.unmet_frame);
        manageBtn = view.findViewById(R.id.manageBtn);
        manageDengvaxia = view.findViewById(R.id.manageDengvaxia);

        txtFamilyId.setText(familyProfile.familyId);
        if (!toUpdate) {
            view.findViewById(R.id.layout_head).setVisibility(View.GONE);
            if (!addHead || txtHead.getText().toString().equalsIgnoreCase("NO")) {
                updateFields.setVisibility(View.GONE);
                unmetFrame.setVisibility(View.GONE);
                view.findViewById(R.id.layout_relation).setVisibility(View.VISIBLE);
            }

            manageBtn.setText("Add");
        } else {
            setFieldTexts();
        }

        txtBrgy.setOnClickListener(this);
        txtBday.setOnClickListener(this);
        txtEducation.setOnClickListener(this);

        if (!addHead) txtHead.setOnClickListener(this);

        txtRelation.setOnClickListener(this);
        txtSex.setOnClickListener(this);
        txtSuffix.setOnClickListener(this);
        txtIncome.setOnClickListener(this);
        txtUnmet.setOnClickListener(this);        txtSupply.setOnClickListener(this);
        txtToilet.setOnClickListener(this);

        manageBtn.setOnClickListener(this);
        manageDengvaxia.setOnClickListener(this);

        txtSex.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sex = txtSex.getText().toString();
                if (((age >= 15 && age <= 49) || addHead) && sex.equalsIgnoreCase("Female"))
                    unmetFrame.setVisibility(View.VISIBLE);
                else unmetFrame.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        txtBday.addTextChangedListener(getDateTextWatcher(txtBday));

        if (!toUpdate) showProfileCheckerDialog();
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
                showOptionDialog(R.array.yes_no, txtHead);
                break;
            case R.id.manage_relation:
                showOptionDialog(R.array.realation_to_head, txtRelation);
                break;
            case R.id.manage_sex:
                showOptionDialog(R.array.sex, txtSex);
                break;
            case R.id.manage_suffix:
                showOptionDialog(R.array.suffix, txtSuffix);
                break;
            case R.id.manage_bday:
                dpd.show(getActivity().getFragmentManager(), "bday");
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
            case R.id.manageDengvaxia:
                showDengvaxiaDialog();
                break;
            case R.id.dengvaxia_dose_screen:
                showOptionDialog(R.array.yes_no, txtDoseScreen);
                break;
            case R.id.dengvaxia_dose_date:
                dpd.show(getActivity().getFragmentManager(), "dose_date");
                break;
            case R.id.dengvaxia_dose_expiration:
                dpd.show(getActivity().getFragmentManager(), "dose_expiry");
                break;
            case R.id.dengvaxia_dose_aefi:
                showOptionDialog(R.array.yes_no, txtDoseAefi);
                break;
            case R.id.dengvaxia_patient_image:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                CAMERA_CODE);
                        break;
                    }
                }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_CODE);

                break;
            case R.id.dengvaxiaBtn:
                String doseDate = txtDoseDate.getText().toString().trim();
                if (doseDate.isEmpty() && doseDate.length() != 10) {
                    Toast.makeText(getContext(), "Invalid date, please follow YYYY-MM-DD format", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        JSONObject request = new JSONObject();
                        request.accumulate("tsekap_id", familyProfile.id);
                        request.accumulate("facility_name", txtFacilityName.getText().toString());
                        request.accumulate("list_number", txtListNumber.getText().toString());
                        request.accumulate("fname", familyProfile.fname);
                        request.accumulate("mname", familyProfile.mname);
                        request.accumulate("lname", familyProfile.lname);
                        request.accumulate("muncity", familyProfile.muncityId);
                        request.accumulate("barangay", familyProfile.barangayId);
                        request.accumulate("dob", familyProfile.dob);
                        request.accumulate("sex", familyProfile.sex);
                        request.accumulate("dose_screened", txtDoseScreen.getText().toString());
                        request.accumulate("dose_date_given", doseDate);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date;
                        Calendar c = Calendar.getInstance();

                        try {
                            date = sdf.parse(doseDate);
                            c.setTime(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String age = Constants.getAge(familyProfile.dob, c);

                        request.accumulate("dose_age", age);

                        if(!age.contains("/") && (Integer.parseInt(age) >= 9 &&
                                Integer.parseInt(age) <= 14)) request.accumulate("validation", "Yes");
                        else request.accumulate("validation", "No");

                        request.accumulate("dose_lot_no", txtDoseLot.getText().toString());
                        request.accumulate("dose_batch_no", txtDoseBatch.getText().toString());
                        request.accumulate("dose_expiration", txtDoseExpiration.getText().toString());
                        request.accumulate("dose_AEFI", txtDoseAefi.getText().toString());
                        request.accumulate("remarks", txtRemarks.getText().toString());

                        MainActivity.pd = ProgressDialog.show(getContext(), "Loading", "Please wait...", false, false);
                        JSONApi.getInstance(getContext()).dengvaxiaRegister(Constants.dengvaxiaUrl + "cmd=register", request);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.manageBtn:
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                famId = txtFamilyId.getText().toString().trim();
                philId = txtPhilHealthId.getText().toString().trim();
                nhtsId = txtNhtsId.getText().toString().trim();
                fname = txtFname.getText().toString().trim();
                mname = txtMname.getText().toString().trim();
                lname = txtLname.getText().toString().trim();
                bday = txtBday.getText().toString().trim();
                brgy = txtBrgy.getText().toString().trim();
                head = txtHead.getText().toString().trim();

                relation = txtRelation.getText().toString().trim();
                if (relation.equalsIgnoreCase("Live-in Partner")) relation = "partner";

                suffix = txtSuffix.getText().toString().trim();
                sex = txtSex.getText().toString().trim();

                try {
                    income = txtIncome.getTag().toString();
                } catch (Exception e) {
                    income = familyProfile.income;
                }
                try {
                    unmet = txtUnmet.getTag().toString();
                } catch (Exception e) {
                    unmet = familyProfile.unmetNeed;
                }
                try {
                    supply = txtSupply.getTag().toString();
                } catch (Exception e) {
                    supply = familyProfile.waterSupply;
                }
                try {
                    toilet = txtToilet.getTag().toString();
                } catch (Exception e) {
                    toilet = familyProfile.sanitaryToilet;
                }
                try {
                    education = txtEducation.getTag().toString();
                } catch (Exception e) {
                    education = familyProfile.educationalAttainment;
                }

                if (fname.isEmpty()) {
                    txtFname.setError("Required");
                    txtFname.requestFocus();
                } else if (lname.isEmpty()) {
                    txtLname.setError("Required");
                    txtLname.requestFocus();
                } else if (bday.isEmpty() && bday.length() != 10) {
                    Toast.makeText(getContext(), "Invalid date, please follow YYYY-MM-DD format", Toast.LENGTH_SHORT).show();
                } else if (sex.isEmpty()) {
                    Toast.makeText(getContext(), "Gender required", Toast.LENGTH_SHORT).show();
                } else if (brgy.isEmpty()) {
                    Toast.makeText(getContext(), "Barangay required", Toast.LENGTH_SHORT).show();
                } else {
                    if (brgyFieldClicked) brgy = txtBrgy.getTag().toString();
                    else brgy = familyProfile.barangayId;

                    //Toast.makeText(getContext(), brgy, Toast.LENGTH_SHORT).show();
                    if (manageBtn.getText().toString().equalsIgnoreCase("ADD")) {
                        if (addHead) {
                            head = "YES";
                            relation = "Head";
                        } else head = "NO";

                        FamilyProfile newFamilyProfile = new FamilyProfile("", fname + mname + lname + suffix + brgy + MainActivity.user.muncity, famId, philId, nhtsId, head,
                                relation, fname, lname, mname, suffix, bday, sex, brgy, MainActivity.user.muncity, "", income, unmet, supply, toilet, education, "1");
                        MainActivity.db.addProfile(newFamilyProfile);
                        Toast.makeText(getContext(), "Successfully added", Toast.LENGTH_SHORT).show();
                    } else {
                        if (relation.isEmpty()) relation = familyProfile.relation;
                        if (head.equalsIgnoreCase("Yes")) relation = "Head";

                        FamilyProfile updatedFamilyProfile = new FamilyProfile(familyProfile.id, familyProfile.uniqueId, famId, philId, nhtsId, head,
                                relation, fname, lname, mname, suffix, bday, sex, brgy, familyProfile.muncityId, familyProfile.provinceId, income, unmet,
                                supply, toilet, education, "1");
                        MainActivity.db.updateProfile(updatedFamilyProfile);
                        Toast.makeText(getContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
                    }

                    MainActivity.fm.popBackStack();
                }
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            Log.e("ManagePop", data.toString());
            switch (requestCode) {
                case CAMERA_CODE:
                    Uri imageUri = data.getData();
                    CropImage.activity(imageUri)
                            .setFixAspectRatio(true)
                            .start(getContext(), this);
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri resultUri = result.getUri();
                    ivPatient.setImageURI(resultUri);
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_CODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_CODE);
                }else{
                    Toast.makeText(getContext(), "Camera Permission Denied, cant proceed this action", Toast.LENGTH_SHORT).show();
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

        if (familyProfile.relation.equalsIgnoreCase("partner"))
            txtRelation.setText("Live-in Partner");
        else txtRelation.setText(familyProfile.relation);

        txtBrgy.setText(Constants.getBrgyName(familyProfile.barangayId));

        if (toUpdate) {
            txtHead.setText(familyProfile.isHead);

            if (txtHead.getText().toString().equalsIgnoreCase("NO")) {
                updateFields.setVisibility(View.GONE);
                view.findViewById(R.id.layout_relation).setVisibility(View.VISIBLE);
            }

            age = Integer.parseInt(Constants.getAge(txtBday.getText().toString(), Calendar.getInstance()).split(" ")[0]);
            if ((age < 15 || age > 49) && txtSex.getText().toString().equalsIgnoreCase("Female"))
                unmetFrame.setVisibility(View.GONE);
            else unmetFrame.setVisibility(View.VISIBLE);

        } else txtHead.setText(familyProfile.relation);

        txtSuffix.setText(familyProfile.suffix);
        txtSex.setText(familyProfile.sex);

        Log.e("MPF", familyProfile.income + " " + familyProfile.unmetNeed + " " + familyProfile.waterSupply + " " + familyProfile.sanitaryToilet);
        if (!familyProfile.income.isEmpty() && !familyProfile.income.equals("0"))
            txtIncome.setText(getResources()
                    .getStringArray(R.array.monthly_income)[Integer.parseInt(familyProfile.income) - 1]);
        if (!familyProfile.unmetNeed.isEmpty() && !familyProfile.unmetNeed.equals("0"))
            txtUnmet.setText(getResources()
                    .getStringArray(R.array.unmet_needs)[Integer.parseInt(familyProfile.unmetNeed) - 1]);
        if (!familyProfile.waterSupply.isEmpty() && !familyProfile.waterSupply.equals("0"))
            txtSupply.setText(getResources()
                    .getStringArray(R.array.water_supply)[Integer.parseInt(familyProfile.waterSupply) - 1]);
        try {
            if (!familyProfile.sanitaryToilet.isEmpty() && !familyProfile.sanitaryToilet.equals("0"))
                txtToilet.setText(getResources()
                        .getStringArray(R.array.sanitary_toilet)[Integer.parseInt(familyProfile.sanitaryToilet) - 1]);
        } catch (Exception e) {
            String toilet = familyProfile.sanitaryToilet;

            if (toilet.equals("non")) {
                txtToilet.setText(getResources()
                        .getStringArray(R.array.sanitary_toilet)[0]);
            } else if (toilet.equals("comm")) {
                txtToilet.setText(getResources()
                        .getStringArray(R.array.sanitary_toilet)[1]);
            } else {
                txtToilet.setText(getResources()
                        .getStringArray(R.array.sanitary_toilet)[2]);
            }
        }

        for (int i = 0; i < value.length; i++) {
            if (familyProfile.educationalAttainment.equals(value[i])) {
                txtEducation.setText(getResources()
                        .getStringArray(R.array.educational_attainment)[i]);
                break;
            }
        }
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

        ViewGroup.LayoutParams rbParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < labels.length; i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setLayoutParams(rbParam);
            radioButton.setText(labels[i]);

            if (txtView.getId() == R.id.manage_barangay)
                radioButton.setId(Integer.parseInt(brgyIds[i]));
            else if (txtView.getId() == R.id.manage_education) {
                radioButton.setTag(value[i]);
            } else if (txtView.getId() == R.id.manage_income || txtView.getId() == R.id.manage_unmet ||
                    txtView.getId() == R.id.manage_supply || txtView.getId() == R.id.manage_toilet) {
                radioButton.setId(i + 1);
            }
            View lineView = new View(getContext());
            lineView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            lineView.setLayoutParams(params);

            radioGroup.addView(radioButton);
            radioGroup.addView(lineView);
        }

        //((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
        optionHolder.addView(radioGroup);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        final AlertDialog optionDialog = builder.create();
        optionDialog.show();

        optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                if (radioButton != null) {
                    txtView.setText(radioButton.getText());

                    if (txtView.getId() == R.id.manage_barangay) {
                        txtView.setTag(radioButton.getId());
                        txtBrgy = txtView;
                    } else if (txtView.getId() == R.id.manage_education) {
                        txtView.setTag(radioButton.getTag().toString());
                    } else if (txtView.getId() == R.id.manage_income || txtView.getId() == R.id.manage_unmet ||
                            txtView.getId() == R.id.manage_supply || txtView.getId() == R.id.manage_toilet) {
                        txtView.setTag(radioButton.getId());
                    } else if (txtView.getId() == R.id.manage_head) {
                        if (txtView.getText().toString().equalsIgnoreCase("NO")) {
                            updateFields.setVisibility(View.GONE);
                            age = Integer.parseInt(Constants.getAge(txtBday.getText().toString(), Calendar.getInstance()).split(" ")[0]);
                            if ((age < 15 || age > 49) && txtSex.getText().toString().equalsIgnoreCase("Female"))
                                unmetFrame.setVisibility(View.GONE);
                            ManagePopulationFragment.this.view.findViewById(R.id.layout_relation).setVisibility(View.VISIBLE);
                            txtRelation.setText("Son");
                        } else {
                            updateFields.setVisibility(View.VISIBLE);
                            if (txtSex.getText().toString().equalsIgnoreCase("Female"))
                                unmetFrame.setVisibility(View.VISIBLE);
                            ManagePopulationFragment.this.view.findViewById(R.id.layout_relation).setVisibility(View.GONE);
                        }
                    } else if (txtView.getId() == R.id.manage_relation) {
                        String relation = txtView.getText().toString();
                        if (males.contains(txtView.getText().toString())) txtSex.setText("Male");
                        else if (females.contains(txtView.getText().toString()))
                            txtSex.setText("Female");
                        else txtSex.setText("");
                    }

                } else {
                    txtView.setText("");
                    txtView.setTag("");
                }

                optionDialog.dismiss();
            }
        });
    }

    public void showDengvaxiaDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dengvaxia_dialog, null);

        tvStatus = view.findViewById(R.id.dengvaxia_status);
        txtFacilityName = view.findViewById(R.id.dengvaxia_facility_name);
        txtListNumber = view.findViewById(R.id.dengvaxia_list_number);
        txtDoseScreen = view.findViewById(R.id.dengvaxia_dose_screen);
        txtDoseDate = view.findViewById(R.id.dengvaxia_dose_date);
        txtDoseLot = view.findViewById(R.id.dengvaxia_dose_lot);
        txtDoseBatch = view.findViewById(R.id.dengvaxia_dose_batch);
        txtDoseExpiration = view.findViewById(R.id.dengvaxia_dose_expiration);
        txtDoseAefi = view.findViewById(R.id.dengvaxia_dose_aefi);
        txtRemarks = view.findViewById(R.id.dengvaxia_remarks);
        dengvaxiaRegisterBtn = view.findViewById(R.id.dengvaxiaBtn);
        ivPatient = view.findViewById(R.id.dengvaxia_patient_image);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.show();

        txtDoseScreen.setOnClickListener(this);
        txtDoseDate.setOnClickListener(this);
        txtDoseExpiration.setOnClickListener(this);
        txtDoseAefi.setOnClickListener(this);
        dengvaxiaRegisterBtn.setOnClickListener(this);
        ivPatient.setOnClickListener(this);

        txtDoseDate.addTextChangedListener(getDateTextWatcher(txtDoseDate));
        txtDoseExpiration.addTextChangedListener(getDateTextWatcher(txtDoseExpiration));

        MainActivity.pd = ProgressDialog.show(getContext(), "Loading", "Please wait...", false, false);
        JSONApi.getInstance(getContext()).getDengvaxiaDetails(Constants.dengvaxiaUrl + "cmd=dose&id="+familyProfile.id);
    }

    public void setDengvaxiaDetails(DengvaxiaDetails details){
        tvStatus.setText("STATUS: " + details.getStatus().toUpperCase());
        txtFacilityName.setText(details.getFacilityName());
        txtListNumber.setText(details.getListNumber());
        txtDoseScreen.setText(details.getDoseScreen());
        txtDoseDate.setText(details.getDoseDate());
        txtDoseLot.setText(details.getDoseLot());
        txtDoseBatch.setText(details.getDoseBatch());
        txtDoseExpiration.setText(details.getDoseExpiry());
        txtDoseAefi.setText(details.getDoseAefi());
        txtRemarks.setText(details.getRemarks());

        txtFacilityName.setEnabled(false);
        txtListNumber.setEnabled(false);
        txtDoseScreen.setEnabled(false);
        txtDoseDate.setEnabled(false);
        txtDoseLot.setEnabled(false);
        txtDoseBatch.setEnabled(false);
        txtDoseExpiration.setEnabled(false);
        txtDoseAefi.setEnabled(false);
        txtRemarks.setEnabled(false);

        dengvaxiaRegisterBtn.setVisibility(View.GONE);

        MainActivity.pd.dismiss();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        switch (view.getTag().toString()) {
            case "bday":
                txtBday.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));

                age = Integer.parseInt(Constants.getAge(txtBday.getText().toString(), Calendar.getInstance()).split(" ")[0]);
                if (((age >= 15 && age <= 49) || addHead) && txtSex.getText().toString().equalsIgnoreCase("Female"))
                    unmetFrame.setVisibility(View.VISIBLE);
                else unmetFrame.setVisibility(View.GONE);
                break;
            case "dose_date":
                txtDoseDate.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
                break;
            case "dose_expiry":
                txtDoseExpiration.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
                break;
        }
    }

    public void showProfileCheckerDialog() {
        View checkerDialogView = LayoutInflater.from(getContext()).inflate(R.layout.profile_checker_dialog, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(checkerDialogView);

        final EditText txtCheckerFname, txtCheckerMname, txtCheckerLname, txtCheckerSuffix;
        final ListView lvMatchingProfiles;
        final LinearLayout txtFrame, lvFrame;
        TextView btnCheck, btnUpdate, btnNew;

        txtCheckerFname = checkerDialogView.findViewById(R.id.checker_fname);
        txtCheckerMname = checkerDialogView.findViewById(R.id.checker_mname);
        txtCheckerLname = checkerDialogView.findViewById(R.id.checker_lname);
        txtCheckerSuffix = checkerDialogView.findViewById(R.id.checker_suffix);
        lvMatchingProfiles = checkerDialogView.findViewById(R.id.checker_lv);
        lvFrame = checkerDialogView.findViewById(R.id.chercker_lv_frame);
        txtFrame = checkerDialogView.findViewById(R.id.checker_txt_frame);
        btnCheck = checkerDialogView.findViewById(R.id.checker_btnCheck);
        btnUpdate = checkerDialogView.findViewById(R.id.checker_btnUpdate);
        btnNew = checkerDialogView.findViewById(R.id.checker_btnNew);

        final AlertDialog checkerDialog = builder.create();
        checkerDialog.show();

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname, mname, lname, suffix;

                fname = txtCheckerFname.getText().toString().trim();
                mname = txtCheckerMname.getText().toString().trim();
                lname = txtCheckerLname.getText().toString().trim();
                suffix = txtCheckerSuffix.getText().toString().trim();

                matchingProfiles = MainActivity.db.getMatchingProfiles(fname, mname, lname, suffix);

                if (matchingProfiles.size() > 0) {
                    ListAdapter adapter = new ListAdapter(getContext(), R.layout.population_dialog_item, matchingProfiles, null, null);
                    lvMatchingProfiles.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    lvFrame.setVisibility(View.VISIBLE);
                    txtFrame.setVisibility(View.GONE);
                } else {
                    txtFname.setText(fname);
                    txtMname.setText(mname);
                    txtLname.setText(lname);
                    txtSuffix.setText(suffix);
                    checkerDialog.dismiss();
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lvMatchingProfiles.getCheckedItemPosition() >= 0) {
                    familyProfile = matchingProfiles.get(lvMatchingProfiles.getCheckedItemPosition());
                    setFieldTexts();
                    checkerDialog.dismiss();
                } else
                    Toast.makeText(getContext(), "Please select profile to update.", Toast.LENGTH_SHORT).show();
            }
        });

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtFname.setText(txtCheckerFname.getText().toString().trim());
                txtMname.setText(txtCheckerMname.getText().toString().trim());
                txtLname.setText(txtCheckerLname.getText().toString().trim());
                txtSuffix.setText(txtCheckerSuffix.getText().toString().trim());
                checkerDialog.dismiss();
            }
        });
    }

    public TextWatcher getDateTextWatcher(final EditText editText){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Log.e("QWEQWE", i + " " + i1 + " " + i2);

                String date = editText.getText().toString();
                Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int day = c.get(Calendar.DAY_OF_MONTH);

                if (i1 == 0) {
                    if (date.length() == 4) {
                        if (Integer.parseInt(date) > year) {
                            Toast.makeText(getContext(), "Person cant have a future birthdate.", Toast.LENGTH_SHORT).show();
                            date = "";
                            editText.setText("");
                        }
                    } else if (date.length() == 7) {
                        if (Integer.parseInt(date.substring(5, 7)) > 12) {

                            if (Integer.parseInt(date.substring(0, 4)) == year) {
                                date = date.replace(date.substring(5, 7), String.format("%02d", month));
                                editText.setText(date);
                                editText.setSelection(editText.getText().length());
                                Toast.makeText(getContext(), "Date should not exceed current date.", Toast.LENGTH_SHORT).show();
                            } else {
                                date = date.replace(date.substring(5, 7), "12");
                                Toast.makeText(getContext(), "Maximum month is 12", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (date.length() == 10) {
                        c.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5, 7)) - 1, 1);
                        int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);

                        if (Integer.parseInt(date.substring(8, 10)) > maxDay) {
                            Toast.makeText(getContext(), "Maximum day for " + date.substring(0, date.length() - 3) + " is " + maxDay, Toast.LENGTH_LONG).show();
                            date = date.replace(date.substring(8, 10), maxDay + "");

                            editText.setText(date);
                            editText.setSelection(editText.getText().length());
                        }

                        if (Integer.parseInt(date.substring(0, 4)) == year && Integer.parseInt(date.substring(5, 7)) >= month &&
                                Integer.parseInt(date.substring(8, 10)) > day) {
                            date = date.replace(date.substring(8, 10), String.format("%02d", day));
                            Toast.makeText(getContext(), "Date should not exceed current date.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if ((date.length() == 4 || date.length() == 7)) {
                        txtBday.setText(date += "-");
                        txtBday.setSelection(txtBday.getText().length());
                    }   
                } else if (i1 == 1) {
                    if (date.length() == 4 || date.length() == 7) {
                        txtBday.setText(date.substring(0, date.length() - 1));
                        txtBday.setSelection(txtBday.getText().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }


}
