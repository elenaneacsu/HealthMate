package com.elenaneacsu.healthmate.screens.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.screens.MainActivity;
import com.elenaneacsu.healthmate.screens.signup.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

public class LogInActivity extends AppCompatActivity {

    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private TextView mTextViewForgotPassword;
    private ProgressDialog mAuthProgressDialog;
    private FirebaseAuth mFirebaseAuth;
    public SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mFirebaseAuth = FirebaseAuth.getInstance();

        initView();

        createAuthProgressDialog();
        mSharedPreferences = getSharedPreferences("LOGIN", MODE_PRIVATE);
        if (mSharedPreferences.getBoolean("logged", false)) {
            startActivity(new Intent(LogInActivity.this, MainActivity.class));
            finish();
        }

        mTextViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mEditTextEmail = findViewById(R.id.edittext_email);
        mEditTextPassword = findViewById(R.id.edittext_password);
        mTextViewForgotPassword = findViewById(R.id.textview_forgotpassword);
    }

    public void goToSignUp(View view) {
        startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
    }


    public void login(View view) {
        String email = mEditTextEmail.getText().toString();
        String password = mEditTextPassword.getText().toString();
        if (email.isEmpty()) {
            mEditTextEmail.setError(getString(com.elenaneacsu.healthmate.R.string.please_add_your_email));
        }
        if (password.isEmpty()) {
            mEditTextPassword.setError(getString(com.elenaneacsu.healthmate.R.string.please_add_your_password));
        }
        if (password.length() < 6) {
            showToast(getApplicationContext(), getString(R.string.wrong_credentials));
        }

        mAuthProgressDialog.show();

        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mAuthProgressDialog.dismiss();
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.wrong_credentials), Toast.LENGTH_LONG).show();
                        } else {
                            FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
                            if (currentUser.isEmailVerified()) {
                                startActivity(new Intent(LogInActivity.this, MainActivity.class));
                                mSharedPreferences.edit().putBoolean("logged", true).apply();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.verify_email), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void createAuthProgressDialog() {
        mAuthProgressDialog = new ProgressDialog(this, R.style.AlertDialogStyle);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);
    }
}
