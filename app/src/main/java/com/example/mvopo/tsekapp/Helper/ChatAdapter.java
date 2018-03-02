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
import com.example.mvopo.tsekapp.Fragments.ViewChatThreadFragment;
import com.example.mvopo.tsekapp.MainActivity;
import com.example.mvopo.tsekapp.Model.FamilyProfile;
import com.example.mvopo.tsekapp.Model.Message;
import com.example.mvopo.tsekapp.Model.MessageThread;
import com.example.mvopo.tsekapp.Model.User;
import com.example.mvopo.tsekapp.R;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

/**
 * Created by mvopo on 1/30/2018.
 */

public class ChatAdapter extends ArrayAdapter {
    Context mContext;
    int layoutId;
    List<MessageThread> chatThreads;
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

        if (chatThreads != null) size = chatThreads.size();
        else if (messages != null) size = messages.size();

        return size;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        if (layoutId == R.layout.chat_list_item) {
            TextView tvName = convertView.findViewById(R.id.message_thread_name);
            TextView tvLastMessage = convertView.findViewById(R.id.message_thread_phone);
            TextView tvDate = convertView.findViewById(R.id.message_thread_date);

            MessageThread messageThread = chatThreads.get(position);

            tvName.setText(ViewChatThreadFragment.chatKeys.get(position));
            tvDate.setText(chatThreads.get(position).time);

            String lastMessage = chatThreads.get(position).lastMessage;
            if (lastMessage.length() > 20) tvLastMessage.setText(lastMessage.substring(0, 20));
            else tvLastMessage.setText(lastMessage);
        } else if (layoutId == R.layout.message_list_layout) {
            try {
                int layoutId2;
                Message message = messages.get(position);

                if (message.messageFrom.equals(MainActivity.user.fname + " " + MainActivity.user.lname)) {
                    layoutId2 = R.layout.message_out_layout;
                } else layoutId2 = R.layout.message_in_layout;

                convertView = LayoutInflater.from(mContext).inflate(layoutId2, parent, false);
                TextView message_text = convertView.findViewById(R.id.message_text);
                message_text.setText(message.messageBody);
            }catch (Exception e) { convertView = LayoutInflater.from(mContext).inflate(R.layout.temporary_layout, parent, false); }
        }

        return convertView;
    }
}
