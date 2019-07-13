package com.elenaneacsu.healthmate.screens.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.elenaneacsu.healthmate.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText mEditTextEmail;
    private Button mButtonResetPassword;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initView();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mButtonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void initView() {
        mEditTextEmail = findViewById(R.id.edittext_email);
        mButtonResetPassword = findViewById(R.id.button_resetpassword);
    }

    private void resetPassword() {
        String email = mEditTextEmail.getText().toString();
        if("".equals(email)) {
            showToast(this, "Please insert your email address!");
        } else {
            mFirebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                showToast(getApplicationContext(), "Email sent!");
                                finishActivity();
                            } else {
                                showToast(getApplicationContext(), "An error occurred. Please try again later.");
                            }
                        }
                    });
        }
    }

    private void finishActivity() {
        startActivity(new Intent(ForgotPasswordActivity.this, LogInActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
