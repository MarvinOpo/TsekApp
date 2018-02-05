package com.example.mvopo.tsekapp.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.example.mvopo.tsekapp.Model.Message;
import com.example.mvopo.tsekapp.Model.User;
import com.example.mvopo.tsekapp.R;

import java.util.ArrayList;

/**
 * Created by mvopo on 1/30/2018.
 */

public class MessageThreadFragment extends Fragment {

    public static String messageThread;
    ListView lv;
    ArrayList<Message> messages = new ArrayList<>();
    ChatAdapter adapter;

    EditText txtMessage;
    TextView tvSend;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_list_layout, container, false);
        messageThread = getArguments().getString("messageThread");

        ChatActivity.toolbar.setTitle(getArguments().getString("messageName"));

        messages.add(new Message("1", ChatActivity.user.id, "Bai"));
        messages.add(new Message(ChatActivity.user.id, "2", "Bai ni chat si mark nako adto ta kalingawan ron, game?"));
        messages.add(new Message(ChatActivity.user.id, "1", "Oh? unsa man?"));
        messages.add(new Message("1", ChatActivity.user.id, "Adto ta kalingawan na"));

        lv = view.findViewById(R.id.message_list);
        txtMessage = view.findViewById(R.id.message_text);
        tvSend = view.findViewById(R.id.message_send);

        lv.setDivider(null);
        adapter = new ChatAdapter(getContext(), R.layout.message_list_layout, null, messages);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message_body = txtMessage.getText().toString().trim();

                if(!message_body.isEmpty()){
                    messages.add( new Message(ChatActivity.user.id, messageThread, message_body));
                    txtMessage.setText("");
                    adapter.notifyDataSetChanged();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(message_body.contains("login")){
                                messages.add(new Message(messageThread, ChatActivity.user.id, "Make sure naa kay internet connection maam/sir"));
                                messages.add(new Message(messageThread, ChatActivity.user.id, "And make sure naa kay user account :-)"));
                            }else if(message_body.contains("hi")){
                                messages.add(new Message(messageThread, ChatActivity.user.id, "hello"));
                            }else if(message_body.contains("online")){
                                messages.add(new Message(messageThread, ChatActivity.user.id, "You only need an internet during login, downloading and uploading of data"));
                            }else if(message_body.contains("future")){
                                messages.add(new Message(messageThread, ChatActivity.user.id, "Magstorya lang nya ta about sa atong future. hehehe. Bitaw, our future plans for our mobile app are....hmmmmmmm, e-announce lang namu sa online haha"));
                            }else if(message_body.contains("upload")){
                                messages.add(new Message(messageThread, ChatActivity.user.id, "To upload your data, please connect to the internet. Don't worry if ma-disconnect kay it will continue to where it left off once you re-upload!"));
                            }else if(message_body.contains("double entry")){
                                messages.add(new Message(messageThread, ChatActivity.user.id, "Para dili mag-double entry maam/sir, it is advised nga e-search daan ang name before adding the profile. "));
                            }else if(message_body.contains("thank you")){
                                messages.add(new Message(messageThread, ChatActivity.user.id, "You're welcome :-). "));
                            }else if(message_body.contains("fuck") || message_body.contains("shit") || message_body.contains("damn")){
                                messages.add(new Message(messageThread, ChatActivity.user.id, "Please refrain from using inappropriate words."));
                            } else{
                                messages.add(new Message(messageThread, ChatActivity.user.id, "..."));
                            }

                            adapter.notifyDataSetChanged();
                            lv.setSelection(lv.getCount() - 1);
                        }
                    }, 5000);

                    lv.setSelection(lv.getCount() - 1);
                }else Toast.makeText(getContext(), "Nothing to send", Toast.LENGTH_SHORT);
            }
        });
        return view;
    }
}
