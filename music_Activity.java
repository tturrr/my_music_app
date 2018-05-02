package com.example.user.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class music_Activity extends AppCompatActivity {

    int i = 0;

    ImageButton add_btn;
    ImageButton back_imgbtn;




    private ArrayList<music_item_list> arrayList;
    private custom_music_adapter adapter;
    private ListView music_List;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_);

        add_btn = (ImageButton)findViewById(R.id.add_btn);
        back_imgbtn = (ImageButton)findViewById(R.id.back_imgbutton);

        music_List = (ListView)findViewById(R.id.music_list);
        arrayList = new ArrayList<>();
        arrayList.add(new music_item_list("bboombboom","모모랜드",R.raw.boomboom));
        arrayList.add(new music_item_list("what is love","트와이스",R.raw.twice_what_is_love));


        adapter = new custom_music_adapter(this,R.layout.custom_music_item,arrayList);
        music_List.setAdapter(adapter);




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
