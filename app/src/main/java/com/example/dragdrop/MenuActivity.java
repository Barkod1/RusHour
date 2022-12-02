package com.example.dragdrop;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {


    FirebaseAuth firebaseAuth;
    Button btnAddPost;
    Button btnReg, btnLogin;//dialog buttons
    Button btnMainLogin, btnMainRegister;
    EditText etEmail, etPass, etName;
    Dialog d;
    ProgressDialog progressDialog;
    Button btnAllPost;
    DatabaseReference userRef;
    FirebaseDatabase firebaseDatabase;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        AddLevel.bitmaps = new TempVehicle[8];
        AddLevel.bitmaps[0] = new TempVehicle(BitmapFactory.decodeResource(getResources(), R.drawable.green_car2), 2, 0,
                120,  0);
        Levels.d2.add(resizeBitmap(AddLevel.bitmaps[0].bitmap, 380));
        float xx = 190;
        AddLevel.bitmaps[1] = new TempVehicle(BitmapFactory.decodeResource(getResources(), R.drawable.green_car2), 2, 1,
                0,  0);
        Levels.u2.add(resizeBitmap(AddLevel.bitmaps[1].bitmap, 380));


        AddLevel.bitmaps[2] = new TempVehicle(BitmapFactory.decodeResource(getResources(), R.drawable.orange_car2), 2, 0,
                xx,  xx);
        Levels.d2.add(resizeBitmap(AddLevel.bitmaps[2].bitmap, 380));

        AddLevel.bitmaps[3] = new TempVehicle(BitmapFactory.decodeResource(getResources(), R.drawable.orange_car2), 2, 1,
                xx,  xx);
        Levels.u2.add(resizeBitmap(AddLevel.bitmaps[3].bitmap, 380));

        AddLevel.bitmaps[4] = new TempVehicle(BitmapFactory.decodeResource(getResources(), R.drawable.pink_car3), 3, 0,
                xx,  xx);
        Levels.d3.add(resizeBitmap(AddLevel.bitmaps[4].bitmap, 570));

        AddLevel.bitmaps[5] = new TempVehicle(BitmapFactory.decodeResource(getResources(), R.drawable.pink_car3), 3, 1,
                xx,  xx);
        Levels.u3.add(resizeBitmap(AddLevel.bitmaps[5].bitmap, 570));

        AddLevel.bitmaps[6] = new TempVehicle(BitmapFactory.decodeResource(getResources(), R.drawable.blue_car3), 3, 0,
                xx,  xx);
        Levels.d3.add(resizeBitmap(AddLevel.bitmaps[6].bitmap, 570));

        AddLevel.bitmaps[7] = new TempVehicle(BitmapFactory.decodeResource(getResources(), R.drawable.blue_car3), 3, 1,
                xx,  xx);
        Levels.u3.add(resizeBitmap(AddLevel.bitmaps[7].bitmap, 570));
        firebaseAuth = FirebaseAuth.getInstance();
        btnMainLogin = (Button) findViewById(R.id.btnLogin);
        btnMainLogin.setOnClickListener(this);
        btnAllPost = (Button) findViewById(R.id.btnAllPost);

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
        btnAddPost = (Button) findViewById(R.id.btnAddPost);
        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnAllPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, AllLevelsActivity.class);
                startActivity(intent);

            }
        });
        Button btnPlay = (Button)findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                    Intent intent = new Intent(MenuActivity.this, RushHourGame.class);
                    startActivity(intent);
                }
            }
        });
        String str = "\uD83D\uDE07";
        Log.d("str emoji", str);
    }

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

    public void register() {

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MenuActivity.this, "Successfully registered", Toast.LENGTH_LONG).show();
                    btnMainLogin.setText("Logout");
                    addUserDetails();
                } else {
                    Toast.makeText(MenuActivity.this, "Registration Error", Toast.LENGTH_LONG).show();

                }

                d.dismiss();
                progressDialog.dismiss();


            }
        });

    }

    private void addUserDetails() {
        String uid = firebaseAuth.getCurrentUser().getUid().toString();
         user = new User(etName.getText().toString(),uid,"" );
        userRef = firebaseDatabase.getReference("Users").push();
        user.key = userRef.getKey();
        userRef.setValue(user);
    }

    public void login() {
        progressDialog.setMessage("Login Please Wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(MenuActivity.this, "auth_success", Toast.LENGTH_SHORT).show();
                            btnMainLogin.setText("Logout");

                        } else {
                            Toast.makeText(MenuActivity.this, "auth_failed", Toast.LENGTH_SHORT).show();

                        }
                        d.dismiss();
                        progressDialog.dismiss();

                    }
                });

    }

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
}