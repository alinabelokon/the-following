package com.wedevol.xmpp.service.impl;

import com.google.firebase.database.*;

import com.wedevol.xmpp.bean.CcsInMessage;
import com.wedevol.xmpp.bean.CcsOutMessage;
import com.wedevol.xmpp.server.CcsClient;
import com.wedevol.xmpp.server.MessageHelper;
import com.wedevol.xmpp.service.PayloadProcessor;
import com.wedevol.xmpp.util.Util;

/**
 * Created by Alina on 4/22/2017.
 */
public class AddgroupProcessor implements PayloadProcessor  {
    DatabaseReference ref;
    String GroupID;

    @Override
    public void handleMessage(CcsInMessage inMessage) {
        CcsClient client = CcsClient.getInstance();
        String messageId = Util.getUniqueMessageId();
        String to = inMessage.getDataPayload().get(Util.PAYLOAD_ATTRIBUTE_RECIPIENT);
        String GroupName= inMessage.getDataPayload().get("my_message");
        String GroupAdmin= inMessage.getDataPayload().get("admin");
        String GroupAdminID= inMessage.getDataPayload().get("userID");
        String GroupAdminName= inMessage.getDataPayload().get("adminName");

        // TODO: handle the data payload sent to the client device. Here, I just
        // resend the incoming message.
        // CcsOutMessage outMessage = new CcsOutMessage(to, messageId, inMessage.getDataPayload());
        //String jsonRequest = MessageHelper.createJsonOutMessage(outMessage);
        //client.send(jsonRequest);




        ref = FirebaseDatabase
                .getInstance()
                .getReference();


        //ref.child("stam").setValue("bla");


        DatabaseReference ref2= ref.child("Groups");
        DatabaseReference pushedPostRef= ref2.push();
        pushedPostRef.child("GroupName").setValue(GroupName);
        GroupID= pushedPostRef.getKey();
        pushedPostRef.child("GroupID").setValue(GroupID);
        pushedPostRef.child("GroupAdmin").child("thisadmin").child("mail").setValue(GroupAdmin);
        pushedPostRef.child("GroupAdmin").child("thisadmin").child("name").setValue(GroupAdminName);
        pushedPostRef.child("users").child(GroupAdmin.replace(".","*dot*")).child("Mail").setValue(GroupAdmin);
        pushedPostRef.child("users").child(GroupAdmin.replace(".","*dot*")).child("Name").setValue(GroupAdminName);
        pushedPostRef.child("users").child(GroupAdmin.replace(".","*dot*")).child("Status").setValue("Accept");
        pushedPostRef.child("MiddlePoint").child("point").child("lat").setValue(0);
        pushedPostRef.child("MiddlePoint").child("point").child("lon").setValue(0);

        // pushedPostRef.child("users").child(GroupAdminID).child("Mail").setValue("test");
        //pushedPostRef.child("users").child(GroupAdminID).child("Name").setValue("test");
        //pushedPostRef.child("users").child(GroupAdminID).child("Status").setValue("Accept");
        //ref.child("stam2").setValue("bla");

//TODO read from DB the userID and add it to the DB in user
        // ref.child("UserGroups").child(GroupAdminID).child("userName").setValue(GroupAdmin);
        ref.child("UserGroups").child(GroupAdminID).child(GroupID).child("GroupName").setValue(GroupName);
        ref.child("UserGroups").child(GroupAdminID).child(GroupID).child("GroupID").setValue(GroupID);








        // child(GroupAdmin).setValue(GroupID);

        //.child("GroupName").setValue(GroupName);
        //ref.child("Usern")
    }

    private void removeMsg(String id) {
        ref.child("Groups").child(GroupID).child("chat").child(id).removeValue();
    }

}

