package com.elenaneacsu.healthmate.screens.signup.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.model.User;
import com.elenaneacsu.healthmate.screens.signup.SignUpActivity;
import com.elenaneacsu.healthmate.utils.Constants;

import static com.elenaneacsu.healthmate.utils.Constants.USER;
import static com.elenaneacsu.healthmate.utils.GetThemeColors.getThemeAccentColor;
import static com.elenaneacsu.healthmate.utils.GetThemeColors.getThemePrimaryColor;
import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class GenderAgeFragment extends Fragment {

    private Button mBtnMale;
    private Button mBtnFemale;
    private ImageButton mBtnNext;
    private ImageButton mBtnBack;
    private EditText mEditTextAge;
    private User user;
    public SignUpActivity signUpActivity;

    private boolean enableNextGender = false;
    private boolean enableNextAge = false;

    public GenderAgeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gender_age, container, false);

        mBtnMale = view.findViewById(R.id.btn_male);
        mBtnFemale = view.findViewById(R.id.btn_female);
        mEditTextAge = view.findViewById(R.id.edittext_age);
        mBtnNext = view.findViewById(R.id.btn_next);
        mBtnBack = view.findViewById(R.id.btn_back);
        signUpActivity = (SignUpActivity) getActivity();

        Bundle bundle = getArguments();
        if (bundle != null) {
            user = bundle.getParcelable(USER);
        }

        return view;
    }

    public static GenderAgeFragment newInstance(Bundle arguments) {
        GenderAgeFragment genderAgeFragment = new GenderAgeFragment();
        if (arguments != null) {
            genderAgeFragment.setArguments(arguments);
        }
        return genderAgeFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setGender(getString(R.string.m));
                setButtonsDesign(mBtnMale, mBtnFemale);
                enableNextGender = true;
            }
        });

        mBtnFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsDesign(mBtnFemale, mBtnMale);
                user.setGender(getString(R.string.f));
                enableNextGender = true;
            }
        });

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEditTextAge.getText().toString().isEmpty() && mEditTextAge != null) {
                    int age = Integer.parseInt(mEditTextAge.getText().toString());
                    user.setAge(age);
                    enableNextAge = true;
                }
                if (!enableNextGender) {
                    showToast(getContext(), getString(R.string.select_gender));
                } else if (!enableNextAge) {
                    showToast(getContext(), getString(com.elenaneacsu.healthmate.R.string.please_insert_age));
                } else {
                    signUpActivity.mView3.setBackgroundColor(getThemeAccentColor(getContext()));
                    initNextFragment();
                }
            }
        });

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initBackFragment();
                signUpActivity.mView3.setBackgroundColor(Color.WHITE);
            }
        });
    }

    private void setButtonsDesign(Button buttonOne, Button buttonTwo) {
        buttonOne.setBackgroundResource(R.drawable.button_rounded_color_accent);
        buttonOne.setTextColor(Color.WHITE);
        buttonTwo.setBackgroundResource(R.drawable.button_rounded_corners);
        buttonTwo.setTextColor(getThemePrimaryColor(getContext()));
    }

    private void initNextFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.USER, user);
        FragmentManager fragmentManager = signUpActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, WeightHeightFragment.newInstance(bundle));
        fragmentTransaction.commit();
    }

    private void initBackFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.USER, user);
        FragmentManager fragmentManager = signUpActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, ActivityLevelFragment.newInstance(bundle));
        fragmentTransaction.commit();
    }
}
