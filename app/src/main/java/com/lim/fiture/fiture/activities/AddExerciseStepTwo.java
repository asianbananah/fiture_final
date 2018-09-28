package com.lim.fiture.fiture.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.adapters.StepAdapter;
import com.lim.fiture.fiture.models.Exercise;

import java.util.ArrayList;
import java.util.Set;

public class AddExerciseStepTwo extends AppCompatActivity implements View.OnClickListener{

    private Exercise exercise;
    private RecyclerView stepsRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private StepAdapter stepAdapter;

    private ArrayList<String> steps;
    private Button nextBtn;

    private ArrayList<String> finalSteps;

    private String action = "";

    DatabaseReference databaseExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise_step_two);
        databaseExercise = FirebaseDatabase.getInstance().getReference("Exercises");
        findViews();
        getDataFromPreviousActivity();
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

    private void resumeValues() {
        stepAdapter = new StepAdapter(this, exercise.getSteps());
        stepsRecycler.setAdapter(stepAdapter);
    }

    private void findViews(){
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        getSupportActionBar().setTitle("Exercise Steps");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        stepsRecycler = findViewById(R.id.stepsRecycler);
        layoutManager = new LinearLayoutManager(this);
        stepsRecycler.setLayoutManager(layoutManager);

        steps = new ArrayList();
        steps.add("");
        stepAdapter = new StepAdapter(AddExerciseStepTwo.this,steps);
        stepsRecycler.setAdapter(stepAdapter);

        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(this);

        finalSteps = new ArrayList<>();
    }

   public void addStep(String step){
        steps.add(step);
        stepAdapter.notifyDataSetChanged();
    }

    public void removeStep(int position){
        steps.remove(position);
       stepAdapter.notifyDataSetChanged();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nextBtn:
                for(StepAdapter.ViewHolder viewHolder : stepAdapter.viewHolders){
                    if(!viewHolder.getStepTxt().equals(""))
                    finalSteps.add(viewHolder.getStepTxt());
                }

                exercise.setSteps(finalSteps);
                startActivity(new Intent(AddExerciseStepTwo.this,AddExerciseStepThree.class)
                        .putExtra("exercise", exercise)
                        .putExtra("action", action));
                finish();
                break;
        }
    }
}
