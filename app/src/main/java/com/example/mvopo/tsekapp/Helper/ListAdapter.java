package com.example.mvopo.tsekapp.Helper;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mvopo.tsekapp.Model.FamilyProfile;
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
    LayoutInflater inflater;

    public ListAdapter(Context context, int resource, List familyProfiles) {
        super(context, resource);

        this.context = context;
        layoutId = resource;
        this.familyProfiles = familyProfiles;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int size = 0;

        if(familyProfiles!=null) size = familyProfiles.size();

        return size;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = inflater.inflate(layoutId, parent, false);

        String fullName = familyProfiles.get(position).fname + " " +
                familyProfiles.get(position).mname + " " + familyProfiles.get(position).lname + " " + familyProfiles.get(position).suffix;

        if(layoutId == R.layout.population_item){
            TextView name = convertView.findViewById(R.id.population_name);
            TextView id = convertView.findViewById(R.id.population_family_id);

            name.setText(fullName);
            id.setText(familyProfiles.get(position).familyId);
        }else if(layoutId == R.layout.services_item){
            TextView name = convertView.findViewById(R.id.services_name);
            TextView id = convertView.findViewById(R.id.services_family_id);
            TextView barangay = convertView.findViewById(R.id.services_barangay);

            name.setText(fullName);
            id.setText(familyProfiles.get(position).familyId);
            barangay.setText(familyProfiles.get(position).barangayId);
        }
        return convertView;
    }
}
