package com.beyzanur.expiration_date_reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    private ProgressDialog mProgress;
    private User account;

    private FirebaseFirestore mFireStore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    private EditText editName, editEmail, editPassword, editRetypePassword;
    private String txtName, txtEmail, txtPassword, txtRetypePassword;

    private void init(){
        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();

        editName = (EditText)findViewById(R.id.sign_up_edit_name);
        editEmail = (EditText)findViewById(R.id.sign_up_edit_email);
        editPassword = (EditText)findViewById(R.id.sign_up_edit_password);
        editRetypePassword = (EditText)findViewById(R.id.sign_up_edit_retype_password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();

    }

    public void btnSignUp(View v){
        txtName = editName.getText().toString().trim();
        txtEmail = editEmail.getText().toString().trim();
        txtPassword = editPassword.getText().toString().trim();
        txtRetypePassword = editRetypePassword.getText().toString().trim();

        if(!TextUtils.isEmpty(txtName)){
            if (!TextUtils.isEmpty(txtEmail)){
                if (!TextUtils.isEmpty(txtPassword)){
                    if (!TextUtils.isEmpty(txtRetypePassword)){
                        if (txtPassword.equals(txtRetypePassword)){
                            mProgress = new ProgressDialog(this);
                            mProgress.setTitle("Registration is in progress...");
                            mProgress.show();

                            mAuth.createUserWithEmailAndPassword(txtEmail, txtPassword)
                                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()){
                                                mUser = mAuth.getCurrentUser();
                                                if (mUser != null){
                                                    account = new User(txtName,txtEmail,txtPassword);
                                                    mFireStore.collection("users").document(mUser.getUid())
                                                            .set(account)
                                                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        progressAyar();
                                                                        Toast.makeText(SignUpActivity.this, "You have successfully registered.", Toast.LENGTH_SHORT).show();
                                                                        finish();
                                                                        startActivity(new Intent(SignUpActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                                    }else{
                                                                        progressAyar();
                                                                        Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }
                                                            });
                                                }

                                            }else{
                                                progressAyar();
                                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                            }


                                        }
                                    });

                        }else
                            Toast.makeText(SignUpActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();

                    }else
                        Toast.makeText(SignUpActivity.this, "Please enter the password information again.", Toast.LENGTH_SHORT).show();

                }else
                    Toast.makeText(SignUpActivity.this, "Please choose a valid password.", Toast.LENGTH_SHORT).show();

            }else
                Toast.makeText(SignUpActivity.this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();

        }else
            Toast.makeText(SignUpActivity.this, "Please enter a valid name information.", Toast.LENGTH_SHORT).show();

    }
    private void progressAyar(){
        if (mProgress.isShowing())
            mProgress.dismiss();
    }
}