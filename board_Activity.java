package com.example.user.music;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class board_Activity extends AppCompatActivity {

    ListView board_listView;
    ImageView board_add_btn;
    ImageButton back_btn;
    board_Adapter board_adapter;
    ArrayList<board_List_Item> board_list_item;
    ArrayList<Integer> list_num = new ArrayList<>();
    SharedPreferences akey_save;
    SharedPreferences img ;
    SharedPreferences title_txt1 ;
    SharedPreferences content1;
    int a = 0;
    int check_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_);

        Intent intent = getIntent();
        final String login_id = intent.getStringExtra("login_id");
        final SharedPreferences ID = getSharedPreferences("id", MODE_PRIVATE);
        final SharedPreferences.Editor edit_ID = ID.edit();


        akey_save = getSharedPreferences("akey",0);
        a = akey_save.getInt("akey1",0);

        back_btn = (ImageButton)findViewById(R.id.back_imgbutton);
        board_add_btn = (ImageView) findViewById(R.id.board_add_btn);
        board_listView = (ListView)findViewById(R.id.board_listView);


        img = getSharedPreferences("img",0);
        title_txt1 = getSharedPreferences("title_txt",0);
        content1 = getSharedPreferences("content_txt",0);

        board_list_item = new ArrayList<board_List_Item>();
        board_adapter = new board_Adapter(board_Activity.this,board_list_item);
        board_listView.setAdapter(board_adapter);


        board_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(board_Activity.this,writeBoard_Activity.class);
                intent.putExtra("login_id",login_id);
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

        SharedPreferences img = getSharedPreferences("img",0);
        SharedPreferences akey_save = getSharedPreferences("akey",0);
        SharedPreferences title_txt1 = getSharedPreferences("title_txt",0);
        SharedPreferences content1 = getSharedPreferences("content_txt",0);
        final SharedPreferences bod_num = getSharedPreferences("bod_num",0);
        SharedPreferences.Editor edit_img = img.edit();

        SharedPreferences.Editor akey_editor = akey_save.edit();

        SharedPreferences.Editor title_txt_editor = title_txt1.edit();

        SharedPreferences.Editor content_txt_editor = content1.edit();

        final SharedPreferences.Editor edit_bod_num = bod_num.edit();


//        edit_img.clear().commit();
//        akey_editor.clear().commit();
//        title_txt_editor.clear().commit();
//        content_txt_editor.clear().commit();
//        edit_bod_num.clear().commit();



        board_adapter.notifyDataSetChanged();


        for (int i = 1; i <= a ; i++) {


            String tit = title_txt1.getString(String.valueOf(i), "no_title");
            String cont = content1.getString(String.valueOf(i), "no_contents");
            String image = img.getString(String.valueOf(i),"no");

            Uri get_uri = Uri.parse(image);




            list_num.add(i);
            board_list_item.add(new board_List_Item(tit, cont,get_uri));

        }
        for (int i = a-1   ; i >= 0;  i = i- 1) {
            String title_main = board_list_item.get(i).getTitle();
            if (title_main == "no_title") {
                list_num.remove(i);
                board_list_item.remove(i);
            }
        }




        //아이템 클릭시 listview 의 아이템의 의 정보를 modify 액티비티로 이동시킨다.
        board_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(board_Activity.this, boardModify_Activity.class);
                check_position = list_num.get(board_listView.getCheckedItemPosition());
                intent.putExtra("position", check_position);
                intent.putExtra("login_id",login_id);

                startActivityForResult(intent, 2002);
            }
        });


    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == 105) {
            if (data != null) {
                //리스트뷰 추가 하는 부분.
                a = akey_save.getInt("akey1", 0);
                img = getSharedPreferences("img",0);
                title_txt1 = getSharedPreferences("title_txt",0);
                content1 = getSharedPreferences("content_txt",0);

                String tit = title_txt1.getString(String.valueOf(a), "무쟈게! 개 어려2웡");
                String cont = content1.getString(String.valueOf(a), "무쟈게! 개 어려2웡");
                String image = img.getString(String.valueOf(a),"아이디 추가 어려워");

                Uri get_uri = Uri.parse(image);
                board_list_item.add(new board_List_Item(tit, cont,get_uri));
                list_num.add(a);
            }
        } board_adapter.notifyDataSetChanged();

        //리스트 뷰 삭제 하는 부분
        if (requestCode == 2002 && resultCode == 200) {
            if (data != null) {
                a = akey_save.getInt("akey1", 0);

                board_list_item.clear();
                list_num.clear();
                for (int i = 1; i <= a ; i++) {
                    a = akey_save.getInt("akey1", 0);
                    String tit = title_txt1.getString(String.valueOf(i), "no_title");
                    String cont = content1.getString(String.valueOf(i), "no_contents");
                    String image = img.getString(String.valueOf(i),"no");

                    Uri get_uri = Uri.parse(image);


                    list_num.add(i);
                    board_list_item.add(new board_List_Item(tit, cont,get_uri));
                }
                for (int i = a - 1; i >= 0; i--) {
                    String title_main = board_list_item.get(i).getTitle();

                    if (title_main == "no_title") {
                        list_num.remove(i);
                        board_list_item.remove(i);
                    }
                }
                board_adapter.notifyDataSetChanged();

            }
        }

        //리스트뷰 수정하는 부분.
        if (requestCode == 2002 && resultCode == 300) {
            if (data != null) {
                a = akey_save.getInt("akey1", 0);

            board_list_item.clear();
            list_num.clear();
            for (int i = 1; i <= a ; i++) {

                String title_main = title_txt1.getString(String.valueOf(i), "no_title");
                String contents_main = content1.getString(String.valueOf(i), "no_contents");
                String image = img.getString(String.valueOf(i),"no");


                Uri get_uri = Uri.parse(image);


                list_num.add(i);
                board_list_item.add(new board_List_Item(title_main, contents_main,get_uri));

            }
            for (int i = a - 1; i >= 0; i = i-1) {
                String title_main = board_list_item.get(i).getTitle();
                if (title_main == "no_title") {
                    list_num.remove(i);
                    board_list_item.remove(i);
                }
            }
        }
            board_adapter.notifyDataSetChanged();
        }
    }

    public Bitmap StringToBitMap(String encodedString){ // 스트링으로 받은 이미지를 비트맵으로 다시 변환
        try{
            byte [] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    }

