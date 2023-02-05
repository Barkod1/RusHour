package com.example.dragdrop;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private Switch musicSwitch;
    private Button selectMusicBtn;
    public static Uri songUri;
    // button for calling
    public Button buttonCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(new BatteryCheckReceiver(), filter);
        selectMusicBtn = (Button)findViewById(R.id.BtnChooseSong);
        selectMusicBtn.setOnClickListener(this);
        musicSwitch = findViewById(R.id.switch1);
        musicSwitch.setOnClickListener(this);
        if(MusicPlayerService.player != null) {
            musicSwitch.setChecked(MusicPlayerService.isPlaying);
        }
        buttonCall = (Button)findViewById(R.id.button_call);
        buttonCall.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==buttonCall.getId()){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            // Start the activity of the phone
            startActivity(intent);
        }
        if(view.getId() == selectMusicBtn.getId()){
            Intent intent = new Intent();
            intent.setType("audio/*");
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            startActivityForResult(intent, 1,null);
        }
        if(musicSwitch.isChecked()){
            Intent svc=new Intent(this, MusicPlayerService.class);
            startService(svc);
        }

        else{
            Intent svc=new Intent(this, MusicPlayerService.class);
            stopService(svc);
            MusicPlayerService.isPlaying = false;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MusicPlayerService.isPlaying = true;
        musicSwitch.setChecked(true);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            songUri = data.getData();
            startService(new Intent(this,MusicPlayerService.class));

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);

    }
}