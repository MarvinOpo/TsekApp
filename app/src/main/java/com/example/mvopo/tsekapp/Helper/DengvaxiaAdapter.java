package com.example.mvopo.tsekapp.Helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mvopo.tsekapp.Model.DengvaxiaPatient;
import com.example.mvopo.tsekapp.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by mvopo on 5/28/2018.
 */

public class DengvaxiaAdapter extends ArrayAdapter {
    Context mContext;
    int layoutID;
    List<DengvaxiaPatient> patients;


    public DengvaxiaAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);

        mContext = context;
        layoutID = resource;
        patients = objects;
    }

    @Override
    public int getCount() {
        int size = 0;

        if (patients != null) size = patients.size();

        return size;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.dengvaxia_pending_item, parent, false);

        TextView name = convertView.findViewById(R.id.dengvaxia_name);
        TextView address = convertView.findViewById(R.id.dengvaxia_address);
        TextView status = convertView.findViewById(R.id.dengvaxia_status);
        final TextView remarks = convertView.findViewById(R.id.dengvaxia_remarks);
        final LinearLayout remarksContainer = convertView.findViewById(R.id.remarks_container);

        name.setText(patients.get(position).getName());
        address.setText(patients.get(position).getAddress());
        status.setText(patients.get(position).getStatus());
        remarks.setText(patients.get(position).getRemarks());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (remarksContainer.getVisibility() == View.VISIBLE)
                    remarksContainer.setVisibility(View.GONE);
                else remarksContainer.setVisibility(View.VISIBLE);
            }
        });

        return convertView;
    }
}
