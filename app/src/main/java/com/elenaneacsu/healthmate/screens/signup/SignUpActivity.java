package com.elenaneacsu.healthmate.screens.signup;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.screens.adapters.SignUpTabAdapter;
import com.elenaneacsu.healthmate.screens.entities.User;
import com.elenaneacsu.healthmate.screens.signup.fragments.AccountDetailsFragment;
import com.elenaneacsu.healthmate.screens.signup.fragments.ActivityLevelFragment;
import com.elenaneacsu.healthmate.screens.signup.fragments.GenderBirthFragment;
import com.elenaneacsu.healthmate.screens.signup.fragments.GoalFragment;
import com.elenaneacsu.healthmate.screens.signup.fragments.WeightHeightFragment;
import com.elenaneacsu.healthmate.screens.utils.Constants;

public class SignUpActivity extends AppCompatActivity {

    private SignUpTabAdapter mAdapter;
    private TabLayout mTabLayout;
    public CustomViewPager mCustomViewPager;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();
        initAdapter();

        mCustomViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mCustomViewPager);
        mCustomViewPager.setEnableSwipe(false);
    }

    private void initView() {
        mTabLayout = findViewById(R.id.tablayout_signup);
        mCustomViewPager = findViewById(R.id.customviewpager_signup);
        user = new User();
    }

    private void initAdapter() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.USER, user);
        mAdapter = new SignUpTabAdapter(getSupportFragmentManager(), bundle);
        mAdapter.addFragment(GoalFragment.newInstance(bundle), "1");
        mAdapter.addFragment(ActivityLevelFragment.newInstance(bundle), "2");
        mAdapter.addFragment(GenderBirthFragment.newInstance(bundle), "3");
        mAdapter.addFragment(WeightHeightFragment.newInstance(bundle), "4");
        mAdapter.addFragment(new AccountDetailsFragment(), "5");
    }
}
