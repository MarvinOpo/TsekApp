package com.example.mvopo.tsekapp.Helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mvopo.tsekapp.Fragments.MessageThreadFragment;
import com.example.mvopo.tsekapp.MainActivity;
import com.example.mvopo.tsekapp.Model.FamilyProfile;
import com.example.mvopo.tsekapp.Model.Message;
import com.example.mvopo.tsekapp.Model.User;
import com.example.mvopo.tsekapp.R;

import java.util.List;

/**
 * Created by mvopo on 1/30/2018.
 */

public class ChatAdapter extends ArrayAdapter {
    Context mContext;
    int layoutId;
    List<User> chatThreads;
    List<Message> messages;

    public ChatAdapter(Context context, int resource, List chatThreads, List messages) {
        super(context, resource);

        mContext = context;
        layoutId = resource;
        this.chatThreads = chatThreads;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        int size = 0;

        if(chatThreads!=null) size = chatThreads.size();
        else if(messages!=null) size = messages.size();

        return size;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        if(layoutId == R.layout.chat_list_item){
            TextView name = convertView.findViewById(R.id.message_thread_name);
            TextView phone = convertView.findViewById(R.id.message_thread_phone);

            User user = chatThreads.get(position);

            name.setText(user.fname + " " + user.lname);
            phone.setText(user.contact);
        }else if(layoutId == R.layout.message_list_layout){
            int layoutId2;
            Message message = messages.get(position);

            if(message.messageTo.equals(MessageThreadFragment.messageThread) || message.messageFrom.equals(MessageThreadFragment.messageThread)) {
                if (message.messageTo.equals(MainActivity.user.id)) {
                    layoutId2 = R.layout.message_in_layout;
                } else layoutId2 = R.layout.message_out_layout;

                convertView = LayoutInflater.from(mContext).inflate(layoutId2, parent, false);
                TextView message_text = convertView.findViewById(R.id.message_text);
                message_text.setText(message.messageBody);
            }else{
                convertView = LayoutInflater.from(mContext).inflate(R.layout.temporary_layout, parent, false);
            }
        }

        return convertView;
    }
}
