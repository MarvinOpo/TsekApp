package com.example.mvopo.tsekapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvopo.tsekapp.ChatActivity;
import com.example.mvopo.tsekapp.Helper.ChatAdapter;
import com.example.mvopo.tsekapp.ChatActivity;
import com.example.mvopo.tsekapp.MainActivity;
import com.example.mvopo.tsekapp.Model.Message;
import com.example.mvopo.tsekapp.Model.MessageThread;
import com.example.mvopo.tsekapp.Model.User;
import com.example.mvopo.tsekapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mvopo on 1/30/2018.
 */

public class MessageThreadFragment extends Fragment {

    ListView lv;
    ArrayList<Message> messages = new ArrayList<>();
    ChatAdapter adapter;

    EditText txtMessage;
    TextView tvSend;

    String messageTo = "", conversationID = "";
    String messageFrom = ChatActivity.user.fname + " " + ChatActivity.user.lname;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference conversationRef;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_list_layout, container, false);
        messageTo = getArguments().getString("messageTo");
        conversationID = getArguments().getString("conversationID");

        context = getContext();
        ChatActivity.toolbar.setTitle(messageTo);

        lv = view.findViewById(R.id.message_list);
        txtMessage = view.findViewById(R.id.message_text);
        tvSend = view.findViewById(R.id.message_send);

        lv.setDivider(null);

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message_body = txtMessage.getText().toString().trim();

                if (!message_body.isEmpty()) {
//                    messages.add( new Message(ChatActivity.user.id, messageThread, message_body));
//                    txtMessage.setText("");
//                    adapter.notifyDataSetChanged();
//
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(message_body.contains("login")){
//                                messages.add(new Message(messageThread, ChatActivity.user.id, "Make sure naa kay internet connection maam/sir"));
//                                messages.add(new Message(messageThread, ChatActivity.user.id, "And make sure naa kay user account :-)"));
//                            }else if(message_body.contains("hi")){
//                                messages.add(new Message(messageThread, ChatActivity.user.id, "hello"));
//                                messages.add(new Message(messageThread, ChatActivity.user.id, "You only need an internet during login, downloading and uploading of data"));
//                            }else if(message_body.contains("future")){
//                                messages.add(new Message(messageThread, ChatActivity.user.id, "Magstorya lang nya ta about sa atong future. hehehe. Bitaw, our future plans for our mobile app are....hmmmmmmm, e-announce lang namu sa online haha"));
//                            }else if(message_body.contains("upload")){
//                                messages.add(new Message(messageThread, ChatActivity.user.id, "To upload your data, please connect to the internet. Don't worry if ma-disconnect kay it will continue to where it left off once you re-upload!"));
//                            }else if(message_body.contains("double entry")){
//                                messages.add(new Message(messageThread, ChatActivity.user.id, "Para dili mag-double entry maam/sir, it is advised nga e-search daan ang name before adding the profile. "));
//                            }else if(message_body.contains("thank you")){
//                                messages.add(new Message(messageThread, ChatActivity.user.id, "You're welcome :-). "));
//                            }else if(message_body.contains("fuck") || message_body.contains("shit") || message_body.contains("damn")){
//                                messages.add(new Message(messageThread, ChatActivity.user.id, "Please refrain from using inappropriate words."));
//                            } else{
//                                messages.add(new Message(messageThread, ChatActivity.user.id, "..."));
//                            }
//
//                            adapter.notifyDataSetChanged();
//                            lv.setSelection(lv.getCount() - 1);
//                        }
//                    }, 5000);
//
//                    lv.setSelection(lv.getCount() - 1);
                    createMessageThread(message_body);
                } else Toast.makeText(getContext(), "Nothing to send", Toast.LENGTH_SHORT);
            }
        });

        getMessages();
        return view;
    }

    public void createMessageThread(final String body) {
        txtMessage.setText("");

        final DatabaseReference myRef = database.getReference("Thread").child(messageFrom);
        final DatabaseReference othersRef = database.getReference("Thread").child(messageTo);
        final ValueEventListener keyListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = "";

                if (!dataSnapshot.exists())
                    key = database.getReference("Conversation").push().getKey();
                else key = dataSnapshot.getValue(String.class);

                conversationID = key;
                database.getReference("Conversation/" + key).push().getRef()
                        .setValue(new Message(messageFrom, body, getDate()));

                othersRef.child(messageFrom).setValue(new MessageThread(body, getDate(), key));
                myRef.child(messageTo).setValue(new MessageThread(body, getDate(), key));

                try { removeRegisteredListener();} catch (Exception e) {}
                getMessages();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myRef.child(messageTo).getRef().child("conversationID").addListenerForSingleValueEvent(keyListener);
    }

    public void getMessages() {
        conversationRef = database.getReference("Conversation").child(conversationID);
        conversationRef.addValueEventListener(messageListener);

        adapter = new ChatAdapter(context, R.layout.message_list_layout, null, messages);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv.setSelection(lv.getCount() - 1);
    }

    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
        Date date = new Date();

        return dateFormat.format(date);
    }

    ValueEventListener messageListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            messages.clear();
            for (DataSnapshot message : dataSnapshot.getChildren()) {
                String messageBody = (String) message.child("messageBody").getValue();
                String messageFrom = (String) message.child("messageFrom").getValue();
                String time = (String) message.child("time").getValue();

                messages.add(new Message(messageFrom, messageBody, time));
            }

            adapter = new ChatAdapter(context, R.layout.message_list_layout, null, messages);
            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            lv.setSelection(lv.getCount() - 1);
        }

        @Override
        public void onCancelled(DatabaseError error) {
        }
    };

    public void removeRegisteredListener() {
        conversationRef.removeEventListener(messageListener);
    }
}
