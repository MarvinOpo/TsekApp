package com.example.mvopo.tsekapp.Helper;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mvopo.tsekapp.Model.Constants;
import com.example.mvopo.tsekapp.Model.FamilyProfile;
import com.example.mvopo.tsekapp.Model.ServicesStatus;
import com.example.mvopo.tsekapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mvopo on 10/19/2017.
 */

public class ListAdapter extends ArrayAdapter {

    Context context;
    int layoutId;
    List<FamilyProfile> familyProfiles;
    List<ServicesStatus> serviceStatus;
    LayoutInflater inflater;

    public ListAdapter(Context context, int resource, List familyProfiles, List serviceStatus) {
        super(context, resource);

        this.context = context;
        layoutId = resource;
        this.familyProfiles = familyProfiles;
        this.serviceStatus = serviceStatus;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int size = 0;

        if(familyProfiles!=null) size = familyProfiles.size();
        if(serviceStatus!=null) size = serviceStatus.size();

        return size;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = inflater.inflate(layoutId, parent, false);

        if(layoutId == R.layout.population_item){
            TextView name = convertView.findViewById(R.id.population_name);
            TextView id = convertView.findViewById(R.id.population_family_id);

            String fullName = familyProfiles.get(position).fname + " " +
                    familyProfiles.get(position).mname + " " + familyProfiles.get(position).lname + " " + familyProfiles.get(position).suffix;

            name.setText(fullName);
            id.setText(familyProfiles.get(position).familyId);

            if(familyProfiles.get(position).isHead.equalsIgnoreCase("Yes")) name.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
        }else if(layoutId == R.layout.services_item){
            TextView name = convertView.findViewById(R.id.services_name);
            ImageView ivGroup1 = convertView.findViewById(R.id.iv_group1);
            ImageView ivGroup2 = convertView.findViewById(R.id.iv_group2);
            ImageView ivGroup3 = convertView.findViewById(R.id.iv_group3);

            name.setText(serviceStatus.get(position).name);

            if(serviceStatus.get(position).group1.equals("1")) ivGroup1.setImageResource(R.drawable.success);
            if(serviceStatus.get(position).group2.equals("1")) ivGroup2.setImageResource(R.drawable.success);
            if(serviceStatus.get(position).group3.equals("1")) ivGroup3.setImageResource(R.drawable.success);
        }
        return convertView;
    }
}
