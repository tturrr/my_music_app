package com.example.user.music;

import android.content.Context;
import android.Manifest;
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

public class boardModify_Activity extends AppCompatActivity {

    ImageButton back_imgbutton;
    ImageView img_btn;
    ImageView delete_btn;
    ImageView modify_btn;
    EditText title_txt;
    EditText content_txt;
    ImageView writeImg_view;

    SharedPreferences akey_save;
    SharedPreferences img ;
    SharedPreferences title_txt1 ;
    SharedPreferences content1;

    private String mCurrentPhotoPath;
    private String asd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_modify_);


        back_imgbutton = (ImageButton)findViewById(R.id.back_imgbutton);
        img_btn = (ImageView)findViewById(R.id.img_btn);
        delete_btn = (ImageView)findViewById(R.id.delete_btn);
        modify_btn = (ImageView)findViewById(R.id.modify_btn);
        title_txt = (EditText)findViewById(R.id.title_txt);
        content_txt = (EditText)findViewById(R.id.content_txt);
        writeImg_view = (ImageView)findViewById(R.id.writeImg_view);

        title_txt1 = getSharedPreferences("title_txt", 0);
        content1 = getSharedPreferences("content_txt",0);
        img = getSharedPreferences("img",0);

        final int chk_position = getIntent().getIntExtra("position",0);
        int chk = chk_position;
        //이미지와 텍스트들을 포지션값을 가져와 거기에맞는 아이템들을 가지고 셋트한다.
        title_txt.setText(title_txt1.getString(String.valueOf(chk),"no_title"));
        content_txt.setText(content1.getString(String.valueOf(chk),"no_content"));
        String image = img.getString(String.valueOf(chk),"sad");
        Uri get_uri = Uri.parse(image);
        writeImg_view.setImageURI(get_uri);


        //카메라&&갤러리 버튼 클릭시 이미지를 가져와 뷰에 적용한다.
        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(boardModify_Activity.this);
                dialog.setTitle("알림")
                        .setMessage("사진을 가져올 곳을 선택해 주세요.")
                        .setPositiveButton("카메라", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(boardModify_Activity.this,"카메라 버튼을 누르셨습니다.",Toast.LENGTH_SHORT).show();
                                requirePermission();
                                boolean camera =  ContextCompat.checkSelfPermission
                                        (v.getContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;


                                boolean write = ContextCompat.checkSelfPermission(v.getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

                                if (camera && write) {
                                    //사진찍는 인텐트 코드 넣기.

                                    takePicture();

                                }else {
                                    Toast.makeText(boardModify_Activity.this,"권한이 없습니다 권한을 받아주세요",Toast.LENGTH_LONG).show();

                                }


                            }
                        })
                        .setNegativeButton("갤러리", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(boardModify_Activity.this,"갤러리 버튼을 누르셨습니다",Toast.LENGTH_SHORT).show();
                                selectGallery();
                            }
                        }).create().show();
            }
        });

        //수정버튼 클릭시 포지션위치에 맞는 리스트뷰의 내용을 수정한다.
        modify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(boardModify_Activity.this);
                dialog.setTitle("알림")
                        .setMessage("글을 수정 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                title_txt1 = getSharedPreferences("title_txt", 0);
                                content1 = getSharedPreferences("content_txt",0);
                                img = getSharedPreferences("img",0);

                                SharedPreferences.Editor edit_title = title_txt1.edit();
                                SharedPreferences.Editor edit_contents = content1.edit();
                                SharedPreferences.Editor edit_img = img.edit();
                                String urs = mCurrentPhotoPath;
                                String gal = asd;


                                edit_title.putString(String.valueOf(chk_position),title_txt.getText().toString()).commit();
                                edit_contents.putString(String.valueOf(chk_position),content_txt.getText().toString()).commit();

                                if(mCurrentPhotoPath==null){


                                    edit_img.putString(String.valueOf(chk_position), String.valueOf(gal)).commit();
                                }else{
                                    edit_img.putString(String.valueOf(chk_position),urs).commit();
                                }

                                Intent intent = new Intent(boardModify_Activity.this,board_Activity.class);

                                setResult(300,intent);
                                finish();
                            }
                        })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
        }
        });


        //삭제버튼 클릭시 포지션 위치에 맞는 리스트뷰의 아이템을 지운다.

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(boardModify_Activity.this);
                dialog.setTitle("알림")
                        .setMessage("글을 삭제 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                title_txt1 = getSharedPreferences("title_txt", 0);


                                SharedPreferences.Editor edit_title = title_txt1.edit();

                                edit_title.remove(String.valueOf(chk_position)).commit();


                                Intent intent = new Intent(boardModify_Activity.this, board_Activity.class);
                                setResult(200, intent);
                                finish();
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create().show();
            }
        });



        //뒤로가기 버튼 클릭시 뒤로간다.
        back_imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(boardModify_Activity.this,board_Activity.class);
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
        return getImageUri(boardModify_Activity.this,bitmap1);

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

            try {
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
            } catch (IOException e) {
                e.getStackTrace();
            }

            writeImg_view.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
        }
        else if(requestCode == 20){



            asd= String.valueOf(sendPicture(data.getData()));
//            asd = String.valueOf(data.getData());
        }

    }
    //카메라로 찍은 사진의 회전을 위하여 저장하는 부분.
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
            }catch(IOException e){
                //("IOException", e.getMessage());
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
