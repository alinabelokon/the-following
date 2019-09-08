package com.google.firebase.udacity.friendlychat;
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

import android.content.ContentUris;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;//package com.google.firebase.quickstart.fcm;

import android.provider.Settings;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.concurrent.atomic.AtomicInteger;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        System.out.print(refreshedToken);


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */



    private void sendRegistrationToServer(String token) {

        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("IDS");
        MyAppApplication CurrUserHelper =((MyAppApplication)getApplicationContext());
        String ID= CurrUserHelper.getCurrID();
        //TODO CurrUserHelper sucks, also ID is null when no current user yet :D
        if(ID == null || ID.equals(""))
            return;

        mDatabase.child(ID).setValue(token);
        //  FirebaseMessaging fm = FirebaseMessaging.getInstance();

        //fm.send(new RemoteMessage.Builder("1066282643284" + "@gcm.googleapis.com")
        //     .setMessageId(Integer.toString(msgId.incrementAndGet()))
        //   .addData(token)
        // .addData("my_action","SAY_HELLO")
        //.build());

        // TODO: Implement this method to send token to your app server.




    }

    public void FirstTimeToken() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(refreshedToken);

    }

}
