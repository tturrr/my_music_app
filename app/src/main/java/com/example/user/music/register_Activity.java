package com.example.user.music;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class register_Activity extends AppCompatActivity {

    ImageButton back_imgbtn;
    EditText register_id_edit;
    EditText register_pass_edit;
    EditText register_pass_confirm_edit;
    Button register_suc_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        back_imgbtn = (ImageButton)findViewById(R.id.back_imgbutton);
        register_suc_btn = (Button)findViewById(R.id.register_suc_btn);
        register_id_edit = (EditText)findViewById(R.id.register_id_edit);
        register_pass_edit = (EditText)findViewById(R.id.register_pass_edit);
        register_pass_confirm_edit = (EditText)findViewById(R.id.register_pass_confirm_edit);



        //뒤로가는 화살표 이미지버튼을 클릭하면 로그인화면으로 돌아가게 된다.
        back_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register_Activity.this,login_Activity.class);
                startActivity(intent);
            }
        });


        register_suc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String edit_id =  register_id_edit.getText().toString();
                String edit_pass = register_pass_edit.getText().toString();
                String edit_confpass = register_pass_confirm_edit.getText().toString();

                if(edit_id.isEmpty()){
                    Toast.makeText(register_Activity.this,"아이디를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else if(edit_pass.isEmpty()){
                    Toast.makeText(register_Activity.this,"비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                }
                else if(edit_confpass.isEmpty()){
                    Toast.makeText(register_Activity.this,"비밀번호 확인 란을 입력해주세요.",Toast.LENGTH_SHORT).show();
                }

                else {
                    intent.putExtra("id",edit_id);
                    setResult(100,intent);
                    finish();
                }

            }
        });



    }
}