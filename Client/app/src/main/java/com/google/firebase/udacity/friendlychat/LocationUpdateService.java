package com.google.firebase.udacity.friendlychat;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LocationUpdateService extends Service {

    private final Thread locationRequestThread;
    private GoogleApiClient mGoogleApiClient;

    private DatabaseReference mMessagesDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;

    private Runnable runnable = new Runnable() {
        @Override
        public void run()
        {
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mMessagesDatabaseReference = mFirebaseDatabase.getReference();
            connectApiClient();
        }
    };

    public LocationUpdateService()
    {
        locationRequestThread = new Thread(runnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d("THEFOLLOWING","location service start command");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("The Following")
                .setContentText("Location Tracking in progress")
                .setContentIntent(pendingIntent)
                .build();

        startForeground(666, notification);

        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        Log.d("THEFOLLOWING","location service thread starting");
        locationRequestThread.start();
        Log.d("THEFOLLOWING","location service thread started");
    }

    private void connectApiClient()
    {
        Log.d("THEFOLLOWING","location service initiating api client connection");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(connectionFailedListener)
                .build();

        mGoogleApiClient.connect();
        Log.d("THEFOLLOWING","location service initiated api client connection");
    }

    private final GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {

        @Override
        public void onConnected(@Nullable Bundle bundle) {

            Log.d("THEFOLLOWING","location service connected to api client");
            LocationRequest r = new LocationRequest();

            r.setInterval(5000);
            r.setInterval(100);

            try {

                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, r, locationListener);
                Log.d("THEFOLLOWING","location service issued location update request");
            } catch (SecurityException sex) {

                Log.d("FAILED", sex.toString());

            }

        }

        @Override
        public void onConnectionSuspended(int i) {

        }
    };

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            Log.d("THEFOLLOWING","location service received location update");
            if(location==null)
                return;

            Log.d("THEFOLLOWING","location service obtaining user name");
            String userName=getUserName();

            if(userName == null || userName.isEmpty())
            {
                Log.e("THEFOLLOWING", "service started and no user name logged in");
                return;
            }


            Log.d("THEFOLLOWING","location service for user: " + userName);
            DatabaseReference Users = mMessagesDatabaseReference.child("IDS");
            DatabaseReference thisUser = Users.child(userName.replace(".","*dot*"));
            DatabaseReference thisLocation = thisUser.child("Location").child("CurLoc");

            thisLocation.child("lat").setValue(location.getLatitude());
            thisLocation.child("lon").setValue(location.getLongitude());
            Log.d("THEFOLLOWING","location service location updated");
        }
    };

    private String getUserName() {

        SharedPreferences sharedPreferences = getSharedPreferences("TheFolowingAuth", MODE_PRIVATE);
        return sharedPreferences.getString("Mail", "");
    }


    private final GoogleApiClient.OnConnectionFailedListener connectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Log.d("THEFOLLOWING","location service failed to establish api client connection");
            stopSelf();
        }
    };
}
