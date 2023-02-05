package com.example.dragdrop;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.List;

// custom adapter class that extends the ArrayAdapter class
public class AllLevelsAdapter extends ArrayAdapter<Level> {

    // context of the activity
    Context context;
    // list of levels to display
    List<Level> objects;
    // instance of FirebaseAuth to access user authentication information
    FirebaseAuth firebaseAuth;
    // database reference to the 'Users' node in the Firebase Realtime Database
    DatabaseReference databaseUsers;
    // a view object that is inflated from the custom_layout.xml file
    View view;
    // an iterator to iterate through the children of the 'Users' node in the database
    Iterator<DataSnapshot> iter;
    // a user object to store the current user's information
    User user;

    // constructor to initialize the custom adapter class
    public AllLevelsAdapter(Context context, int resource, int textViewResourceId, List<Level> objects) {
        // call the super class constructor
        super(context, resource, textViewResourceId, objects);

        // initialize the context variable
        this.context=context;
        // initialize the objects variable (list of levels)
        this.objects=objects;
        // get a reference to the 'Users' node in the Firebase Realtime Database
        databaseUsers = FirebaseDatabase.getInstance().getReference("/Users");
        // get an instance of the FirebaseAuth object
        firebaseAuth = FirebaseAuth.getInstance();
        // initialize the user object
        user = new User();
        // call the retreiveUser method to retrieve the current user's information from the database
        retreiveUser();
    }

    // method to get the view for each level in the list
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        view = layoutInflater.inflate(R.layout.custom_layout, parent, false);
        TextView tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        TextView tvPublisher = (TextView) view.findViewById(R.id.tvPublisher);
        TextView tvStars = (TextView) view.findViewById(R.id.tvStars);
        Level temp = objects.get(position);
        tvTitle.setText(temp.title.substring(0,temp.title.length()-1));
        tvPublisher.setText(temp.uid);
        ImageView imgView = (ImageView)view.findViewById(R.id.imgIsDone);
        if(user != null)
            if(user.finishedLevels.contains(temp.key)){
                Log.d("position ", temp.key +  "");
                imgView.setImageResource(android.R.drawable.checkbox_on_background);
            }
        tvStars.setText("difficulty: "+temp.difficulty);

        return view;
    }

    //get the firebase user
    public void retreiveUser(){
        databaseUsers = FirebaseDatabase.getInstance().getReference("/Users");
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uid = firebaseAuth.getCurrentUser().getUid().toString();
                iter = snapshot.getChildren().iterator();
                for(DataSnapshot data: snapshot.getChildren()){
                    String uidcheck =iter.next().child("uid").getValue(String.class);
                    Log.d("string uid", uidcheck + " ");
                    if(uidcheck.equals(uid)){
                        user = data.getValue(User.class);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
