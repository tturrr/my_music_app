package com.example.user.music;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class music_Activity extends AppCompatActivity {

    MediaPlayer mp;
    int pause;
    ImageButton add_btn;
    ImageButton back_imgbutton;
    boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_);

        add_btn = (ImageButton)findViewById(R.id.add_btn);
        back_imgbutton = (ImageButton)findViewById(R.id.back_imgbutton);





        back_imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(music_Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
