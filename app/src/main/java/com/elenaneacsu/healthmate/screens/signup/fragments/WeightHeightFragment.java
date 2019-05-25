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
import android.widget.EditText;
import android.widget.ImageButton;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.screens.entities.User;
import com.elenaneacsu.healthmate.screens.signup.SignUpActivity;
import com.elenaneacsu.healthmate.utils.Constants;

import static com.elenaneacsu.healthmate.utils.Constants.USER;
import static com.elenaneacsu.healthmate.utils.GetThemeColors.getThemeAccentColor;
import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeightHeightFragment extends Fragment {

    private EditText mEditTextCurrentWeight;
    private EditText mEditTextGoalWeight;
    private EditText mEditTextHeight;
    private ImageButton mBtnNext;
    private ImageButton mBtnBack;
    private User user;
    public SignUpActivity signUpActivity;
    private static final String TAG = "mytest";

    public WeightHeightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_weight_height, container, false);

        mEditTextCurrentWeight = view.findViewById(R.id.edittext_weight);
        mEditTextGoalWeight = view.findViewById(R.id.edittext_desiredweight);
        mEditTextHeight = view.findViewById(R.id.edittext_height);
        mBtnNext = view.findViewById(R.id.btn_next);
        mBtnBack = view.findViewById(R.id.btn_back);
        signUpActivity = (SignUpActivity) getActivity();

        Bundle bundle = getArguments();
        if (bundle != null) {
            user = bundle.getParcelable(USER);
        }

        return view;
    }

    public static WeightHeightFragment newInstance(Bundle arguments) {
        WeightHeightFragment weightHeightFragment = new WeightHeightFragment();
        if (arguments != null) {
            weightHeightFragment.setArguments(arguments);
        }
        return weightHeightFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEditTextGoalWeight.getText()==null || mEditTextGoalWeight.getText().toString().isEmpty()
                        || mEditTextCurrentWeight.getText() == null || mEditTextCurrentWeight.getText().toString().isEmpty()
                        || mEditTextHeight.getText() == null || mEditTextHeight.getText().toString().isEmpty()) {
                    showToast(getContext(), getString(R.string.supply_values));
                } else {
                    user.setCurrentWeight(Float.parseFloat(mEditTextCurrentWeight.getText().toString()));
                    user.setDesiredWeight(Float.parseFloat(mEditTextGoalWeight.getText().toString()));
                    user.setHeight(Integer.parseInt(mEditTextHeight.getText().toString()));
                    signUpActivity.mView4.setBackgroundColor(getThemeAccentColor(getContext()));
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

    private void initNextFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.USER, user);
        FragmentManager fragmentManager = signUpActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, AccountDetailsFragment.newInstance(bundle));
        fragmentTransaction.commit();
    }

    private void initBackFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.USER, user);
        FragmentManager fragmentManager = signUpActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, GenderAgeFragment.newInstance(bundle));
        fragmentTransaction.commit();
    }
}
