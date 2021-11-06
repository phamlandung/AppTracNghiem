package com.example.apptracnghiem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.apptracnghiem.model.Category;
import com.example.apptracnghiem.model.Dangnhap;
import com.example.apptracnghiem.model.Startapp;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textViewHighScore;
    private Spinner spinnerCategory;
    private Button buttonStartQuestion;
    Button btnDangxuat;
    TextView txtemail;
    AGConnectUser user;
    String userID;

    private int highscore;
    private static final int REQUEST_CODE_QUESTION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();

        loadCategories();

        loadHighScore();
        txtemail.setText(AGConnectAuth.getInstance().getCurrentUser().getEmail());

        buttonStartQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuestion();
            }
        });
        btnDangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AGConnectAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, Startapp.class));
                finish();
            }
        });
    }

    private void loadHighScore() {
        SharedPreferences preferences = getSharedPreferences("share",MODE_PRIVATE);
        highscore = preferences.getInt("highscore",0);
        textViewHighScore.setText("Điểm cao : "+highscore);
    }

    private void startQuestion() {

        Category category = (Category) spinnerCategory.getSelectedItem();
        int categoryID = category.getId();
        String categoryName = category.getName();

        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);

        intent.putExtra("idcategories",categoryID);
        intent.putExtra("catgoriesname",categoryName);

        startActivityForResult(intent,REQUEST_CODE_QUESTION);

    }

    private void AnhXa(){
        textViewHighScore = findViewById(R.id.textview_high_score);
        buttonStartQuestion = findViewById(R.id.button_start_question);
        spinnerCategory = findViewById(R.id.spinner_category);
        btnDangxuat=findViewById(R.id.btnDangxuat);
        txtemail=findViewById(R.id.txtemail);
    }

    private void loadCategories(){
        Database database = new Database(this);

        List<Category> categories = database.getDataCategories();

        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,categories);

        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategory.setAdapter(categoryArrayAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_QUESTION){
            if (resultCode == RESULT_OK){
                int score = data.getIntExtra("Score", 0);

                if (score > highscore){
                    updateHighScore(score);
                }
            }
        }
    }
    //cập nhật điểm cao
    private void updateHighScore(int score) {

        highscore = score;

        textViewHighScore.setText("Điểm cao : "+highscore);

        SharedPreferences preferences = getSharedPreferences("share",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //gán giá trị cho điểm cao mới vào khóa
        editor.putInt("highscore",highscore);
        //hoàn tất
        editor.apply();

    }





}