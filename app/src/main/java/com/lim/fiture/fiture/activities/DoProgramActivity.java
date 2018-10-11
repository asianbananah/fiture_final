package com.lim.fiture.fiture.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.adapters.InstructionsAdapter;
import com.lim.fiture.fiture.adapters.ProgramDetailsImageAdapter;
import com.lim.fiture.fiture.fragments.RestDialog;
import com.lim.fiture.fiture.models.Exercise;
import com.lim.fiture.fiture.models.ProgramExercise;

import java.util.ArrayList;

public class DoProgramActivity extends AppCompatActivity {

    private TextView currentSetTxt, totalSetTxt,timerTxt, repsTxt;
    private Button startBtn, stopBtn, nextBtn;
    private RecyclerView instructionsRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private InstructionsAdapter adapter;
    private ViewPager imagesViewPager;

    private ArrayList<ProgramExercise> programExercises;
    private int exerciseNum = 0;

    private DatabaseReference exerciseReference;
    private Exercise exercise;

    public int currentSet = 1;
    private long startTime = 0;
    private String TAG = "DoProgramActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_program);
        exerciseReference = FirebaseDatabase.getInstance().getReference("Exercises");

        getDataFromPreviousActivity();
        findViews();
        getExerciseDetails();
    }

    public void getDataFromPreviousActivity(){
        programExercises = (ArrayList<ProgramExercise>) getIntent().getSerializableExtra("programExercises");
        String tempNum = getIntent().getStringExtra("exerciseNum");
        exerciseNum = Integer.parseInt(tempNum);
        Log.d(TAG,"exerciseNum: " + exerciseNum);
    }

    private void getExerciseDetails(){
        exerciseReference.child(programExercises.get(exerciseNum).getExerciseId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                exercise = dataSnapshot.getValue(Exercise.class);
                setExerciseDetails();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setExerciseDetails(){
        ProgramDetailsImageAdapter adapterView = new ProgramDetailsImageAdapter(this, exercise.getExerciseImages());
        imagesViewPager.setAdapter(adapterView);

        currentSetTxt.setText(String.valueOf(currentSet));
        totalSetTxt.setText(String.valueOf(programExercises.get(exerciseNum).getSets()));
        repsTxt.setText(String.valueOf(programExercises.get(exerciseNum).getReps()));

        adapter = new InstructionsAdapter(this, exercise.getSteps());
        instructionsRecycler.setAdapter(adapter);
    }


    public void findViews(){
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        getSupportActionBar().setTitle(programExercises.get(exerciseNum).getExerciseName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentSetTxt = findViewById(R.id.currentSetTxt);
        totalSetTxt = findViewById(R.id.totalSetTxt);
        timerTxt = findViewById(R.id.timerTxt);
        repsTxt = findViewById(R.id.repsTxt);
        startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
                Toast.makeText(DoProgramActivity.this, "Started", Toast.LENGTH_SHORT).show();
            }
        });
        stopBtn = findViewById(R.id.stopBtn);
        stopBtn.setEnabled(false);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
                FragmentManager fm = getFragmentManager();
                RestDialog restDialog = RestDialog.newInstance(programExercises.get(exerciseNum).getRest());
                restDialog.show(fm, "fragment_rest_dialog");
            }
        });
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setEnabled(false);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNextExercise();
            }
        });
        instructionsRecycler = findViewById(R.id.instructionsRecycler);
        layoutManager = new LinearLayoutManager(this);
        instructionsRecycler.setLayoutManager(layoutManager);
        imagesViewPager = findViewById(R.id.viewPageAndroid);
    }

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTxt.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    public void startTimer(){
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
        startBtn.setEnabled(false);
        stopBtn.setEnabled(true);
    }
    public void stopTimer(){
        timerHandler.removeCallbacks(timerRunnable);
        timerTxt.setText("0:00");
        startBtn.setEnabled(true);
        stopBtn.setEnabled(false);
    }

    public void updateCurrentSet(){
        currentSet++;

        if(currentSet > programExercises.get(exerciseNum).getSets()){
            startBtn.setEnabled(false);
            stopBtn.setEnabled(false);
            nextBtn.setEnabled(true);
        }else{
            currentSetTxt.setText(String.valueOf(currentSet));
        }
    }

    private void goToNextExercise(){

        exerciseNum++;

        if(exerciseNum == programExercises.size()){
            finish();
        }else {
            currentSet = 1;
            startTime = 0;
            findViews();
            startBtn.setEnabled(true);
            getExerciseDetails();
        }
    }
}
