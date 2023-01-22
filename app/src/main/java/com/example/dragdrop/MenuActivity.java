package com.example.dragdrop;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

// התפריט הראשי עם אפשרות להירשם, להתחבר, להתנתק ולהגיע לכל מקום באפליקציה
public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

//משמש לאוטנטיקציה עם פיירבייס
    FirebaseAuth firebaseAuth;
  //כפתור המעביר לADDLEVEL אקטיביטי
    Button btnAddLevel;
    //  כפתור להרשמה והתחברות (פותח דיאלוג להכנסת פרטים רלוונטיים)
    Button btnReg, btnLogin;//dialog buttons
    //כפתור שרושם ומחבר לאחר הזנת הפרטים
    Button btnMainLogin, btnMainRegister;
    //המקום בו מזינים את המייל, סיסמא ושם משתמש
    EditText etEmail, etPass, etName;
    //הדיאלוג הקופץ במקרה של הרשמה או התחברות
    Dialog d;
    //דיאלוג נטען כשנרשמים
    ProgressDialog progressDialog;
    //כפתור שמראה את כל השלבים
    Button btnAllLevels;
    //רפרנס בענן למשתמש
    DatabaseReference userRef;
    //דוגמא של המסד נתונים בפיירבייס
    FirebaseDatabase firebaseDatabase;
    //המשתמש הנוכחי לשימוש בכל המחלקות
    public static User user;
    //כפתור שמעביר לאקטיביטי של ההגדרות
    Button btnSettings;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(new BatteryCheckReceiver(), filter);
        AddLevelCanvas.bitmaps = new TempVehicle[8];
        AddLevelCanvas.bitmaps[0] = new TempVehicle(BitmapFactory.decodeResource(getResources(), R.drawable.green_car2), 2, 0,
                120,  0);
        GetBitmap.d2.add(resizeBitmap(AddLevelCanvas.bitmaps[0].bitmap, 380));
        float xx = 190;
        AddLevelCanvas.bitmaps[1] = new TempVehicle(BitmapFactory.decodeResource(getResources(), R.drawable.green_car2), 2, 1,
                0,  0);
        GetBitmap.u2.add(resizeBitmap(AddLevelCanvas.bitmaps[1].bitmap, 380));


        AddLevelCanvas.bitmaps[2] = new TempVehicle(BitmapFactory.decodeResource(getResources(), R.drawable.orange_car2), 2, 0,
                xx,  xx);
        GetBitmap.d2.add(resizeBitmap(AddLevelCanvas.bitmaps[2].bitmap, 380));

        AddLevelCanvas.bitmaps[3] = new TempVehicle(BitmapFactory.decodeResource(getResources(), R.drawable.orange_car2), 2, 1,
                xx,  xx);
        GetBitmap.u2.add(resizeBitmap(AddLevelCanvas.bitmaps[3].bitmap, 380));

        AddLevelCanvas.bitmaps[4] = new TempVehicle(BitmapFactory.decodeResource(getResources(), R.drawable.pink_car3), 3, 0,
                xx,  xx);
        GetBitmap.d3.add(resizeBitmap(AddLevelCanvas.bitmaps[4].bitmap, 570));

        AddLevelCanvas.bitmaps[5] = new TempVehicle(BitmapFactory.decodeResource(getResources(), R.drawable.pink_car3), 3, 1,
                xx,  xx);
        GetBitmap.u3.add(resizeBitmap(AddLevelCanvas.bitmaps[5].bitmap, 570));

        AddLevelCanvas.bitmaps[6] = new TempVehicle(BitmapFactory.decodeResource(getResources(), R.drawable.blue_car3), 3, 0,
                xx,  xx);
        GetBitmap.d3.add(resizeBitmap(AddLevelCanvas.bitmaps[6].bitmap, 570));

        AddLevelCanvas.bitmaps[7] = new TempVehicle(BitmapFactory.decodeResource(getResources(), R.drawable.blue_car3), 3, 1,
                xx,  xx);
        GetBitmap.u3.add(resizeBitmap(AddLevelCanvas.bitmaps[7].bitmap, 570));
        firebaseAuth = FirebaseAuth.getInstance();
        btnMainLogin = (Button) findViewById(R.id.btnLogin);
        btnMainLogin.setOnClickListener(this);
        btnAllLevels = (Button) findViewById(R.id.btnAllPost);

        btnMainRegister = (Button) findViewById(R.id.btnRegister);
        btnMainRegister.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            btnMainLogin.setText("Logout");

        } else {
            btnMainLogin.setText("Login");

        }
        btnSettings = (Button)findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(this);
        btnAddLevel = (Button) findViewById(R.id.btnAddPost);
        btnAddLevel.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, AddLevelContext.class);
            startActivity(intent);
        });

        btnAllLevels.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, AllLevelsActivity.class);
            startActivity(intent);

        });
        Button btnPlay = (Button)findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(view -> {
            if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                Intent intent = new Intent(MenuActivity.this, RushHourContext.class);
                startActivity(intent);
            }
        });
        if(MusicPlayerService.player == null) {
            MusicPlayerService.player = MediaPlayer.create(this, R.raw.hotel_california);

            Intent intent = new Intent(this, MusicPlayerService.class);
            startService(intent);
        }
    }
    //יוצר דיאלוג להרשמה
    public void createRegisterDialog() {
        d = new Dialog(this);
        d.setContentView(R.layout.register_layout);
        d.setTitle("Register");
        d.setCancelable(true);
        etEmail = (EditText) d.findViewById(R.id.etEmail);
        etPass = (EditText) d.findViewById(R.id.etPass);
        etName = (EditText)d.findViewById(R.id.etName);
        btnReg = (Button) d.findViewById(R.id.btnRegister);
        btnReg.setOnClickListener(this);
        d.show();

    }
