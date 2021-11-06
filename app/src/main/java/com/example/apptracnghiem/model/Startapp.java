package com.example.apptracnghiem.model;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.apptracnghiem.MainActivity;
import com.example.apptracnghiem.R;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;

import java.util.Timer;
import java.util.TimerTask;

public class Startapp extends AppCompatActivity {


    private TimerTask timerTask;
    private static int SPLASH_TIME_OUT = 3000;

    public String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startapp);

        //full screen
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AGConnectUser user = AGConnectAuth.getInstance().getCurrentUser();

        if(user!=null)
//            silentlySignIn();
//        else
            userEmail = user.getEmail();
        Timer RunSplash = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (userEmail!=null) {
//                            lottieAnimationView.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(Startapp.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(Startapp.this,"Xin chào !",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            Toast.makeText(Startapp.this, "Đăng nhập tài khoản của bạn để tiếp tục !",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Startapp.this, Dangnhap.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        };
        RunSplash.schedule(timerTask, SPLASH_TIME_OUT);
    }
}