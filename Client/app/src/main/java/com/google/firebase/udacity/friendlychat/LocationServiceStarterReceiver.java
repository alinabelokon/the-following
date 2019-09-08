package com.google.firebase.udacity.friendlychat;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by Alina on 6/10/2017.
 */

public class LocationServiceStarterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(tryObtainLocationPermissions(context))
        {

            Intent myIntent = new Intent(context, LocationUpdateService.class);
            context.startService(myIntent);
        }
    }

    private boolean tryObtainLocationPermissions(Context context) {

        boolean hasFineLocation = doesHavePermission(android.Manifest.permission.ACCESS_FINE_LOCATION, context);
        boolean hasCoarseLocation = doesHavePermission(android.Manifest.permission.ACCESS_COARSE_LOCATION, context);
        return hasFineLocation || hasCoarseLocation;
    }

    private boolean doesHavePermission(String permission, Context context) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}