//יוצר דיאלוג להתחברות
    public void createLoginDialog() {
        d = new Dialog(this);
        d.setContentView(R.layout.login_layout);
        d.setTitle("Login");
        d.setCancelable(true);
        etEmail = (EditText) d.findViewById(R.id.etEmail);
        etPass = (EditText) d.findViewById(R.id.etPass);
        btnLogin = (Button) d.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        d.show();

    }

    @SuppressLint("SetTextI18n")
    public void register() {

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPass.getText().toString()).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MenuActivity.this, "Successfully registered", Toast.LENGTH_LONG).show();
                btnMainLogin.setText("Logout");
                addUserDetails();
            } else {
                Toast.makeText(MenuActivity.this, "Registration Error", Toast.LENGTH_LONG).show();

            }

            d.dismiss();
            progressDialog.dismiss();


        });

    }

    private void addUserDetails() {
        String uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
         user = new User(etName.getText().toString(),uid,"" );
        userRef = firebaseDatabase.getReference("Users").push();
        user.key = userRef.getKey();
        userRef.setValue(user);
    }

    @SuppressLint("SetTextI18n")
    public void login() {
        progressDialog.setMessage("Login Please Wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPass.getText().toString())
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {
                        Toast.makeText(MenuActivity.this, "auth_success", Toast.LENGTH_SHORT).show();
                        btnMainLogin.setText("Logout");

                    } else {
                        Toast.makeText(MenuActivity.this, "auth_failed", Toast.LENGTH_SHORT).show();

                    }
                    d.dismiss();
                    progressDialog.dismiss();

                });

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {

        if (v == btnMainLogin) {

            if (btnMainLogin.getText().toString().equals("Login")) {
                createLoginDialog();
            } else if (btnMainLogin.getText().toString().equals("Logout")) {
                firebaseAuth.signOut();
                btnMainLogin.setText("Login");
            }

        } else if (v == btnMainRegister) {
            createRegisterDialog();
        } else if (btnReg == v) {
            register();
        } else if (v == btnLogin) {
            login();
        }

    if(v == btnSettings){
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }
    }

    public static Bitmap resizeBitmap(Bitmap source, int maxLength) {
        try {
            if (source.getHeight() >= source.getWidth()) {
                if (source.getHeight() <= maxLength) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                int targetWidth = (int) (maxLength * aspectRatio);

                return Bitmap.createScaledBitmap(source, targetWidth, maxLength, false);
            } else {

                if (source.getWidth() <= maxLength) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = ((double) source.getHeight()) / ((double) source.getWidth());
                int targetHeight = (int) (maxLength * aspectRatio);

                return Bitmap.createScaledBitmap(source, maxLength, targetHeight, false);

            }
        }
        catch (Exception e)
        {
            return source;
        }
    }
}