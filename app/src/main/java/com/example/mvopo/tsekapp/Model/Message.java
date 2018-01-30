package com.example.mvopo.tsekapp.Model;

/**
 * Created by mvopo on 1/30/2018.
 */

public class Message {
    public String messageFrom, messageTo, messageBody, messageType;

    public Message(String messageFrom, String messageTo, String messageBody) {
        this.messageFrom = messageFrom;
        this.messageTo = messageTo;
        this.messageBody = messageBody;
    }
}
