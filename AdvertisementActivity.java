package com.example.user.music;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class AdvertisementActivity extends AppCompatActivity {

    ImageButton back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);

        back_btn = (ImageButton) findViewById(R.id.back_imgbutton);
        String tel = "tel:01020703968";


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdvertisementActivity.this, music_Activity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void calling(View view) {
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:010-2070-3968")); startActivity(myIntent);

    }
}
