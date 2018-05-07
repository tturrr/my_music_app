package com.example.user.music;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class writeBoard_Activity extends AppCompatActivity {

    ImageView write_btn;
    ImageButton back_btn;
    ImageView writeImg_view;
    EditText title_txt;
    EditText content_txt;
    ImageView write_cmr_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_board_);

        write_cmr_btn = (ImageView)findViewById(R.id.write_cmr_btn);
        writeImg_view = (ImageView)findViewById(R.id.writeImg_view);
        write_btn = (ImageView)findViewById(R.id.write_btn);
        back_btn = (ImageButton)findViewById(R.id.back_imgbutton);
        content_txt = (EditText)findViewById(R.id.content_txt);
        title_txt = (EditText)findViewById(R.id.title_txt);





        //뒤로가기 버튼 뒤로 이동한다.
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(writeBoard_Activity.this,board_Activity.class);
                startActivity(intent);
            }
        });
    }
}
