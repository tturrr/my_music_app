package com.example.user.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

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
                Intent intent = new Intent(intro_Activity.this,login_Activity.class);
                startActivity(intent);
                finish();
            }
        });


    }

//    private void onClick_kakao_Logout() {
//        UserManagement.requestLogout(new LogoutResponseCallback() {
//            @Override
//            public void onCompleteLogout() {
//                // 원하는 코드 ( 예를 들면 액티비티 이동)
//                Intent intent = new Intent(intro_Activity.this,login_Activity.class);
//                startActivity(intent);
//            }
//        });
//    }

}
