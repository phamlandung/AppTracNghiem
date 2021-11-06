package com.example.apptracnghiem.model;

import static com.huawei.agconnect.auth.VerifyCodeSettings.ACTION_REGISTER_LOGIN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apptracnghiem.MainActivity;
import com.example.apptracnghiem.R;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.agconnect.auth.EmailUser;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.agconnect.auth.VerifyCodeResult;
import com.huawei.agconnect.auth.VerifyCodeSettings;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hmf.tasks.TaskExecutors;

import java.util.Locale;

public class Dangky extends AppCompatActivity {
    Button btnGuima, btnQuaylai, btnDangky;
    EditText edEmail, edMatkhau, edCode;
    private AGConnectAuth huaweiAuth;
    private String email;
    private String password;
    private String verCode;
    AGConnectUser user;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);
        btnDangky=findViewById(R.id.btnDangky);
        btnQuaylai=findViewById(R.id.btnQuaylai);
        btnGuima=findViewById(R.id.btnGuima);
        edEmail=findViewById(R.id.edEmail);
        edMatkhau=findViewById(R.id.edMatkhau);
        edCode=findViewById(R.id.edCode);
        huaweiAuth = AGConnectAuth.getInstance();


        btnGuima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCodeVerification();
            }
        });

        btnDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpWithEmail();
            }
        });

        btnQuaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void sendCodeVerification() {
        email = edEmail.getText().toString();
        if (email.isEmpty() || email == null) {
            Toast.makeText(this, "Không được để trông Email !!! ", Toast.LENGTH_LONG).show();
        } else {
            VerifyCodeSettings settings = VerifyCodeSettings.newBuilder()
                    .action(ACTION_REGISTER_LOGIN)
                    .sendInterval(30)
                    .locale(Locale.ENGLISH)
                    .build();
            Task<VerifyCodeResult> task = huaweiAuth.requestVerifyCode(email, settings);
            task.addOnSuccessListener(TaskExecutors.uiThread(), new OnSuccessListener<VerifyCodeResult>() {
                @Override
                public void onSuccess(VerifyCodeResult verifyCodeResult) {
                    Toast.makeText(Dangky.this, "Kiểm tra mã xác minh trong Email của bạn !", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(TaskExecutors.uiThread(), new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Log.d("VerifyCodeErr", e.getMessage());
                }
            });


        }
    }
    private void signUpWithEmail() {
            email = edEmail.getText().toString();
            password = edMatkhau.getText().toString();
            verCode = edCode.getText().toString();


        if (email.isEmpty() || password.isEmpty() || verCode.isEmpty()) {
            Toast.makeText(this, "Không được để trống !", Toast.LENGTH_LONG).show();
        } else {
            EmailUser emailUser = new EmailUser.Builder().setEmail(email)
                    .setVerifyCode(verCode)
                    .setPassword(password).build();
            AGConnectAuth.getInstance().createUser(emailUser)
                    .addOnCompleteListener(new OnCompleteListener<SignInResult>() {
                        @Override
                        public void onComplete(Task<SignInResult> task) {
                            if (task.isSuccessful()) {
                                user = AGConnectAuth.getInstance().getCurrentUser();
                                userID = user.getUid();
                                //call back login form
                                Intent intent = new Intent();
                                intent.putExtra("uid", userID);
                                intent.putExtra("email", email);
                                intent.putExtra("password", password);
                                setResult(RESULT_OK, intent);
                                Toast.makeText(Dangky.this, "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
                                Intent intt = new Intent(Dangky.this, MainActivity.class);
                                startActivity(intt);
                                finish();


                            } else {
                                Log.d("SignUpErr", task.getException().getMessage());
                                Toast.makeText(Dangky.this, "Đăng ký tài khoản thất bại", Toast.LENGTH_SHORT).show();
                                Toast.makeText(Dangky.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}