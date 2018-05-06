package com.example.user.music;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class login_Activity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Button register_button;
    Button login_button;
    SignInButton google_btn;
    private GoogleApiClient mGoogleApiClient;

    EditText id_edit;
    EditText pass_edit;

    String email, birth, name1, name2, profile_img_string;

    private CallbackManager callbackManager;  //페이스북 콜백매니저

    private com.kakao.usermgmt.LoginButton btnKakao;
    private SessionCallback callback; //카카오 세션콜백

    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";
    com.facebook.login.widget.LoginButton face_login; //페이스북 로그인 버튼
    private static final int  RC_SIGN_IN = 10;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());//페이스북 버튼을 사용하는 경우에 페이스북sdk를 초기화해주는 작업 안하면 레이아웃에 버튼을 추가하는 것만으로 에러가 생긴다.
        setContentView(R.layout.activity_login_);

        mAuth = FirebaseAuth.getInstance();
        register_button = (Button) findViewById(R.id.register_button);
        login_button = (Button) findViewById(R.id.login_button);

        id_edit = (EditText) findViewById(R.id.login_id_text);
        pass_edit = (EditText) findViewById(R.id.login_pass_text);

        google_btn = (SignInButton)findViewById(R.id.google_login_btn);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)      //구글 + 로그인 준비.
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        callback = new SessionCallback(); //세션콜백을 부르고
        Session.getCurrentSession().addCallback(callback); // 추가시키면 끝입니다!!


        callbackManager = CallbackManager.Factory.create(); //페북 세션콜백
        face_login = (com.facebook.login.widget.LoginButton) findViewById(R.id.login_facebook);
        face_login.setReadPermissions(Arrays.asList("public_profile","email","user_birthday"));


        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null) {
                                        // User is signed in
                                               Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                                    } else {
                                       // User is signed out
                                               Log.d(TAG, "onAuthStateChanged:signed_out");
                                    }
                                // [START_EXCLUDE]

                                // [END_EXCLUDE]
                                   }
        };



            //온컴플리트는 이미 정보를 받아온상태에서 진행하는 부분이다.
        if (AccessToken.getCurrentAccessToken() != null) {
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    getdata(object);
                    if (getdata(object) == 1) {
                        Intent intent = new Intent(login_Activity.this, MainActivity.class);
                        intent.putExtra("face_email", email);
                        intent.putExtra("face_birth", birth);
                        intent.putExtra("face_name", name1+name2);
                        intent.putExtra("face_img",profile_img_string);
                        startActivity(intent);
                        finish();
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields","id, email, birthday, friends, first_name, last_name");
            request.setParameters(parameters);
            request.executeAsync();
        }



        requestMe(); //카카오톡 메소드. 아래쪽에서 확인.



        //페이스북 로그인 버튼 클릭시 로그인이 성공 취소 실패로 나뉘는 부븐
        face_login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            //온석세스는 딱한번 로그인을시도할떄 나오는 부분이다.
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSucces LoginResult=" + loginResult);
                Log.d("getAccessToken()", String.valueOf(loginResult.getAccessToken()));

                String accesstoken = loginResult.getAccessToken().getToken();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        Log.d("response",response.toString());
                        getdata(object);
                        if (getdata(object) == 1) {
                            Intent intent = new Intent(login_Activity.this, MainActivity.class);
                            intent.putExtra("face_email", email);
                            intent.putExtra("face_birth", birth);
                            intent.putExtra("face_name", name1 + name2);
                            intent.putExtra("face_img",profile_img_string);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields","id, email, birthday, first_name, last_name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }




        });


        //로그인 버튼을 눌러서 메인 화면으로 이동하는 버튼.
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        //회원가입 버튼을 눌러서 회원가입 화면으로 이동하는 버튼.
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(login_Activity.this, register_Activity.class);
                startActivityForResult(intent, 0);
            }
        });

    }


    //회원가입을 하게되었다면 id 텍스트에 있는 글이 그대로 로그인 텍스트에 넘겨진다.
    //카카오톡 로그인 api를 구현한다. success 부분에 로그인성공시에 구현할 것들을 적으면 된다.

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, intent)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            if (resultCode == 100) {
                String id = intent.getStringExtra("id");
                id_edit.setText(id);
            }

        }
        //페이스북 로그인 콜백을 받기위해 .
        callbackManager.onActivityResult(requestCode, resultCode, intent);

        //구글 로그인 관련.
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();

                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    //카카오톡 세션콜백.
    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        //에러로 인한 로그인 실패
//                        finish();
                    } else {
                        //redirectMainActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onNotSignedUp() {

                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                    //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.

//                    Log.e("UserProfile", userProfile.toString());
//                    Log.e("UserProfile", userProfile.getId() + "");

                    Intent intent = new Intent(login_Activity.this,MainActivity.class);

                    long kakao_number = userProfile.getId();
                    String kakao_name = userProfile.getNickname();
                    String kakao_image = userProfile.getProfileImagePath();
                    intent.putExtra("kakao_number",kakao_number);
                    intent.putExtra("kakao_name",kakao_name);
                    intent.putExtra("kakao_image",kakao_image);
                    startActivity(intent);

                }
            });

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            // 세션 연결이 실패했을때
            // 어쩔때 실패되는지는 테스트를 안해보았음 ㅜㅜ

        }
    }
    public void requestMe() {
        //유저의 정보를 받아오는 함수

        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.e(TAG, "error message=" + errorResult);
//                super.onFailure(errorResult);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {

                Log.d(TAG, "onSessionClosed1 =" + errorResult);
            }

            @Override
            public void onNotSignedUp() {
                //카카오톡 회원이 아닐시
                Log.d(TAG, "onNotSignedUp ");

            }

            @Override
            public void onSuccess(UserProfile result) {
                Log.e("UserProfile", result.toString());
                Log.e("UserProfile", result.getId() + "");
                Intent intent = new Intent(login_Activity.this,MainActivity.class);
                long kakao_number = result.getId();
                String kakao_name = result.getNickname();
                String kakao_image = result.getProfileImagePath();
                intent.putExtra("kakao_number",kakao_number);
                intent.putExtra("kakao_name",kakao_name);
                intent.putExtra("kakao_image",kakao_image);


                startActivity(intent);
            }
        });
    }

    public int getdata(JSONObject object) {
        try {
            email = object.getString("email");
            birth = object.getString("birthday");
            name1 = object.getString("last_name");
            name2 = object.getString("first_name");
            Profile profile = Profile.getCurrentProfile();
            profile_img_string = profile.getProfilePictureUri(100, 100).toString();
            return 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(login_Activity.this,"로그인이 되었습니다.",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(login_Activity.this,MainActivity.class);
                            intent.putExtra("google_id",acct.getId());
                            onStart();
                            startActivity(intent);
                            finish();
                        } else {

                            // If sign in fails, display a message to the user.

                        }

                        // ...
                    }
                });
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListener);

    }
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
