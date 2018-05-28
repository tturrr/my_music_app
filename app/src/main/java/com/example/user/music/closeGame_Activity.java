package com.example.user.music;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.kakao.util.helper.SharedPreferencesCache;

public class closeGame_Activity extends AppCompatActivity {


    ImageView restart_game_view;
    TextView id_text;
    TextView count_text;
    ImageView star1_view,star2_view,star3_view,star4_view,star5_view,star6_view,star7_view;




    SharedPreferences get_count;
    SharedPreferences get_stopwatch;
    SharedPreferences id_info;



    int a=0;
    int b=30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_game_);

        star1_view = (ImageView)findViewById(R.id.star1_view);
        star2_view = (ImageView)findViewById(R.id.star2_view);
        star3_view = (ImageView)findViewById(R.id.star3_view);
        star4_view = (ImageView)findViewById(R.id.star4_view);




        //애니메이션 효과를 뷰에 적용.
        Animation star1_anim =  AnimationUtils.loadAnimation(closeGame_Activity.this,R.anim.star1_anim);

        star1_view.startAnimation(star1_anim);
        star2_view.startAnimation(star1_anim);
        star3_view.startAnimation(star1_anim);
        star4_view.startAnimation(star1_anim);





        get_count = getSharedPreferences("get_count_file",0);
        get_stopwatch = getSharedPreferences("get_stopwatch_file",0);

        id_text = (TextView)findViewById(R.id.id_text);
        count_text = (TextView)findViewById(R.id.count_text);

        String getintent = getIntent().getStringExtra("login_id");
        SharedPreferences nicks_sp = getSharedPreferences("nick",0);
        String nick  = nicks_sp.getString(getintent,"");
        id_info = getSharedPreferences("id",0);


        int count = get_count.getInt(getintent,0);

        id_text.setText(nick);
        count_text.setText(""+count);



        restart_game_view = (ImageView)findViewById(R.id.restart_game);







        restart_game_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences put_count = getSharedPreferences("get_count_file",0);
                SharedPreferences put_stopwatch = getSharedPreferences("get_stopwatch_file",0);

                SharedPreferences.Editor put1_count = put_count.edit();
                SharedPreferences.Editor put1_stopwatch = put_stopwatch.edit();




                put1_count.putInt("get_count",0).commit();
                put1_stopwatch.putInt("get_stopwatch",0).commit();


                Intent intent = new Intent(closeGame_Activity.this,Game_Activity.class);
                setResult(200,intent);
                finish();
            }
        });


    }
}
