package com.example.mvopo.tsekapp.Model;

/**
 * Created by mvopo on 2/20/2018.
 */

public class MessageThread {
    public String lastMessage, time, conversationID;

    public MessageThread(){}

    public MessageThread(String lastMessage, String time, String conversationID) {
        this.lastMessage = lastMessage;
        this.time = time;
        this.conversationID = conversationID;
    }
}
