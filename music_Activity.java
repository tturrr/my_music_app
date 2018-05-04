package com.example.user.music;

import android.content.pm.PackageManager;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContentResolverCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentUris;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;



public class music_Activity extends AppCompatActivity {

    int e ;
    int i = 0;

    ImageButton back_imgbtn;
    Button add_btn;

    private ListView listView;
    public static ArrayList<music_item_list> list;
    dbHelper helper;
    SQLiteDatabase db;
    String sql;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_);
        add_btn = (Button)findViewById(R.id.add_btn);
        helper = new dbHelper(this);
        db = helper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from faber",null);
        startManagingCursor(cursor);

        helper = new dbHelper(this);
        try {
            db = helper.getWritableDatabase();
            //데이터베이스 객체를 얻기 위하여 getWritableDatabse()를 호출

        } catch (SQLiteException e) {
            db = helper.getReadableDatabase();
        }




        requirePermission();
        getMusicList();

        MyAdapter adapter = new MyAdapter(this,list);



        back_imgbtn = (ImageButton)findViewById(R.id.back_imgbutton);
        listView = (ListView)findViewById(R.id.music_list);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(music_Activity.this,detail_music_Activity.class);
                intent.putExtra("position",position);
                intent.putExtra("playlist",list);
                startActivity(intent);
            }
        });



        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list = new ArrayList<>();
                //가져오고 싶은 컬럼 명을 나열합니다. 음악의 아이디, 앰블럼 아이디, 제목, 아스티스트 정보를 가져옵니다.
                String[] projection = {MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.ALBUM_ID,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ARTIST
                };

                Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        projection, null, null, null);

                while(cursor.moveToNext()){
                    music_item_list musicDto = new music_item_list();
                    musicDto.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                    musicDto.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                    musicDto.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                    musicDto.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                    list.add(musicDto);
                }
                cursor.close();

                for(i=0; i<list.size(); i++) {
                    sql = String.format("INSERT INTO faber VALUES(null, '" + list.get(i) + "','" + list.get(i) + "','" + list.get(i) + "','" + list.get(i) + "');");
                    db.execSQL(sql);
                }
                Intent intent = new Intent(music_Activity.this,faver_music_Activity.class);
                startActivity(intent);
            }
        });


        back_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(music_Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });





    }








    public  void getMusicList(){
        list = new ArrayList<>();
        //가져오고 싶은 컬럼 명을 나열합니다. 음악의 아이디, 앰블럼 아이디, 제목, 아스티스트 정보를 가져옵니다.
        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST
        };

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);

        while(cursor.moveToNext()){
            music_item_list musicDto = new music_item_list();
            musicDto.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            musicDto.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
            musicDto.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            musicDto.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            list.add(musicDto);
        }
        cursor.close();
    }


    //미디어 컨트롤 권한 획득.
    void requirePermission() {
        String [] permissions = new String [] {Manifest.permission.MEDIA_CONTENT_CONTROL, Manifest.permission.READ_EXTERNAL_STORAGE};
        ArrayList<String> ListPermissionNeeded = new ArrayList<>();

        for(String permission : permissions){


            //권한이 허가가 안됬을 경우 요청할 권한을 모집하는 부분.
            if(ContextCompat.checkSelfPermission(this,permission) == PackageManager.PERMISSION_DENIED) {
                ListPermissionNeeded.add(permission);
            }
        }

        //데이터가 없을경우 권한 요청하는 부분
        if(!ListPermissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,ListPermissionNeeded.toArray(new String[ListPermissionNeeded.size()]),1);

        }

    }

    //광고창이 나오게 만든다.
    public void onRestart() {
        e++;
        if(e == 2){
            Intent intent = new Intent(music_Activity.this,AdvertisementActivity.class);
            startActivity(intent);
        }


        super.onRestart();
    }


    class dbHelper extends SQLiteOpenHelper {


        private static final String DATABASE_NAME = "mycontacts.db";
        private static final int DATABASE_VERSION = 1;


        /*
         *먼저 SQLiteOpenHelper클래스를 상속받은 dbHelper클래스가 정의 되어 있다. 데이터베이스 파일 이름은 "mycontacts.db"가되고,
         *데이터베이스 버전은 1로 되어있다. 만약 데이터베이스가 요청되었는데 데이터베이스가 없으면 onCreate()를 호출하여 데이터베이스
         *파일을 생성해준다.
         */

        public dbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE faber (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, ALBUM_ID TEXT,TITLE TEXT,ARTIST TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS faber");
            onCreate(db);
        }
    }

}
