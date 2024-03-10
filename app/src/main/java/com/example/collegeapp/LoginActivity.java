package com.example.collegeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {


    private EditText emailEditText;
    public boolean admin;
    private EditText passwordEditText;
    private Button loginbtn;
    public Button uploadbtn,deletebtn;
    Button b1  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginbtn = findViewById(R.id.loginbtn);

        Button btnOpenBottomSheet = findViewById(R.id.btnOpenBottomSheet);
        Intent intent = new Intent(this,MainActivity.class);



        btnOpenBottomSheet.setOnClickListener(view -> {

            startActivity(intent);
        });
        loginbtn = (Button) findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (emailEditText.getText().toString().equals("k@gmail.com") && passwordEditText.getText().toString().equals("1")) {
                    Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();

                    startActivity(intent);

                    AppData.isLoggedIn=true;
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }



            }
        });


    }
}