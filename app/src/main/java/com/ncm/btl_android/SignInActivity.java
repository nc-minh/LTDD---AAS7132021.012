package com.ncm.btl_android;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private LinearLayout layoutSignUp;
    private EditText editText, editPassWord;
    private Button btnSignIn;
    private ProgressDialog progressDialog;
    private TextView forgotPassword, countDownTimer;

    private int countSubmit = 0;
    public static final int GONE = 0x00000008;
    public static final int VISIBLE = 0x00000000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initUI();
        initListener();
    }

    //create Login UI
    private void initUI(){
        progressDialog = new ProgressDialog(this);

        layoutSignUp = findViewById(R.id.layout_sign_up);
        editText = findViewById(R.id.edt_email);
        editPassWord = findViewById(R.id.edt_password);
        btnSignIn = findViewById(R.id.btn_sign_in);
        forgotPassword = findViewById(R.id.forgot_password);

        countDownTimer = findViewById(R.id.countDownTimer);


    }

    //listener onclick btnlogin
    private void initListener() {
        layoutSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

        btnSignIn.setOnClickListener(v ->{
            onClickSignIn();
        });

        forgotPassword.setOnClickListener(v -> {
            onClickForgotPassword();
        });

    }

    private void countDownTimer(){
        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                countDownTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            @SuppressLint("WrongConstant")
            public void onFinish() {
                Toast.makeText(SignInActivity.this, "Đếm xong!", Toast.LENGTH_SHORT).show();
                countSubmit = 0;
                countDownTimer.setVisibility(GONE);
                btnSignIn.setVisibility(VISIBLE);
            }
        }.start();
    }

    private void onClickSignIn(){
        countSubmit++;
        //chua verify -> de sau
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String strEmail = editText.getText().toString().trim();
        String strPassWord = editPassWord.getText().toString().trim();

            progressDialog.show();

            mAuth.signInWithEmailAndPassword(strEmail, strPassWord)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @SuppressLint("WrongConstant")
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                startActivity(intent);
                                finishAffinity();

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                if(countSubmit == 3){
                                    countDownTimer();
                                    countDownTimer.setVisibility(VISIBLE);
                                    btnSignIn.setVisibility(GONE);
                                }
                            }
                        }
                    });
        //equals == so sanh chuoi
        //compareTo
        //show dialog loading :>

    }
    private void onClickForgotPassword(){
        progressDialog.show();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = "minhxinkzai@gmail.com";

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, "Email sent! please check your email!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SignInActivity.this, "Email sending failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}