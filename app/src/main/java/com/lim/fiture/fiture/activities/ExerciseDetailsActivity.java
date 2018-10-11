package com.lim.fiture.fiture.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.adapters.ExerciseDetailsImageAdapter;
import com.lim.fiture.fiture.adapters.ProgramDetailsImageAdapter;
import com.lim.fiture.fiture.adapters.ProgramExercisesAdapter;
import com.lim.fiture.fiture.models.Exercise;

import java.util.ArrayList;

public class ExerciseDetailsActivity extends AppCompatActivity {

    private ViewPager mviewPager;
    private TextView exerciseName, typeTxt, muscleGroupTxt, difficultyTxt;
    private Button startExercise;
    private RecyclerView.LayoutManager layoutManager;
    private ProgramExercisesAdapter programExercisesAdapter;

    private Exercise exercise;
    private DatabaseReference exerciseReference;

    private ArrayList<Exercise> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);

        findViews();
        getDataFromPreviousActivity();
        setProgramDetails();

    }

    private void getDataFromPreviousActivity(){
        if (getIntent().getSerializableExtra("exercises")!= null)
            exercise = (Exercise) getIntent().getSerializableExtra("exercises");

    }

    private void findViews(){
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        getSupportActionBar().setTitle("Exercise Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startExercise = findViewById(R.id.startBtn);
        exerciseName = findViewById(R.id.exerciseName);
        typeTxt = findViewById(R.id.typeTxt);
        muscleGroupTxt = findViewById(R.id.muscleGroupTxt);
        difficultyTxt = findViewById(R.id.difficultyTxt);

        layoutManager = new LinearLayoutManager(this);

        mviewPager = findViewById(R.id.viewPageAndroid);

    }

    private void setProgramDetails(){
        ExerciseDetailsImageAdapter adapterView = new ExerciseDetailsImageAdapter(this, exercise.getExerciseImages());
        mviewPager.setAdapter(adapterView);

        exerciseName.setText(exercise.getExerciseName());
        typeTxt.setText(exercise.getType());
        muscleGroupTxt.setText(exercise.getMainMuscleGroup());
        difficultyTxt.setText(exercise.getDifficulty());


    }
}
