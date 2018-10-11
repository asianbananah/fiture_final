package com.lim.fiture.fiture.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.adapters.ProgramDetailsImageAdapter;
import com.lim.fiture.fiture.adapters.ProgramExercisesAdapter;
import com.lim.fiture.fiture.adapters.UserProgramsAdapter;
import com.lim.fiture.fiture.models.Exercise;
import com.lim.fiture.fiture.models.Program;
import com.lim.fiture.fiture.models.ProgramExercise;

import java.util.ArrayList;

public class ProgramDetailsActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TextView programName, programDescription, weeksNumTxt, exercisesNumTxt, difficultyTxt;
    private RecyclerView programExercisesRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private ProgramExercisesAdapter adapter;

    private Program program;
    private DatabaseReference programExerciseReference, exercisesReference, programExerciseReferenceCount;

    private ArrayList<ProgramExercise> programExercises = new ArrayList<>();
    private ArrayList<Exercise> exercises = new ArrayList<>();
    private int programExerciseCount = 0;
    private int tempCount = 0;
    private boolean startGettingExercise = false;
    private Button startProgram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_details);

        findViews();
        getDataFromPreviousActivity();
        getProgramExerciseCount();
    }

    private void getDataFromPreviousActivity(){
        if(getIntent().getSerializableExtra("program") != null)
            program = (Program) getIntent().getSerializableExtra("program");
        if(getIntent().getStringExtra("action") != null
                && getIntent().getStringExtra("action").equals("myProgram")){
            startProgram = findViewById(R.id.startProgram);
            startProgram.setVisibility(View.VISIBLE);
            startProgram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ProgramDetailsActivity.this,StartProgramActivity.class)
                            .putExtra("program",program));
                }
            });
        }
    }

    private void findViews(){
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        getSupportActionBar().setTitle("Program Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        programName = findViewById(R.id.programName);
        programDescription = findViewById(R.id.programDescription);
        weeksNumTxt = findViewById(R.id.weeksNumTxt);
        exercisesNumTxt = findViewById(R.id.exercisesNumTxt);
        difficultyTxt = findViewById(R.id.difficultyTxt);
        programExercisesRecycler = findViewById(R.id.programExercisesRecycler);
        layoutManager = new LinearLayoutManager(this);
        programExercisesRecycler.setLayoutManager(layoutManager);
        mViewPager = findViewById(R.id.viewPageAndroid);

    }

    private void getProgramExerciseCount(){
        programExerciseReferenceCount = FirebaseDatabase.getInstance().getReference().child("ProgramExercise").child(program.getProgramsId());
        programExerciseReferenceCount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("getProgramExercises", "countzz: " + dataSnapshot.getChildrenCount() );
                programExerciseCount = (int) dataSnapshot.getChildrenCount();
                startGettingExercise = true;
                getProgramExercises();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getProgramExercises(){
        tempCount = 0;
        programExerciseReference = FirebaseDatabase.getInstance().getReference().child("ProgramExercise").child(program.getProgramsId());
        Log.d("programExerciseRef", programExerciseReference.toString());
        programExerciseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (startGettingExercise) {
                    tempCount++;
                    ProgramExercise programExercise = dataSnapshot.getValue(ProgramExercise.class);
                    programExercises.add(programExercise);
                    Log.d("getProgramExercises", "count: " + tempCount);
                    if (tempCount >= programExerciseCount) {
                        //TODO: this is the last iteration, set the program details here
                        Log.d("getProgramExercises", "last iteration");
                        setProgramDetails();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setProgramDetails(){
        ProgramDetailsImageAdapter adapterView = new ProgramDetailsImageAdapter(this, program.getProgramImages());
        mViewPager.setAdapter(adapterView);

        programName.setText(program.getProgramsName());
        programDescription.setText(program.getProgramDesc());
        weeksNumTxt.setText(program.getProgramWeeks() + " Week/s");
        exercisesNumTxt.setText(String.valueOf(programExerciseCount));
        difficultyTxt.setText(program.getProgramDifficulty());

        //TODO: after setting the program's initial details, we proceed to setting the program's exercises
        setProgramExercises();
    }

    private void setProgramExercises(){
        for(int i = 0; i < programExercises.size(); i++){
             exercisesReference = FirebaseDatabase.getInstance().getReference().child("Exercises").child(programExercises.get(i).getExerciseId());
             exercisesReference.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                    Exercise exercise = dataSnapshot.getValue(Exercise.class);
                    exercises.add(exercise);
                    adapter = new ProgramExercisesAdapter(ProgramDetailsActivity.this, exercises);
                    programExercisesRecycler.setAdapter(adapter);
                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {

                 }
             });
        }
    }
}
