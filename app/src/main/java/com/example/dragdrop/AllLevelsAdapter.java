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

public class AllLevelsAdapter extends ArrayAdapter<Level> {
    Context context;
    List<Level> objects;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseUsers;
    View view;
    Iterator<DataSnapshot> iter;
    User user;
    public AllLevelsAdapter(Context context, int resource, int textViewResourceId, List<Level> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context=context;
        this.objects=objects;
        databaseUsers = FirebaseDatabase.getInstance().getReference("/Users");
        firebaseAuth = FirebaseAuth.getInstance();
        user = new User();
        retreiveUser();
    }

    @Override
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
