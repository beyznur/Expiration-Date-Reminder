package com.beyzanur.expiration_date_reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private ProgressDialog mProgress;
    Button reset_password_button;
    EditText edit_email;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        edit_email= findViewById(R.id.reset_password_edit_email);
        reset_password_button = findViewById(R.id.btn_reset_password);


        reset_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress=new ProgressDialog(ResetPasswordActivity.this);
                mProgress.setTitle("Sending Password Reset Link...");
                mProgress.show();

                String e = edit_email.getText().toString();

                FirebaseAuth.getInstance().sendPasswordResetEmail(e).addOnCompleteListener(new OnCompleteListener<Void>() {


                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            progress();
                            Toast.makeText(ResetPasswordActivity.this, "Your Password Reset Link has been Sent.", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        } else
                            progress();
                        Toast.makeText(ResetPasswordActivity.this, "You have entered an incorrect email address.", Toast.LENGTH_SHORT).show();
                    }

                });


            }
        });
    }
    private void progress(){
        if (mProgress.isShowing())
            mProgress.dismiss();
    }

}