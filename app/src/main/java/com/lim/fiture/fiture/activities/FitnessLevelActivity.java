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

import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.models.User;

public class FitnessLevelActivity extends AppCompatActivity implements View.OnClickListener {

    private Button beginnerBtn, intermediateBtn, expertBtn, nextBtn;
    public static String LEVEL_BEGINNER = "Beginner";
    public static String LEVEL_INTERMEDIATE = "Intermediate";
    public static String LEVEL_EXPERT = "Expert";
    private String fitnessLevel;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_level);

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

        beginnerBtn = findViewById(R.id.beginnerBtn);
        beginnerBtn.setOnClickListener(this);
        intermediateBtn = findViewById(R.id.intermediateBtn);
        intermediateBtn.setOnClickListener(this);
        expertBtn = findViewById(R.id.expertBtn);
        expertBtn.setOnClickListener(this);
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(this);

        //set beginner as the default selected button
        fitnessLevel = LEVEL_BEGINNER;
        beginnerBtn.getBackground().setColorFilter(Color.parseColor("#2980b9"), PorterDuff.Mode.MULTIPLY);
        beginnerBtn.setTextColor(Color.parseColor("#FFFFFF"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.beginnerBtn:
                resetButtons();
                fitnessLevel = LEVEL_BEGINNER;
                beginnerBtn.getBackground().setColorFilter(Color.parseColor("#2980b9"), PorterDuff.Mode.MULTIPLY);
                beginnerBtn.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.intermediateBtn:
                resetButtons();
                fitnessLevel = LEVEL_INTERMEDIATE;
                intermediateBtn.getBackground().setColorFilter(Color.parseColor("#2980b9"), PorterDuff.Mode.MULTIPLY);
                intermediateBtn.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.expertBtn:
                resetButtons();
                fitnessLevel = LEVEL_EXPERT;
                expertBtn.getBackground().setColorFilter(Color.parseColor("#2980b9"), PorterDuff.Mode.MULTIPLY);
                expertBtn.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.nextBtn:
                mUser.setFitnessLevel(fitnessLevel);
                startActivity(new Intent(FitnessLevelActivity.this,FitnessGoalActivity.class).putExtra("user",mUser));
                finish();
                break;
        }
    }

    private void resetButtons(){
        beginnerBtn.getBackground().clearColorFilter();
        beginnerBtn.setTextColor(getResources().getColor(android.R.color.black));
        intermediateBtn.getBackground().clearColorFilter();
        intermediateBtn.setTextColor(getResources().getColor(android.R.color.black));
        expertBtn.getBackground().clearColorFilter();
        expertBtn.setTextColor(getResources().getColor(android.R.color.black));
    }


}
