package com.lim.fiture.fiture.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.models.Exercise;

/**
 * Created by User on 30/08/2018.
 */

public class AddExerciseStepOne extends AppCompatActivity {

    private Exercise exercise;
    private EditText nameTxt;
    private Spinner mainMuscleGroup, otherMuscleGroup, type, equipment, difficulty;
    private Button nextBtn;
    private String action = "";

    DatabaseReference databaseExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercises);
        databaseExercise = FirebaseDatabase.getInstance().getReference("Exercises");

        findviews();
        if (getIntent().getStringExtra("action") != null
                && getIntent().getStringExtra("action").equals("edit")) {
            exercise = (Exercise) getIntent().getSerializableExtra("exercise");
            action = "edit";
            resumeValues();
        } else {
            exercise = new Exercise();
        }

    }

    private void resumeValues() {
        nameTxt.setText(exercise.getExerciseName());
        setSpinnerValues(R.array.muscle_groups, mainMuscleGroup, exercise.getMainMuscleGroup());
        setSpinnerValues(R.array.muscle_groups, otherMuscleGroup, exercise.getOtherMuscleGroup());
        setSpinnerValues(R.array.type, type, exercise.getType());
        setSpinnerValues(R.array.equipment, equipment, exercise.getEquipment());
        setSpinnerValues(R.array.Difficulty, difficulty, exercise.getDifficulty());
    }

    private void setSpinnerValues(int resource, Spinner spinner, String compareValue) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, resource, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (compareValue != null) {
            int spinnerPosition = adapter.getPosition(compareValue);
            spinner.setSelection(spinnerPosition);
        }
    }

    private void findviews() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        getSupportActionBar().setTitle("Exercise Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameTxt = findViewById(R.id.exerciseName);
        mainMuscleGroup = findViewById(R.id.mainMuscleGroup);
        otherMuscleGroup = findViewById(R.id.otherMuscleGroup);
        type = findViewById(R.id.type);
        equipment = findViewById(R.id.equipment);
        difficulty = findViewById(R.id.difficulty);
        nextBtn = findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(view -> {
            exercise.setExerciseName(nameTxt.getText().toString());
            exercise.setMainMuscleGroup(mainMuscleGroup.getSelectedItem().toString());
            exercise.setOtherMuscleGroup(otherMuscleGroup.getSelectedItem().toString());
            exercise.setType(type.getSelectedItem().toString());
            exercise.setEquipment(equipment.getSelectedItem().toString());
            exercise.setDifficulty(difficulty.getSelectedItem().toString());

            startActivity(new Intent(AddExerciseStepOne.this, AddExerciseStepTwo_v2.class)
                    .putExtra("exercise", exercise)
                    .putExtra("action", action));
            finish();
        });
    }
}
