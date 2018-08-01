package com.example.mvopo.tsekapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.mvopo.tsekapp.Helper.DengvaxiaAdapter;
import com.example.mvopo.tsekapp.Helper.JSONApi;
import com.example.mvopo.tsekapp.Model.Constants;
import com.example.mvopo.tsekapp.Model.DengvaxiaPatient;
import com.example.mvopo.tsekapp.R;

import java.util.ArrayList;

/**
 * Created by mvopo on 5/28/2018.
 */

public class PendingDengvaxiaFragment extends Fragment {

    public static ArrayList<DengvaxiaPatient> patients = new ArrayList<>();
    public static DengvaxiaAdapter adapter;
    public static boolean flag = false;

    EditText edtSearch;
    ListView lvPending;
    ImageView ivSearchBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout, container, false);

        patients.clear();
        edtSearch = view.findViewById(R.id.list_searchTxt);
        lvPending = view.findViewById(R.id.lv);
        ivSearchBtn = view.findViewById(R.id.list_searchBtn);

        adapter = new DengvaxiaAdapter(getContext(), R.layout.dengvaxia_pending_item, patients);
        lvPending.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        JSONApi.getInstance(getContext()).getDengvaxiaPending(Constants.dengvaxiaUrl + "cmd=profiles&offset="+ patients.size() +"&keyword=" + edtSearch.getText().toString().trim());

        ivSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patients.clear();
                JSONApi.getInstance(getContext()).getDengvaxiaPending(Constants.dengvaxiaUrl + "cmd=profiles&offset=0&keyword=" + edtSearch.getText().toString().trim());
            }
        });

        lvPending.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0) {
                    if(!flag) {
                        flag = true;
                        JSONApi.getInstance(getContext()).getDengvaxiaPending(Constants.dengvaxiaUrl + "cmd=profiles&offset=" + patients.size() + "&keyword=" + edtSearch.getText().toString().trim());
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_head, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add_member:

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
