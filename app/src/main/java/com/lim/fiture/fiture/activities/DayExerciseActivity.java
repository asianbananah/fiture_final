package com.lim.fiture.fiture.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.adapters.DayExerciseAdapter;
import com.lim.fiture.fiture.models.Program;
import com.lim.fiture.fiture.models.ProgramExercise;
import com.lim.fiture.fiture.models.ProgramTracker;
import com.lim.fiture.fiture.util.GlobalUser;

import java.util.ArrayList;

public class DayExerciseActivity extends AppCompatActivity {

    private TextView dayTxt;
    private RecyclerView dayExerciseRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private DayExerciseAdapter dayExerciseAdapter;
    private Button startBtn;

    private ArrayList<ProgramExercise> programExercises = new ArrayList<>();
    private ArrayList<ProgramTracker> programTrackers = new ArrayList<>();
    private DatabaseReference programTrackersRef;

    private Program program;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_exercise);

        getDataFromPreviousActivity();
        findViews();
    }

    private void getDataFromPreviousActivity(){
        programExercises = (ArrayList<ProgramExercise>) getIntent().getSerializableExtra("programExercises");
    }

    private void findViews(){
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        getSupportActionBar().setTitle("Monday");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dayTxt = findViewById(R.id.dayTxt);
        dayTxt.setText("Monday");
        dayExerciseRecycler = findViewById(R.id.dayExercisesRecycler);
        layoutManager = new LinearLayoutManager(this);
        dayExerciseRecycler.setLayoutManager(layoutManager);
        dayExerciseAdapter = new DayExerciseAdapter(this, programExercises);
        dayExerciseRecycler.setAdapter(dayExerciseAdapter);

        startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DayExerciseActivity.this, DoProgramActivity.class)
                        .putExtra("programExercises",programExercises)
                        .putExtra("exerciseNum",String.valueOf(0)));
            }
        });
    }

//    private void finishedExercise(){
//        programTrackers = new ArrayList<>();
//        programTrackersRef = FirebaseDatabase.getInstance().getReference().child("ProgramTracker").child(GlobalUser.getmUser().getiD()).child(program.getProgramsId());
//        programTrackersRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
////                ProgramTracker programTracker = dataSnapshot.getChildren();
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
}
