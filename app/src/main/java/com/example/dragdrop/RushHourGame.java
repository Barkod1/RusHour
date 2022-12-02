package com.example.dragdrop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class RushHourGame extends AppCompatActivity implements View.OnClickListener {
    BoardGame boardGame;
    ArrayList<Vehicle> vehicles;
    DatabaseReference levelRef;
    FirebaseAuth firebaseAuth;
    private DatabaseReference database, databaseUsers;
    private User user = new User();
    Level currentLevel;
    Iterator<DataSnapshot> iter;
    RushHourGame itself;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            key = extras.getString("key");
            Log.d("key" , key + " ");
        }
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("/Levels");
        databaseUsers = FirebaseDatabase.getInstance().getReference("/Users");
        retreiveUser();
        this.itself = this;
        this.retrieveData();


    }



    public void retrieveData()
    {
        database.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Level level = new Level();

                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    String data = ds.getValue(String.class);
                    vehicles = new ArrayList<>();

                    String[] levelStr = data.split("\\$");
                    String[] res = levelStr[0].split("\\^");

                    int i = 0;
                    for (String word : res) {
                        Vehicle vehicle = new Vehicle();
                        String[] par = word.split(", ");
                        vehicle.length = Integer.parseInt(par[0].trim());
                        vehicle.direction = Integer.parseInt(par[1]);
                        vehicle.x = Float.parseFloat(par[2]);
                        vehicle.y = Float.parseFloat(par[3]);
                        vehicle.w = Float.parseFloat(par[4]);
                        vehicle.h = Float.parseFloat(par[5]);
                        vehicle.code = par[6];
                        vehicle.bitmap = Levels.getBitmap(vehicle.code);
                        vehicles.add(vehicle);
                        i++;

                    }
                    level = new Level();
                    level.difficulty = Integer.parseInt(levelStr[1]);
                    level.uid = levelStr[2];
                    level.title = levelStr[3];
                    level.key = levelStr[4];
                    currentLevel = level;
                    Log.d("key",key +  " " + level.key);
                    if(key != null){
                        if(level.key.equals(key)){
                            break;
                        }
                    }
                    else if(!user.finishedLevels.contains(level.key)){
                        break;
                    }
                }

                FrameLayout frameLayout  =(FrameLayout)findViewById(R.id.mainfram);
                Vehicle[] arr = new Vehicle[vehicles.size()];
                arr = vehicles.toArray(arr);
                boardGame = new BoardGame(RushHourGame.this,frameLayout ,arr, itself);
                frameLayout.addView(boardGame);

                               }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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

                if(uid.equals(uidcheck)){
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

    public void updateDialogDetails(){
        boardGame.tvTitle.setText("Great Job, " +user.nickname + "!");
        databaseUsers = FirebaseDatabase.getInstance().getReference("/Users/" + user.key);
        if(!user.finishedLevels.contains(currentLevel.key))
        user.finishedLevels.add(currentLevel.key);
        user.stars += currentLevel.difficulty;
        databaseUsers.setValue(user);
    }


    @Override
    public void onClick(View view) {
        boardGame.d.dismiss();
        if(view == boardGame.btnNext){

            retrieveData();

        }
        if(view == boardGame.btnBack){
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        }

        if(view == boardGame.btnShare){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, boardGame.vehiclesArr.toString());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
    }
}