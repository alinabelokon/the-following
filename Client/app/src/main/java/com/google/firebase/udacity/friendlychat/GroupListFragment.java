package com.google.firebase.udacity.friendlychat;

/**
 * Created by Alina on 5/30/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class GroupListFragment extends Fragment {
    private static final String TAG = "Tab1Fragment";

    DatabaseReference mDatabase;
    private ChildEventListener listener2;

    private MessageAdapter mMessageAdapter;
    private ListView mMessageListView;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mMessagesDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;


    private Button btnTEST;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grouplistfragment,container,false);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        MyAppApplication CurrUserHelper =((MyAppApplication)getApplicationContext());
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("UserGroups").child(CurrUserHelper.getCurrID());


        try {


            getActivity().setContentView(R.layout.group_list);


            final String[] GroupNames2= {"1","2"};
            final String[] GroupNames= new String[2];
            GroupNames[0]="111";
            GroupNames[1]="222";
            mMessageListView = (ListView) getActivity().findViewById(R.id.messageListView);


            //ArrayList GroupNames2 = new



// ...


            // mDatabase = FirebaseDatabase.getInstance().getReference().child("UserGrops").child("12345678").child("Groups");
            //DatabaseReference childd = mDatabase.child("UserGroups/12345678/Groups");

           /* listener2= new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    GroupDataFromDB data = dataSnapshot.getValue(GroupDataFromDB.class);
                    String test = data.getGroupNameToDis();
                    Log.d("",test);

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
            };

            mDatabase.addChildEventListener(listener2);

           /* childd.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //for(DataSnapshot tmp : dataSnapshot.getChildren())
                    //{
                        int i=0;

                        //GroupDataFromDB Groups= new GroupDataFromDB();
                                //dataSnapshot.getValue(GroupDataFromDB.class);

                        //Groups.setGroupIDtoDis(tmp.child("")getValue(GroupDataFromDB.class).getGroupNameToDis());
                        GroupNames[i]= dataSnapshot.getValue(String.class);

                                //Groups.getGroupNameToDis();

                        i=i++;
                    //}
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            /*ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI


                    for(DataSnapshot tmp : dataSnapshot.getChildren())
                    {
                        int i=0;
                        GroupDataFromDB Groups= dataSnapshot.getValue(GroupDataFromDB.class);
                        GroupNames[i]= tmp.getValue(GroupDataFromDB.class).getGroupNameToDis();

                                i=i++;
                    }
                    // ...
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            };*/

            //NewPostActivity.java
            List<FriendlyMessage> friendlyMessages = new ArrayList<>();
            mMessageAdapter = new MessageAdapter(this.getContext(), R.layout.item_message, friendlyMessages);
            mMessageListView.setAdapter(mMessageAdapter);

            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                    mMessageAdapter.add(friendlyMessage);
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
                    Intent intent = new Intent(getActivity(), UserList.class);
                    intent.putExtra("GroupID", f.getGroupID().toString());
                    intent.putExtra("GroupName", f.getgroupName().toString());
                    startActivity(intent);
                }

            });



            // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.group_list_item,GroupNames);
            // ListView GroupList = (ListView) findViewById(R.id.messageListView);
            //GroupList.setAdapter(adapter);

        }
        catch (Throwable t)
        {
            Log.d("lflf",t.toString());
        }

        //HashMap<String,String> ListHashMap= new HashMap<>();



        return view;
    }
}
