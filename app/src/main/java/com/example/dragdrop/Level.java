package com.example.dragdrop;

import androidx.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

    @IgnoreExtraProperties
public class Level {
    public ArrayList<Vehicle> vehicles;
    public int difficulty;
    public String uid;
    public String title;
    public String key;


        public Level(){

    }

        @NonNull
        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            for(Vehicle v: vehicles){
                if(v != vehicles.get(vehicles.size() -1))
                str.append(v.toString()).append("^ ");
                else
                    str.append(v.toString());
            }
            return
                    str +
                    "$" + difficulty +
                    "$" + uid + '\'' +
                    "$" + title + '\'' +
                    "$" + key + '\'';
        }

        public Level(ArrayList<Vehicle> vehicles, String uid, String title, String key) {
        this.vehicles = vehicles;
        this.difficulty = 0;
        this.uid = uid;
        this.title = title;
        this.key = key;

    }
}
