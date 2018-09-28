package com.lim.fiture.fiture.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.models.User;

public class FitnessGoalActivity extends AppCompatActivity implements View.OnClickListener {

    private User mUser;
    public Button loseBtn, maintainBtn, gainBtn, nextBtn;
    public static String GOAL_LOSE = "Lose";
    public static String GOAL_MAINTAIN = "Maintain";
    public static String GOAL_GAIN = "Gain";
    private String fitnessGoal;
    private DatabaseReference databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_goal);

        databaseUser = FirebaseDatabase.getInstance().getReference("Users");
        getDataFromPreviousActivity();
        findViews();
    }

    private void getDataFromPreviousActivity(){
        mUser = (User) getIntent().getSerializableExtra("user");
    }

    public void findViews(){
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        bar.setTitle("Fitness Level");
        bar.setDisplayHomeAsUpEnabled(true);

        loseBtn = findViewById(R.id.loseBtn);
        loseBtn.setOnClickListener(this);
        maintainBtn = findViewById(R.id.maintainBtn);
        maintainBtn.setOnClickListener(this);
        gainBtn = findViewById(R.id.gainBtn);
        gainBtn.setOnClickListener(this);
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(this);

        //set lose as the default selected button
        fitnessGoal = GOAL_LOSE;
        loseBtn.getBackground().setColorFilter(Color.parseColor("#2980b9"), PorterDuff.Mode.MULTIPLY);
        loseBtn.setTextColor(Color.parseColor("#FFFFFF"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loseBtn:
                resetButtons();
                fitnessGoal = GOAL_LOSE;
                loseBtn.getBackground().setColorFilter(Color.parseColor("#2980b9"), PorterDuff.Mode.MULTIPLY);
                loseBtn.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.maintainBtn:
                resetButtons();
                fitnessGoal = GOAL_MAINTAIN;
                maintainBtn.getBackground().setColorFilter(Color.parseColor("#2980b9"), PorterDuff.Mode.MULTIPLY);
                maintainBtn.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.gainBtn:
                resetButtons();
                fitnessGoal = GOAL_GAIN;
                gainBtn.getBackground().setColorFilter(Color.parseColor("#2980b9"), PorterDuff.Mode.MULTIPLY);
                gainBtn.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.nextBtn:
                mUser.setFitnessGoal(fitnessGoal);
                databaseUser.child(mUser.getiD()).setValue(mUser);
                startActivity(new Intent(FitnessGoalActivity.this,UserMainActivity.class).putExtra("userId",mUser.getiD()));
                finish();
                break;
        }
    }

    private void resetButtons(){
        loseBtn.getBackground().clearColorFilter();
        loseBtn.setTextColor(getResources().getColor(android.R.color.black));
        maintainBtn.getBackground().clearColorFilter();
        maintainBtn.setTextColor(getResources().getColor(android.R.color.black));
        gainBtn.getBackground().clearColorFilter();
        gainBtn.setTextColor(getResources().getColor(android.R.color.black));
    }
}
