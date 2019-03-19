package com.elenaneacsu.healthmate.screens.signup.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.screens.entities.User;
import com.elenaneacsu.healthmate.screens.signup.SignUpActivity;
import com.elenaneacsu.healthmate.screens.utils.Constants;

import static com.elenaneacsu.healthmate.screens.utils.Constants.USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityLevelFragment extends Fragment {

    private Button mBtnNotActive;
    private Button mBtnLightlyActive;
    private Button mBtnActive;
    private Button mBtnVeryActive;
    private User user;
    public SignUpActivity signUpActivity;

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
        signUpActivity = (SignUpActivity) getActivity();

        signUpActivity.mCustomViewPager.setEnableSwipe(false);

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
                signUpActivity.mCustomViewPager.setEnableSwipe(true);
                setButtonsDesign(mBtnNotActive, mBtnLightlyActive, mBtnActive, mBtnVeryActive);
                user.setActivityLevel(getString(R.string.not_active));
            }
        });

        mBtnLightlyActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsDesign(mBtnLightlyActive, mBtnNotActive, mBtnActive, mBtnVeryActive);
                user.setActivityLevel(getString(R.string.lightly_active));
                signUpActivity.mCustomViewPager.setEnableSwipe(true);
            }
        });

        mBtnActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsDesign(mBtnActive, mBtnNotActive, mBtnLightlyActive, mBtnVeryActive);
                user.setActivityLevel(getString(R.string.active));
                signUpActivity.mCustomViewPager.setEnableSwipe(true);
            }
        });

        mBtnVeryActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsDesign(mBtnVeryActive, mBtnNotActive, mBtnLightlyActive, mBtnActive);
                user.setActivityLevel(getString(R.string.very_active));
                signUpActivity.mCustomViewPager.setEnableSwipe(true);
            }
        });
    }

    public static int getThemePrimaryColor(final Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
        return value.data;
    }

    private void setButtonsDesign(Button buttonOne, Button buttonTwo, Button buttonThree, Button buttonFour) {
        buttonOne.setBackgroundResource(R.drawable.button_rounded_color_accent);
        buttonOne.setTextColor(Color.WHITE);
        buttonTwo.setBackgroundResource(R.drawable.button_rounded);
        buttonTwo.setTextColor(getThemePrimaryColor(getContext()));
        buttonThree.setBackgroundResource(R.drawable.button_rounded);
        buttonThree.setTextColor(getThemePrimaryColor(getContext()));
        buttonFour.setBackgroundResource(R.drawable.button_rounded);
        buttonFour.setTextColor(getThemePrimaryColor(getContext()));
    }

}
