package com.example.user.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class music_Activity extends AppCompatActivity {

    int i = 0;
    MediaPlayer mp;
    int pause;
    ImageButton add_btn;
    ImageButton back_imgbtn;
    ImageView play_imgbtn;

    boolean isPlaying = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_);

        add_btn = (ImageButton)findViewById(R.id.add_btn);
        back_imgbtn = (ImageButton)findViewById(R.id.back_imgbutton);
        play_imgbtn = (ImageView) findViewById(R.id.play_img);



        play_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(music_Activity.this,AudioService.class);

                i = 1 - i;
                if ( i == 0 ){


                    play_imgbtn.setImageResource(R.drawable.play_icon);
                    if (!isPlaying) {
                        Toast.makeText(getApplicationContext(),
                                "서비스중이 아닙니다, 데이터받을수 없음",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                }



                else{


                    play_imgbtn.setImageResource(R.drawable.pause_icon);
                }
            }
        });





        back_imgbtn.setOnClickListener(new View.OnClickListener() {
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
