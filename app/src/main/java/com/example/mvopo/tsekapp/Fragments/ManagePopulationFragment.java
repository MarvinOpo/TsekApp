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

import com.example.mvopo.tsekapp.R;

/**
 * Created by mvopo on 10/20/2017.
 */

public class ManagePopulationFragment extends Fragment implements View.OnClickListener {

    LinearLayout updateFields, optionHolder;
    EditText txtHead, txtEducation, txtSuffix, txtSex, txtIncome, txtUnmet, txtSupply, txtToilet;
    Button manageBtn, optionBtn;
    boolean toUpdate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_member, container, false);
        toUpdate = getArguments().getBoolean("toUpdate");

        txtHead = view.findViewById(R.id.manage_head);
        txtEducation = view.findViewById(R.id.manage_education);
        txtSex = view.findViewById(R.id.manage_sex);
        txtSuffix = view.findViewById(R.id.manage_suffix);
        txtIncome = view.findViewById(R.id.manage_income);
        txtUnmet = view.findViewById(R.id.manage_unmet);
        txtSupply = view.findViewById(R.id.manage_supply);
        txtToilet = view.findViewById(R.id.manage_toilet);
        updateFields = view.findViewById(R.id.updateFields_holder);
        manageBtn = view.findViewById(R.id.manageBtn);

        if(!toUpdate){
            updateFields.setVisibility(View.GONE);
            txtHead.setHint("Relation to Head");
            manageBtn.setText("Add");
        }else{
            txtHead.setHint("Family Head?");
            manageBtn.setText("Update");
        }

        txtEducation.setOnClickListener(this);
        txtHead.setOnClickListener(this);
        txtSex.setOnClickListener(this);
        txtSuffix.setOnClickListener(this);
        txtIncome.setOnClickListener(this);
        txtUnmet.setOnClickListener(this);
        txtSupply.setOnClickListener(this);
        txtToilet.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id){
            case R.id.manage_education:
                showOptionDialog(R.array.educational_attainment);
                break;
            case R.id.manage_head:
                if(txtHead.getHint().equals("Family Head?")) showOptionDialog(R.array.is_family_head);
                else showOptionDialog(R.array.realation_to_head);
                break;
            case R.id.manage_sex:
            case R.id.manage_income:
                showOptionDialog(R.array.monthly_income);
                break;
            case R.id.manage_unmet:
                showOptionDialog(R.array.unmet_needs);
                break;
            case R.id.manage_supply:
                showOptionDialog(R.array.water_supply);
                break;
            case R.id.manage_toilet:
                showOptionDialog(R.array.sanitary_toilet);
                break;
        }
    }

    public void showOptionDialog(int id){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.options_dialog, null);
        optionHolder = view.findViewById(R.id.option_holder);
        optionBtn = view.findViewById(R.id.optionBtn);

        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        params.bottomMargin = 5;
        params.topMargin = 5;

        String[] labels = getResources().getStringArray(id);

        RadioGroup radioGroup = new RadioGroup(getContext());

        for(int i = 0; i < labels.length; i++){
            RadioButton radioButton = new RadioButton(getContext());
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
                optionDialog.dismiss();
            }
        });
    }
}
