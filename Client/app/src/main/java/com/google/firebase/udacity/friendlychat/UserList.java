package com.google.firebase.udacity.friendlychat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Alina on 5/18/2017.
 */

public class UserList extends AppCompatActivity implements UserDelInterface {
    private ListView mMessageListView;
    private UserAdapter mMessageAdapter;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mMessagesDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private String GroupID;
    private String GroupName;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_list);

        Button AddUsers=(Button) findViewById(R.id.AddUserBtn);
        Button ChatBtn= (Button) findViewById(R.id.PplBtnChat);

        mMessageListView = (ListView) findViewById(R.id.listView);

        GroupID = getIntent().getStringExtra("GroupID");
        GroupName = getIntent().getStringExtra("GroupName");


        List<UserForDisplay> MyUserList = new ArrayList<>();
        mMessageAdapter = new UserAdapter(this, R.layout.item_user, MyUserList, this, true);
        mMessageListView.setAdapter(mMessageAdapter);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        MyAppApplication CurrUserHelper =((MyAppApplication)getApplicationContext());
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("Groups").child(GroupID).child("users");

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserForDisplay User = dataSnapshot.getValue(UserForDisplay.class);
                mMessageAdapter.add(User);
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



        AddUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserList.this, AddUsersToGroup.class);
                intent.putExtra("GroupID", GroupID);
                intent.putExtra("GroupName", GroupName);
                startActivity(intent);
            }
        });


        ChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserList.this, GroupChat.class);
                intent.putExtra("GroupID", GroupID);
                startActivity(intent);
            }
        });
    }

    @Override
    public void DeleteUser(int position) {
        UserForDisplay f;
        f= (UserForDisplay)mMessageListView.getItemAtPosition(position);
        mMessagesDatabaseReference.child(f.getMail().replace(".","*dot*")).removeValue();
        mMessageAdapter.remove(f);


        Log.d("del",f.getName().toString());




    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

}
