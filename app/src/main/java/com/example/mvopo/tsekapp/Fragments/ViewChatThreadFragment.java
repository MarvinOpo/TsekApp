package com.example.mvopo.tsekapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mvopo.tsekapp.ChatActivity;
import com.example.mvopo.tsekapp.Helper.ChatAdapter;
import com.example.mvopo.tsekapp.Helper.JSONApi;
import com.example.mvopo.tsekapp.LoginActivity;
import com.example.mvopo.tsekapp.MainActivity;
import com.example.mvopo.tsekapp.Model.Constants;
import com.example.mvopo.tsekapp.Model.FamilyProfile;
import com.example.mvopo.tsekapp.Model.Message;
import com.example.mvopo.tsekapp.Model.MessageThread;
import com.example.mvopo.tsekapp.Model.User;
import com.example.mvopo.tsekapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mvopo on 1/30/2018.
 */

public class ViewChatThreadFragment extends Fragment {


    public static List<String> chatKeys =  new ArrayList<>();

    ListView lv;
    ArrayList<MessageThread> chatUsers = new ArrayList<>();
    ChatAdapter adapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference threadRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout, container, false);

        MainActivity.toolbar.setTitle("PHA Check-App");

        chatUsers.clear();
//        chatUsers.add(new User("1", "Mark Vincent", "B.", "Opo", "", "0912-345-6789", "", ""));
//        chatUsers.add(new User("2", "Jimmy", "B.", "Parker", "", "0916-207-2427", "", ""));
//        chatUsers.add(new User("3", "NDP", "O.", "Assistant", "", "418-7633", "", ""));

        lv = view.findViewById(R.id.lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                MessageThreadFragment mtf = new MessageThreadFragment();
//                mtf.setArguments(bundle);
//
//                MainActivity.ft = MaindActivity.fm.beginTransaction();
//                MainActivity.ft.replace(R.id.fragment_container, mtf).addToBackStack("").commit();
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("messageTo", chatKeys.get(i));
                intent.putExtra("conversationID", chatUsers.get(i).conversationID);
                intent.putExtra("user", MainActivity.user);
                startActivity(intent);


            }
        });

        getMessageThreads();

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
                showNewMessageDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getMessageThreads() {
        threadRef = database.getReference("Thread")
                .child(MainActivity.user.fname + " " + MainActivity.user.lname);
        threadRef.orderByChild("time").addValueEventListener(threadListener);
    }

    ValueEventListener threadListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            chatUsers.clear();
            for (DataSnapshot messageThread : dataSnapshot.getChildren()) {
                String lastMessage = (String) messageThread.child("lastMessage").getValue();
                String time = (String) messageThread.child("time").getValue();
                String conversationID = (String) messageThread.child("conversationID").getValue();

                chatUsers.add(0, new MessageThread(lastMessage, time, conversationID));
                chatKeys.add(0, messageThread.getKey());
            }

            adapter = new ChatAdapter(getContext(), R.layout.chat_list_item, chatUsers, null);
            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError error) {}
    };

    public void removeRegisteredListener(){
        threadRef.removeEventListener(threadListener);
    }

    public void showNewMessageDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.new_message_dialog, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);


        TextView tvConfirmBtn = view.findViewById(R.id.pin_btn);

        final EditText txtName = view.findViewById(R.id.pin_txt);

        final AlertDialog dialog = builder.create();
        dialog.show();

        tvConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = txtName.getText().toString().trim();

                if (name.isEmpty()) {
                    txtName.setError("Please provide a name.");
                    txtName.requestFocus();
                } else {
                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    intent.putExtra("messageTo", name);
                    intent.putExtra("conversationID", "");
                    intent.putExtra("user", MainActivity.user);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }
        });
    }
}
