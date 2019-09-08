package com.google.firebase.udacity.friendlychat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.concurrent.atomic.AtomicInteger;//package com.google.firebase.quickstart.fcm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Alina on 4/23/2017.
 */

public class AddUsersToGroup extends AppCompatActivity {
    String GroupID;
    String GroupName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_users_to_group);

        final TextView helpText= (TextView) findViewById(R.id.AddusersHelpText);
        final TextView userMail= (TextView) findViewById(R.id.UserMailText);
        Button submitUserButton= (Button) findViewById(R.id.SubmitUserButton);
        GroupID = getIntent().getStringExtra("GroupID");
        GroupName = getIntent().getStringExtra("GroupName");
        helpText.setText("Add users by email to thr group: "+GroupName);

        final MyAppApplication CurrUserHelper;
        CurrUserHelper =((MyAppApplication)getApplicationContext());
        submitUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                String userMailToSend= userMail.getText().toString();


                String admin= CurrUserHelper.getCurrUser();
                FirebaseMessaging fm = FirebaseMessaging.getInstance();
                AtomicInteger msgId;

                fm.send(new RemoteMessage.Builder("1066282643284" + "@gcm.googleapis.com")
                        .setMessageId("111")
                        .addData("group_id",GroupID)
                        .addData("group_name", GroupName)
                        .addData("admin",admin)
                        .addData("action", ".ADDUSER")
                        .addData("userMail",userMailToSend)
                        //.addData("recipient","99")
                        .build());

                userMail.setText("");




        AlertDialog alertDialog = new AlertDialog.Builder(AddUsersToGroup.this).create();
        alertDialog.setTitle("OK");
        alertDialog.setMessage("Request sent to "+userMailToSend);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //Intent intent = new Intent(AddGoupActivity.this, AddUsersToGroup.class);
                        //intent.putExtra("groupID","GroupNameToSend");
                        //startActivity(intent);
                    }
                });
        alertDialog.show();
        //alertDialog.dismiss();

            }
        });
    }
}
