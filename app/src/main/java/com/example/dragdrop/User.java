package com.example.dragdrop;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

//המחלקה מייצגת את המשתמש
@IgnoreExtraProperties
public class User {
    //כינוי המשתמש
    public String nickname;
    //הID של המשתמש בענן
    public String uid;
    //המפתח של המשתמש בענן
    public String key;
    //מערך-רשימה של השלבים שהמשתמש פתר
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
