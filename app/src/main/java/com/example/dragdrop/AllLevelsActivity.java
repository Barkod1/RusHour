package com.example.dragdrop;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
//המחלקה מראה את כל השלבים שבענן ונותנת אפשרות לשחק אותם
public class AllLevelsActivity extends AppCompatActivity {
    //מערך של כל השלבים המובאים מהענן
    ArrayList<Level> levels;
    //רפרנס לענן ששומר את השלבים
    DatabaseReference levelsRef;
    //האדפטר של השלבים
    AllLevelsAdapter allLevelsAdapter;
    //הרשימה בה רואים את השלבים
    ListView lv;
    //שלב מסויים
    Level level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_levels);
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(new BatteryCheckReceiver(), filter);
        levels = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lvLevels);
        levelsRef = FirebaseDatabase.getInstance().getReference("/Levels");
        this.retrieveData();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Level level = levels.get(position);
                Intent intent = new Intent(AllLevelsActivity.this, RushHourContext.class);
                intent.putExtra("key", level.key);
                startActivity(intent);
            }
        });
    }

//get the data for each level
    public void retrieveData()
    {
        levelsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                level = new Level();
                levels = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String data = ds.getValue(String.class);
                    String[] levelStr = data.split("\\$");
                    level.difficulty = Integer.parseInt(levelStr[1]);
                    level.title = levelStr[3];
                    level.key = levelStr[4];
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

    //if hits the back button go back to menu
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);

    }
}
