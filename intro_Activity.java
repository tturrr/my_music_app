package com.example.user.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class intro_Activity extends AppCompatActivity {
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_);



        imageView = (ImageView) findViewById(R.id.my_title);

        //타이틀 제목을 클릭할시 로그인 화면으로 이동.
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(intro_Activity.this, login_Activity.class);
                startActivity(intent);
                finish();
            }
        });


    }



}
