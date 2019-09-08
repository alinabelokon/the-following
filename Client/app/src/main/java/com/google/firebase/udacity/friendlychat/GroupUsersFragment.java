package com.google.firebase.udacity.friendlychat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Alina on 6/3/2017.
 */

public class GroupUsersFragment extends Fragment implements UserDelInterface{

    private ListView mMessageListView;
    private UserAdapter mMessageAdapter;
    private ChildEventListener mChildEventListener;
    //private ChildEventListener mChildEventListener2;
    private DatabaseReference mMessagesDatabaseReference;
    private DatabaseReference mMessagesDatabaseReference2;
    private FirebaseDatabase mFirebaseDatabase;
    private String GroupID;
    private String GroupName;
    private GroupAdmin ThisGroupAdmin;

    @Override
    public void onStart() {
        super.onStart();

        Button AddUsers=(Button) getView().findViewById(R.id.AddUserBtn);
        final TextView AdminName= (TextView) getView().findViewById(R.id.GroupAdmin);

        mMessageListView = (ListView) getView().findViewById(R.id.listView);

        GroupID = getActivity().getIntent().getStringExtra("GroupID");
        GroupName = getActivity().getIntent().getStringExtra("GroupName");




        mFirebaseDatabase = FirebaseDatabase.getInstance();
        final MyAppApplication CurrUserHelper =((MyAppApplication)getActivity().getApplicationContext());
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("Groups").child(GroupID).child("users");
        mMessagesDatabaseReference2 = mFirebaseDatabase.getReference().child("Groups").child(GroupID).child("GroupAdmin");

        mMessagesDatabaseReference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ThisGroupAdmin= dataSnapshot.getValue(GroupAdmin.class);
                AdminName.setText("Group Admin: "+ThisGroupAdmin.getName());
                mMessagesDatabaseReference.addChildEventListener(mChildEventListener);

                boolean isAdmin= ThisGroupAdmin.getMail().equals(CurrUserHelper.getCurrUser());
                SetAdaptr(isAdmin);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserForDisplay User = dataSnapshot.getValue(UserForDisplay.class);

                if(ThisGroupAdmin.getMail().equals(CurrUserHelper.getCurrUser()))
                        mMessageAdapter.add(User);

                else {

                    if(User.getStatus().equals("Accept"))
                    {
                        mMessageAdapter.add(User);
                    }


                }
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            public void onChildRemoved(DataSnapshot dataSnapshot) {



            }
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            public void onCancelled(DatabaseError databaseError) {
                Log.d("lgjslgjl", "loadPost:onCancelled", databaseError.toException());

            }
        };

        AddUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddUsersToGroup.class);
                intent.putExtra("GroupID", GroupID);
                intent.putExtra("GroupName", GroupName);
                startActivity(intent);
            }
        });
    }

    private void SetAdaptr(boolean isAdmin) {

        List<UserForDisplay> MyUserList = new ArrayList<>();
        mMessageAdapter = new UserAdapter(getActivity(), R.layout.item_user, MyUserList, this, isAdmin);
        mMessageListView.setAdapter(mMessageAdapter);
    }

    @Override
    public void DeleteUser(int position) {
        UserForDisplay f;
        f= (UserForDisplay)mMessageListView.getItemAtPosition(position);
        //mMessagesDatabaseReference.child(f.getMail().replace(".","*dot*")).removeValue();
        mMessageAdapter.remove(f);


        Log.d("del",f.getName().toString());



        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        AtomicInteger msgId;

        fm.send(new RemoteMessage.Builder("1066282643284" + "@gcm.googleapis.com")
                .setMessageId("111")
                .addData("group_id",GroupID)
                .addData("group_name", GroupName)
                .addData("userMail",f.getMail().replace(".","*dot*").toString())
                .addData("action", ".DELUSER")
                //.addData("recipient","99")
                .build());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_users, container, false);
    }
}
