package com.google.firebase.udacity.friendlychat;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

public class UserAdapter extends ArrayAdapter<UserForDisplay> {
    UserDelInterface MyDeluser;
    boolean delBut;
    public UserAdapter(Context context, int resource, List<UserForDisplay> objects, UserDelInterface delFunc, boolean isAdmin)
    {
        super(context, resource, objects);
        MyDeluser= delFunc;
        delBut=isAdmin;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_user, parent, false);
        }

       // ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
        TextView userTextView = (TextView) convertView.findViewById(R.id.userTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView userStatus= (TextView) convertView.findViewById(R.id.Status);



        Button DelUserButton = (Button) convertView.findViewById(R.id.DelUser);

        if(delBut) {
            DelUserButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getItemId(position);
                    MyDeluser.DeleteUser(position);
                }
            });
        }
        else {
            DelUserButton.setVisibility(View.INVISIBLE);
            userStatus.setVisibility(View.INVISIBLE);
        }

        UserForDisplay message = getItem(position);

        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            userTextView.setVisibility(View.VISIBLE);
           // photoImageView.setVisibility(View.VISIBLE);
            //Glide.with(photoImageView.getContext())
              //      .load(message.getPhotoUrl())
                //    .into(photoImageView);
        } else {
            userTextView.setVisibility(View.VISIBLE);
            //photoImageView.setVisibility(View.GONE);
            userTextView.setText(message.getName());

        }
        authorTextView.setText(message.getMail());
        userStatus.setText("Status: "+message.getStatus());

        return convertView;
    }
}
