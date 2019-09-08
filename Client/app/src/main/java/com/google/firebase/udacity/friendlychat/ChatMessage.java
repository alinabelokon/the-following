package com.google.firebase.udacity.friendlychat;

/**
 * Created by Alina on 5/31/2017.
 */

public class ChatMessage {


    private String text;
    private String name;
    private String timeStamp;

    public ChatMessage() {
    }

    public ChatMessage(String text, String name, String timeStamp) {
        this.text = text;
        this.name = name;
        this.timeStamp=timeStamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }


}
