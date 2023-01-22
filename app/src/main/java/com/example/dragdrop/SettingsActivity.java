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
            if(MusicPlayerService.isPlaying())
            musicSwitch.setChecked(true);
        }
        else{
            musicSwitch.setChecked(false);
        }
    }

    @Override
    public void onClick(View view) {
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