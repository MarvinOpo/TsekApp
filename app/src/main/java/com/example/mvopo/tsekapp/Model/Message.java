package com.example.mvopo.tsekapp.Model;

import java.sql.Timestamp;

/**
 * Created by mvopo on 1/30/2018.
 */

public class Message {
    public String messageFrom, messageBody, time;

    public Message(String messageFrom, String messageBody, String time) {
        this.messageFrom = messageFrom;
        this.messageBody = messageBody;
        this.time = time;
    }
}
