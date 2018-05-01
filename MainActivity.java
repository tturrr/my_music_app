package com.example.user.music;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    ImageView info_view;
    LinearLayout music_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info_view = (ImageView)findViewById(R.id.info_img);
        music_btn = (LinearLayout)findViewById(R.id.music_btn);

        final String face_name = getIntent().getStringExtra("face_name");
        final String face_birth = getIntent().getStringExtra("face_birth");
        final String face_email = getIntent().getStringExtra("face_email");
        final String face_img = getIntent().getStringExtra("face_img");

         final String kakao_image = getIntent().getStringExtra("kakao_image");
         final String kakao_name = getIntent().getStringExtra("kakao_name");
         final long kakao_number = getIntent().getLongExtra("kakao_number",0);


        //노래듣기 라는 버튼을 클릭시 뮤직이 있는 액티비티로 이동한다.
        music_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,music_Activity.class);
                startActivity(intent);

            }
        });


        //인포 버튼클릭시 카카오의 회원정보를 가지고 간다.
        info_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,info_Activity.class);
                intent.putExtra("face_name",face_name);
                intent.putExtra("face_birth",face_birth);
                intent.putExtra("face_email",face_email);
                intent.putExtra("face_img",face_img);
                intent.putExtra("kakao_image",kakao_image);
                intent.putExtra("kakao_name",kakao_name);
                intent.putExtra("kakao_number",kakao_number);
                startActivityForResult(intent,0);
            }
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == 0) {
            // Make sure the request was successful
            if (resultCode == 100) {

            }
        }
    }


}
