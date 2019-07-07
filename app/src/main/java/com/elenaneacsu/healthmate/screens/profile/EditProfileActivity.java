package com.elenaneacsu.healthmate.screens.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.screens.MainActivity;
import com.elenaneacsu.healthmate.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

import static com.elenaneacsu.healthmate.utils.Constants.FRAGMENT;
import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextViewName;
    private TextView mTextViewAge;
    private TextView mTextViewGender;
    private TextView mTextViewGoal;
    private TextView mTextViewActivityLevel;
    private TextView mTextViewCurrentWeight;
    private TextView mTextViewGoalWeight;
    private TextView mTextViewHeight;
    private CardView mCardViewName;
    private CardView mCardViewAge;
    private CardView mCardViewGender;
    private CardView mCardViewGoal;
    private CardView mCardViewActivityLevel;
    private CardView mCardViewCurrentWeight;
    private CardView mCardViewGoalWeight;
    private CardView mCardViewHeight;
    private Button mBtnSave;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFirebaseAuth;

    private String name;
    private int age;
    private String goal;
    private String gender;
    private String activityLevel;
    private float currentWeight;
    private float goalWeight;
    private int height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        initView();

        mCardViewName.setOnClickListener(this);
        mCardViewAge.setOnClickListener(this);
        mCardViewGender.setOnClickListener(this);
        mCardViewGoal.setOnClickListener(this);
        mCardViewActivityLevel.setOnClickListener(this);
        mCardViewCurrentWeight.setOnClickListener(this);
        mCardViewGoalWeight.setOnClickListener(this);
        mCardViewHeight.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);

        final FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser != null) {
            final DocumentReference documentReference = mFirestore.collection("users").document(currentUser.getUid());
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        setFields(user);
                        instantiateAttributes(user);
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardview_name:
                getNameFromUser();
                break;
            case R.id.cardview_age:
                getAgeFromUser();
                break;
            case R.id.cardview_gender:
                getGenderFromUser();
                break;
            case R.id.cardview_goal:
                getGoalFromUser();
                break;
            case R.id.cardview_activitylevel:
                getActivityLevelFromUser();
                break;
            case R.id.cardview_currentweight:
                getCurrentWeightFromUser();
                break;
            case R.id.cardview_goalweight:
                getGoalWeightFromUser();
                break;
            case R.id.cardview_height:
                getHeightFromUser();
                break;
            case R.id.btn_save:
                saveToFirestore();
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                intent.putExtra(FRAGMENT, 0);
                startActivity(intent);
                finish();
        }
    }

    private void initView() {
        mTextViewName = findViewById(R.id.textview_name);
        mTextViewAge = findViewById(R.id.textview_age);
        mTextViewGender = findViewById(R.id.textview_gender);
        mTextViewGoal = findViewById(R.id.textview_goal);
        mTextViewActivityLevel = findViewById(R.id.textview_activitylevel);
        mTextViewCurrentWeight = findViewById(R.id.textview_currentweight);
        mTextViewGoalWeight = findViewById(R.id.textview_goalweight);
        mTextViewHeight = findViewById(R.id.textview_height);
        mCardViewName = findViewById(R.id.cardview_name);
        mCardViewAge = findViewById(R.id.cardview_age);
        mCardViewGender = findViewById(R.id.cardview_gender);
        mCardViewGoal = findViewById(R.id.cardview_goal);
        mCardViewActivityLevel = findViewById(R.id.cardview_activitylevel);
        mCardViewCurrentWeight = findViewById(R.id.cardview_currentweight);
        mCardViewGoalWeight = findViewById(R.id.cardview_goalweight);
        mCardViewHeight = findViewById(R.id.cardview_height);
        mBtnSave = findViewById(R.id.btn_save);
    }

    private void setFields(User user) {
        mTextViewName.setText(user.getName());
        mTextViewActivityLevel.setText(user.getActivityLevel());
        mTextViewGoal.setText(user.getGoal());
        mTextViewGender.setText(user.getGender());
        mTextViewAge.setText(String.format("%d", user.getAge()));
        mTextViewHeight.setText(String.format("%d", user.getHeight()));
        mTextViewGoalWeight.setText(String.format("%s", user.getDesiredWeight()));
        mTextViewCurrentWeight.setText(String.format("%s", user.getCurrentWeight()));
    }

    private void instantiateAttributes(User user) {
        name = user.getName();
        age = user.getAge();
        gender = user.getGender();
        goal = user.getGoal();
        activityLevel = user.getActivityLevel();
        currentWeight = user.getCurrentWeight();
        goalWeight = user.getDesiredWeight();
        height = user.getHeight();
    }

    private void getNameFromUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(com.elenaneacsu.healthmate.R.string.what_is_your_name);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(com.elenaneacsu.healthmate.R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = input.getText().toString();
                mTextViewName.setText(name);
            }
        });

        builder.setNegativeButton(com.elenaneacsu.healthmate.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void getAgeFromUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(com.elenaneacsu.healthmate.R.string.how_old_are_you);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton(com.elenaneacsu.healthmate.R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                age = Integer.valueOf(input.getText().toString());
                mTextViewAge.setText(String.format("%d", age));
            }
        });

        builder.setNegativeButton(com.elenaneacsu.healthmate.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void getGenderFromUser() {
        final CharSequence[] choices = {"Male", "Female"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(com.elenaneacsu.healthmate.R.string.gender_you_identify_with);
        builder.setSingleChoiceItems(choices, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        gender = "Male";
                        showToast(getApplicationContext(), "gender " + gender);
                        break;
                    case 1:
                        gender = "Female";
                        break;
                }
            }
        });

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTextViewGender.setText(gender);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void getGoalFromUser() {
        final CharSequence[] choices = {"Lose weight", "Maintain weight", "Gain weight"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(com.elenaneacsu.healthmate.R.string.what_is_your_goal);
        builder.setSingleChoiceItems(choices, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        goal = "lose";
                        break;
                    case 1:
                        goal = "maintain";
                        break;
                    case 2:
                        goal = "gain";
                        break;
                }
            }
        });

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTextViewGoal.setText(goal);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void getActivityLevelFromUser() {
        final CharSequence[] choices = {"Not Active", "Lightly Active", "Active", "Very Active"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(com.elenaneacsu.healthmate.R.string.what_is_your_activity_level);
        builder.setSingleChoiceItems(choices, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        activityLevel = "not active";
                        break;
                    case 1:
                        activityLevel = "lightly active";
                        break;
                    case 2:
                        activityLevel = "active";
                        break;
                    case 3:
                        activityLevel = "very active";
                        break;
                }
            }
        });

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTextViewActivityLevel.setText(activityLevel);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void getCurrentWeightFromUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(com.elenaneacsu.healthmate.R.string.how_much_do_you_weigh_right_now);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentWeight = Float.parseFloat(input.getText().toString());
                mTextViewCurrentWeight.setText(String.format("%s", currentWeight));
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void getGoalWeightFromUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(com.elenaneacsu.healthmate.R.string.how_much_do_you_weigh_right_now);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goalWeight = Float.parseFloat(input.getText().toString());
                mTextViewGoalWeight.setText(String.format("%s", goalWeight));
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void getHeightFromUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(com.elenaneacsu.healthmate.R.string.how_much_do_you_weigh_right_now);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                height = Integer.parseInt(input.getText().toString());
                mTextViewHeight.setText(String.format("%d", height));
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void saveToFirestore() {
        final FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser != null) {
            final DocumentReference documentReference = mFirestore.collection("users").document(currentUser.getUid());
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    User user = documentSnapshot.toObject(User.class);
                    String oldName, oldGoal, oldActivityLevel, oldGender;
                    int oldHeight, oldAge;
                    float oldGoalWeight, oldCurrentWeight;
                    if(user!=null) {
                        oldName = user.getName();
                        oldGoal = user.getGoal();
                        oldActivityLevel = user.getActivityLevel();
                        oldGender = user.getGender();
                        oldHeight = user.getHeight();
                        oldAge = user.getAge();
                        oldGoalWeight = user.getDesiredWeight();
                        oldCurrentWeight = user.getCurrentWeight();
                        if (!oldName.equals(name)) {
                            documentReference.update("name", name);
                        }
                        if (!oldGoal.equals(goal)) {
                            documentReference.update("goal", goal);
                        }
                        if (!oldActivityLevel.equals(activityLevel)) {
                            documentReference.update("activityLevel", activityLevel);
                        }
                        if (!oldGender.equals(gender)) {
                            documentReference.update("gender", gender);
                        }
                        if(oldHeight!=height) {
                            documentReference.update("height", height);
                        }
                        if(oldAge!=age) {
                            documentReference.update("age", age);
                        }
                        if(oldCurrentWeight!=currentWeight) {
                            documentReference.update("currentWeight", currentWeight);
                        }
                        if(oldGoalWeight!=goalWeight) {
                            documentReference.update("desiredWeight", goalWeight);
                        }
                    }
                }
            });
        }
    }
}
