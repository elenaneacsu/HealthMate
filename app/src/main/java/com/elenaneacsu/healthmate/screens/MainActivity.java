package com.elenaneacsu.healthmate.screens;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.screens.logging.exercise.LogExerciseActivity;
import com.elenaneacsu.healthmate.screens.logging.food.LogFoodActivity;
import com.elenaneacsu.healthmate.screens.logging.sleep.LogSleepActivity;
import com.elenaneacsu.healthmate.screens.logging.sleep.SleepHistoryFragment;
import com.elenaneacsu.healthmate.screens.logging.water.LogWaterActivity;
import com.elenaneacsu.healthmate.screens.logging.water.WaterHistoryFragment;
import com.elenaneacsu.healthmate.screens.logging.weight_history.WeightHistoryFragment;
import com.elenaneacsu.healthmate.screens.login.LogInActivity;
import com.elenaneacsu.healthmate.screens.main.MainFragment;
import com.elenaneacsu.healthmate.screens.profile.ViewProfileFragment;
import com.elenaneacsu.healthmate.screens.recipe.SearchRecipeFragment;
import com.elenaneacsu.healthmate.screens.statistics.StatisticsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import static com.elenaneacsu.healthmate.utils.Constants.FRAGMENT;
import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int fragment = bundle.getInt(FRAGMENT);
            if(fragment == 0) {
                initFragment(new ViewProfileFragment());
            } else {
                initFragment(new MainFragment());
            }
        } else {
            initFragment(new MainFragment());
        }


        BoomMenuButton boomMenuButton = findViewById(R.id.bmb);
        TextInsideCircleButton.Builder breakfastBuilder = new TextInsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_breakfast)
                .imagePadding(new Rect(10, -10, 5, 20))
                .normalText("Breakfast")
                .listener(new OnBMClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent intent = new Intent(MainActivity.this, LogFoodActivity.class);
                        intent.putExtra("meal", "breakfast");
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }
                });
        boomMenuButton.addBuilder(breakfastBuilder);

        TextInsideCircleButton.Builder lunchBuilder = new TextInsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_lunch)
                .normalText("Lunch")
                .listener(new OnBMClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent intent = new Intent(MainActivity.this, LogFoodActivity.class);
                        intent.putExtra("meal", "lunch");
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }
                });
        boomMenuButton.addBuilder(lunchBuilder);

        TextInsideCircleButton.Builder dinnerBuilder = new TextInsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_dinner)
                .imagePadding(new Rect(10, -10, 5, 20))
                .normalText("Dinner")
                .listener(new OnBMClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent intent = new Intent(MainActivity.this, LogFoodActivity.class);
                        intent.putExtra("meal", "dinner");
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }
                });
        boomMenuButton.addBuilder(dinnerBuilder);

        TextInsideCircleButton.Builder snackBuilder = new TextInsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_snack)
                .imagePadding(new Rect(10, -10, 5, 20))
                .normalText("Snack")
                .listener(new OnBMClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent intent = new Intent(MainActivity.this, LogFoodActivity.class);
                        intent.putExtra("meal", "snack");
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }
                });
        boomMenuButton.addBuilder(snackBuilder);

        TextInsideCircleButton.Builder sleepBuilder = new TextInsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_sleep)
                .imagePadding(new Rect(10, -10, 5, 20))
                .normalText("Sleep")
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent intent = new Intent(MainActivity.this, LogSleepActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }
                });
        boomMenuButton.addBuilder(sleepBuilder);

        TextInsideCircleButton.Builder waterBuilder = new TextInsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_water)
                .imagePadding(new Rect(10, -10, 5, 20))
                .normalText("Water")
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent intent = new Intent(MainActivity.this, LogWaterActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }
                });
        boomMenuButton.addBuilder(waterBuilder);

        TextInsideCircleButton.Builder sportBuilder = new TextInsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_sport)
                .imagePadding(new Rect(10, -10, 5, 20))
                .normalText("Sport")
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent intent = new Intent(MainActivity.this, LogExerciseActivity.class);
                        startActivity(intent);
                    }
                });
        boomMenuButton.addBuilder(sportBuilder);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            initFragment(new MainFragment());
        } else if (id == R.id.nav_profile) {
            initFragment(new ViewProfileFragment());
        } else if (id == R.id.nav_recipes) {
            initFragment(new SearchRecipeFragment());
        } else if (id == R.id.nav_water) {
            initFragment(new WaterHistoryFragment());
        } else if (id == R.id.nav_sleep) {
            initFragment(new SleepHistoryFragment());
        } else if(id==R.id.nav_weight) {
            initFragment(new WeightHistoryFragment());
        }else if(id == R.id.nav_statistics) {
            initFragment(new StatisticsFragment());
        } else if(id == R.id.nav_deleteaccount) {
            deleteAccount();
        }else if(id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void deleteAccount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_my_account);
        builder.setMessage(com.elenaneacsu.healthmate.R.string.delete_account_confirmation);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteUserFromFirebase();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void deleteUserFromFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            finishActivity();
                            showToast(getApplicationContext(), "Your account has been successfully deleted");
                        } else {
                            showToast(getApplicationContext(), "An error occurred");
                        }
                    }
                });
    }

    private void finishActivity() {
        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        sp.edit().putBoolean("logged", false).apply();
        startActivity(new Intent(MainActivity.this, LogInActivity.class));
        finish();
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.logout));
        builder.setMessage(R.string.logout_message);
        builder.setPositiveButton(getString(R.string.logout_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
                sp.edit().putBoolean("logged", false).apply();
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
