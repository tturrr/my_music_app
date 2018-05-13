package com.example.user.music;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.music.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class register_Activity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 10;
    ImageButton back_imgbtn;
    EditText register_id_edit;
    EditText register_pass_edit;
    EditText register_pass_confirm_edit;
    Button register_suc_btn;
    EditText register_nick_edit;
    private FirebaseAuth mAuth;
    private String asd;
    private ImageView profile_view;
    ArrayList<String> id_List;
    String ID_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        back_imgbtn = (ImageButton)findViewById(R.id.back_imgbutton);
        final SharedPreferences ID = getSharedPreferences("id", MODE_PRIVATE);
        final SharedPreferences.Editor edit_ID = ID.edit();

        profile_view = (ImageView)findViewById(R.id.profile_image);
        register_suc_btn = (Button)findViewById(R.id.register_suc_btn);
        register_id_edit = (EditText)findViewById(R.id.register_id_edit);
        register_pass_edit = (EditText)findViewById(R.id.register_pass_edit);
        register_nick_edit = (EditText)findViewById(R.id.nick_txt);
        register_pass_confirm_edit = (EditText)findViewById(R.id.register_pass_confirm_edit);
        mAuth = FirebaseAuth.getInstance();


        //뒤로가는 화살표 이미지버튼을 클릭하면 로그인화면으로 돌아가게 된다.
        back_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register_Activity.this,login_Activity.class);
                startActivity(intent);
            }
        });

        //프로필 이미지 뷰 클릭시.
        profile_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGallery();
            }
        });

        //회원가입 완료할시에 데이터베이스에 유저의 고유아이디를 저장한다.
        register_suc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                final String edit_id =  register_id_edit.getText().toString();
                String edit_pass = register_pass_edit.getText().toString();
                String edit_confpass = register_pass_confirm_edit.getText().toString();
                final String edit_nick = register_nick_edit.getText().toString();


                if(edit_nick.isEmpty()){
                    Toast.makeText(register_Activity.this,"닉네임을 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else if(edit_id.isEmpty()){
                    Toast.makeText(register_Activity.this,"아이디를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else if(edit_pass.isEmpty()){
                    Toast.makeText(register_Activity.this,"비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                }
                else if(edit_confpass.isEmpty()){
                    Toast.makeText(register_Activity.this,"비밀번호 확인 란을 입력해주세요.",Toast.LENGTH_SHORT).show();
                }else if(null == asd){

                    asd = String.valueOf(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.kakao_default));
                }
                else if(!ID.getString(edit_id,"noID").equals("noID")){ //ID라는 SharedPreference안에 값이 있는지 확인하기 위한 절차가 필요
                    Toast.makeText(register_Activity.this, "이미 사용중인 ID 입니다. 다른 ID를 입력해주세요", Toast.LENGTH_SHORT).show();

                }
                else{

                    Toast.makeText(register_Activity.this, "사용 가능한 ID입니다.", Toast.LENGTH_SHORT).show();
                    ID_check = edit_id;


                    SharedPreferences nick = getSharedPreferences("nick", MODE_PRIVATE);
                    SharedPreferences.Editor edit_name = nick.edit();
                    SharedPreferences password = getSharedPreferences("password", MODE_PRIVATE);
                    SharedPreferences.Editor edit_passwrod = password.edit();
                    SharedPreferences id_img = getSharedPreferences("id_img",0);
                    SharedPreferences.Editor edit_id_img = id_img.edit();


                    edit_ID.putString(edit_id, edit_id);
                    edit_name.putString(edit_id, edit_nick);
                    edit_passwrod.putString(edit_id,edit_pass);
                    edit_id_img.putString(edit_id, String.valueOf(asd));

                    edit_ID.commit();
                    edit_name.commit();
                    edit_passwrod.commit();
                    edit_id_img.commit();

                    mAuth
                            .createUserWithEmailAndPassword(register_id_edit.getText().toString(),register_pass_edit.getText().toString())
                            .addOnCompleteListener(register_Activity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    final String uid = task.getResult().getUser().getUid();

                                    FirebaseStorage.getInstance().getReference().child("userImages").child(uid).putFile(Uri.parse(asd)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                            String imageUrl = task.getResult().getDownloadUrl().toString();


                                            UserModel userModel = new UserModel();
                                            userModel.userName = edit_nick;
                                            userModel.profileImageUrl = imageUrl;
                                            userModel.uid = mAuth.getUid();




                                            FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    intent.putExtra("login_id",edit_id);

                                                    setResult(100,intent);
                                                    finish();
                                                }
                                            });


                                        }
                                    });

                                }
                            });
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 20 && resultCode == RESULT_OK){


            sendPicture(data.getData()); // 이미지뷰를 변경함.
            asd = String.valueOf(data.getData()); //이미지 경로 원본
        }
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
        profile_view.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기
        Bitmap bitmap1 = ((BitmapDrawable)profile_view.getDrawable()).getBitmap();
        return getImageUri(register_Activity.this,bitmap1);

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
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



}