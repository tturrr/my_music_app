package com.example.user.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ImageView info_view;
    LinearLayout chat_btn;
    LinearLayout music_btn;
    LinearLayout friends_btn;
    LinearLayout board_btn;
    String  login_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        passPushTokenToServer();

        board_btn = (LinearLayout)findViewById(R.id.board_btn);
        chat_btn = (LinearLayout)findViewById(R.id.chat_btn);
        info_view = (ImageView)findViewById(R.id.info_img);
        music_btn = (LinearLayout)findViewById(R.id.music_btn);
        friends_btn = (LinearLayout)findViewById(R.id.friend_btn);

        final String google_id = getIntent().getStringExtra("google_id");

        final String face_name = getIntent().getStringExtra("face_name");
        final String face_birth = getIntent().getStringExtra("face_birth");
        final String face_email = getIntent().getStringExtra("face_email");
        final String face_img = getIntent().getStringExtra("face_img");

         final String kakao_image = getIntent().getStringExtra("kakao_image");
         final String kakao_name = getIntent().getStringExtra("kakao_name");
         final long kakao_number = getIntent().getLongExtra("kakao_number",0);

         Intent intent = getIntent();
        login_id = intent.getStringExtra("login_id");

         //메모장으로 이동한다
        board_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,board_Activity.class);
                intent.putExtra("login_id",login_id);
                startActivity(intent);
            }
        });


         //친구목록으로 이동한다
        friends_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,friend_Activity.class);
                startActivity(intent);
            }
        });


         //채팅방 액티비티로 이동합니다.
        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,chat_Actvity.class);
                startActivity(intent);
            }
        });

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
                intent.putExtra("google_id",google_id);
                intent.putExtra("login_id",login_id);
                startActivityForResult(intent,0);
            }
        });

    }

    void passPushTokenToServer(){

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();

        Map<String,Object>map = new HashMap<>();
        map.put("pushToken",token);

        FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(map);
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
