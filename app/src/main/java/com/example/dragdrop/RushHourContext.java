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

//המחלקה בה מוצג הקנבס. המחלקה אחראית לסנכרון השלב והקשר עם פיירבייס במקרה של ניצחון
public class RushHourContext extends AppCompatActivity implements View.OnClickListener {
    //מחלקת המשחק של הקנבס
    RushHourCanvas rushHourCanvas;

    //הרכבים של השלב המשוחק
    ArrayList<Vehicle> vehicles;
    //הרפרנס לשלב בענן
    DatabaseReference levelRef;
    //פיירבייס לקבלת מידע על המשתמש והשלב
    FirebaseAuth firebaseAuth;
    private DatabaseReference database, databaseUsers;
    private User user = new User();

    //השלב הנוכחי המשוחק
    Level currentLevel;
    //איטרטור לפיירבייס
    Iterator<DataSnapshot> iter;

    //דוגמא של עצמו
    RushHourContext itself;

    //המפתח לפיירבייס
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
        database = FirebaseDatabase.getInstance().getReference("/GetBitmap");
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
                        vehicle.bitmap = GetBitmap.getBitmap(vehicle.code);
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
                rushHourCanvas = new RushHourCanvas(RushHourContext.this,frameLayout ,arr, itself);
                frameLayout.addView(rushHourCanvas);

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
        rushHourCanvas.tvTitle.setText("Great Job, " +user.nickname + "!");
        databaseUsers = FirebaseDatabase.getInstance().getReference("/Users/" + user.key);
        if(!user.finishedLevels.contains(currentLevel.key))
        user.finishedLevels.add(currentLevel.key);
        user.stars += currentLevel.difficulty;
        databaseUsers.setValue(user);
    }


    @Override
    public void onClick(View view) {
        rushHourCanvas.d.dismiss();
        if(view == rushHourCanvas.btnNext){

            retrieveData();

        }
        if(view == rushHourCanvas.btnBack){
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        }

        if(view == rushHourCanvas.btnShare){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, rushHourCanvas.vehiclesArr.toString());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
    }
}