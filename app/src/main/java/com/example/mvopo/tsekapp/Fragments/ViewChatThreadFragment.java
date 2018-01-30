package com.example.mvopo.tsekapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mvopo.tsekapp.Helper.ChatAdapter;
import com.example.mvopo.tsekapp.MainActivity;
import com.example.mvopo.tsekapp.Model.User;
import com.example.mvopo.tsekapp.R;

import java.util.ArrayList;

/**
 * Created by mvopo on 1/30/2018.
 */

public class ViewChatThreadFragment extends Fragment {

    ListView lv;
    ArrayList<User> chatUsers = new ArrayList<>();
    ChatAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout, container, false);

        MainActivity.toolbar.setTitle("PHA Check-App");

        chatUsers.clear();
        chatUsers.add(new User("1", "Mark Vincent", "B.", "Opo", "", "09123456789", "", ""));
        chatUsers.add(new User("2", "Jimmy", "B.", "Tumalon", "", "0909876543", "", ""));
        chatUsers.add(new User("3", "NDP", "O.", "Nathalie", "", "09876543211", "", ""));

        lv = view.findViewById(R.id.lv);
        adapter = new ChatAdapter(getContext(), R.layout.chat_list_item, chatUsers, null);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("messageThread", chatUsers.get(i).id);
                bundle.putString("messageName", chatUsers.get(i).fname + " " + chatUsers.get(i).lname);

                MessageThreadFragment mtf = new MessageThreadFragment();
                mtf.setArguments(bundle);

                MainActivity.ft = MainActivity.fm.beginTransaction();
                MainActivity.ft.replace(R.id.fragment_container, mtf).addToBackStack("").commit();
            }
        });
        return view;
    }
}
