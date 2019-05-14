package com.elenaneacsu.healthmate.screens.signup.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.screens.entities.User;
import com.elenaneacsu.healthmate.screens.signup.SignUpActivity;
import com.elenaneacsu.healthmate.utils.Constants;

import static com.elenaneacsu.healthmate.utils.Constants.USER;
import static com.elenaneacsu.healthmate.utils.GetThemeColors.getThemeAccentColor;
import static com.elenaneacsu.healthmate.utils.GetThemeColors.getThemePrimaryColor;
import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityLevelFragment extends Fragment {

    private Button mBtnNotActive;
    private Button mBtnLightlyActive;
    private Button mBtnActive;
    private Button mBtnVeryActive;
    private ImageButton mBtnNext;
    private ImageButton mBtnBack;
    private User user;
    public SignUpActivity signUpActivity;

    private boolean enableNext = false;

    public ActivityLevelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_level, container, false);

        mBtnNotActive = view.findViewById(R.id.btn_notactive);
        mBtnLightlyActive = view.findViewById(R.id.btn_lightlyactive);
        mBtnActive = view.findViewById(R.id.btn_active);
        mBtnVeryActive = view.findViewById(R.id.btn_veryactive);
        mBtnNext = view.findViewById(R.id.btn_next);
        mBtnBack = view.findViewById(R.id.btn_back);
        signUpActivity = (SignUpActivity) getActivity();

        Bundle bundle = getArguments();
        if(bundle!=null) {
            user = bundle.getParcelable(USER);
        }

        return view;
    }

    public static ActivityLevelFragment newInstance(Bundle arguments) {
        ActivityLevelFragment activityLevelFragment = new ActivityLevelFragment();
        if (arguments != null) {
            activityLevelFragment.setArguments(arguments);
        }
        return activityLevelFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnNotActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsDesign(mBtnNotActive, mBtnLightlyActive, mBtnActive, mBtnVeryActive);
                user.setActivityLevel(getString(R.string.not_active));
                enableNext = true;
            }
        });

        mBtnLightlyActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsDesign(mBtnLightlyActive, mBtnNotActive, mBtnActive, mBtnVeryActive);
                user.setActivityLevel(getString(R.string.lightly_active));
                enableNext = true;
            }
        });

        mBtnActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsDesign(mBtnActive, mBtnNotActive, mBtnLightlyActive, mBtnVeryActive);
                user.setActivityLevel(getString(R.string.active));
                enableNext = true;
            }
        });

        mBtnVeryActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsDesign(mBtnVeryActive, mBtnNotActive, mBtnLightlyActive, mBtnActive);
                user.setActivityLevel(getString(R.string.very_active));
                enableNext = true;
            }
        });

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!enableNext) {
                    showToast(getContext(), getString(R.string.choose_option));
                } else {
                    signUpActivity.mView2.setBackgroundColor(getThemeAccentColor(getContext()));
                    initNextFragment();
                }
            }
        });

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initBackFragment();
                signUpActivity.mView2.setBackgroundColor(Color.WHITE);
            }
        });
    }


    private void setButtonsDesign(Button buttonOne, Button buttonTwo, Button buttonThree, Button buttonFour) {
        buttonOne.setBackgroundResource(R.drawable.button_rounded_color_accent);
        buttonOne.setTextColor(Color.WHITE);
        buttonTwo.setBackgroundResource(R.drawable.button_rounded_corners);
        buttonTwo.setTextColor(getThemePrimaryColor(getContext()));
        buttonThree.setBackgroundResource(R.drawable.button_rounded_corners);
        buttonThree.setTextColor(getThemePrimaryColor(getContext()));
        buttonFour.setBackgroundResource(R.drawable.button_rounded_corners);
        buttonFour.setTextColor(getThemePrimaryColor(getContext()));
    }

    private void initNextFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.USER, user);
        FragmentManager fragmentManager = signUpActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, GenderBirthFragment.newInstance(bundle));
        fragmentTransaction.commit();
    }

    private void initBackFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.USER, user);
        FragmentManager fragmentManager = signUpActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, GoalFragment.newInstance(bundle));
        fragmentTransaction.commit();
    }

}
