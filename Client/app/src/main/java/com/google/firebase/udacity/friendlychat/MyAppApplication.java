package com.google.firebase.udacity.friendlychat;

import android.app.Application;//package com.google.firebase.quickstart.fcm;

import android.app.Application;

/**
 * Created by Alina on 3/6/2017.
 */

public class MyAppApplication extends Application {

    private String CurrUser;
    private String CurrUserID;
    private String CurrUserName;


    public String getCurrUser()
    {
        return CurrUser;

    }

    public void setCurrUser(String user)
    {
        CurrUser = user;

    }


    public String getCurrUsername()
    {
        return CurrUserName;

    }

    public void setCurrUserName(String user)
    {
        CurrUserName =user;

    }


    public String getCurrID()
    {
        return CurrUserID;

    }

    public void setCurrUserID(String user)
    {
        CurrUserID =user;

    }





















    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;
}
