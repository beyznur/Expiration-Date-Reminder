package com.beyzanur.expiration_date_reminder;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.Nullable;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private LinearLayout mLinear;

    private EditText editEmail, editPassword;
    TextView forgotPassword, signUpText;
    private String txtEmail, txtPassword;

    SharedPreferences preferences;

    SharedPreferences.Editor editor;


    private void init(){
        preferences = getSharedPreferences("GirisBilgi", MODE_PRIVATE);
        editor = preferences.edit();
        mAuth = FirebaseAuth.getInstance();
        editEmail = (EditText)findViewById(R.id.username);
        editPassword = (EditText)findViewById(R.id.password);
        mLinear=findViewById(R.id.mLinear);
        forgotPassword=findViewById(R.id.forgot_password);
        signUpText=findViewById(R.id.signupText);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        String email = preferences.getString("email", "");
        String password = preferences.getString("password", "");

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

    }

        public void btnLogin(View v){
            txtEmail = editEmail.getText().toString().trim();
            txtPassword = editPassword.getText().toString().trim();

            if (!TextUtils.isEmpty(txtEmail)){
                if (!TextUtils.isEmpty(txtPassword)){

                    mProgress = new ProgressDialog(this);
                    mProgress.setTitle("Logging in...");
                    mProgress.show();

                    mAuth.signInWithEmailAndPassword(txtEmail, txtPassword)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){

                                        editor.putString("email", txtEmail);
                                        editor.putString("password", txtPassword);
                                        editor.apply();

                                        progressAyar();
                                      Toast.makeText(LoginActivity.this, "You have successfully logged in", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    }else{
                                        progressAyar();
                                        Snackbar.make(mLinear, task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }else
                    Toast.makeText(LoginActivity.this, "Please enter a valid password.", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(LoginActivity.this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
        }

        private void progressAyar(){
            if (mProgress.isShowing())
                mProgress.dismiss();
        }
    }