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
 * Created by Alina on 4/23/2017.
 */
public class AddUserProcessor implements PayloadProcessor {

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


        //  String to = inMessage.getDataPayload().get(Util.PAYLOAD_ATTRIBUTE_RECIPIENT);
        // String to="ep0q2qE4p24:APA91bF57N9akYnKbJUCJ_8W3vVHXZErd2dADTDoveffsi1MWZYHEU_mkELQickKw32UMKgzS7WYIkupHhmgFCKmB1XzVb49X9w7CVrAOrQf8k96JrbYldiUQCdKL8rR1voJScRVRdCX";
        //String to="eF9lohb_Azk:APA91bEZokQmXKeEXdUHKqIZ2pBUdplHwESIDRXr4iZfw45aH1YZPJi58pVv7WqTo-A4sAH0PoWYLncjE2Clo29XtahZGBy9Uru5I78KTMC0Ao8CNKDF-tPHku5dyON_iy9_z2CguGE_";

        //String to="%sfcN2OGisSWM:APA91bFGrx2GRXRfyEJ_kTp2kJto-BEyldUP5_jpptFxLpkTm3GKmHy_p498R305BVpmHW0Zo9CAPIgy2S5MPhQ_zeS32eD5nI9Cl6mLbIKLyHuyAHQ-hqzSUb8ZAzkpuMHiy7sKUMrb";
        userMail= inMessage.getDataPayload().get("userMail");
        GroupID=inMessage.getDataPayload().get("group_id");
        GropName=inMessage.getDataPayload().get("group_name");


        // TODO: handle the data payload sent to the client device. Here, I just
        // resend the incoming message.
        // CcsOutMessage outMessage = new CcsOutMessage(to, messageId, inMessage.getDataPayload());
        //String jsonRequest = MessageHelper.createJsonOutMessage(outMessage);
        //client.send(jsonRequest);





        ref.child("Groups").child(GroupID).child("users").child(userMail.replace(".","*dot*")).child("Status").setValue("pending");
        ref.child("Groups").child(GroupID).child("users").child(userMail.replace(".","*dot*")).child("Mail").setValue(userMail);
        ref.child("Groups").child(GroupID).child("users").child(userMail.replace(".","*dot*")).child("Name").setValue(GropName);


//userMail.replace(".","*dot*"))


        ref.child("IDS").child(userMail.replace(".","*dot*")).child("Info").addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userNameToDisplay=dataSnapshot.getValue(String.class);
                logger.log(Level.INFO,userNameToDisplay);

                ref.child("Groups").child(GroupID).child("users").child(userMail.replace(".","*dot*")).child("Name").setValue(userNameToDisplay);
                //SetUserInfo(userNameToDisplay);


            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ref.child("IDS").child(userMail.replace(".","*dot*")).child("token").addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //if (dataSnapshot.exists()) {
                //  for (DataSnapshot data : dataSnapshot.getChildren()) {

                String tmp = dataSnapshot.getValue(String.class);
                UserIDtoSend[0] = tmp;

                String to = UserIDtoSend[0];
                logger.log(Level.INFO, "test" + to);
                CcsOutMessage outMessage = new CcsOutMessage(to, messageId, inMessage2.getDataPayload());
                String jsonRequest = MessageHelper.createJsonOutMessage(outMessage);
                CcsClient client = CcsClient.getInstance();
                client.send(jsonRequest);

                //User currentUser = data.getValue(User.class);
                //Log.i("THE_SNAPSHOT_AS_STRIN", data.toString());
                //Log.i("THE_USERS_EMAIL:::", currentUser.getEmail());
                // }
                //  } else {
                // User does not exist at this point.
                // }


            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            public void onCancelled(DatabaseError databaseError) {

                logger.log(Level.INFO, "error" + databaseError.toException().toString());
            }
        });

        // to=UserIDtoSend[0];
        //CcsOutMessage outMessage = new CcsOutMessage(to, messageId, inMessage.getDataPayload());
        //String jsonRequest = MessageHelper.createJsonOutMessage(outMessage);
        //client.send(jsonRequest);
    }

    private void SetUserInfo(String name) {


    }
}
