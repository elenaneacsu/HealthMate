package com.elenaneacsu.healthmate.screens.signup.fragments;


import android.content.Context;
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
import com.elenaneacsu.healthmate.model.User;
import com.elenaneacsu.healthmate.model.WeightRecord;
import com.elenaneacsu.healthmate.screens.login.LogInActivity;
import com.elenaneacsu.healthmate.screens.signup.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.elenaneacsu.healthmate.utils.CaloriesUtils.calculateGoalCalories;
import static com.elenaneacsu.healthmate.utils.Constants.USER;
import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountDetailsFragment extends Fragment {

    private EditText mEditTextName;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private EditText mEditTextConfirmPassword;
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
        mEditTextConfirmPassword = view.findViewById(R.id.edittext_confirmpassword);
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
                final Context context = getActivity().getApplicationContext();
                if (mEditTextName.getText().toString().isEmpty() || mEditTextEmail.getText().toString().isEmpty()
                        || mEditTextPassword.getText().toString().isEmpty() || mEditTextConfirmPassword.getText().toString().isEmpty()) {
                    showToast(context, getString(R.string.supply_values));
                } else if (!mEditTextPassword.getText().toString().equals(mEditTextConfirmPassword.getText().toString())) {
                    showToast(context, getString(R.string.passwords_dont_match));
                } else {
                    final String name = mEditTextName.getText().toString();
                    final String email = mEditTextEmail.getText().toString();
                    final String password = mEditTextPassword.getText().toString();

                    mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(signUpActivity, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        if (password.length() < 6) {
                                            showToast(context, getString(R.string.password_too_short));
                                        } else {
                                            showToast(context, getString(R.string.signup_error));
                                        }
                                    } else {
                                        user.setName(name);
                                        user.setEmail(email);
                                        user.setPassword(password);

                                        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
                                        if (currentUser != null) {
                                            currentUser.sendEmailVerification()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                showToast(context, getString(R.string.verify_email));
                                                            }
                                                        }
                                                    });
                                        }
                                        startActivity(new Intent(signUpActivity, LogInActivity.class));
                                        mFirestore.collection("users").document(currentUser.getUid()).set(user);

                                        logWeight(currentUser);
                                        //logGoalCalsForCurrentDate(currentUser);

                                        //getActivity().finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void logWeight(FirebaseUser currentUser) {
        WeightRecord weightRecord = new WeightRecord();
        weightRecord.setWeight(user.getCurrentWeight());
        weightRecord.setDate(Calendar.getInstance().getTime());
        mFirestore.collection("users").document(currentUser.getUid())
                .collection("weight_history").add(weightRecord);
    }



    private void logGoalCalsForCurrentDate(FirebaseUser currentUser) {
        Map<String, Long> goalCals = new HashMap<>();
        goalCals.put("goalCalories", calculateGoalCalories(user));

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mFirestore.collection("users").document(currentUser.getUid()).collection("stats")
                .document(String.valueOf(year)).collection(String.valueOf(month)).document(String.valueOf(day)).set(goalCals);

    }
}
