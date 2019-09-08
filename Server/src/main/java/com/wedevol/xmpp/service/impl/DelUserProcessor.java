package com.wedevol.xmpp.service.impl;

import com.google.firebase.database.*;
import com.wedevol.xmpp.bean.CcsInMessage;
import com.wedevol.xmpp.bean.CcsOutMessage;
import com.wedevol.xmpp.server.CcsClient;
import com.wedevol.xmpp.server.MessageHelper;
import com.wedevol.xmpp.service.PayloadProcessor;
import com.wedevol.xmpp.util.Util;

import java.util.logging.Level;

import static com.wedevol.xmpp.server.CcsClient.logger;

/**
 * Created by Alina on 6/15/2017.
 */
public class DelUserProcessor implements PayloadProcessor {



    String userMail;
    String GroupID;
    String GropName;
    String userNameToDisplay;


    DatabaseReference ref = FirebaseDatabase
            .getInstance()
            .getReference();
    public void handleMessage(CcsInMessage inMessage) {


        final CcsInMessage inMessage2=inMessage;
        final String messageId = Util.getUniqueMessageId();
        final String[] UserIDtoSend = new String[1];

        userMail= inMessage.getDataPayload().get("userMail");
        GroupID=inMessage.getDataPayload().get("group_id");
        //GropName=inMessage.getDataPayload().get("group_name");

        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference();

        ref.child("Groups").child(GroupID).child("users").child(userMail).removeValue();
        ref.child("UserGroups").child(userMail).child(GroupID).removeValue();

        ref.child("IDS").child(userMail.replace(".","*dot*")).child("token").addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String tmp = dataSnapshot.getValue(String.class);
                UserIDtoSend[0] = tmp;

                String to = UserIDtoSend[0];
                logger.log(Level.INFO, "test" + to);
                CcsOutMessage outMessage = new CcsOutMessage(to, messageId, inMessage2.getDataPayload());
                String jsonRequest = MessageHelper.createJsonOutMessage(outMessage);
                CcsClient client = CcsClient.getInstance();
                client.send(jsonRequest);
            }
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            public void onCancelled(DatabaseError databaseError) {}
        });


        ref.child("IDS").child(userMail.replace(".","*dot*")).child("UID").addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String tmp = dataSnapshot.getValue(String.class);
                UserIDtoSend[0] = tmp;

                String to = UserIDtoSend[0];
                removeGroup(to);

            }
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            public void onCancelled(DatabaseError databaseError) {}
        });





    }

    private void removeGroup(String to) {
        ref.child("UserGroups").child(to).child(GroupID).removeValue();
    }
}
