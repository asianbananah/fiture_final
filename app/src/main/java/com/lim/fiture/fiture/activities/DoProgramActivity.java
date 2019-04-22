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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.adapters.InstructionsAdapter;
import com.lim.fiture.fiture.adapters.ProgramDetailsImageAdapter;
import com.lim.fiture.fiture.fragments.InputWeightDialog;
import com.lim.fiture.fiture.fragments.NoMovementDialog;
import com.lim.fiture.fiture.fragments.RestDialog;
import com.lim.fiture.fiture.models.Exercise;
import com.lim.fiture.fiture.models.Program;
import com.lim.fiture.fiture.models.ProgramExercise;
import com.lim.fiture.fiture.models.ProgramTracker;
import com.lim.fiture.fiture.util.GlobalUser;

import java.util.ArrayList;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

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

    //new additions
    private int currentWeek;
    private int count;
    private ArrayList<ProgramTracker> programTrackers;
    private int programTrackersCount = 0;
    boolean falseDetected = false;
    private KonfettiView viewKonfetti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_program);
        exerciseReference = FirebaseDatabase.getInstance().getReference("Exercises");

        getDataFromPreviousActivity();
        findViews();
        getExerciseDetails();
        getAllProgramTrackersForCurrentWeekCount();
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
                finishProgramExercise();
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
            nextBtn.setText("FINISH");
            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkIfProgramComplete();
                }
            });
//            checkIfProgramComplete();
//            finish();
        }else {
            currentSet = 1;
            startTime = 0;
            findViews();
            startBtn.setEnabled(true);
            getExerciseDetails();
        }
    }

    private void finishProgramExercise(){
        try {
            DatabaseReference programTrackerRef = FirebaseDatabase.getInstance().getReference("ProgramTracker")
                    .child(GlobalUser.getmUser().getiD())
                    .child(programExercises.get(exerciseNum).getProgramId())
                    .child(programExercises.get(exerciseNum).getProgramExerciseId());
            programTrackerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ProgramTracker programTracker = dataSnapshot.getValue(ProgramTracker.class);
                    programTracker.setProgramExerciseFinished(true);
                    programTrackerRef.setValue(programTracker);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getAllProgramTrackersForCurrentWeekCount(){
        programTrackersCount = 0;
        programTrackers = new ArrayList<>();
        currentWeek = programExercises.get(0).getWeek();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ProgramTracker")
                .child(GlobalUser.getmUser().getiD())
                .child(programExercises.get(0).getProgramId());
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ProgramTracker programTracker = dataSnapshot.getValue(ProgramTracker.class);
                if(programTracker.getProgramExerciseWeek() == currentWeek){
                    programTrackersCount++;
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

    private void checkIfProgramComplete() {
        currentWeek = programExercises.get(0).getWeek();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ProgramTracker")
                .child(GlobalUser.getmUser().getiD())
                .child(programExercises.get(0).getProgramId());
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ProgramTracker programTracker = dataSnapshot.getValue(ProgramTracker.class);
                if (programTracker.getProgramExerciseWeek() == currentWeek) {
                    programTrackers.add(programTracker);
                    if (programTrackers.size() == programTrackersCount) {
                        for (int i = 0; i < programTrackers.size(); i++) {
                            if (!programTrackers.get(i).isProgramExerciseFinished()) {
                                Log.d("checkingggg", "false detected");
                                falseDetected = true;
                            }
                        }

                        if (falseDetected) {
                            finish();
                        } else {
                            //meaning this week for the current program is done
                            viewKonfetti = findViewById(R.id.viewKonfetti);
                            viewKonfetti.build()
                                    .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                                    .setDirection(0.0, 359.0)
                                    .setDirection(0.0, 359.0)
                                    .setSpeed(1f, 5f)
                                    .setFadeOutEnabled(true)
                                    .setTimeToLive(2000L)
                                    .addShapes(Shape.RECT, Shape.CIRCLE)
                                    .addSizes(new Size(12, 5))
                                    .setPosition(-50f, viewKonfetti.getWidth() + 50f, -50f, -50f)
                                    .streamFor(300, 5000L);

                            FragmentManager fm = getFragmentManager();
                            InputWeightDialog weightDialog = new InputWeightDialog();
                            weightDialog.show(fm, "fragment_input_weight");
                        }
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
//    private void checkIfProgramComplete(){
//        currentWeek = programExercises.get(0).getWeek();
//        Log.d("checkingggg", "ni sud");
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ProgramTracker")
//                .child(GlobalUser.getmUser().getiD())
//                .child(programExercises.get(0).getProgramId());
//        Log.d("checkingggg", databaseReference.toString());
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                count = 0;
//                programTrackers = new ArrayList<>();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    ProgramTracker programTracker = snapshot.getValue(ProgramTracker.class);
//                    count++;
//
//                    if(programTracker.getProgramExerciseWeek() == currentWeek) {
//                        Log.d("checkingggg", programTracker.getProgramExerciseId());
//                        programTrackers.add(programTracker);
//
//                        if(count >= programTrackersCount){
//                            for(int i = 0 ; i< programTrackers.size(); i++){
//                                if (!programTrackers.get(i).isProgramExerciseFinished()){
//                                    Log.d("checkingggg", "false detected");
//                                    falseDetected = true;
//                                }
//                            }
//
//                            if (falseDetected){
//                                Toast.makeText(DoProgramActivity.this, "incomplete", Toast.LENGTH_SHORT).show();
//                                finish();
//                            }else{
//                                Toast.makeText(DoProgramActivity.this, "show week finished here, and weight dialog", Toast.LENGTH_SHORT).show();
//                            }
//                        }
////                        if (!programTracker.isProgramExerciseFinished()) {
////                            Log.d("checkingggg", "false detected");
////                            finish();
////                            break;
////                        } else {
////                            //check last iteration
////                            if (count >= programTrackersCount) {
////                                Log.d("checkingggg", "finished");
////                                Toast.makeText(DoProgramActivity.this, "show week finished here, and weight dialog", Toast.LENGTH_SHORT).show();
////                            }
////                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
}
