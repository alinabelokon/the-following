package com.google.firebase.udacity.friendlychat;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Alina on 5/30/2017.
 */

public class ChatMessageAdapter  extends ArrayAdapter<ChatMessage> {
   // UserDelInterface MyDeluser;
    public ChatMessageAdapter(Context context, int resource, List<ChatMessage> objects){
        super(context, resource, objects);
      //  MyDeluser= delFunc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_chat, parent, false);
        }

        //ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageChatTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.userNameTextView);
        TextView TimeStampView= (TextView) convertView.findViewById(R.id.TimeStampMsg);


        ChatMessage message = getItem(position);
        if(message!=null)
        {
            messageTextView.setVisibility(View.VISIBLE);
            authorTextView.setVisibility(View.VISIBLE);
            TimeStampView.setVisibility(View.VISIBLE);

            authorTextView.setText(message.getText().toString());
            messageTextView.setText("sent by :"+ message.getName().toString());
        TimeStampView.setText("at: "+message.getTimeStamp().toString());}

        /*
        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            messageTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(photoImageView);
        } else {
            messageTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            messageTextView.setText(message.getgroupName());
        }
        authorTextView.setText(message.getGroupID());
*/
        return convertView;
    }
}

