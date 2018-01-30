package com.example.mvopo.tsekapp.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvopo.tsekapp.Helper.ChatAdapter;
import com.example.mvopo.tsekapp.MainActivity;
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

        MainActivity.toolbar.setTitle(getArguments().getString("messageName"));

        messages.add(new Message("1", MainActivity.user.id, "Bai"));
        messages.add(new Message(MainActivity.user.id, "2", "Bai ni chat si mark nako adto ta kalingawan ron, game?"));
        messages.add(new Message(MainActivity.user.id, "1", "Oh? unsa man?"));
        messages.add(new Message("1", MainActivity.user.id, "Adto ta kalingawan na"));

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
                    messages.add( new Message(MainActivity.user.id, messageThread, message_body));
                    txtMessage.setText("");
                    adapter.notifyDataSetChanged();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(message_body.contains("jimmy")){
                                messages.add(new Message(messageThread, MainActivity.user.id, "Asay jimmy ato? katong gwapo?"));
                            }else if(message_body.contains("hi")){
                                messages.add(new Message(messageThread, MainActivity.user.id, "hello"));
                            }else if(message_body.contains("blue")){
                                messages.add(new Message(messageThread, MainActivity.user.id, "Lagi blue nga DOH shirt, Gwapo man gud to!"));
                            }else if(message_body.contains("dili man")){
                                messages.add(new Message(messageThread, MainActivity.user.id, "Lagi si Jimmy Lomocso lagi, ako gi search. Siya lagi! gwapo kaau oy!"));
                            } else if(message_body.contains("")){
                                messages.add(new Message(messageThread, MainActivity.user.id, "Lagi si Jimmy Lomocso lagi, ako gi search. Siya lagi! gwapo kaau oy!"));
                            } else{
                                messages.add(new Message(messageThread, MainActivity.user.id, "Unsa?"));
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
