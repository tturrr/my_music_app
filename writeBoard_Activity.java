package com.example.user.music;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class writeBoard_Activity extends AppCompatActivity {

    private String mCurrentPhotoPath;

    ImageView write_btn;
    ImageButton back_btn;
    ImageView writeImg_view;
    EditText title_txt;
    EditText content_txt;
    ImageView write_cmr_btn;
    SharedPreferences akey_save;
    int a=0;
    private String asd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_board_);
        Intent intent = getIntent();
        final String login_id = intent.getStringExtra("login_id");
        write_cmr_btn = (ImageView)findViewById(R.id.write_cmr_btn);
        writeImg_view = (ImageView)findViewById(R.id.writeImg_view);
        write_btn = (ImageView)findViewById(R.id.write_btn);
        back_btn = (ImageButton)findViewById(R.id.back_imgbutton);
        content_txt = (EditText)findViewById(R.id.content_txt);
        title_txt = (EditText)findViewById(R.id.title_txt);
        final SharedPreferences bod_num = getSharedPreferences(login_id+"bod_num",0);
        final SharedPreferences.Editor edit_bod_num = bod_num.edit();
        akey_save = getSharedPreferences(login_id+"akey",0);
        a = akey_save.getInt(login_id+"akey1",0);










        //글쓰기 버튼 클릭시 보드액티비티의 온액티비티 리절트로 돌아간다.
        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(writeBoard_Activity.this,board_Activity.class);

                SharedPreferences img = getSharedPreferences(login_id+"img",0);
                SharedPreferences.Editor edit_img = img.edit();
                String urs = mCurrentPhotoPath;
                String gal = asd;




                SharedPreferences akey_save = getSharedPreferences(login_id+"akey",0);
                SharedPreferences.Editor akey_editor = akey_save.edit();
                a++;

                SharedPreferences title_txt1 = getSharedPreferences(login_id+"title_txt",0);
                SharedPreferences.Editor title_txt_editor = title_txt1.edit();

                SharedPreferences content1 = getSharedPreferences(login_id+"content_txt",0);
                SharedPreferences.Editor content_txt_editor = content1.edit();

                content_txt_editor.putString(String.valueOf(a),content_txt.getText().toString()).commit();
                title_txt_editor.putString(String.valueOf(a),title_txt.getText().toString()).commit();
                akey_editor.putInt(login_id+"akey1",a).commit();
                if(mCurrentPhotoPath == null){
                    edit_img.putString(String.valueOf(a),gal).commit();
                }else {
                    edit_img.putString(String.valueOf(a), urs).commit();
                }
                edit_bod_num.putString(String.valueOf(a),String.valueOf(a)).commit();

                setResult(105,intent);
                finish();
            }
        });



        //카메라 or 갤러리버튼 사진또는 갤러리에서 이미지를 이미지뷰로 가져온다.
        write_cmr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(writeBoard_Activity.this);
                dialog.setTitle("알림")
                        .setMessage("사진을 가져올 곳을 선택해 주세요.")
                        .setPositiveButton("카메라", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(writeBoard_Activity.this,"카메라 버튼을 누르셨습니다.",Toast.LENGTH_SHORT).show();
                                requirePermission();
                                boolean camera =  ContextCompat.checkSelfPermission
                                        (v.getContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;


                                boolean write = ContextCompat.checkSelfPermission(v.getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

                                if (camera && write) {
                                    //사진찍는 인텐트 코드 넣기.

                                    takePicture();

                                }else {
                                    Toast.makeText(writeBoard_Activity.this,"권한이 없습니다 권한을 받아주세요",Toast.LENGTH_LONG).show();

                                }


                            }
                        })
                        .setNegativeButton("갤러리", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(writeBoard_Activity.this,"갤러리 버튼을 누르셨습니다",Toast.LENGTH_SHORT).show();
                                selectGallery();
                            }
                        }).create().show();
            }
        });





        //뒤로가기 버튼 뒤로 이동한다.
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(writeBoard_Activity.this,board_Activity.class);
                startActivity(intent);
            }
        });
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
    private Uri sendPicture(Uri imgUri) {

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
        writeImg_view.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기
        Bitmap bitmap1 = ((BitmapDrawable)writeImg_view.getDrawable()).getBitmap();
        return getImageUri(writeBoard_Activity.this,bitmap1);
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
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);

            try{
                // 기본 카메라 모듈을 이용해 촬영할 경우 가끔씩 이미지가
                // 회전되어 출력되는 경우가 존재하여
                // 이미지를 상황에 맞게 회전시킨다
                ExifInterface exif = new ExifInterface(mCurrentPhotoPath);
                int exifOrientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int exifDegree = exifOrientationToDegrees(exifOrientation);

                //회전된 이미지를 다시 회전시켜 정상 출력
                imageBitmap = rotate(imageBitmap, exifDegree);

                //회전시킨 이미지를 저장
                saveExifFile(imageBitmap, mCurrentPhotoPath);

                //비트맵 메모리 반환
                imageBitmap.recycle();
            }catch (IOException e){
                e.getStackTrace();
            }

            writeImg_view.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
        }else if(requestCode == 20){
            sendPicture(data.getData());
            asd = String.valueOf(data.getData());

        }

    }

    public void saveExifFile(Bitmap imageBitmap, String savePath){
        FileOutputStream fos = null;
        File saveFile = null;

        try{
            saveFile = new File(savePath);
            fos = new FileOutputStream(saveFile);
            //원본형태를 유지해서 이미지 저장
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

        }catch(FileNotFoundException e){
            //("FileNotFoundException", e.getMessage());
        }finally {
            try {
                if(fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
            }
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
