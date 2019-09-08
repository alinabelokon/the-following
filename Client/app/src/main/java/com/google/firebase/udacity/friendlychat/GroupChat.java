package com.google.firebase.udacity.friendlychat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Alina on 5/30/2017.
 */

public class GroupChat extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ChildEventListener mChildEventListenerOffActivity;
    private String GroupID;
    private ListView mMessageListView;
    private String GroupName;
    private MyAppApplication CurrUserHelper;
    private TextView MessageToSend;

    private ChatMessageAdapter mMessageAdapter;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chat_list);

        // Initialize Firebase components
        GroupID = getIntent().getStringExtra("GroupID");
        GroupName = getIntent().getStringExtra("GroupName");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("Groups").child(GroupID).child("Chat");
        mMessageListView = (ListView) findViewById(R.id.messageListView);

        Button SendBtn=(Button) findViewById(R.id.SendChatMsgBtn);
        Button UserListButn = (Button) findViewById(R.id.ChatBtnUserList);
        Button MapBytn=(Button) findViewById(R.id.ChatBtnMap);

        MessageToSend= (TextView) findViewById(R.id.chatMsgTxtBox);
        CurrUserHelper =((MyAppApplication)getApplicationContext());

        final List<ChatMessage> ChatList = new ArrayList<>();

        mMessageAdapter = new ChatMessageAdapter(this, R.layout.item_chat, ChatList);

        mMessageListView.setAdapter(mMessageAdapter);

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage msg = dataSnapshot.getValue(ChatMessage.class);
                Log.d("Test",msg.getText());
                mMessageAdapter.add(msg);

                mMessageListView.setSelection(mMessageAdapter.getCount()-1);


            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            public void onChildRemoved(DataSnapshot dataSnapshot) {



            }
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            public void onCancelled(DatabaseError databaseError) {
                Log.d("lgjslgjl", "loadPost:onCancelled", databaseError.toException());

            }
        };










        mMessagesDatabaseReference.addChildEventListener(mChildEventListener);


        SendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TmpText= MessageToSend.getText().toString();
                if(!TmpText.isEmpty())
                {
                ChatMessage tmpMsg= new ChatMessage();
                tmpMsg.setText(TmpText);
                    tmpMsg.setName(CurrUserHelper.getCurrUser());
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yyyy");
                    String currentDateandTime = sdf.format(new Date());
                    tmpMsg.setTimeStamp(currentDateandTime);
                mMessagesDatabaseReference.push().setValue(tmpMsg);

                }
                MessageToSend.setText("");
            }
        });

        UserListButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupChat.this, UserList.class);
                intent.putExtra("GroupID", GroupID);
                intent.putExtra("GroupName", GroupName);
                startActivity(intent);
            }
        });

        MapBytn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(GroupChat.this, GroupMap.class);
                //intent.putExtra("GroupID", GroupID);
                //intent.putExtra("GroupName", GroupName);
                //startActivity(intent);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
       // CurrUserHelper.activityResumed();
       //mMessagesDatabaseReference.removeEventListener(mChildEventListenerOffActivity);
        //mMessagesDatabaseReference.addChildEventListener(mChildEventListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
       // CurrUserHelper.activityPaused();

       // mMessagesDatabaseReference.removeEventListener(mChildEventListener);
        //mMessagesDatabaseReference.addChildEventListener(mChildEventListenerOffActivity);

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, GroupList.class));
        finish();

    }



}



