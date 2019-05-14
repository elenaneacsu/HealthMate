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
public class GoalFragment extends Fragment {

    private Button btnMaintain;
    private Button btnLose;
    private Button btnGain;
    private ImageButton btnNext;
    private User user;
    public SignUpActivity signUpActivity;

    private boolean enableNext = false;

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
        btnNext = view.findViewById(R.id.btn_next);
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
                setButtonsDesign(btnMaintain, btnLose, btnGain);
                user.setGoal(getString(R.string.maintain));
                enableNext=true;
            }
        });

        btnLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsDesign(btnLose, btnGain, btnMaintain);
                user.setGoal(getString(R.string.lose));
                enableNext=true;
            }
        });

        btnGain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsDesign(btnGain, btnLose, btnMaintain);
                user.setGoal(getString(R.string.gain));
                enableNext=true;
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!enableNext) {
                    showToast(getContext(), getString(R.string.choose_option));
                } else {
                    signUpActivity.mView1.setBackgroundColor(getThemeAccentColor(getContext()));
                    initFragment();
                }
            }
        });

    }

    private void setButtonsDesign(Button buttonOne, Button buttonTwo, Button buttonThree) {
        buttonOne.setBackgroundResource(R.drawable.button_rounded_color_accent);
        buttonOne.setTextColor(Color.WHITE);
        buttonTwo.setBackgroundResource(R.drawable.button_rounded_corners);
        buttonTwo.setTextColor(getThemePrimaryColor(getContext()));
        buttonThree.setBackgroundResource(R.drawable.button_rounded_corners);
        buttonThree.setTextColor(getThemePrimaryColor(getContext()));
    }

    private void initFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.USER, user);
        FragmentManager fragmentManager = signUpActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, ActivityLevelFragment.newInstance(bundle));
        fragmentTransaction.commit();
    }
}
