package com.google.firebase.udacity.friendlychat;

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

import java.util.concurrent.atomic.AtomicInteger;//package com.google.firebase.quickstart.fcm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Alina on 4/20/2017.
 */

public class AddGoupActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_group_activity);


        final TextView GroupNameText=(TextView) findViewById(R.id.groupNameText);
        Button addButton=(Button) findViewById(R.id.addGroupButton);


        MyAppApplication CurrUserHelper =((MyAppApplication)getApplicationContext());
        final String CurrUser= CurrUserHelper.getCurrUser();
        final String CurrUserID= CurrUserHelper.getCurrID();
        final String CurrUserName= CurrUserHelper.getCurrUsername();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging fm = FirebaseMessaging.getInstance();
                final String GroupNameToSend= GroupNameText.getText().toString();
                AtomicInteger msgId;
                fm.send(new RemoteMessage.Builder("1066282643284" + "@gcm.googleapis.com")
                        .setMessageId("NewGroup")
                        .addData("my_message",GroupNameToSend)
                        .addData("action", ".ADDGROUP")
                        .addData("admin", CurrUser)
                        .addData("userID",CurrUserID)
                        .addData("adminName",CurrUserName)
                        .addData("message_type","Receipt")
                        //.addData("recipient","99")
                        .build());

                GroupNameText.setText("");

                AlertDialog alertDialog = new AlertDialog.Builder(AddGoupActivity.this).create();
                alertDialog.setTitle("OK");
                alertDialog.setMessage("Group Creation request sent");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(AddGoupActivity.this, GroupList.class);
                                intent.putExtra("groupID",GroupNameToSend);
                                startActivity(intent);
                            }
                        });
                alertDialog.show();
            }
        });
    }


}
