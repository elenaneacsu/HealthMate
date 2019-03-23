package com.elenaneacsu.healthmate.screens.signup.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.screens.entities.User;
import com.elenaneacsu.healthmate.screens.signup.SignUpActivity;

import static com.elenaneacsu.healthmate.utils.Constants.USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeightHeightFragment extends Fragment {

    private EditText mEditTextCurrentWeight;
    private EditText mEditTextGoalWeight;
    private EditText mEditTextHeight;
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
        signUpActivity = (SignUpActivity) getActivity();

        signUpActivity.mCustomViewPager.setEnableSwipe(false);

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
        if(mEditTextGoalWeight.getText()==null || mEditTextGoalWeight.getText().toString().isEmpty()
                || mEditTextCurrentWeight.getText() == null || mEditTextCurrentWeight.getText().toString().isEmpty()
                || mEditTextHeight.getText() == null || mEditTextHeight.getText().toString().isEmpty()) {
            signUpActivity.mCustomViewPager.setEnableSwipe(false);
        } else {
            user.setCurrentWeight(Float.parseFloat(mEditTextCurrentWeight.getText().toString()));
            user.setDesiredWeight(Float.parseFloat(mEditTextGoalWeight.getText().toString()));
            user.setHeight(Integer.parseInt(mEditTextHeight.getText().toString()));
            signUpActivity.mCustomViewPager.setEnableSwipe(true);
        }
    }
}
