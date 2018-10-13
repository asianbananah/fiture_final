package com.lim.fiture.fiture.activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.adapters.TestStackAdapter;
import com.lim.fiture.fiture.models.Program;
import com.lim.fiture.fiture.models.ProgramExercise;
import com.loopeer.cardstack.CardStackView;

import java.util.ArrayList;
import java.util.Arrays;

public class StartProgramActivity extends AppCompatActivity implements CardStackView.ItemExpendListener {

    public static Integer[] daysOfWeek = new Integer[]{
            R.color.Monday,
            R.color.Tuesday,
            R.color.Wednesday,
            R.color.Thursday,
            R.color.Friday,
            R.color.Saturday,
            R.color.Sunday
    };

    private Toolbar toolBar;
    private TextView weekTitle, weekStateTxt;
    private ImageView nextBtn, backBtn;
    private CardStackView cardStackView;
    private TestStackAdapter mTestStackAdapter;
    private Program program;
    private DatabaseReference programExerciseReference;
    private ArrayList<ProgramExercise> programExercises = new ArrayList<>();
    private ArrayList<ProgramExercise> dayOfWeekExercises = new ArrayList<>();
    private String TAG = "StartProgramActivity";

    private int totalWeeks;
    private int currentWeek = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_program);
        programExerciseReference = FirebaseDatabase.getInstance().getReference("ProgramExercise");

        getDataFromPreviousActivity();
        getProgramDetails(currentWeek);
        findViews();
    }

    public void getDataFromPreviousActivity(){
        if(getIntent().getSerializableExtra("program") != null) {
            program = (Program) getIntent().getSerializableExtra("program");
            totalWeeks = Integer.parseInt(program.getProgramWeeks());
        }
    }

    private void setUpWeekNavigation(){
        if(currentWeek == 1){
            backBtn.setVisibility(View.GONE);
        }else{
            backBtn.setVisibility(View.VISIBLE);
        }

        if(totalWeeks > 1){
            nextBtn.setVisibility(View.VISIBLE);
        }

        if(currentWeek == totalWeeks){
            nextBtn.setVisibility(View.GONE);
        }
    }

    private void determineWeek(int command){
        // if 0 is passed, meaning back is pressed
        // if 1 is passed, meaning next is pressed

        switch (command){
            case 0:
                currentWeek--;
                weekTitle.setText("Week " + currentWeek);
                setUpWeekNavigation();
                getProgramDetails(currentWeek);
                break;
            case 1:
                currentWeek++;
                weekTitle.setText("Week " + currentWeek);
                setUpWeekNavigation();
                getProgramDetails(currentWeek);
                break;
        }
    }

    private void findViews(){
        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        weekTitle = findViewById(R.id.weekTitle);
        weekStateTxt = findViewById(R.id.weekStateTxt);
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StartProgramActivity.this, "next", Toast.LENGTH_SHORT).show();
                determineWeek(1);
            }
        });
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StartProgramActivity.this, "back", Toast.LENGTH_SHORT).show();
                determineWeek(0);
            }
        });
        setUpWeekNavigation();
        cardStackView = findViewById(R.id.cardStackView);
        cardStackView.setItemExpendListener(this);
        mTestStackAdapter = new TestStackAdapter(this);
        cardStackView.setAdapter(mTestStackAdapter);

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        mTestStackAdapter.updateData(Arrays.asList(daysOfWeek));
                    }
                }
                , 200
        );
    }

    @Override
    public void onItemExpend(boolean expend) {
//        mActionButtonContainer.setVisibility(expend ? View.VISIBLE : View.GONE);
    }

    public void getProgramDetails(int week){
        programExercises = new ArrayList<>();
        programExerciseReference.child(program.getProgramsId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ProgramExercise programExercise = dataSnapshot.getValue(ProgramExercise.class);
                if(programExercise.getWeek() == (week-1)) {
                    programExercises.add(programExercise);
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

    public void setWeekExercisesData(int position){
        switch (position){
            case 0:
                dayOfWeekExercises = new ArrayList<>();
                for (int i = 0; i<programExercises.size(); i++){
                    if(programExercises.get(i).getDay().equals("Monday")){
                        dayOfWeekExercises.add(programExercises.get(i));
                    }
                }
                Log.d(TAG, "DayOfWeekExercises: " + dayOfWeekExercises.toString());
                break;
            case 1:
                dayOfWeekExercises = new ArrayList<>();
                for (int i = 0; i<programExercises.size(); i++){
                    if(programExercises.get(i).getDay().equals("Tuesday")){
                        dayOfWeekExercises.add(programExercises.get(i));
                    }
                }
                Log.d(TAG, "DayOfWeekExercises: " + dayOfWeekExercises.toString());
                break;
            case 2:
                dayOfWeekExercises = new ArrayList<>();
                for (int i = 0; i<programExercises.size(); i++){
                    if(programExercises.get(i).getDay().equals("Wednesday")){
                        dayOfWeekExercises.add(programExercises.get(i));
                    }
                }
                Log.d(TAG, "DayOfWeekExercises: " + dayOfWeekExercises.toString());
                break;
            case 3:
                dayOfWeekExercises = new ArrayList<>();
                for (int i = 0; i<programExercises.size(); i++){
                    if(programExercises.get(i).getDay().equals("Thursday")){
                        dayOfWeekExercises.add(programExercises.get(i));
                    }
                }
                Log.d(TAG, "DayOfWeekExercises: " + dayOfWeekExercises.toString());
                break;
            case 4:
                dayOfWeekExercises = new ArrayList<>();
                for (int i = 0; i<programExercises.size(); i++){
                    if(programExercises.get(i).getDay().equals("Friday")){
                        dayOfWeekExercises.add(programExercises.get(i));
                    }
                }
                Log.d(TAG, "DayOfWeekExercises: " + dayOfWeekExercises.toString());
                break;
            case 5:
                dayOfWeekExercises = new ArrayList<>();
                for (int i = 0; i<programExercises.size(); i++){
                    if(programExercises.get(i).getDay().equals("Saturday")){
                        dayOfWeekExercises.add(programExercises.get(i));
                    }
                }
                Log.d(TAG, "DayOfWeekExercises: " + dayOfWeekExercises.toString());
                break;
            case 6:
                dayOfWeekExercises = new ArrayList<>();
                for (int i = 0; i<programExercises.size(); i++){
                    if(programExercises.get(i).getDay().equals("Sunday")){
                        dayOfWeekExercises.add(programExercises.get(i));
                    }
                }
                Log.d(TAG, "DayOfWeekExercises: " + dayOfWeekExercises.toString());
                break;
        }
    }

    public ArrayList<ProgramExercise> getDayOfWeekExercises() {
        return dayOfWeekExercises;
    }
}
