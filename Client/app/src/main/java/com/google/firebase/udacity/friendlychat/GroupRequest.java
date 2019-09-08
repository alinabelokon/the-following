package com.google.firebase.udacity.friendlychat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;//package com.google.firebase.quickstart.fcm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Alina on 4/20/2017.
 */

public class GroupRequest extends AppCompatActivity {


    final String CurUser="ali";
    MyAppApplication CurrUserHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_request);

        final DatabaseReference mDatabase;

        CurrUserHelper =((MyAppApplication)getApplicationContext());

        TextView text= (TextView) findViewById(R.id.textView2);
        Button accept = (Button) findViewById(R.id.AcceptGroupReq);
        Button deny = (Button) findViewById(R.id.DenyGroupReq);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        final String GroupName= getIntent().getStringExtra("GroupName");
        String admin=getIntent().getStringExtra("admin");
        text.setText(admin+" wants to add you to the group: " + GroupName);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String GroupID=getIntent().getStringExtra("GroupID");
                mDatabase.child("Groups").child(GroupID).child("users").child(CurrUserHelper.getCurrUser().replace(".","*dot*")).child("Status").setValue("Accept");

                mDatabase.child("UserGroups").child(CurrUserHelper.getCurrID()).child(GroupID).child("GroupID").setValue(GroupID);
                mDatabase.child("UserGroups").child(CurrUserHelper.getCurrID()).child(GroupID).child("GroupName").setValue(GroupName);

                Intent intent = new Intent(GroupRequest.this, GroupList.class);
                //intent.putExtra("groupID",GroupNameToSend);
                startActivity(intent);

            }
        });


        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String GroupID=getIntent().getStringExtra("GroupID");
                mDatabase.child("Groups").child(GroupID).child("users").child(CurrUserHelper.getCurrUser().replace(".","*dot*")).child("Status").setValue("Denny");
                Intent intent = new Intent(GroupRequest.this, MainActivity.class);
                //intent.putExtra("groupID",GroupNameToSend);
                startActivity(intent);

            }
        });



    }}
