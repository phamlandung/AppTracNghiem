package com.example.apptracnghiem.model;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.apptracnghiem.MainActivity;
import com.example.apptracnghiem.R;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.agconnect.auth.EmailAuthProvider;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.banner.BannerView;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;

public class Dangnhap extends AppCompatActivity {
    Button btnDangnhap, btnIntenDangKy;
    EditText LoginEmail, LoginPassword;
    AccountAuthParams authParams;
    AGConnectUser user;
    String userID;
    final static int CREATE_USER = 101;
    private static int SPLASH_TIME_OUT = 3000;
    String email = "", password = "", uid = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);
        btnDangnhap=findViewById(R.id.btnDangnhap);
        btnIntenDangKy=findViewById(R.id.btnIntenDangky);
        LoginEmail=findViewById(R.id.LoginEmail);
        LoginPassword=findViewById(R.id.LoginPassword);
        user = AGConnectAuth.getInstance().getCurrentUser();
        authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).createParams();

        btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = LoginEmail.getText().toString();
                password = LoginPassword.getText().toString();

                if (!email.isEmpty() || !password.isEmpty()) {
                    signInID(email, password);
                }else{
                    Toast.makeText(Dangnhap.this, "Hãy nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnIntenDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dangnhap.this, Dangky.class);
                startActivityForResult(intent, CREATE_USER);
                finish();

            }
        });
        HwAds.init(Dangnhap.this);

        // Obtain BannerView configured in the XML layout file.
        BannerView bottomBannerView = findViewById(R.id.hw_banner_view);
        AdParam adParam = new AdParam.Builder().build();
        bottomBannerView.loadAd(adParam);

        // Add BannerView through coding.
        BannerView topBannerView = new BannerView(Dangnhap.this);
        topBannerView.setAdId("testw6vs28auh3");
        topBannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_SMART);


        RelativeLayout rootView = findViewById(R.id.root_view);
        rootView.addView(topBannerView);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_USER && resultCode == RESULT_OK) {
            uid = data.getStringExtra("uid");
            email = data.getStringExtra("email");
            password = data.getStringExtra("password");
        }
    }
    private void signInID(String email, String password) {

        AGConnectAuthCredential credential = EmailAuthProvider.credentialWithPassword(email, password);
        AGConnectAuth.getInstance().signIn(credential)
                .addOnSuccessListener(new OnSuccessListener<SignInResult>() {
                    @Override
                    public void onSuccess(SignInResult signInResult) {
                        // Obtain sign-in information.

                        user = AGConnectAuth.getInstance().getCurrentUser();
                        userID = user.getUid();
                        callMainActivity(userID);
                        finish();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(Dangnhap.this, "Tài khoản hoặc mật khẩu không chính xác !", Toast.LENGTH_LONG).show();
                        Log.e("login error: ", e.getMessage());
                    }
                });
    }
    private void callMainActivity(String userID) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent1 = new Intent(Dangnhap.this, MainActivity.class);
                startActivity(intent1);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}