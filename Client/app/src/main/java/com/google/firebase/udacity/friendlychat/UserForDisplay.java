package com.google.firebase.udacity.friendlychat;

/**
 * Created by Alina on 5/18/2017.
 */

public class UserForDisplay {

    private String text;
    private String Name;
    private String photoUrl;
    private String GroupName;
    private String GroupID;
    private String Status;
    private String Mail;

    public UserForDisplay() {
    }

    public UserForDisplay(String text, String name, String photoUrl, String Mail) {
        this.text = text;
        this.Name = name;
        this.Mail=Mail;
        this.photoUrl = photoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        this.Mail = mail;
    }


    public String getgroupName() {
        return GroupName;
    }

    public void setgroupName(String text) {
        this.GroupName = GroupName;
    }


    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String text) {
        this.GroupID = GroupID;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}

