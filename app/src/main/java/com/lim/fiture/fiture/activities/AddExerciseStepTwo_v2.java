package com.lim.fiture.fiture.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.adapters.StepAdapter;
import com.lim.fiture.fiture.models.Exercise;

import java.util.ArrayList;

public class AddExerciseStepTwo_v2 extends AppCompatActivity {

    private FloatingActionButton fab;
    private EditText stepTxt;
    private Button nextBtn;
    private LinearLayout parentLinearLayout;
    private Exercise exercise;
    private String action = "";

    private ArrayList<EditText> editTexts = new ArrayList<>();
    private ArrayList<String> finalSteps = new ArrayList<>();
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise_step_two_v2);

        findViews();
        getDataFromPreviousActivity();
    }

    private void findViews(){
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        getSupportActionBar().setTitle("Exercise Steps");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = findViewById(R.id.addStep);
        stepTxt = findViewById(R.id.stepTxt);
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllSteps();
            }
        });
        parentLinearLayout = findViewById(R.id.parentLinearLayout);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addField("");
            }
        });
    }

    private void getDataFromPreviousActivity(){
        if (getIntent().getStringExtra("action") != null
                && getIntent().getStringExtra("action").equals("edit")) {
            exercise = (Exercise) getIntent().getSerializableExtra("exercise");
            action = "edit";
            resumeValues();
        } else {
            exercise = (Exercise) getIntent().getSerializableExtra("exercise");
        }
    }

    public void addField(String step) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.step_layout_item, null);
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());

        EditText editText = rowView.findViewById(R.id.stepTxt);
        editText.setContentDescription(String.valueOf(count));
        editText.requestFocus();
        editText.setText(step);
        editTexts.add(editText);

        FloatingActionButton fab = rowView.findViewById(R.id.addStep);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteField(editText, rowView);
            }
        });

        count++;

        resetContentDescriptions();
        listEt();
    }

    private void listEt(){
        for(int i = 0 ; i<editTexts.size(); i++){
            Log.d("moymoy", "contentDesc: " + editTexts.get(i).getContentDescription());
        }
    }

    private void deleteField(EditText editText, View view) {
        editTexts.remove(Integer.parseInt(editText.getContentDescription().toString()));
        parentLinearLayout.removeView(view);

        resetContentDescriptions();
        listEt();
    }

    private void resetContentDescriptions(){
        for(int i = 0; i < editTexts.size(); i++){
            editTexts.get(i).setContentDescription(String.valueOf(i));
        }
    }

    private void getAllSteps(){
        finalSteps = new ArrayList<>();
        if(!stepTxt.getText().equals(""))
            finalSteps.add(stepTxt.getText().toString());

        for(int i = 0 ; i<editTexts.size(); i++){
            if(!editTexts.get(i).getText().toString().equals(""))
                finalSteps.add(editTexts.get(i).getText().toString());
        }

        exercise.setSteps(finalSteps);
        Log.d("moymoyy", exercise.getSteps().toString());
        startActivity(new Intent(AddExerciseStepTwo_v2.this,AddExerciseStepThree.class)
                .putExtra("exercise", exercise)
                .putExtra("action", action));
        finish();
    }

    private void resumeValues() {
        for(int i = 0; i < exercise.getSteps().size(); i++){
            if(i == 0)
                stepTxt.setText(exercise.getSteps().get(i));
            else
                addField(exercise.getSteps().get(i));
        }
    }


}
