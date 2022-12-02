package com.example.dragdrop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllLevelsActivity extends AppCompatActivity {
    ArrayList<Level> levels;
    DatabaseReference levelsRef;
    AllLevelsAdapter allLevelsAdapter;
    ListView lv;
    Level level;

    public String nickname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_levels);
        levels = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lvLevels);
        levelsRef = FirebaseDatabase.getInstance().getReference("/Levels");
        this.retrieveData();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Level level = levels.get(position);
                Intent intent = new Intent(AllLevelsActivity.this, RushHourGame.class);
                intent.putExtra("key", level.key);
                Log.d("put key", level.key + "");
                startActivity(intent);
            }
        });
    }


    public void retrieveData()
    {
        levelsRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                level = new Level();
                levels = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

//                    ArrayList<Vehicle> vehicles = new ArrayList<>();
                    //String[] res = levelStr[0].split("\\^");
//                    for (String word : res) {
                    String data = ds.getValue(String.class);
                    String[] levelStr = data.split("\\$");
//                        Vehicle vehicle = new Vehicle();
//                        String[] par = word.split(", ");
//                        vehicle.length = Integer.parseInt(par[0].trim());
//                        vehicle.direction = Integer.parseInt(par[1]);
//                        vehicle.x = Float.parseFloat(par[2]);
//                        vehicle.y = Float.parseFloat(par[3]);
//                        vehicle.w = Float.parseFloat(par[4]);
//                        vehicle.h = Float.parseFloat(par[5]);
//                        vehicle.code = par[6];
//                        vehicle.bitmap = Levels.getBitmap(vehicle.code);
//                        vehicles.add(vehicle);
//
//                    }
                    level.difficulty = Integer.parseInt(levelStr[1]);
                    level.title = levelStr[3];
                   // level.uid = levelStr[2];
                    level.key = levelStr[4];
                    //retrieveUser(level);
                    level.uid = "";
                    levels.add(level);
                    level = new Level();
                }
                allLevelsAdapter = new AllLevelsAdapter(AllLevelsActivity.this,0,0,levels);
                lv.setAdapter(allLevelsAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
//    public void retrieveUser(Level level){
//        DatabaseReference databaseUsers = FirebaseDatabase.getInstance().getReference("/Users");
//        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Iterator<DataSnapshot> iter = snapshot.getChildren().iterator();
//
//                for(DataSnapshot data: snapshot.getChildren()){
//                    User user = iter.next().getValue(User.class);
//                    Log.d("uid got" , level.uid +  " ");
//                    //String user1 = iter.next().child("uid").getValue(String.class);
//                    if(level.uid != null && user.uid != null)
//                        if(user.uid.equals(level.uid.substring(0,level.uid.length()-1))){
//                        nickname = user.nickname;
//
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//
//        });
//    }

}
