package com.elenaneacsu.healthmate.screens.signup.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.screens.entities.User;
import com.elenaneacsu.healthmate.screens.login.LogInActivity;
import com.elenaneacsu.healthmate.screens.signup.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.elenaneacsu.healthmate.utils.Constants.USER;
import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountDetailsFragment extends Fragment {

    private EditText mEditTextName;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private Button mButtonSignup;
    private User user;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFirebaseAuth;
    public SignUpActivity signUpActivity;

    public AccountDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_details, container, false);

        mEditTextName = view.findViewById(R.id.edittext_name);
        mEditTextEmail = view.findViewById(R.id.edittext_email);
        mEditTextPassword = view.findViewById(R.id.edittext_password);
        mButtonSignup = view.findViewById(R.id.btn_signup);
        signUpActivity = (SignUpActivity) getActivity();

        mFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        Bundle bundle = getArguments();
        if (bundle != null) {
            user = bundle.getParcelable(USER);
        }

        return view;
    }

    public static AccountDetailsFragment newInstance(Bundle arguments) {
        AccountDetailsFragment accountDetailsFragment = new AccountDetailsFragment();
        if (arguments != null) {
            accountDetailsFragment.setArguments(arguments);
        }
        return accountDetailsFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mButtonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditTextName.getText().toString().isEmpty() || mEditTextEmail.getText().toString().isEmpty()
                        || mEditTextPassword.getText().toString().isEmpty()) {
                    showToast(getContext(), getString(R.string.supply_values));
                } else {
                    final String name = mEditTextName.getText().toString();
                    final String email = mEditTextEmail.getText().toString();
                    final String password = mEditTextPassword.getText().toString();
                    if (email.isEmpty() || email == null) {
                        showToast(getContext(), getString(R.string.enter_email));
                    }

                    if (password.isEmpty() || password == null) {
                        showToast(getContext(), getString(R.string.enter_password));
                    }


                    mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(signUpActivity, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        if (password.length() < 6) {
                                            showToast(getContext(), getString(R.string.password_too_short));
                                        } else {
                                            showToast(getContext(), getString(R.string.signup_error));
                                        }
                                    } else {
                                        user.setName(name);
                                        user.setEmail(email);
                                        user.setPassword(password);
                                        initialiseUser();
                                        showToast(getContext(), getString(R.string.success_signup));

                                        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
                                        if (currentUser != null) {
                                            currentUser.sendEmailVerification()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                showToast(getContext(), getString(R.string.verify_email));
                                                            }
                                                        }
                                                    });
                                        }
                                        startActivity(new Intent(signUpActivity, LogInActivity.class));
                                    }
                                }
                            });
                }
            }
        });
    }

    private void initialiseUser() {
        mFirestore.collection("users").add(user);
    }
}
