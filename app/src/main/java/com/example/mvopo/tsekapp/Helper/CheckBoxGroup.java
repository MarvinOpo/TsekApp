package com.example.mvopo.tsekapp.Helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CheckBoxGroup extends GridLayout {

    String TAG = "CheckBoxGroup";

    public CheckBoxGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public List<String> getSelectedAsList(){
        List<String> selectedOptions = new ArrayList<>();
        for(int i = 0; i < getChildCount(); i++){
            CheckBox cb = null;
            View view = getChildAt(i);
            if(view instanceof CheckBox){
                cb = (CheckBox) view;

                if (cb.isChecked()) {
                    selectedOptions.add(cb.toString());
                }
            }
        }

        return selectedOptions;
    }

    public JSONArray getSelectedAsJSONArray(){
        CheckBox cb = null;
        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i < getChildCount(); i++){
            View view = getChildAt(i);
            if(view instanceof CheckBox){
                cb = (CheckBox) getChildAt(i);

                if (cb.isChecked()) {
                    jsonArray.put(cb.getText().toString());
                }
            }
        }

        return jsonArray;
    }

    public JSONObject getSelectedAsJSONObjectWithTags(){
        CheckBox cb;
        TextView tvTag = null;
        String tag = "";

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i < getChildCount(); i++){
            View view = getChildAt(i);
            if(view instanceof CheckBox){
                cb = (CheckBox) view;

                if (cb.isChecked()) {
                    jsonArray.put(cb.getText().toString());
                }
            }else if(view instanceof TextView){
                if(tvTag == null){
                    tvTag = (TextView) view;
                    tag = tvTag.getText().toString();
                    continue;
                }

                try {
                    jsonObject.accumulate(tag, jsonArray);
                    jsonArray = new JSONArray();

                    tvTag = (TextView) view;
                    tag = tvTag.getText().toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonObject;
    }
}
