package com.wedevol.xmpp;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.*;
import com.wedevol.xmpp.server.MessageHelper;
//import main.java.com.wedevol.xmpp.Util;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.XMPPException;

import com.wedevol.xmpp.bean.CcsOutMessage;
import com.wedevol.xmpp.server.CcsClient;
import com.wedevol.xmpp.server.MessageHelper;
import com.wedevol.xmpp.util.Util;

//import com.google.firebase;

/**
 * Entry Point class for the XMPP Server in dev mode for debugging and testing
 * purposes
 */
public class EntryPoint {

    static final String fcmProjectSenderId = "1066282643284";

    //static final String fcmServerKey ="fSxYe281Bsc:APA91bFpgj7nFT4HL4iNH4WducgWSb1UFigGHWLfjnfl_v9qp1e7V5kuiS2hZQDg5ekIn-Iubxl9Wdna1MPzwlCPlLwNZnMJs2QAdJeXv5pD6HrUHRi2z2lREgIMs3uZvENgjsKp2AQJ";
    static final String fcmServerKey = "AAAA-ENl71Q:APA91bGDr_Kf-R_N0al8EjdZe-5p7s09dp3-oFVaYc-cqJ2_aKnMQoT_VvQSthp7Ayw7EYgyFbrMXbXNrtn4vuBlTXeNJvKq0wSZqeC49Lr1tIkGA-BwZsSei5YSaLRdFagWoLl48Pn-eqt5dMjpX0MBhWPZ78_HlQ";

    static CcsClient ccsClient = CcsClient.prepareClient(fcmProjectSenderId, fcmServerKey, true);

    public static void main(String[] args) throws IOException {


        FileInputStream serviceAccount =
                null;
        try {
            serviceAccount = new FileInputStream("serviceAccountKey.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                .setDatabaseUrl("https://braude2017-ce2dc.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);


        // Fetch the service account key JSON file contents
        //FileInputStream serviceAccount = new FileInputStream("path/to/serviceAccountCredentials.json");

// Initialize the app with a service account, granting admin privileges
        //FirebaseOptions options = new FirebaseOptions.Builder()
        //		.setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
        //		.setDatabaseUrl("https://databaseName.firebaseio.com")
        //		.build();
        //FirebaseApp.initializeApp(options);

// As an admin, the app has access to read and write all data, regardless of Security Rules
        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference("restricted_access/secret_document");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object document = dataSnapshot.getValue();
                System.out.println(document);
            }

            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //final String toRegId = "dMmhIo59b14:APA91bFhs-jlkqWsPVslKiO9REbN1EhOhyVb4uQZtP1d0ECM_2sTXD169cRG21rpSt4hz-vNfBvr_mFMxSX1ES1srPzDHHI8j1x0hf6STbWPoDEOGwvBnX6jVn5ZPFYbVn2se-9TcHl8";
        //final String toRegId ="APA91bElr_IEtSx4a9esCTRBJICqUtm-2Vz4YtKy0wrPa1Yd3fk-XLqF7InrzCuY6bMzQOkjkfAN94tNHiz15wuN1-UwakLdC1wIVhWHENiLrUKh4GcuEZhRHEx0ckYtMjQq8ITSdBmp";
//final String toRegId="exd5sC9k2hs:APA91bEquW8q371bgvjODoB8vbsoOGC20rJNOZJfLI7mxGy4x4TA1uG921u-nBswRC663OF-3dEzBTUbO35HVpEGKpxqqoc6qioImU_LJOpWZ4FOmnGEh4CrSnuTFc6M70utysa9e56g";

        //sfinal String toRegId="dz9ZU-4bYe4:APA91bElr_IEtSx4a9esCTRBJICqUtm-2Vz4YtKy0wrPa1Yd3fk-XLqF7InrzCuY6bMzQOkjkfAN94tNHiz15wuN1-UwakLdC1wIVhWHENiLrUKh4GcuEZhRHEx0ckYtMjQq8ITSdBmp";
//final String toRegId="exd5sC9k2hs:APA91bEquW8q371bgvjODoB8vbsoOGC20rJNOZJfLI7mxGy4x4TA1uG921u-nBswRC663OF-3dEzBTUbO35HVpEGKpxqqoc6qioImU_LJOpWZ4FOmnGEh4CrSnuTFc6M70utysa9e56g";
//final String toRegId="d1cxbZPWSpo:APA91bE9X-g45BAEHs80w5wDS_Gpk4LCVa3BZrLm9lSFL-HzntW2AupYcreTavSY0L1kAbct1Cffz7izYwkFAxfLHHXncyrdg4lkD1nwKAP6YbrLWrdZLc6jTSYLYJP_TgItKwED8bcc";
        final String toRegId="%sfcN2OGisSWM:bPA91bFGrx2GRXRfyEJ_kTp2kJto-BEyldUP5_jpptFxLpkTm3GKmHy_p498R305BVpmHW0Zo9CAPIgy2S5MPhQ_zeS32eD5nI9Cl6mLbIKLyHuyAHQ-hqzSUb8ZAzkpuMHiy7sKUMrb";
        //final String toRegId="eF9lohb_Azk:APA91bEZokQmXKeEXdUHKqIZ2pBUdplHwESIDRXr4iZfw45aH1YZPJi58pVv7WqTo-A4sAH0PoWYLncjE2Clo29XtahZGBy9Uru5I78KTMC0Ao8CNKDF-tPHku5dyON_iy9_z2CguGE_";
        //final String toRegId="dz9ZU-4bYe4:APA91bElr_I	EtSx4a9esCTRBJICqUtm-2Vz4YtKy0wrPa1Yd3fk-XLqF7InrzCuY6bMzQOkjkfAN94tNHiz15wuN1-UwakLdC1wIVhWHENiLrUKh4GcuEZhRHEx0ckYtMjQq8ITSdBmp";
        //final String toRegId ="fSxYe281Bsc:APA91bFpgj7nFT4HL4iNH4WducgWSb1UFigGHWLfjnfl_v9qp1e7V5kuiS2hZQDg5ekIn-Iubxl9Wdna1MPzwlCPlLwNZnMJs2QAdJeXv5pD6HrUHRi2z2lREgIMs3uZvENgjsKp2AQJ";
        String data = "This is the simple sample message";
        ArrayList<String> res= new ArrayList<String>();

        res.add(toRegId);





        try {

            while (true)
            {
                char c = (char)System.in.read();
                switch (c)
                {
                    case 'q':
                        return;
                    case 's':{
                        //SendMessage(toRegId, data);
                        //ref.setValue("jjj");

                        try {
                            ccsClient.connect();
                        } catch (XMPPException e) {
                            e.printStackTrace();
                        }

                        //ccsClient.sendBroadcast(,res);
                    }

                    default:
                        continue;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void SendMessage(String toRegId, String data) {

        CcsClient ccsClient = CcsClient.prepareClient(fcmProjectSenderId, fcmServerKey, true);



        // Send a sample downstream message to a device
        String messageId = Util.getUniqueMessageId();
        Map<String, String> dataPayload = new HashMap<String, String>();
        dataPayload.put(Util.PAYLOAD_ATTRIBUTE_MESSAGE, data);
        CcsOutMessage message = new CcsOutMessage(toRegId, messageId, dataPayload);
        String jsonRequest = MessageHelper.createJsonOutMessage(message);
        ccsClient.send(jsonRequest);
    }


}
