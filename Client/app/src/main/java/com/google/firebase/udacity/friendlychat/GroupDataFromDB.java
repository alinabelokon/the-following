package com.google.firebase.udacity.friendlychat;

//package com.google.firebase.quickstart.fcm;

/**
 * Created by Alina on 5/2/2017.
 */

public class GroupDataFromDB {


    private String GroupID;

    private String GroupName;


    public String getGroupIDtoDis() {
        return GroupID;
    }

    public void setGroupIDtoDis(String groupIDtoDis) {
        this.GroupID = groupIDtoDis;
    }

    public void setGroupNameToDis(String groupNameToDis) {
        GroupName = groupNameToDis;
    }



    public String getGroupNameToDis() {
        return GroupName;
    }


}

