package com.example.user.music;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.content.SharedPreferences;

public class board_Activity extends AppCompatActivity {

    ListView board_listView;
    ImageView board_add_btn;
    ImageButton back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_);

        SharedPreferences count_shared;
        back_btn = (ImageButton)findViewById(R.id.back_imgbutton);
        board_add_btn = (ImageView) findViewById(R.id.board_add_btn);
        board_listView = (ListView)findViewById(R.id.board_listView);



        board_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(board_Activity.this,writeBoard_Activity.class);
                startActivityForResult(intent,100);
            }
        });



        //뒤로가기 버튼 뒤로이동한다.
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(board_Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
