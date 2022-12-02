package com.example.dragdrop;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
@IgnoreExtraProperties
public class User {
    public String nickname;
    public String uid;
    public String key;
    public ArrayList<String> finishedLevels;
    public ArrayList<Level> publishedLevels;
    public int stars;

    public User(){
        this.finishedLevels = new ArrayList<>();
        this.publishedLevels = new ArrayList<>();
        this.stars = 0;
    }

    public User(String nickname, String uid, String key) {
        this.nickname = nickname;
        this.uid = uid;
        this.key = key;
        this.finishedLevels = new ArrayList<>();
        this.publishedLevels = new ArrayList<>();
        this.stars = 0;
    }
}
