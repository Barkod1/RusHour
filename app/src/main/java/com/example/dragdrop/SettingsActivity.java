package com.example.dragdrop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private Switch musicSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        musicSwitch = findViewById(R.id.switch1);
        musicSwitch.setOnClickListener(this);
        if(MusicPlayerService.player != null) {
            if(MusicPlayerService.isPlaying())
            musicSwitch.setChecked(true);
        }
        else{
            musicSwitch.setChecked(false);
        }
    }

    @Override
    public void onClick(View view) {
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
}