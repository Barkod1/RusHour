package com.example.dragdrop;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class AddLevelContext extends Activity implements View.OnClickListener {
    Button btnUpload;
    public ArrayList<TempVehicle> copyVehicles;

    Level level;
    EditText title;
    AddLevelCanvas boardGame;
    IsLegalCheck isLegalCheck;
    Button btnSubmit;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference levelRef;
    public ArrayList<TempVehicle> vehiclesArr = new ArrayList<>();
    ArrayList<Vehicle> vehicles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_level);
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(new BatteryCheckReceiver(), filter);
        firebaseDatabase = FirebaseDatabase.getInstance();
        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        FrameLayout frameLayout  =(FrameLayout)findViewById(R.id.mainfram);

        for(int i = 0; i< AddLevelCanvas.bitmaps.length; i++){
            vehiclesArr.add((TempVehicle) AddLevelCanvas.bitmaps[i]);
        }
        boardGame = new AddLevelCanvas(this, vehiclesArr, this);
        frameLayout.addView(boardGame);
    }


    @Override
    public void onClick(View view) {
        if(view == btnSubmit){
            Dialog d = new Dialog(this);
            d.setContentView(R.layout.activity_main);
            d.setTitle("Upload Level");
            d.setCancelable(true);
            FrameLayout frameLayout  =(FrameLayout)d.findViewById(R.id.mainfram);
            copyVehicles = new ArrayList<>();
            this.level = new Level(new ArrayList<>(),"", "","");
            for(int i = 0 ; i < boardGame.vehiclesArr.size(); i ++) {
                Vehicle tv = boardGame.vehiclesArr.get(i);
                copyVehicles.add((new TempVehicle(tv.bitmap,  tv.length, tv.direction,tv.x, tv.y, true)));

                level.vehicles.add(new Vehicle(tv.bitmap,  tv.length, tv.direction,tv.x, tv.y));
                Log.d("first vehicle ", tv.x + " ");
            }


            Vehicle[] arr = new Vehicle[boardGame.vehiclesArr.size()];
            arr = boardGame.vehiclesArr.toArray(arr);
            isLegalCheck = new IsLegalCheck(this,frameLayout, arr);
            Log.d("first vehicle ", arr[0].x + " ");
            frameLayout.addView(isLegalCheck);
            d.setOnCancelListener(boardGame);
            d.show();




        }
        if(view == isLegalCheck.btnUpload){
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
            levelRef = firebaseDatabase.getReference("/Levels").push();
            level.uid = uid;
            level.title = isLegalCheck.title.getText().toString();
            level.difficulty = (int) isLegalCheck.ratingBar.getRating();
            level.key = levelRef.getKey();
            levelRef.setValue(level.toString());
            Intent intent = new Intent(this,MenuActivity.class);
            startActivity(intent);
        }
    }




    public static Bitmap resizeBitmap(Bitmap source, int maxLength) {
        try {
            if (source.getHeight() >= source.getWidth()) {
                int targetHeight = maxLength;
                if (source.getHeight() <= targetHeight) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                int targetWidth = (int) (targetHeight * aspectRatio);

                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                return result;
            } else {
                int targetWidth = maxLength;

                if (source.getWidth() <= targetWidth) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = ((double) source.getHeight()) / ((double) source.getWidth());
                int targetHeight = (int) (targetWidth * aspectRatio);

                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                return result;

            }
        }
        catch (Exception e)
        {
            return source;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);

    }
}