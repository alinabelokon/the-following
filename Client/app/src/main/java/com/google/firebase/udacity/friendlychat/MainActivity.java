/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//package com.google.firebase.quickstart.fcm;
package com.google.firebase.udacity.friendlychat;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;

//import com.google.firebase.quickstart.fcm.MyAppApplication;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String PID="%sfcN2OGisSWM:APA91bFGrx2GRXRfyEJ_kTp2kJto-BEyldUP5_jpptFxLpkTm3GKmHy_p498R305BVpmHW0Zo9CAPIgy2S5MPhQ_zeS32eD5nI9Cl6mLbIKLyHuyAHQ-hqzSUb8ZAzkpuMHiy7sKUMrb";
    public static final int RC_SIGN_IN = 1;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    MyAppApplication CurrUserHelper;

    private DatabaseReference mMessagesDatabaseReference;

    private String CurrUser= "temp";
    private String CurrUserID= "temp2";
    MyFirebaseInstanceIDService TokenService= new MyFirebaseInstanceIDService();

    private boolean tryObtainLocationPermissions() {

        boolean hasFineLocation = tryRequestPermissionOnce(android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (!hasFineLocation) {
            Toast.makeText(this, "Without GPS permissions, your group members will receive inaccurate info regarding your location", Toast.LENGTH_LONG).show();
        }

        boolean hasCoarseLocation = tryRequestPermissionOnce(android.Manifest.permission.ACCESS_COARSE_LOCATION);

        if (!hasCoarseLocation) {
            Toast.makeText(this, "Without Cellular location permissions, your group members might not recieve info regarding your location when there's no GPS reception", Toast.LENGTH_LONG).show();
        }

        if (!hasFineLocation && !hasCoarseLocation) {
            Toast.makeText(this, "Without GPS & Cellular location permissions, your group members might not recieve info regarding your location", Toast.LENGTH_LONG).show();
        }

        return hasFineLocation || hasCoarseLocation;
    }

    private boolean tryRequestPermissionOnce(String permission) {
        boolean granted = doesHavePermission(permission);

        if (granted)
            return true;

        if (!granted) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 1111);
        }

        return doesHavePermission(permission);
    }

    private boolean doesHavePermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean canWriteSettings = tryRequestPermissionOnce(Manifest.permission.WRITE_SETTINGS);
        if(!canWriteSettings)
        {
            Toast.makeText(this, "Application might not properly work since you did not provide it write settings permission", Toast.LENGTH_LONG);
        }

        if(tryObtainLocationPermissions())
        {
            Intent myIntent = new Intent(this, LocationUpdateService.class);
            startService(myIntent);
        }

        mFirebaseAuth = FirebaseAuth.getInstance();

        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]


        mFirebaseAuth = FirebaseAuth.getInstance();
        Button Groups= (Button) findViewById(R.id.GroupList);
        CurrUserHelper =((MyAppApplication)getApplicationContext());

        Groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GroupList.class);
                startActivity(intent);
            }
        });


        /*Button button2=(Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick (View v){

                Intent intent = new Intent(MainActivity.this, AddGoupActivity.class);
                intent.putExtra("CurrUSer",CurrUser);
                startActivity(intent);

            }
        });*/





       /* Button SignOutB=(Button) findViewById(R.id.SignB);
        SignOutB.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick (View v){

                AuthUI.getInstance().signOut(MainActivity.this);

            }
        });*/





        Button logTokenButton = (Button) findViewById(R.id.logTokenButton);

        logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v){
                // Get token
                try

                {
                    String token = FirebaseInstanceId.getInstance().getToken();

                    // Log and toast
                    String msg = "InstanceID Token: %s" + token;
                    Log.d(TAG, msg);


                    ///////////////////////


                    FirebaseMessaging fm = FirebaseMessaging.getInstance();
                    AtomicInteger msgId;
                    fm.send(new RemoteMessage.Builder("1066282643284" + "@gcm.googleapis.com")
                            .setMessageId("1235666")
                            .addData("my_message", "Hello World")
                            .addData("action", ".MESSAGE")
                            .addData("message_type","Receipt")
                            .addData("recipient",PID)
                            .build());


                    //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

                    //////////////////////////////
                }

                catch (Throwable e) {
                    e.printStackTrace();
                }

            }
        });





        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    CurrUserHelper.setCurrUser(firebaseAuth.getCurrentUser().getEmail());
                    CurrUserHelper.setCurrUserID(firebaseAuth.getCurrentUser().getUid());
                    CurrUserHelper.setCurrUserName(firebaseAuth.getCurrentUser().getDisplayName());

                    SharedPreferences.Editor editor = getSharedPreferences("TheFolowingAuth", MODE_PRIVATE).edit();
                    String email = firebaseAuth.getCurrentUser().getEmail();
                    editor.putString("Mail", email);
                    editor.commit();

                    //TokenService.FirstTimeToken();
                    String UserToken = FirebaseInstanceId.getInstance().getToken();
                    mMessagesDatabaseReference= FirebaseDatabase.getInstance().getReference();
                    //mMessagesDatabaseReference.child("IDS").child(CurrUserHelper.getCurrID()).child("token").setValue(UserToken);
                    //mMessagesDatabaseReference.child("IDS").child(CurrUserHelper.getCurrID()).child("userName").setValue(CurrUserHelper.getCurrUser());
                    mMessagesDatabaseReference.child("IDS").child(CurrUserHelper.getCurrUser().replace(".","*dot*")).child("token").child("tokennow").setValue(UserToken);
                    String Name= CurrUserHelper.getCurrUser();
                    mMessagesDatabaseReference.child("IDS").child(CurrUserHelper.getCurrUser().replace(".","*dot*")).child("Info").child("Name").setValue(firebaseAuth.getCurrentUser().getDisplayName());
                    mMessagesDatabaseReference.child("IDS").child(CurrUserHelper.getCurrUser().replace(".","*dot*")).child("UID").child("CURRUID").setValue(firebaseAuth.getCurrentUser().getUid());

                    mMessagesDatabaseReference.child("IDS").child(CurrUserHelper.getCurrUser().replace(".","*dot*")).child("Location").child("CurLoc").child("lat").setValue("NULL");
                    mMessagesDatabaseReference.child("IDS").child(CurrUserHelper.getCurrUser().replace(".","*dot*")).child("Location").child("CurLoc").child("lon").setValue("NULL");



                    //onSignedInInitialize(user.getDisplayName());
                } else {
                    // User is signed out
                   //onSignedOutCleanup();

                    startActivityForResult(
                              AuthUI.getInstance().createSignInIntentBuilder().build(), RC_SIGN_IN);

                    /*startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(
                                            AuthUI.EMAIL_PROVIDER,
                                           AuthUI.EMAIL_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);*/


                }
            }
        };




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
                CurrUserHelper.setCurrUser(mFirebaseAuth.getCurrentUser().getDisplayName());
                CurrUserHelper.setCurrUserID(mFirebaseAuth.getCurrentUser().getUid());
               // TokenService.FirstTimeToken();

                CurrUser= CurrUserHelper.getCurrUser();
                CurrUserID= CurrUserHelper.getCurrID();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        //mMessageAdapter.clear();
       // detachDatabaseReadListener();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mMessagesDatabaseReference.child("IDS").child(CurrUserHelper.getCurrUser().replace(".","*dot*")).child("token").child("tokennow").setValue("NULL");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                SharedPreferences.Editor editor = getSharedPreferences("TheFolowingAuth", MODE_PRIVATE).edit();
                editor.putString("Mail", "");
                editor.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
