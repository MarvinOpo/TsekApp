package com.example.mvopo.tsekapp.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvopo.tsekapp.Helper.CheckBoxGroup;
import com.example.mvopo.tsekapp.Helper.JSONApi;
import com.example.mvopo.tsekapp.MainActivity;
import com.example.mvopo.tsekapp.Model.Constants;
import com.example.mvopo.tsekapp.Model.FamilyProfile;
import com.example.mvopo.tsekapp.R;
import com.wdullaer.materialdatetimepicker.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class DengvaxiaFormFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    String TAG = "DengvaxiaFormFragment";

    FamilyProfile familyProfile;

    View view;
    Rect offsetViewBounds = new Rect();

    ScrollView mainContainer;
    LinearLayout formParentLayout, phicLayout, phicMoreFormLayout, asthmaLayout,
            hospitalLayout, surgicalLayout, vaccineLayout, physicalExamLayout,
            genInfoLayout, vaccineFemaleOpt;

    CheckBoxGroup familyLayout, medicalLayout, tuberculosisLayout,
            disabilityLayout, personalLayout, mensGyneLayout, vaccineOptionLayout,
            reviewLayout, physicalExamOptionLayout, proceduresLayout;

    CardView cvMedAsthma, cvMedTuberculosis, cvMensGyne;

    TextView phicToggle, familyToggle, medicalToggle,
            asthmaToggle, tuberculosisToggle, disabilityToggle,
            hospitalToggle, surgicalToggle, personalToggle,
            mensGyneToggle, vaccineToggle, reviewToggle,
            physicalExamToggle, proceduresToggle, genInfoToggle;

    EditText edtxPhicStatus, edtxPhicType, edtxPhicTypeOpt, edtxPhicBenefit,
            edtxDisabilityWithAssistive, edtxDisabilityNeedAssistive, edtxDisabilityDescription,
            edtxDisabilityMedication, edtxHospitalReason, edtxHospitalDate, edtxHospitalPlace,
            edtxHospitalPhicUsed, edtxHospitalCostNotCovered, edtxSurgicalOperation, edtxSurgicalDate,
            edtxPersonalSmoking, edtxPersonalSmokingStarted, edtxPersonalSmokingSticks, edtxPersonalSmokingQuit,
            edtxPersonalFatIntake, edtxPersonalVege, edtxPersonalFruit, edtxPersonalActivity,
            edtxPersonalTriedAlcohol, edtxPersonalDrunk, edtxPersonalDrugs, edtxMensAge, edtxMensLastPeriod,
            edtxMensPads, edtxMensDuration, edtxMensInterval, edtxVaccineDose, edtxVaccineDate, edtxVaccinePlace,
            edtxVaccineSulfate, edtxVaccineCapsule, edtxVaccineDeworm, edtxPhysicalStatus, edtxPhysicalBP, edtxPhysicalHR,
            edtxPhysicalRR, edtxPhysicalTemp, edtxPhysicalBlood, edtxPhysicalWeight, edtxPhysicalHeight, edtxPhysicalBMI,
            edtxPhysicalWaist, edtxPhysicalHip, edtxPhysicalRatio, edtxPhysicalSkin, edtxAsthmaDiagnosed, edtxAsthmaAttacks,
            edtxAsthmaMed, edtxTuberculosisDiagnose, edtxGenInfoContact, edtxGenInfoReligion, edtxGenInfoBirthPlace, edtxGenInfoYrs;

    CheckBox cbFamAllergy, cbFamCancer, cbFamIDD, cbFamEpilepsy, cbFamHeart,
            cbFamKidney, cbFamOthers, cbMedAllergy, cbMedEpilepsy, cbMedKidney, cbMedIDD, cbMedHepa,
            cbMedHeart, cbMedPoisoning, cbMedSTI, cbMedThyroid, cbMedCancer, cbMedOthers,
            cbMedAsthma, cbMedTuberculosis, cbMensGynesOthers, cbPhysHeentOthers, cbPhysChestOthers,
            cbPhysHeartOthers, cbPhysAbdomenOthers, cbPhysExtemitiesOthers, cbTuberculosisPPD,
            cbTuberculosisSputum, cbTuberculosisCXR, cbTuberculosisGenXpert, cbProceduresDiagnostic, cbInjuryOthers, cbVaccineOthers;

    Button btnRegister, btnAddHospital, btnAddSurgical, dialogBtnAddHospital,
            dialogBtnAddSurgical, btnAddVaccine, dialogBtnAddVaccine;

    Calendar now = Calendar.getInstance();
    DatePickerDialog dpd = DatePickerDialog.newInstance(
            DengvaxiaFormFragment.this,
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
    );

    JSONArray hospitalHistory = new JSONArray();
    JSONArray surgicalHistory = new JSONArray();
    JSONArray vaccineHistory = new JSONArray();

    View.OnClickListener fieldListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int resId = -1;

            switch (view.getId()) {
                case R.id.phic_status:
                    resId = R.array.phic_status;
                    break;
                case R.id.phic_type:
                    resId = R.array.phic_type;
                    break;
                case R.id.phic_type_opt:
                    String typeOpt = edtxPhicType.getText().toString();
                    if (typeOpt.equalsIgnoreCase("Sponsored"))
                        resId = R.array.phic_sponsored;
                    else if (typeOpt.equalsIgnoreCase("Employed"))
                        resId = R.array.phic_employment;

                    break;
                case R.id.phic_benefit:
                case R.id.disability_with_assistive:
                case R.id.disability_need_assistive:
                case R.id.hospital_history_phic:
                case R.id.personal_high_fat:
                case R.id.personal_vegetable:
                case R.id.personal_fruit:
                case R.id.personal_activity:
                case R.id.personal_tried_alcohol:
                case R.id.personal_drunk_alcohol:
                case R.id.personal_drugs:
                case R.id.bronchial_diagnosed:
                case R.id.bronchial_medication:
                case R.id.tuberculosis_diagnose:
                case R.id.vaccine_ferrous_sulfate:
                case R.id.vaccine_oil_capsule:
                case R.id.vaccine_deworm:
                    resId = R.array.yes_no;
                    break;
                case R.id.physical_gen_status:
                    resId = R.array.physical_status;
                    break;
                case R.id.menstrual_date_last_mens:
                    dpd.show(getActivity().getFragmentManager(), "LastPeriodDate");
                    break;
                case R.id.personal_smoking:
                    resId = R.array.personal_smoking;
                    break;
                case R.id.dialog_vaccine_place:
                    resId = R.array.vaccine_received;
                    break;
                case R.id.physical_skin:
                    resId = R.array.physical_skin;
                    break;
                case R.id.gen_info_religion:
                    resId = R.array.religion;
                    break;
            }

            if (resId != -1) showOptionDialog(resId, (EditText) view);
        }
    };

    public View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int resId = -1;

            switch (view.getId()) {
                case R.id.hospital_history_btn_add:
                    resId = R.layout.hospital_history_dialog;
                    break;
                case R.id.surgical_history_btn_add:
                    resId = R.layout.past_surgical_dialog;
                    break;
                case R.id.vaccine_dengvaxia_btn:
                    resId = R.layout.vaccination_history_dialog;
                    break;
            }

            showFormDialog(resId);
        }
    };

    CompoundButton.OnCheckedChangeListener cbListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {
                if (compoundButton.getId() == R.id.procedures_diagnostic_dengue) {
                    showOptionDialog(R.array.procedure_diagnostic_result, compoundButton);
                    return;
                }

                showSpecifyDialog(compoundButton);
            } else {
                String text = compoundButton.getText().toString();
                if (text.contains(" - ")) {
                    compoundButton.setText(text.split(" - ")[0]);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        familyProfile = getArguments().getParcelable("familyProfile");

        view = inflater.inflate(R.layout.fragment_dengvaxia_form, container, false);

        mainContainer = view.findViewById(R.id.main_container);
        btnRegister = view.findViewById(R.id.register_dengvaxia_button);

        dpd.setMaxDate(now);

        initToggles();
        initLayouts();

        initFields();

        initCheckBox();

        initButtons();

        btnRegister.setOnClickListener(this);
        return view;
    }

    public void initToggles() {
        phicToggle = view.findViewById(R.id.phic_toggle);
        familyToggle = view.findViewById(R.id.family_toggle);
        medicalToggle = view.findViewById(R.id.medical_toggle);
        asthmaToggle = view.findViewById(R.id.asthma_toggle);
        tuberculosisToggle = view.findViewById(R.id.tuberculosis_toggle);
        disabilityToggle = view.findViewById(R.id.disability_toggle);
        hospitalToggle = view.findViewById(R.id.hospital_toggle);
        surgicalToggle = view.findViewById(R.id.surgical_toggle);
        personalToggle = view.findViewById(R.id.personal_toggle);
        mensGyneToggle = view.findViewById(R.id.mens_gyne_toggle);
        vaccineToggle = view.findViewById(R.id.vaccine_toggle);
        reviewToggle = view.findViewById(R.id.review_toggle);
        physicalExamToggle = view.findViewById(R.id.physical_exam_toggle);
        proceduresToggle = view.findViewById(R.id.procedures_toggle);
        genInfoToggle = view.findViewById(R.id.gen_info_toggle);

        phicToggle.setOnClickListener(this);
        familyToggle.setOnClickListener(this);
        medicalToggle.setOnClickListener(this);
        asthmaToggle.setOnClickListener(this);
        tuberculosisToggle.setOnClickListener(this);
        disabilityToggle.setOnClickListener(this);
        hospitalToggle.setOnClickListener(this);
        surgicalToggle.setOnClickListener(this);
        personalToggle.setOnClickListener(this);
        mensGyneToggle.setOnClickListener(this);
        vaccineToggle.setOnClickListener(this);
        reviewToggle.setOnClickListener(this);
        physicalExamToggle.setOnClickListener(this);
        proceduresToggle.setOnClickListener(this);
        genInfoToggle.setOnClickListener(this);
    }

    public void initLayouts() {
        formParentLayout = view.findViewById(R.id.form_parent_layout);
        phicLayout = view.findViewById(R.id.phic_container);
        phicMoreFormLayout = view.findViewById(R.id.phic_more_form);
        familyLayout = view.findViewById(R.id.family_container);
        medicalLayout = view.findViewById(R.id.medical_container);
        asthmaLayout = view.findViewById(R.id.asthma_container);
        tuberculosisLayout = view.findViewById(R.id.tuberculosis_container);
        disabilityLayout = view.findViewById(R.id.disability_container);
        hospitalLayout = view.findViewById(R.id.hospital_container);
        surgicalLayout = view.findViewById(R.id.surgical_container);
        personalLayout = view.findViewById(R.id.personal_container);
        mensGyneLayout = view.findViewById(R.id.mens_gyne_container);
        vaccineLayout = view.findViewById(R.id.vaccine_container);
        vaccineOptionLayout = view.findViewById(R.id.vaccine_options);
        reviewLayout = view.findViewById(R.id.review_container);
        physicalExamLayout = view.findViewById(R.id.physical_exam_container);
        physicalExamOptionLayout = view.findViewById(R.id.physical_exam_options);
        proceduresLayout = view.findViewById(R.id.procedures_container);
        genInfoLayout = view.findViewById(R.id.gen_info_container);
        vaccineFemaleOpt = view.findViewById(R.id.vaccine_female_opt_layout);

        cvMedAsthma = view.findViewById(R.id.medical_bronchial_section);
        cvMedTuberculosis = view.findViewById(R.id.medical_tuberculosis_section);
        cvMensGyne = view.findViewById(R.id.mens_gyne_section);

        if (familyProfile.sex.equalsIgnoreCase("Female")) {
            cvMensGyne.setVisibility(View.VISIBLE);
            vaccineFemaleOpt.setVisibility(View.VISIBLE);
        }
    }

    public void initFields() {
        edtxPhicStatus = view.findViewById(R.id.phic_status);
        edtxPhicType = view.findViewById(R.id.phic_type);
        edtxPhicTypeOpt = view.findViewById(R.id.phic_type_opt);
        edtxPhicBenefit = view.findViewById(R.id.phic_benefit);

        edtxDisabilityWithAssistive = view.findViewById(R.id.disability_with_assistive);
        edtxDisabilityNeedAssistive = view.findViewById(R.id.disability_need_assistive);
        edtxDisabilityDescription = view.findViewById(R.id.disability_description);
        edtxDisabilityMedication = view.findViewById(R.id.disability_medication_taken);

        edtxPersonalSmoking = view.findViewById(R.id.personal_smoking);
        edtxPersonalSmokingStarted = view.findViewById(R.id.smoking_age_started);
        edtxPersonalSmokingQuit = view.findViewById(R.id.smoking_age_quit);
        edtxPersonalSmokingSticks = view.findViewById(R.id.smoking_sticks_day);
        edtxPersonalFatIntake = view.findViewById(R.id.personal_high_fat);
        edtxPersonalVege = view.findViewById(R.id.personal_vegetable);
        edtxPersonalFruit = view.findViewById(R.id.personal_fruit);
        edtxPersonalActivity = view.findViewById(R.id.personal_activity);
        edtxPersonalTriedAlcohol = view.findViewById(R.id.personal_tried_alcohol);
        edtxPersonalDrunk = view.findViewById(R.id.personal_drunk_alcohol);
        edtxPersonalDrugs = view.findViewById(R.id.personal_drugs);

        edtxMensAge = view.findViewById(R.id.menstrual_age);
        edtxMensLastPeriod = view.findViewById(R.id.menstrual_date_last_mens);
        edtxMensPads = view.findViewById(R.id.menstrual_pads);
        edtxMensDuration = view.findViewById(R.id.menstrual_duration);
        edtxMensInterval = view.findViewById(R.id.menstrual_interval);

        Constants.setDateTextWatcher(getContext(), edtxMensLastPeriod);

        edtxVaccineDose = view.findViewById(R.id.vaccine_doses);
        edtxVaccineSulfate = view.findViewById(R.id.vaccine_ferrous_sulfate);
        edtxVaccineCapsule = view.findViewById(R.id.vaccine_oil_capsule);
        edtxVaccineDeworm = view.findViewById(R.id.vaccine_deworm);

        edtxPhysicalStatus = view.findViewById(R.id.physical_gen_status);
        edtxPhysicalBP = view.findViewById(R.id.physical_bp);
        edtxPhysicalHR = view.findViewById(R.id.physical_hr);
        edtxPhysicalRR = view.findViewById(R.id.physical_rr);
        edtxPhysicalTemp = view.findViewById(R.id.physical_temp);
        edtxPhysicalBlood = view.findViewById(R.id.physical_blood_type);
        edtxPhysicalWeight = view.findViewById(R.id.physical_weight);
        edtxPhysicalHeight = view.findViewById(R.id.physical_height);
        edtxPhysicalBMI = view.findViewById(R.id.physical_bmi);
        edtxPhysicalWaist = view.findViewById(R.id.physical_waist);
        edtxPhysicalHip = view.findViewById(R.id.physical_hip);
        edtxPhysicalRatio = view.findViewById(R.id.physical_wh_ratio);
        edtxPhysicalSkin = view.findViewById(R.id.physical_skin);

        edtxAsthmaDiagnosed = view.findViewById(R.id.bronchial_diagnosed);
        edtxAsthmaAttacks = view.findViewById(R.id.bronchial_attacks);
        edtxAsthmaMed = view.findViewById(R.id.bronchial_medication);

        edtxTuberculosisDiagnose = view.findViewById(R.id.tuberculosis_diagnose);

        edtxGenInfoContact = view.findViewById(R.id.gen_info_contact);
        edtxGenInfoReligion = view.findViewById(R.id.gen_info_religion);
        edtxGenInfoBirthPlace = view.findViewById(R.id.gen_info_birthplace);
        edtxGenInfoYrs = view.findViewById(R.id.gen_info_yrs_address);

        addFieldListener();
    }

    public void addFieldListener() {
        edtxPhicStatus.setOnClickListener(fieldListener);
        edtxPhicType.setOnClickListener(fieldListener);
        edtxPhicTypeOpt.setOnClickListener(fieldListener);
        edtxPhicBenefit.setOnClickListener(fieldListener);

        edtxDisabilityWithAssistive.setOnClickListener(fieldListener);
        edtxDisabilityNeedAssistive.setOnClickListener(fieldListener);

        edtxPhysicalStatus.setOnClickListener(fieldListener);

        edtxPersonalSmoking.setOnClickListener(fieldListener);
        edtxPersonalFatIntake.setOnClickListener(fieldListener);
        edtxPersonalVege.setOnClickListener(fieldListener);
        edtxPersonalFruit.setOnClickListener(fieldListener);
        edtxPersonalActivity.setOnClickListener(fieldListener);
        edtxPersonalTriedAlcohol.setOnClickListener(fieldListener);
        edtxPersonalDrunk.setOnClickListener(fieldListener);
        edtxPersonalDrugs.setOnClickListener(fieldListener);

        edtxMensLastPeriod.setOnClickListener(fieldListener);

        edtxVaccineSulfate.setOnClickListener(fieldListener);
        edtxVaccineCapsule.setOnClickListener(fieldListener);
        edtxVaccineDeworm.setOnClickListener(fieldListener);

        edtxPhysicalSkin.setOnClickListener(fieldListener);

        edtxAsthmaDiagnosed.setOnClickListener(fieldListener);
        edtxAsthmaMed.setOnClickListener(fieldListener);

        edtxTuberculosisDiagnose.setOnClickListener(fieldListener);

        edtxGenInfoReligion.setOnClickListener(fieldListener);
    }

    public void initCheckBox() {
        cbFamAllergy = view.findViewById(R.id.family_allergy);
        cbFamCancer = view.findViewById(R.id.family_cancer);
        cbFamIDD = view.findViewById(R.id.family_immune);
        cbFamEpilepsy = view.findViewById(R.id.family_epilepsy_seizure);
        cbFamHeart = view.findViewById(R.id.family_heart);
        cbFamKidney = view.findViewById(R.id.family_kidney);
        cbFamOthers = view.findViewById(R.id.family_others);

        cbMedAllergy = view.findViewById(R.id.medical_allergy);
        cbMedEpilepsy = view.findViewById(R.id.medical_epilepsy_seizure);
        cbMedKidney = view.findViewById(R.id.medical_kidney);
        cbMedIDD = view.findViewById(R.id.medical_immune);
        cbMedHepa = view.findViewById(R.id.medical_hepatitis);
        cbMedHeart = view.findViewById(R.id.medical_heart);
        cbMedPoisoning = view.findViewById(R.id.medical_poisoning);
        cbMedSTI = view.findViewById(R.id.medical_sti);
        cbMedThyroid = view.findViewById(R.id.medical_thyroid);
        cbMedCancer = view.findViewById(R.id.medical_cancer);
        cbMedOthers = view.findViewById(R.id.medical_others);

        cbMedAsthma = view.findViewById(R.id.medical_asthma);
        cbMedTuberculosis = view.findViewById(R.id.medical_tuberculosis);

        cbMensGynesOthers = view.findViewById(R.id.gyne_others);

        cbPhysHeentOthers = view.findViewById(R.id.heent_others);
        cbPhysChestOthers = view.findViewById(R.id.chest_lungs_others);
        cbPhysHeartOthers = view.findViewById(R.id.heart_others);
        cbPhysAbdomenOthers = view.findViewById(R.id.abdomen_others);
        cbPhysExtemitiesOthers = view.findViewById(R.id.extremities_others);

        cbTuberculosisPPD = view.findViewById(R.id.tuberculosis_ppd);
        cbTuberculosisSputum = view.findViewById(R.id.tuberculosis_sputum);
        cbTuberculosisCXR = view.findViewById(R.id.tuberculosis_cxr);
        cbTuberculosisGenXpert = view.findViewById(R.id.tuberculosis_genxpert);

        cbProceduresDiagnostic = view.findViewById(R.id.procedures_diagnostic_dengue);

        cbInjuryOthers = view.findViewById(R.id.injury_others);

        cbVaccineOthers = view.findViewById(R.id.vaccine_others);

        addCheckBoxListener();
    }

    public void addCheckBoxListener() {
        cbFamAllergy.setOnCheckedChangeListener(cbListener);
        cbFamCancer.setOnCheckedChangeListener(cbListener);
        cbFamIDD.setOnCheckedChangeListener(cbListener);
        cbFamEpilepsy.setOnCheckedChangeListener(cbListener);
        cbFamHeart.setOnCheckedChangeListener(cbListener);
        cbFamKidney.setOnCheckedChangeListener(cbListener);
        cbFamOthers.setOnCheckedChangeListener(cbListener);

        cbMedAllergy.setOnCheckedChangeListener(cbListener);
        cbMedEpilepsy.setOnCheckedChangeListener(cbListener);
        cbMedKidney.setOnCheckedChangeListener(cbListener);
        cbMedIDD.setOnCheckedChangeListener(cbListener);
        cbMedHepa.setOnCheckedChangeListener(cbListener);
        cbMedHeart.setOnCheckedChangeListener(cbListener);
        cbMedPoisoning.setOnCheckedChangeListener(cbListener);
        cbMedSTI.setOnCheckedChangeListener(cbListener);
        cbMedThyroid.setOnCheckedChangeListener(cbListener);
        cbMedCancer.setOnCheckedChangeListener(cbListener);
        cbMedOthers.setOnCheckedChangeListener(cbListener);

        cbMedAsthma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getContext(), "Bronchial Asthma Section Added.", Toast.LENGTH_SHORT).show();
                    cvMedAsthma.setVisibility(View.VISIBLE);
                } else cvMedAsthma.setVisibility(View.GONE);
            }
        });

        cbMedTuberculosis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getContext(), "Tuberculosis Section Added.", Toast.LENGTH_SHORT).show();
                    cvMedTuberculosis.setVisibility(View.VISIBLE);
                } else cvMedTuberculosis.setVisibility(View.GONE);
            }
        });

        cbMensGynesOthers.setOnCheckedChangeListener(cbListener);

        cbPhysHeentOthers.setOnCheckedChangeListener(cbListener);
        cbPhysChestOthers.setOnCheckedChangeListener(cbListener);
        cbPhysHeartOthers.setOnCheckedChangeListener(cbListener);
        cbPhysAbdomenOthers.setOnCheckedChangeListener(cbListener);
        cbPhysExtemitiesOthers.setOnCheckedChangeListener(cbListener);

        cbTuberculosisPPD.setOnCheckedChangeListener(cbListener);
        cbTuberculosisSputum.setOnCheckedChangeListener(cbListener);
        cbTuberculosisCXR.setOnCheckedChangeListener(cbListener);
        cbTuberculosisGenXpert.setOnCheckedChangeListener(cbListener);

        cbProceduresDiagnostic.setOnCheckedChangeListener(cbListener);

        cbInjuryOthers.setOnCheckedChangeListener(cbListener);

        cbVaccineOthers.setOnCheckedChangeListener(cbListener);
    }

    public void initButtons() {
        btnAddHospital = view.findViewById(R.id.hospital_history_btn_add);
        btnAddSurgical = view.findViewById(R.id.surgical_history_btn_add);

        btnAddVaccine = view.findViewById(R.id.vaccine_dengvaxia_btn);

        addButtonListener();
    }

    public void addButtonListener() {
        btnAddHospital.setOnClickListener(buttonListener);
        btnAddSurgical.setOnClickListener(buttonListener);

        btnAddVaccine.setOnClickListener(buttonListener);
    }

    @Override
    public void onClick(final View view) {
        hideLayouts();
        View contentView = null;

        switch (view.getId()) {
            case R.id.register_dengvaxia_button:
                try {
                    MainActivity.pd = ProgressDialog.show(getContext(), "Loading", "Please wait...", false, false);

                    JSONObject request = getGeneralInfo();
                    request.accumulate("phic_membership", getPhicSelectedOptions());
                    request.accumulate("family_history", familyLayout.getSelectedAsJSONArray());
                    request.accumulate("medical_history", medicalLayout.getSelectedAsJSONArray());
                    request.accumulate("disability_injury", getDisabilitySelectedOptions());
                    request.accumulate("hospital_history", hospitalHistory);
                    request.accumulate("surgical_history", surgicalHistory);
                    request.accumulate("personal_history", getPersonalHistory());
                    request.accumulate("mens_gyne_history", getMensGyneHistory());
                    request.accumulate("vaccine_history", getVaccineHistory());
                    request.accumulate("review_systems", reviewLayout.getSelectedAsJSONArray());
                    request.accumulate("physical_exam", getPhysicalExamData());
                    request.accumulate("bronchial_asthma", getBronchialAsthma());
                    request.accumulate("tuberculosis", getTuberculosis());
                    request.accumulate("other_procedures", proceduresLayout.getSelectedAsJSONArray());

                    JSONApi.getInstance(getContext()).registerToDengvaxia(Constants.dengvaxiaRegUrl, request);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.phic_toggle:
                contentView = phicLayout;
                break;
            case R.id.family_toggle:
                contentView = familyLayout;
                break;
            case R.id.medical_toggle:
                contentView = medicalLayout;
                break;
            case R.id.asthma_toggle:
                contentView = asthmaLayout;
                break;
            case R.id.tuberculosis_toggle:
                contentView = tuberculosisLayout;
                break;
            case R.id.disability_toggle:
                contentView = disabilityLayout;
                break;
            case R.id.hospital_toggle:
                contentView = hospitalLayout;
                break;
            case R.id.surgical_toggle:
                contentView = surgicalLayout;
                break;
            case R.id.personal_toggle:
                contentView = personalLayout;
                break;
            case R.id.mens_gyne_toggle:
                contentView = mensGyneLayout;
                break;
            case R.id.vaccine_toggle:
                contentView = vaccineLayout;
                break;
            case R.id.review_toggle:
                contentView = reviewLayout;
                break;
            case R.id.physical_exam_toggle:
                contentView = physicalExamLayout;
                break;
            case R.id.procedures_toggle:
                contentView = proceduresLayout;
                break;
            case R.id.gen_info_toggle:
                contentView = genInfoLayout;
        }

        if (view.getId() != R.id.register_dengvaxia_button) {
            contentView.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.getDrawingRect(offsetViewBounds);
                    formParentLayout.offsetDescendantRectToMyCoords(view, offsetViewBounds);
                    int relativeTop = offsetViewBounds.top;

                    mainContainer.smoothScrollTo(0, relativeTop);
                }
            }, 200);
        }
    }

    public void hideLayouts() {
        phicLayout.setVisibility(View.GONE);
        familyLayout.setVisibility(View.GONE);
        medicalLayout.setVisibility(View.GONE);
        asthmaLayout.setVisibility(View.GONE);
        tuberculosisLayout.setVisibility(View.GONE);
        disabilityLayout.setVisibility(View.GONE);
        hospitalLayout.setVisibility(View.GONE);
        surgicalLayout.setVisibility(View.GONE);
        personalLayout.setVisibility(View.GONE);
        mensGyneLayout.setVisibility(View.GONE);
        vaccineLayout.setVisibility(View.GONE);
        reviewLayout.setVisibility(View.GONE);
        physicalExamLayout.setVisibility(View.GONE);
        proceduresLayout.setVisibility(View.GONE);
        genInfoLayout.setVisibility(View.GONE);
    }

    public JSONObject getGeneralInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("unique_id", familyProfile.uniqueId);
            jsonObject.accumulate("familyID", familyProfile.familyId);
            jsonObject.accumulate("phicID", familyProfile.philId);
            jsonObject.accumulate("nhtsID", familyProfile.nhtsId);
            jsonObject.accumulate("head", familyProfile.isHead);
            jsonObject.accumulate("relation", familyProfile.relation);
            jsonObject.accumulate("fname", familyProfile.fname);
            jsonObject.accumulate("mname", familyProfile.mname);
            jsonObject.accumulate("lname", familyProfile.lname);
            jsonObject.accumulate("suffix", familyProfile.suffix);
            jsonObject.accumulate("sex", familyProfile.sex);
            jsonObject.accumulate("dob", familyProfile.dob);
            jsonObject.accumulate("barangay_id", familyProfile.barangayId);
            jsonObject.accumulate("muncity_id", familyProfile.muncityId);
            jsonObject.accumulate("province_id", familyProfile.provinceId);
            jsonObject.accumulate("income", familyProfile.income);
            jsonObject.accumulate("unmet", familyProfile.unmetNeed);
            jsonObject.accumulate("water", familyProfile.waterSupply);
            jsonObject.accumulate("user_id", MainActivity.user.id);

            String toilet = familyProfile.sanitaryToilet;

            if (!toilet.isEmpty()) {
                if (toilet.equals("1")) toilet = "non";
                else if (toilet.equals("2")) toilet = "comm";
                else if (toilet.equals("3")) toilet = "indi";
            }

            jsonObject.accumulate("toilet", toilet);
            jsonObject.accumulate("education", familyProfile.educationalAttainment);

            jsonObject.accumulate("contact", edtxGenInfoContact.getText().toString().trim());
            jsonObject.accumulate("religion", edtxGenInfoReligion.getText().toString().trim());
            jsonObject.accumulate("birth_place", edtxGenInfoBirthPlace.getText().toString().trim());
            jsonObject.accumulate("yrs_address", edtxGenInfoYrs.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public JSONObject getPersonalHistory() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("tried_smoking", edtxPersonalSmoking.getText().toString().trim());
            jsonObject.accumulate("smoking_age_started", edtxPersonalSmokingStarted.getText().toString().trim());
            jsonObject.accumulate("smoking_age_quit", edtxPersonalSmokingQuit.getText().toString().trim());

            String sticks = edtxPersonalSmokingSticks.getText().toString().trim();

            int packs = 0;
            if (!sticks.isEmpty()) packs = Integer.parseInt(sticks) * 365 / 20;

            jsonObject.accumulate("smoking_no_sticks", sticks);
            jsonObject.accumulate("smoking_no_packs", String.valueOf(packs));
            jsonObject.accumulate("fat_salt_intake", edtxPersonalFatIntake.getText().toString().trim());
            jsonObject.accumulate("daily_vegetable", edtxPersonalVege.getText().toString().trim());
            jsonObject.accumulate("daily_fruit", edtxPersonalFruit.getText().toString().trim());
            jsonObject.accumulate("physical_activity", edtxPersonalActivity.getText().toString().trim());
            jsonObject.accumulate("tried_alcohol", edtxPersonalTriedAlcohol.getText().toString().trim());
            jsonObject.accumulate("drunk_in_5mos", edtxPersonalDrunk.getText().toString().trim());
            jsonObject.accumulate("tried_drugs", edtxPersonalDrugs.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public JSONObject getPhicSelectedOptions() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("status", edtxPhicStatus.getText().toString().trim());
            jsonObject.accumulate("type", edtxPhicType.getText().toString().trim());
            jsonObject.accumulate("type_opt", edtxPhicTypeOpt.getText().toString().trim());
            jsonObject.accumulate("benefit", edtxPhicBenefit.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    public JSONObject getDisabilitySelectedOptions() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("with_assistive", edtxDisabilityWithAssistive.getText().toString().trim());
            jsonObject.accumulate("need_assistive", edtxDisabilityNeedAssistive.getText().toString().trim());
            jsonObject.accumulate("description", edtxDisabilityDescription.getText().toString().trim());
            jsonObject.accumulate("medication", edtxDisabilityMedication.getText().toString().trim());
            jsonObject.accumulate("selected_options", disabilityLayout.getSelectedAsJSONArray());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public JSONObject getMensGyneHistory() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("age_menarche", edtxMensAge.getText().toString().trim());
            jsonObject.accumulate("last_period", edtxMensLastPeriod.getText().toString().trim());
            jsonObject.accumulate("no_pads", edtxMensPads.getText().toString().trim());
            jsonObject.accumulate("duration", edtxMensDuration.getText().toString().trim());
            jsonObject.accumulate("interval", edtxMensInterval.getText().toString().trim());
            jsonObject.accumulate("selected_options", mensGyneLayout.getSelectedAsJSONArray());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public JSONObject getVaccineHistory() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("vaccine_received", vaccineOptionLayout.getSelectedAsJSONArray());
            jsonObject.accumulate("no_dose", edtxVaccineDose.getText().toString().trim());
            jsonObject.accumulate("given_sulfate", edtxVaccineSulfate.getText().toString().trim());
            jsonObject.accumulate("given_oil_capsule", edtxVaccineCapsule.getText().toString().trim());
            jsonObject.accumulate("dewormed", edtxVaccineDeworm.getText().toString().trim());
            jsonObject.accumulate("dengvaxia_history", vaccineHistory);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public JSONObject getPhysicalExamData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("status", edtxPhysicalStatus.getText().toString().trim());
            jsonObject.accumulate("bp", edtxPhysicalBP.getText().toString().trim());
            jsonObject.accumulate("hr", edtxPhysicalHR.getText().toString().trim());
            jsonObject.accumulate("rr", edtxPhysicalRR.getText().toString().trim());
            jsonObject.accumulate("temp", edtxPhysicalTemp.getText().toString().trim());
            jsonObject.accumulate("blood_type", edtxPhysicalBlood.getText().toString().trim());
            jsonObject.accumulate("weight", edtxPhysicalWeight.getText().toString().trim());
            jsonObject.accumulate("height", edtxPhysicalHeight.getText().toString().trim());
            jsonObject.accumulate("waist", edtxPhysicalWaist.getText().toString().trim());
            jsonObject.accumulate("hip", edtxPhysicalHip.getText().toString().trim());
            jsonObject.accumulate("ratio", edtxPhysicalRatio.getText().toString().trim());
            jsonObject.accumulate("skin", edtxPhysicalSkin.getText().toString().trim());
            jsonObject.accumulate("selected_options", physicalExamOptionLayout.getSelectedAsJSONObjectWithTags());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public JSONObject getBronchialAsthma() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("diagnosed", edtxAsthmaDiagnosed.getText().toString().trim());
            jsonObject.accumulate("no_attacks", edtxAsthmaAttacks.getText().toString().trim());
            jsonObject.accumulate("with_medication", edtxAsthmaMed.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public JSONObject getTuberculosis() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("diagnosed", edtxTuberculosisDiagnose.getText().toString().trim());
            jsonObject.accumulate("selected_options", tuberculosisLayout.getSelectedAsJSONObjectWithTags());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public void showOptionDialog(final int id, final View editableView) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.options_dialog, null);
        ScrollView optionHolder = view.findViewById(R.id.option_holder);
        Button optionBtn = view.findViewById(R.id.optionBtn);

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);

        String[] labels = getResources().getStringArray(id);

        final RadioGroup radioGroup = new RadioGroup(getContext());

        LinearLayout.LayoutParams rbParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rbParam.bottomMargin = 5;
        rbParam.topMargin = 5;

        for (int i = 0; i < labels.length; i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setLayoutParams(rbParam);
            radioButton.setText(labels[i]);

            View lineView = new View(getContext());
            lineView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            lineView.setLayoutParams(params);

            radioGroup.addView(radioButton);
            radioGroup.addView(lineView);
        }

        optionHolder.addView(radioGroup);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        final AlertDialog optionDialog = builder.create();
        optionDialog.show();

        optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());


                if (editableView instanceof EditText) {
                    EditText edtxView = (EditText) editableView;

                    if (radioButton != null) {
                        String text = radioButton.getText().toString().trim();
                        Boolean shouldSetText = true;


                        if (text.contains("Others")) {
                            showSpecifyDialog(edtxView);
                            optionDialog.dismiss();
                            return;
                        }

                        switch (edtxView.getId()) {
                            case R.id.phic_status:
                                changeContentVisibility(edtxView);
                                break;
                            case R.id.phic_type:
                                edtxPhicTypeOpt.setText("");
                                TextInputLayout optContainer = (TextInputLayout) edtxPhicTypeOpt.getParentForAccessibility();
                                if (!text.equalsIgnoreCase("Lifetime")) {
                                    optContainer.setHint(edtxView.getText().toString() + " by");
                                    optContainer.setVisibility(View.VISIBLE);
                                } else {
                                    optContainer.setVisibility(View.GONE);
                                }

                                break;
                            case R.id.physical_skin:
                                if (text.contains("Lesions"))
                                    showSpecifyDialog(edtxView);
                                break;
                            case R.id.phic_benefit:
                            case R.id.disability_with_assistive:
                            case R.id.disability_need_assistive:
                            case R.id.personal_drugs:
                            case R.id.bronchial_medication:
                            case R.id.tuberculosis_diagnose:
                                if (text.equalsIgnoreCase("Yes")) showSpecifyDialog(edtxView);
                                break;
                            case R.id.personal_tried_alcohol:
                                if (text.equalsIgnoreCase("No"))
                                    edtxPersonalDrunk.setText(text);
                                break;
                            case R.id.vaccine_ferrous_sulfate:
                                if (text.equalsIgnoreCase("Yes")) {
                                    shouldSetText = false;
                                    dpd.show(getActivity().getFragmentManager(), "VaccineSulfate");
                                }
                                break;
                            case R.id.vaccine_oil_capsule:
                                if (text.equalsIgnoreCase("Yes")) {
                                    shouldSetText = false;
                                    dpd.show(getActivity().getFragmentManager(), "VaccineCapsule");
                                }
                                break;
                            case R.id.vaccine_deworm:
                                if (text.equalsIgnoreCase("Yes")) {
                                    shouldSetText = false;
                                    dpd.show(getActivity().getFragmentManager(), "VaccineDeworm");
                                }
                                break;
                        }

                        if (shouldSetText) edtxView.setText(text);
                    }else{
                        edtxView.setText("");
                    }
                } else if (editableView instanceof CompoundButton) {
                    CompoundButton compoundButton = (CompoundButton) editableView;

                    if (radioButton != null) {
                        String text = radioButton.getText().toString().trim();
                        compoundButton.append(" - " + text);
                    }else{
                        compoundButton.setChecked(false);
                    }
                }

                optionDialog.dismiss();
            }
        });
    }

    public void showSpecifyDialog(final View editableView) {
        View specifyDialog = LayoutInflater.from(getContext()).inflate(R.layout.specify_dialog, null);

        final EditText edtxSpecify = specifyDialog.findViewById(R.id.specify_txt);
        Button btnSpecify = specifyDialog.findViewById(R.id.specify_btn);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(specifyDialog);

        final AlertDialog dialog = builder.create();
        dialog.show();

        btnSpecify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String specifiedText = edtxSpecify.getText().toString().trim();
                if (editableView instanceof EditText && !specifiedText.isEmpty()) {
                    EditText edtxView = (EditText) editableView;
                    edtxView.setText("Others - " + specifiedText);
                } else if (editableView instanceof CompoundButton) {
                    CompoundButton compoundButton = (CompoundButton) editableView;

                    if (specifiedText.isEmpty()) {
                        compoundButton.setChecked(false);
                        dialog.dismiss();
                        return;
                    }

                    compoundButton.append(" - " + edtxSpecify.getText().toString().trim());
                }

                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (editableView instanceof CompoundButton) {
                    CompoundButton compoundButton = (CompoundButton) editableView;
                    if (!compoundButton.getText().toString().contains("-")) {
                        compoundButton.setChecked(false);
                    }
                }
            }
        });
    }

    public void showFormDialog(int layoutId) {
        View formDialog = LayoutInflater.from(getContext()).inflate(layoutId, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(formDialog);

        final AlertDialog dialog = builder.create();
        dialog.show();

        switch (layoutId) {
            case R.layout.hospital_history_dialog:
                edtxHospitalReason = formDialog.findViewById(R.id.hospital_history_reason);
                edtxHospitalDate = formDialog.findViewById(R.id.hospital_history_date);
                edtxHospitalPlace = formDialog.findViewById(R.id.hospital_history_place);
                edtxHospitalPhicUsed = formDialog.findViewById(R.id.hospital_history_phic);
                edtxHospitalCostNotCovered = formDialog.findViewById(R.id.hospital_history_phic_cost);
                dialogBtnAddHospital = formDialog.findViewById(R.id.dialog_hospital_btn_add);

                Constants.setDateTextWatcher(getContext(), edtxHospitalDate);
                edtxHospitalPhicUsed.setOnClickListener(fieldListener);
                edtxHospitalDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dpd.show(getActivity().getFragmentManager(), "HospitalDate");
                    }
                });

                dialogBtnAddHospital.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String reason = edtxHospitalReason.getText().toString().trim();
                        String date = edtxHospitalDate.getText().toString().trim();
                        String place = edtxHospitalPlace.getText().toString().trim();
                        String phicUsed = edtxHospitalPhicUsed.getText().toString().trim();
                        String costNotCovered = edtxHospitalCostNotCovered.getText().toString().trim();

                        if (reason.isEmpty() || date.isEmpty() || place.isEmpty() ||
                                phicUsed.isEmpty() || costNotCovered.isEmpty()) {
                            Toast.makeText(getContext(), "Please provide data for all fields.", Toast.LENGTH_SHORT).show();
                        } else if (date.length() < 10) {
                            edtxHospitalDate.setError("Invalid Date Format.");
                            edtxHospitalDate.requestFocus();
                        } else {
                            try {
                                JSONObject history = new JSONObject();
                                history.accumulate("reason", reason);
                                history.accumulate("date", date);
                                history.accumulate("place", place);
                                history.accumulate("phicUsed", phicUsed);
                                history.accumulate("costNotCovered", costNotCovered);

                                hospitalHistory.put(history);

                                Toast.makeText(getContext(), "Successfully Added.", Toast.LENGTH_SHORT).show();

                                String text = "Reason: " + reason + "\nDate: " + date +
                                        "\nPlace: " + place + "\nPhilHealth Used: " + phicUsed +
                                        "\nCost not covered by PhilHealth: " + costNotCovered;
                                addHistoryInfoCardView(text, hospitalLayout);

                                dialog.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                break;
            case R.layout.past_surgical_dialog:
                edtxSurgicalOperation = formDialog.findViewById(R.id.past_surgical_operation);
                edtxSurgicalDate = formDialog.findViewById(R.id.past_surgical_date);
                dialogBtnAddSurgical = formDialog.findViewById(R.id.dialog_surgical_btn_add);

                Constants.setDateTextWatcher(getContext(), edtxSurgicalDate);
                edtxSurgicalDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dpd.show(getActivity().getFragmentManager(), "SurgicalDate");
                    }
                });

                dialogBtnAddSurgical.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String operation = edtxSurgicalOperation.getText().toString().trim();
                        String date = edtxSurgicalDate.getText().toString().trim();

                        if (operation.isEmpty() || date.isEmpty()) {
                            Toast.makeText(getContext(), "Please provide data for all fields.", Toast.LENGTH_SHORT).show();
                        } else if (date.length() < 10) {
                            edtxSurgicalDate.setError("Invalid Date Format.");
                            edtxSurgicalDate.requestFocus();
                        } else {
                            try {
                                JSONObject history = new JSONObject();
                                history.accumulate("operation", operation);
                                history.accumulate("date", date);

                                surgicalHistory.put(history);

                                Toast.makeText(getContext(), "Successfully Added.", Toast.LENGTH_SHORT).show();

                                String text = "Operation: " + operation + "\nDate:" + date;
                                addHistoryInfoCardView(text, surgicalLayout);

                                dialog.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                break;
            case R.layout.vaccination_history_dialog:
                edtxVaccineDate = formDialog.findViewById(R.id.dialog_vaccine_date);
                edtxVaccinePlace = formDialog.findViewById(R.id.dialog_vaccine_place);
                dialogBtnAddVaccine = formDialog.findViewById(R.id.dialog_vaccine_btn_add);

                Constants.setDateTextWatcher(getContext(), edtxVaccineDate);
                edtxVaccineDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dpd.show(getActivity().getFragmentManager(), "VaccineDate");
                    }
                });

                edtxVaccinePlace.setOnClickListener(fieldListener);
                dialogBtnAddVaccine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String place = edtxVaccinePlace.getText().toString().trim();
                        String date = edtxVaccineDate.getText().toString().trim();

                        if (place.isEmpty() || date.isEmpty()) {
                            Toast.makeText(getContext(), "Please provide data for all fields.", Toast.LENGTH_SHORT).show();
                        } else if (date.length() < 10) {
                            edtxVaccineDate.setError("Invalid Date Format.");
                            edtxVaccineDate.requestFocus();
                        } else {
                            try {
                                JSONObject history = new JSONObject();
                                history.accumulate("history_count", String.valueOf(vaccineHistory.length() + 1));
                                history.accumulate("place", place);
                                history.accumulate("date", date);

                                vaccineHistory.put(history);

                                Toast.makeText(getContext(), "Successfully Added.", Toast.LENGTH_SHORT).show();

                                String text = "Dengvaxia History: " + vaccineHistory.length() + "\nVaccine Received: " + place + "\nDate:" + date;
                                addHistoryInfoCardView(text, vaccineLayout);

                                dialog.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                break;
        }
    }

    public void addHistoryInfoCardView(String text, View parentView) {
        View infoCard = LayoutInflater.from(getContext()).inflate(R.layout.history_card_view, null);
        TextView infoView = infoCard.findViewById(R.id.info_text);
        infoView.setText(text);

        if (parentView instanceof LinearLayout) {
            LinearLayout viewContainer = (LinearLayout) parentView;
            viewContainer.addView(infoCard, viewContainer.getChildCount() - 1);
        } else if (parentView instanceof CheckBoxGroup) {
            CheckBoxGroup viewContainer = (CheckBoxGroup) parentView;
            viewContainer.addView(infoCard, viewContainer.getChildCount() - 1);
        }
    }

    public void changeContentVisibility(EditText edtxView) {
        String text = edtxView.getText().toString().trim();
        switch (edtxView.getId()) {
            case R.id.phic_status:
                if (!text.equalsIgnoreCase("Non-Member"))
                    phicMoreFormLayout.setVisibility(View.VISIBLE);
                else phicMoreFormLayout.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        EditText dateView = null;
        String date = year + "-" + String.format("%02d", monthOfYear) + "-" + String.format("%02d", dayOfMonth);

        switch (view.getTag()) {
            case "HospitalDate":
                dateView = edtxHospitalDate;
                break;
            case "SurgicalDate":
                dateView = edtxSurgicalDate;
                break;
            case "LastPeriodDate":
                dateView = edtxMensLastPeriod;
                break;
            case "VaccineDate":
                dateView = edtxVaccineDate;
                break;
            case "VaccineSulfate":
                edtxVaccineSulfate.setText("Yes - " + date);
                return;
            case "VaccineCapsule":
                edtxVaccineCapsule.setText("Yes - " + date);
                return;
            case "VaccineDeworm":
                edtxVaccineCapsule.setText("Yes - " + date);
                return;
        }

        dateView.setText(date);
    }
}
