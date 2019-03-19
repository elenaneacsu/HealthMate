package com.elenaneacsu.healthmate.screens.signup.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.screens.entities.User;
import com.elenaneacsu.healthmate.screens.signup.CustomViewPager;
import com.elenaneacsu.healthmate.screens.signup.SignUpActivity;
import com.elenaneacsu.healthmate.screens.utils.Constants;

import static com.elenaneacsu.healthmate.screens.utils.Constants.USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoalFragment extends Fragment {

    private Button btnMaintain;
    private Button btnLose;
    private Button btnGain;
    private User user;
    public SignUpActivity signUpActivity;

    public GoalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal, container, false);

        btnMaintain = view.findViewById(R.id.btn_maintain);
        btnLose = view.findViewById(R.id.btn_lose);
        btnGain = view.findViewById(R.id.btn_gain);
        signUpActivity = (SignUpActivity) getActivity();

        Bundle bundle = getArguments();
        if(bundle!=null) {
            user = bundle.getParcelable(USER);
        }
        return view;
    }

    public static GoalFragment newInstance(Bundle arguments) {
        GoalFragment goalFragment = new GoalFragment();
        if (arguments != null) {
            goalFragment.setArguments(arguments);
        }
        return goalFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnMaintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpActivity.mCustomViewPager.setEnableSwipe(true);
                setButtonsDesign(btnMaintain, btnLose, btnGain);
                user.setGoal(getString(R.string.maintain));
            }
        });

        btnLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsDesign(btnLose, btnGain, btnMaintain);
                user.setGoal(getString(R.string.lose));
                signUpActivity.mCustomViewPager.setEnableSwipe(true);
            }
        });

        btnGain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsDesign(btnGain, btnLose, btnMaintain);
                user.setGoal(getString(R.string.gain));
                signUpActivity.mCustomViewPager.setEnableSwipe(true);
            }
        });
    }

    public static int getThemePrimaryColor(final Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
        return value.data;
    }

    private void setButtonsDesign(Button buttonOne, Button buttonTwo, Button buttonThree) {
        buttonOne.setBackgroundResource(R.drawable.button_rounded_color_accent);
        buttonOne.setTextColor(Color.WHITE);
        buttonTwo.setBackgroundResource(R.drawable.button_rounded);
        buttonTwo.setTextColor(getThemePrimaryColor(getContext()));
        buttonThree.setBackgroundResource(R.drawable.button_rounded);
        buttonThree.setTextColor(getThemePrimaryColor(getContext()));
    }
}
