package com.example.user.music;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class info_Activity extends AppCompatActivity {

    ImageButton back_imgbutton;
    Button logout_btn;
    ImageView profile_image;
    TextView profile_name;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_);

        final String face_name = getIntent().getStringExtra("face_name");
        final String face_birth = getIntent().getStringExtra("face_birth");
        final String face_email = getIntent().getStringExtra("face_email");
        final String face_img = getIntent().getStringExtra("face_img");


        final String kakao_image = getIntent().getStringExtra("kakao_image");
        final String kakao_name = getIntent().getStringExtra("kakao_name");
        final long kakao_number = getIntent().getLongExtra("kakao_number",0);


        logout_btn = (Button)findViewById(R.id.logout_button);
        back_imgbutton = (ImageButton)findViewById(R.id.back_imgbutton);
        profile_image = (ImageView)findViewById(R.id.profile_image);
        profile_name = (TextView)findViewById(R.id.profile_name);

        if(kakao_name != null) {
            Glide.with(this).load(kakao_image).into(profile_image); //글라이드라이브러리를 사용하여 url 이미지를 바로 이미지뷰에 로드시킨다.

            profile_name.setText(kakao_name);    //카카오톡의 닉네임을
        }else if(face_email != null){
            Glide.with(this).load(face_img).into(profile_image); //글라이드라이브러리를 사용하여 url 이미지를 바로 이미지뷰에 로드시킨다.
            profile_name.setText(face_name);
        }


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(info_Activity.this);
                dialog.setTitle("알림")
                        .setMessage("사진을 가져올 곳을 선택해 주세요.")
                        .setPositiveButton("카메라", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(info_Activity.this,"카메라 버튼을 누르셨습니다.",Toast.LENGTH_SHORT).show();
                                requirePermission();
                                boolean camera =  ContextCompat.checkSelfPermission
                                        (v.getContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;


                                boolean write = ContextCompat.checkSelfPermission(v.getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

                                if (camera && write) {
                                    //사진찍는 인텐트 코드 넣기.

                                    takePicture();

                                }else {
                                    Toast.makeText(info_Activity.this,"권한이 없습니다 권한을 받아주세요",Toast.LENGTH_LONG).show();

                                }


                            }
                        })
                        .setNegativeButton("갤러리", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(info_Activity.this,"갤러리 버튼을 누르셨습니다",Toast.LENGTH_SHORT).show();
                                selectGallery();
                            }
                        }).create().show();



            }
        });




        back_imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(100,intent);
                finish();
            }
        });


            //로그아웃 버튼 클릭시 로그인화면으로 다시 이동한다.
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(kakao_name != null ){
                onClick_kakao_Logout();

            }else if(face_email != null){
                onClick_face_Logout();
            }

            }
        });

    }






    private void onClick_kakao_Logout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                // 원하는 코드 ( 예를 들면 액티비티 이동)
                Intent intent = new Intent(info_Activity.this,login_Activity.class);
                startActivity(intent);
            }
        });
    }

    private void onClick_face_Logout() {
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(info_Activity.this,login_Activity.class);
        startActivity(intent);
    }



    void requirePermission() {
        String [] permissions = new String [] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
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
    void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File phtoFile = createImageFile();
            Uri photoUri = FileProvider.getUriForFile(this,"com.example.user.music.fileprovider",phtoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
            startActivityForResult(intent, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;

    }


    private void selectGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 20);
    }

    //갤러리에서 사진을 가져오기
    private void sendPicture(Uri imgUri) {

        String imagePath = getRealPathFromURI(imgUri); // path 경로
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        profile_image.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기

    }

    //갤러리 사진의 절대경로 구하기.
    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }


    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }



    private Bitmap rotate(Bitmap src, float degree) {

// Matrix 객체 생성
        Matrix matrix = new Matrix();
// 회전 각도 셋팅
        matrix.postRotate(degree);
// 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {

            profile_image.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
        }else if(requestCode == 20){
            sendPicture(data.getData());
        }

    }




}
