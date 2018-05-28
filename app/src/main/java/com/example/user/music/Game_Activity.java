package com.example.user.music;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class Game_Activity extends AppCompatActivity {

    String getintent ;
    Random random = new Random();
    Random random1 = new Random();

    SharedPreferences put_stopwatch;
    SharedPreferences put_count;

    ImageView teeth1;
    ImageView teeth2;
    ImageView teeth3;
    ImageView teeth4;
    ImageView teeth5;
    ImageView teeth6;
    ImageView teeth7;
    ImageView teeth8;
    ImageView teeth9;
    ImageView teeth10;
    ImageView teeth11;
    ImageView teeth12;
    ImageView teeth13;
    ImageView teeth14;
    ImageView teeth15;
    ImageView teeth16;
    ImageView teeth17;
    ImageView teeth18;
    ImageView teeth19;
    ImageView teeth20;
    int t1=1;
    int t2=1;
    int t3=1;
    int t4=1;
    int t5=1;
    int t6=1;
    int t7=1;
    int t8=1;
    int t9=1;
    int t10=1;
    int t11=1;
    int t12=1;
    int t13=1;
    int t14=1;
    int t15=1;
    int t16=1;
    int t17=1;
    int t18=1;
    int t19=1;
    int t20=1;


    int count =0;
    int time =30;


    TextView time_view;
    TextView count_view;


    Handler handler, handler1;
    Thread thread, thread1;


    //스탑와치 .
    private void stopwatch() {
        put_count = getSharedPreferences("get_count_file",0);
        put_stopwatch = getSharedPreferences("get_stopwatch_file",0);
        final SharedPreferences.Editor get_count = put_count.edit();
        final SharedPreferences.Editor get_stopwatch = put_stopwatch.edit();
        getintent = getIntent().getStringExtra("login_id");

        handler1 = new Handler();
        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (time = 5; time >= 0; time--) {
                    try {
                        thread1.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }handler1.post(new Runnable() {
                        @Override
                        public void run() {
                            if(time >= 0) {
                                time_view.setText(time + "초");
                            }else if (time <= 0){

                                get_count.putInt(getintent,count).commit();
                                get_stopwatch.putInt("get_stopwatch",time).commit();
                                Intent intent = new Intent(Game_Activity.this,closeGame_Activity.class);
                                intent.putExtra("login_id",getintent);
                                startActivityForResult(intent,20);
                                finish();
                            }
                        }
                    });

                }
            }
        }); thread1.start();
    }


    //악어이빨 이미지 쓰레드.
    private void teeup(){
        handler = new Handler();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {



                for(;;) {

                    try {
                        thread.sleep(random.nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (random.nextInt(5) == 0) {
                                teeth1.setImageResource(R.drawable.vkfk);
                                t1=1;
                            } if (random1.nextInt(5) == 1){
                                teeth2.setImageResource(R.drawable.vkfk);
                                t2=1;
                            } if (random1.nextInt(5) == 2){
                                teeth3.setImageResource(R.drawable.vkfk);
                                t3=1;
                            } if (random1.nextInt(5) == 3){
                                teeth4.setImageResource(R.drawable.vkfk);
                                t4=1;
                            } if (random1.nextInt(5) == 4){
                                teeth5.setImageResource(R.drawable.vkfk);
                                t5=1;
                            } if (random1.nextInt(5) == 0){
                                teeth6.setImageResource(R.drawable.vkfk);
                                t6=1;
                            } if (random1.nextInt(5) == 1){
                                teeth7.setImageResource(R.drawable.vkfk);
                                t7=1;
                            }if (random1.nextInt(5) == 0){
                                teeth8.setImageResource(R.drawable.vkfk);
                                t8=1;
                            } if (random1.nextInt(5) == 0){
                                teeth9.setImageResource(R.drawable.vkfk);
                                t9=1;
                            } if (random1.nextInt(5) == 3){
                                teeth10.setImageResource(R.drawable.vkfk);
                                t10=1;
                            } if (random1.nextInt(5) == 1){
                                teeth11.setImageResource(R.drawable.teetetee);
                                t11=1;
                            } if (random1.nextInt(5) == 2){
                                teeth12.setImageResource(R.drawable.teetetee);
                                t12=1;
                            } if (random1.nextInt(5) == 2){
                                teeth13.setImageResource(R.drawable.teetetee);
                                t13=1;
                            } if (random1.nextInt(5) == 3){
                                teeth14.setImageResource(R.drawable.teetetee);
                                t14=1;
                            } if (random1.nextInt(5) == 4){
                                teeth15.setImageResource(R.drawable.teetetee);
                                t15=1;
                            } if (random1.nextInt(5) == 1){
                                teeth16.setImageResource(R.drawable.teetetee);
                                t16=1;
                            } if (random1.nextInt(5) == 3){
                                teeth17.setImageResource(R.drawable.teetetee);
                                t17=1;
                            } if (random1.nextInt(5) == 2){
                                teeth18.setImageResource(R.drawable.teetetee);
                                t18=1;
                            } if (random1.nextInt(5) == 4){
                                teeth19.setImageResource(R.drawable.teetetee);
                                t19=1;
                            } if (random1.nextInt(5) == 3){
                                teeth20.setImageResource(R.drawable.teetetee);
                                t20=1;
                            }

                        }
                    });
                }}

        });
        thread.start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_);



        count_view = (TextView)findViewById(R.id.count_view);
        time_view = (TextView)findViewById(R.id.time_view);


        teeth1 = (ImageView)findViewById(R.id.teeth1_view);
        teeth2 = (ImageView)findViewById(R.id.teeth2_view);
        teeth3 = (ImageView)findViewById(R.id.teeth3_view);
        teeth4 = (ImageView)findViewById(R.id.teeth4_view);
        teeth5 = (ImageView)findViewById(R.id.teeth5_view);
        teeth6 = (ImageView)findViewById(R.id.teeth6_view);
        teeth7 = (ImageView)findViewById(R.id.teeth7_view);
        teeth8 = (ImageView)findViewById(R.id.teeth8_view);
        teeth9 = (ImageView)findViewById(R.id.teeth9_view);
        teeth10 = (ImageView)findViewById(R.id.teeth10_view);
        teeth11 = (ImageView)findViewById(R.id.teeth11_view);
        teeth12 = (ImageView)findViewById(R.id.teeth12_view);
        teeth13 = (ImageView)findViewById(R.id.teeth13_view);
        teeth14 = (ImageView)findViewById(R.id.teeth14_view);
        teeth15 = (ImageView)findViewById(R.id.teeth15_view);
        teeth16 = (ImageView)findViewById(R.id.teeth16_view);
        teeth17 = (ImageView)findViewById(R.id.teeth17_view);
        teeth18 = (ImageView)findViewById(R.id.teeth18_view);
        teeth19 = (ImageView)findViewById(R.id.teeth19_view);
        teeth20 = (ImageView)findViewById(R.id.teeth20_view);

        teeup();

        teeth1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teeth1.setImageResource(R.drawable.tspy);
                if(t1==1){
                    count=count+1;
                    t1=0;
                    count_view.setText(""+count);
                }
            }
        });
        teeth2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teeth2.setImageResource(R.drawable.tspy);
                if(t2==1){
                    count=count+1;
                    t2=0;
                    count_view.setText(""+count);
                }
            }
        });


        teeth3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teeth3.setImageResource(R.drawable.tspy);
                if(t3==1){
                    count=count+1;
                    t3=0;
                    count_view.setText(""+count);
                }
            }
        });


        teeth4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teeth4.setImageResource(R.drawable.tspy);
                if(t4==1){
                    count=count+1;
                    t4=0;
                    count_view.setText(""+count);
                }
            }
        });


        teeth5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teeth5.setImageResource(R.drawable.tspy);
                if(t5==1){
                    count=count+1;
                    t5=0;
                    count_view.setText(""+count);
                }
            }
        });


        teeth6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teeth6.setImageResource(R.drawable.tspy);
                if(t6==1){
                    count=count+1;
                    t6=0;
                    count_view.setText(""+count);
                }
            }
        });


        teeth7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teeth7.setImageResource(R.drawable.tspy);
                if(t7==1){
                    count=count+1;
                    t7=0;
                    count_view.setText(""+count);
                }
            }
        });


        teeth8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teeth8.setImageResource(R.drawable.tspy);
                if(t8==1){
                    count=count+1;
                    t8=0;
                    count_view.setText(""+count);
                }
            }
        });


        teeth9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teeth9.setImageResource(R.drawable.tspy);
                if(t9==1){
                    count=count+1;
                    t9=0;
                    count_view.setText(""+count);
                }
            }
        });

        teeth10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teeth10.setImageResource(R.drawable.tspy);
                if(t10==1){
                    count=count+1;
                    t10=0;
                    count_view.setText(""+count);
                }
            }
        });

        teeth11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teeth11.setImageResource(R.drawable.tspy);
                if(t11==1){
                    count=count+1;
                    t11=0;
                    count_view.setText(""+count);
                }
            }
        });


        teeth12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teeth12.setImageResource(R.drawable.tspy);
                if(t12==1){
                    count=count+1;
                    t12=0;
                    count_view.setText(""+count);
                }
            }
        });


        teeth13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teeth13.setImageResource(R.drawable.tspy);
                if(t13==1){
                    count=count+1;
                    t13=0;
                    count_view.setText(""+count);
                }
            }
        });


        teeth14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teeth14.setImageResource(R.drawable.tspy);
                if(t14==1){
                    count=count+1;
                    t14=0;
                    count_view.setText(""+count);
                }
            }
        });


        teeth15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teeth15.setImageResource(R.drawable.tspy);
                if(t15==1){
                    count=count+1;
                    t15=0;
                    count_view.setText(""+count);
                }
            }
        });


        teeth16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teeth16.setImageResource(R.drawable.tspy);
                if(t16==1){
                    count=count+1;
                    t16=0;
                    count_view.setText(""+count);
                }
            }
        });



        teeth17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teeth17.setImageResource(R.drawable.tspy);
                if(t17==1){
                    count=count+1;
                    t17=0;
                    count_view.setText(""+count);
                }
            }
        });

        teeth18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teeth18.setImageResource(R.drawable.tspy);
                if(t18==1){
                    count=count+1;
                    t18=0;
                    count_view.setText(""+count);
                }
            }
        });

        teeth19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teeth19.setImageResource(R.drawable.tspy);
                if(t19==1){
                    count=count+1;
                    t19=0;
                    count_view.setText(""+count);
                }
            }
        });


        teeth20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teeth20.setImageResource(R.drawable.tspy);
                if(t20==1){
                    count=count+1;
                    t20=0;
                    count_view.setText(""+count);
                }
            }
        });


        //악어 이빨 클릭한 횟수가 텍스트뷰에 표시된다.
        count_view.setText(""+count);

        //악어 이빨 게임의 타이머 .
        stopwatch();





/*
        push_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN :
                        push_btn.setImageResource(R.drawable.button2);break;

                    case MotionEvent.ACTION_UP :
                        push_btn.setImageResource(R.drawable.button1);break;

                        default:
                            break;

                }


                return false;
            }
        });*/

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 20 && resultCode == 200) {
            if (data != null) {



            }
        }
    }

}
