package com.example.mvopo.tsekapp.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvopo.tsekapp.MainActivity;
import com.example.mvopo.tsekapp.Model.Constants;
import com.example.mvopo.tsekapp.Model.FamilyProfile;
import com.example.mvopo.tsekapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


/**
 * Created by mvopo on 10/23/2017.
 */

public class AvailServicesFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    CardView cvFemaleStatus, cvHealthEduc, cvDrugRehab, cvFamilyPlanning;
    CheckBox cbBloodPressure, cbWeight, cbHeight, cbBloodTyping, cbBloodCount, cbEyeExam, cbEarExam,
            cbUrinalysis, cbStoolExam, cbOralServices, cbFastingSugar, cbRandomSugar, cbWithUnmet,
            cbCounseling, cbCommodities, cbScreening, cbDrugCounseling, cbDrugTest, cbReferral,
            cbSputumExam, cbPhysicalExam, cbHealthEduc, cbSugarTest, cbRespiratoryInfection, cbHypertension,
            cbFeverUnknown, cbOtherInjury, cbDiarrheaGastroentetitis, cbDermatoses, cbBronchitis, cbPneumonia,
            cbGenitourinaryInfection, cbAnimalBite, cbOthers;
    RadioGroup rgBloodPressure, rgWeight, rgHeight, rgFemaleStatus;
    TextView tvName, tvAge, tvDate;
    Button btnAvail;

    FamilyProfile familyProfile;
    String[] profileAgeInfo;
    String date, bracketId = "1", TAG = "AvailServices";

    View view;
    ScrollView scrollView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        familyProfile = getArguments().getParcelable("profile");
        date = getArguments().getString("date");

        view = inflater.inflate(R.layout.fragment_avail_services, container, false);

        scrollView = view.findViewById(R.id.avail_scrollview);
        cvFemaleStatus = view.findViewById(R.id.cv_female_status);
        cvHealthEduc = view.findViewById(R.id.cv_health_educ);
        cvDrugRehab = view.findViewById(R.id.cv_drug_rehab);
        cvFamilyPlanning = view.findViewById(R.id.cv_family_planning);

        tvName = view.findViewById(R.id.avail_services_name);
        tvAge = view.findViewById(R.id.avail_services_age);
        tvDate = view.findViewById(R.id.avail_date);

        rgBloodPressure = view.findViewById(R.id.rg_blood_pressure);
        rgWeight = view.findViewById(R.id.rg_weight);
        rgHeight = view.findViewById(R.id.rg_height);
        rgFemaleStatus = view.findViewById(R.id.rg_female_status);

        cbBloodPressure = view.findViewById(R.id.avail_blood_pressure);
        cbWeight = view.findViewById(R.id.avail_weight);
        cbHeight = view.findViewById(R.id.avail_height);
        cbBloodTyping = view.findViewById(R.id.avail_blood_typing);
        cbBloodCount = view.findViewById(R.id.avail_blood_count);
        cbEyeExam = view.findViewById(R.id.avail_eye_exam);
        cbEarExam = view.findViewById(R.id.avail_ear_exam);
        cbUrinalysis = view.findViewById(R.id.avail_urinalysis);
        cbStoolExam = view.findViewById(R.id.avail_stool_exam);
        cbOralServices = view.findViewById(R.id.avail_oral_services);
        cbFastingSugar = view.findViewById(R.id.avail_fasting_sugar);
        cbRandomSugar = view.findViewById(R.id.avail_random_sugar);
        cbWithUnmet = view.findViewById(R.id.avail_with_unmet);
        cbCounseling = view.findViewById(R.id.avail_counseling);
        cbCommodities = view.findViewById(R.id.avail_commodities);
        cbScreening = view.findViewById(R.id.avail_screening);
        cbDrugCounseling = view.findViewById(R.id.avail_dug_counseling);
        cbDrugTest = view.findViewById(R.id.avail_drug_testing);
        cbReferral = view.findViewById(R.id.avail_referral);
        cbSputumExam = view.findViewById(R.id.avail_spuntum_exam);
        cbPhysicalExam = view.findViewById(R.id.avail_physical_exam);
        cbHealthEduc = view.findViewById(R.id.avail_health_educ);
        cbSugarTest = view.findViewById(R.id.avail_sugar_test);
        cbRespiratoryInfection = view.findViewById(R.id.avail_respiratory_infection);
        cbHypertension = view.findViewById(R.id.avail_hypertension);
        cbFeverUnknown = view.findViewById(R.id.avail_fever_unknown);
        cbOtherInjury = view.findViewById(R.id.avail_other_injury);
        cbDiarrheaGastroentetitis = view.findViewById(R.id.avail_diarrhea_gastroenteritis);
        cbDermatoses = view.findViewById(R.id.avail_dermatoses);
        cbBronchitis = view.findViewById(R.id.avail_bronchitis_emphysema);
        cbPneumonia = view.findViewById(R.id.avail_pneumonia);
        cbGenitourinaryInfection = view.findViewById(R.id.avail_genitourinary_infection);
        cbAnimalBite = view.findViewById(R.id.avail_animal_bite);
        cbOthers = view.findViewById(R.id.avail_others);
        btnAvail = view.findViewById(R.id.availBtn);

        String age = Constants.getAge(familyProfile.dob, Calendar.getInstance());

        tvName.setText(": " + familyProfile.fname + " " + familyProfile.lname + " " + familyProfile.suffix);
        tvAge.setText(": " + age);
        tvDate.setText(": " + date);

        cbBloodPressure.setOnCheckedChangeListener(this);
        cbWeight.setOnCheckedChangeListener(this);
        cbHeight.setOnCheckedChangeListener(this);

        if (familyProfile.sex.equalsIgnoreCase("Female")) {
            cvFemaleStatus.setVisibility(View.VISIBLE);
        }

        profileAgeInfo = age.split(" ");
        Log.e(TAG, age + " " + (age.contains("d/o") && Integer.parseInt(profileAgeInfo[0]) > 28));
        if ((age.contains("d/o") && Integer.parseInt(profileAgeInfo[0]) > 28) ||
                (age.contains("m/o") && Integer.parseInt(profileAgeInfo[0]) <= 11)) {
            cbUrinalysis.setVisibility(View.VISIBLE);
            cbStoolExam.setVisibility(View.VISIBLE);
            bracketId = "2";
        } else if(!age.contains("d/o") && !age.contains("m/o")){

            if (Integer.parseInt(profileAgeInfo[0]) >= 1) {
                cbUrinalysis.setVisibility(View.VISIBLE);
                cbStoolExam.setVisibility(View.VISIBLE);
                cbOralServices.setVisibility(View.VISIBLE);
                cvHealthEduc.setVisibility(View.VISIBLE);
                bracketId = "3";

                if (Integer.parseInt(profileAgeInfo[0]) >= 6 && Integer.parseInt(profileAgeInfo[0]) <= 9) {
                    cbWeight.setVisibility(View.GONE);
                    cbHeight.setVisibility(View.GONE);
                    bracketId = "4";
                }

                if (Integer.parseInt(profileAgeInfo[0]) >= 10) {
                    cvDrugRehab.setVisibility(View.VISIBLE);
                    cbBloodPressure.setVisibility(View.VISIBLE);
                    cbFastingSugar.setVisibility(View.VISIBLE);
                    cbRandomSugar.setVisibility(View.VISIBLE);
                    cbWithUnmet.setVisibility(View.VISIBLE);
                    cbWithUnmet.setVisibility(View.VISIBLE);
                    cbCounseling.setVisibility(View.VISIBLE);
                    cbCommodities.setVisibility(View.VISIBLE);
                    cbScreening.setVisibility(View.VISIBLE);
                    cbDrugCounseling.setVisibility(View.VISIBLE);
                    cbDrugTest.setVisibility(View.VISIBLE);
                    cbReferral.setVisibility(View.VISIBLE);
                    cbSputumExam.setVisibility(View.VISIBLE);
                    cvFamilyPlanning.setVisibility(View.VISIBLE);
                    bracketId = "5";
                }

                if(Integer.parseInt(profileAgeInfo[0]) >= 20 && Integer.parseInt(profileAgeInfo[0]) <= 49){
                    bracketId = "6";
                }

                if (Integer.parseInt(profileAgeInfo[0]) >= 50 && Integer.parseInt(profileAgeInfo[0]) <= 59) {
                    cbFastingSugar.setVisibility(View.GONE);
                    cbWithUnmet.setVisibility(View.GONE);
                    cbCounseling.setVisibility(View.GONE);
                    cbCommodities.setVisibility(View.GONE);
                    cvFamilyPlanning.setVisibility(View.GONE);

                    cbSugarTest.setVisibility(View.VISIBLE);
                    bracketId = "7";
                }

                if (Integer.parseInt(profileAgeInfo[0]) >= 60) {
                    cvHealthEduc.setVisibility(View.GONE);
                    bracketId = "8";
                }

            }
        }

        btnAvail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject request = new JSONObject();

                JSONObject temp;
                JSONArray services = new JSONArray();
                JSONArray diagnoses = new JSONArray();
                JSONArray option = new JSONArray();

                int optPosition;

                try {
                    if (cbBloodPressure.isChecked()){
                        services.put(new JSONObject("{ \"id\" : 1}"));

                        View radioButton = rgBloodPressure.findViewById(rgBloodPressure.getCheckedRadioButtonId());
                        optPosition = rgBloodPressure.indexOfChild(radioButton);

                        temp = new JSONObject();
                        temp.accumulate("bp", optPosition);

                        option.put(temp);
                    }
                    if (cbWeight.isChecked()){
                        services.put(new JSONObject("{ \"id\" : 2}"));

                        View radioButton = rgWeight.findViewById(rgWeight.getCheckedRadioButtonId());
                        optPosition = rgWeight.indexOfChild(radioButton);

                        temp = new JSONObject();
                        temp.accumulate("wm", optPosition);

                        option.put(temp);
                    }
                    if (cbHeight.isChecked()){
                        services.put(new JSONObject("{ \"id\" : 3}"));

                        View radioButton = rgHeight.findViewById(rgHeight.getCheckedRadioButtonId());
                        optPosition = rgHeight.indexOfChild(radioButton);

                        temp = new JSONObject();
                        temp.accumulate("hm", optPosition);

                        option.put(temp);
                    }

                    if (cbBloodTyping.isChecked()) services.put(new JSONObject("{ \"id\" : 4}"));
                    if (cbBloodCount.isChecked()) services.put(new JSONObject("{ \"id\" : 5}"));
                    if (cbUrinalysis.isChecked()) services.put(new JSONObject("{ \"id\" : 6}"));
                    if (cbFastingSugar.isChecked()) services.put(new JSONObject("{ \"id\" : 7}"));
                    if (cbStoolExam.isChecked()) services.put(new JSONObject("{ \"id\" : 8}"));
                    if (cbEyeExam.isChecked()) services.put(new JSONObject("{ \"id\" : 9}"));
                    if (cbEarExam.isChecked()) services.put(new JSONObject("{ \"id\" : 10}"));
                    if (cbPhysicalExam.isChecked()) services.put(new JSONObject("{ \"id\" : 11}"));
                    if (cbOralServices.isChecked()) services.put(new JSONObject("{ \"id\" : 12}"));
                    if (cbHealthEduc.isChecked()) services.put(new JSONObject("{ \"id\" : 13}"));
                    if (cbWithUnmet.isChecked()) services.put(new JSONObject("{ \"id\" : 15}"));
                    if (cbCounseling.isChecked()) services.put(new JSONObject("{ \"id\" : 16}"));
                    if (cbCommodities.isChecked()) services.put(new JSONObject("{ \"id\" : 17}"));
                    if (cbScreening.isChecked()) services.put(new JSONObject("{ \"id\" : 21}"));
                    if (cbDrugCounseling.isChecked()) services.put(new JSONObject("{ \"id\" : 22}"));
                    if (cbDrugTest.isChecked()) services.put(new JSONObject("{ \"id\" : 23}"));
                    if (cbReferral.isChecked()) services.put(new JSONObject("{ \"id\" : 24}"));
                    if (cbSugarTest.isChecked()) services.put(new JSONObject("{ \"id\" : 25}"));
                    if (cbSputumExam.isChecked()) services.put(new JSONObject("{ \"id\" : 26}"));
                    if (cbRandomSugar.isChecked()) services.put(new JSONObject("{ \"id\" : 27}"));

                    if (cbRespiratoryInfection.isChecked()) diagnoses.put(new JSONObject("{ \"id\" : 1}"));
                    if (cbHypertension.isChecked()) diagnoses.put(new JSONObject("{ \"id\" : 2}"));
                    if (cbFeverUnknown.isChecked()) diagnoses.put(new JSONObject("{ \"id\" : 3}"));
                    if (cbOtherInjury.isChecked()) diagnoses.put(new JSONObject("{ \"id\" : 4}"));
                    if (cbBronchitis.isChecked()) diagnoses.put(new JSONObject("{ \"id\" : 5}"));
                    if (cbDiarrheaGastroentetitis.isChecked()) diagnoses.put(new JSONObject("{ \"id\" : 6}"));
                    if (cbDermatoses.isChecked()) diagnoses.put(new JSONObject("{ \"id\" : 7}"));
                    if (cbPneumonia.isChecked()) diagnoses.put(new JSONObject("{ \"id\" : 8}"));
                    if (cbGenitourinaryInfection.isChecked()) diagnoses.put(new JSONObject("{ \"id\" : 9}"));
                    if (cbAnimalBite.isChecked()) diagnoses.put(new JSONObject("{ \"id\" : 10}"));
                    if (cbOthers.isChecked()) diagnoses.put(new JSONObject("{ \"id\" : 11}"));

                    String status = "";

                       if(!familyProfile.sex.equalsIgnoreCase("Male")) {
                        switch (rgFemaleStatus.indexOfChild(getActivity().findViewById(rgFemaleStatus.getCheckedRadioButtonId()))) {
                            case 0:
                                status = "pregnant";
                                break;
                            case 1:
                                status = "post";
                                break;
                            case 2:
                                status = "non";
                                break;
                        }
                    }

                    request.accumulate("sex", familyProfile.sex);
                    request.accumulate("profile_id", familyProfile.uniqueId);
                    request.accumulate("dateProfile", date);
                    request.accumulate("bracket_id", bracketId);
                    request.accumulate("barangay_id", familyProfile.barangayId);
                    request.accumulate("muncity_id", familyProfile.muncityId);
                    request.accumulate("status", status);
                    request.accumulate("services", services);
                    request.accumulate("diagnoses", diagnoses);
                    request.accumulate("options", option);

                    MainActivity.db.addServicesAvail(request.toString());
                    MainActivity.fm.popBackStackImmediate();
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });

        showTutorial();
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        int visibility = 0;

        visibility = (isChecked) ? View.VISIBLE : View.GONE;

        switch (id) {
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

    public void showTutorial(){
//        new FancyShowCaseView.Builder(getActivity())
//                .focusOn(view.findViewById(R.id.avail_profile_info))
//                .title("This is the profile information of the person who avails service")
//                .titleSize(20, TypedValue.COMPLEX_UNIT_DIP)
//                .showOnce("availServices")
//                .focusShape(FocusShape.ROUNDED_RECTANGLE)
//                .roundRectRadius(15)
//                .dismissListener(new DismissListener() {
//                    @Override
//                    public void onDismiss(String id) {
//                        new FancyShowCaseView.Builder(getActivity())
//                                .focusOn(view.findViewById(R.id.avail_services))
//                                .title("This shows available services inline with the person's age")
//                                .titleSize(20, TypedValue.COMPLEX_UNIT_DIP)
//                                .focusShape(FocusShape.ROUNDED_RECTANGLE)
//                                .titleGravity(Gravity.TOP)
//                                .roundRectRadius(15)
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
        MainActivity.queue.add(MainActivity.makeSpotlightView(view.findViewById(R.id.avail_profile_info),
                "Mate!",
                "This is the profile information of the person who avails service",
                "AvailServiceInfo"));

        MainActivity.queue.add(MainActivity.makeSpotlightView(view.findViewById(R.id.avail_services),
                "You there",
                "This shows available services inline with the person's age",
                "AvailServices"));

        MainActivity.startSequence();
    }
}
