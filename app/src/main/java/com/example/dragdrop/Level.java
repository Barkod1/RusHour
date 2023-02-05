package com.example.dragdrop;

import androidx.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

  //מחלקה המייצגת שלב
    @IgnoreExtraProperties
public class Level {
        //מערך-רשימה המצביע על הרכבים של השלב
    public ArrayList<Vehicle> vehicles;
    //רמת קושי השלב
    public int difficulty;
    //ID של עורך השלב
    public String uid;
    //שם השלב
    public String title;
    //מפתח השלב בענן
    public String key;


        public Level(){
    }

    //get string format for this level
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

        //constructor
        public Level(ArrayList<Vehicle> vehicles, String uid, String title, String key) {
        this.vehicles = vehicles;
        this.difficulty = 0;
        this.uid = uid;
        this.title = title;
        this.key = key;

    }
}
