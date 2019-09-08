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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Alina on 6/3/2017.
 */

public class GroupChatFragment extends Fragment {

    private static final int MAX_MSG = 100;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private DatabaseReference    mMessagesDatabaseReference2;

    private ChildEventListener mChildEventListenerOffActivity;
    private String GroupID;
    private ListView mMessageListView;
    private String GroupName;
    private MyAppApplication CurrUserHelper;
    private TextView MessageToSend;

    private ChatMessageAdapter mMessageAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_chat, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        GroupID = getActivity().getIntent().getStringExtra("GroupID");
        GroupName = getActivity().getIntent().getStringExtra("GroupName");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("Groups").child(GroupID).child("Chat");
        mMessagesDatabaseReference2=mFirebaseDatabase.getReference().child("Groups").child(GroupID);

        mMessageListView = (ListView) getView().findViewById(R.id.messageListView);
        Button SendBtn=(Button) getView().findViewById(R.id.SendChatMsgBtn);
        MessageToSend= (TextView) getView().findViewById(R.id.chatMsgTxtBox);

        CurrUserHelper =((MyAppApplication)getActivity().getApplicationContext());

        final List<ChatMessage> ChatList = new ArrayList<>();

        mMessageAdapter = new ChatMessageAdapter(getActivity(), R.layout.item_chat, ChatList);

        mMessageListView.setAdapter(mMessageAdapter);
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

        mMessagesDatabaseReference2.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals("Chat"))
                {
                    if((dataSnapshot.getChildrenCount())>=MAX_MSG){
                      //  dataSnapshot.getChildren();
                    for (DataSnapshot child : dataSnapshot.getChildren()){

                        removeChtMsg(child.getKey());
                        break;
                    }}
                }
            }
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.getKey().equals("Chat"))
                {
                    if((dataSnapshot.getChildrenCount())>=MAX_MSG){
                        for (DataSnapshot child : dataSnapshot.getChildren()){

                            removeChtMsg(child.getKey());
                            break;
                        }}
                }
            }
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            public void onChildMoved(DataSnapshot dataSnapshot, String s){}
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }


    private ChildEventListener mChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            ChatMessage msg = dataSnapshot.getValue(ChatMessage.class);
            Log.d("Test", msg.getText());
            int count=mMessageAdapter.getCount();
            mMessageAdapter.add(msg);

            mMessageListView.setSelection(mMessageAdapter.getCount() - 1);

            if (getcount(s)>=4)
            {
                for (DataSnapshot child : dataSnapshot.getChildren())
                {
                   String key= s;
                    //removeChtMsg(key);
                    break;

                }
            }


        }



        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        }

        public void onChildRemoved(DataSnapshot dataSnapshot) {


        }

        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        }

        public void onCancelled(DatabaseError databaseError) {
            Log.d("lgjslgjl", "loadPost:onCancelled", databaseError.toException());

        }
    };

    private int getcount(String s) {
return 1;
    }

    private void removeChtMsg(String key) {
        mMessagesDatabaseReference.child(key).removeValue();
    }



}
