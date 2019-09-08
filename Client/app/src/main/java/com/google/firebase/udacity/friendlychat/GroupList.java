package com.google.firebase.udacity.friendlychat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;//package com.google.firebase.quickstart.fcm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alina on 5/3/2017.
 */

public class GroupList extends AppCompatActivity {
    DatabaseReference mDatabase;
    private ChildEventListener listener2;
    MyAppApplication CurrUserHelper;
    private MessageAdapter mMessageAdapter;
    private ListView mMessageListView;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mMessagesDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private Button addGrupBtn;

    private ProgressBar mProgressBar;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        MyAppApplication CurrUserHelper =((MyAppApplication)getApplicationContext());
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("UserGroups").child(CurrUserHelper.getCurrID());
        CurrUserHelper =((MyAppApplication)getApplicationContext());
        final String CurrUser=CurrUserHelper.getCurrUser();

        try {


            setContentView(R.layout.group_list);


            final String[] GroupNames2= {"1","2"};
            final String[] GroupNames= new String[2];
            GroupNames[0]="111";
            GroupNames[1]="222";
            mMessageListView = (ListView) findViewById(R.id.messageListView);

            addGrupBtn= (Button) findViewById(R.id.AddGroupButton);
            mProgressBar= (ProgressBar) findViewById(R.id.progressBarG);


            addGrupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GroupList.this, AddGoupActivity.class);

                    intent.putExtra("CurrUSer",CurrUser);
                    startActivity(intent);

                }
            });


            //NewPostActivity.java
            List<FriendlyMessage> friendlyMessages = new ArrayList<>();
            mMessageAdapter = new MessageAdapter(this, R.layout.item_message, friendlyMessages);
            mMessageListView.setAdapter(mMessageAdapter);

            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                    mMessageAdapter.add(friendlyMessage);
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("lgjslgjl", "loadPost:onCancelled", databaseError.toException());

                }
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
           // mMessagesDatabaseReference.removeEventListener(mChildEventListener);


            mMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                    FriendlyMessage f;
                    f= (FriendlyMessage)mMessageListView.getItemAtPosition(i);
                    Intent intent = new Intent(GroupList.this, GroupTabsActivity.class);
                    intent.putExtra("GroupID", f.getGroupID().toString());
                    intent.putExtra("GroupName", f.getgroupName().toString());
                    intent.putExtra("CurrUser",CurrUser);
                    startActivity(intent);
                }

            });



        }
        catch (Throwable t)
        {
            Log.d("lflf",t.toString());
        }



    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, GroupList.class));
        finish();

    }

}

