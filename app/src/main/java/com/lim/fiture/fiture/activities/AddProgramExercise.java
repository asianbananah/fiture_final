package com.lim.fiture.fiture.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.adapters.WeekExerciseAdapter;
import com.lim.fiture.fiture.fragments.AddWeekExerciseDialog;
import com.lim.fiture.fiture.models.Exercise;
import com.lim.fiture.fiture.models.Program;
import com.lim.fiture.fiture.models.ProgramExercise;

import java.util.ArrayList;

public class AddProgramExercise extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    private ArrayList<RecyclerView> programExercises = new ArrayList<>();
    private ArrayList<WeekExerciseAdapter> weekExerciseAdapters = new ArrayList<>();
    private ArrayList<ArrayList<ProgramExercise>> weekExercises = new ArrayList<>();
    private ArrayList<ProgramExercise> allProgramExercises = new ArrayList<>();
    private Program program;
    private String action;
    private Button finishbtn;


    DatabaseReference databaseProgramExercises;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_program_exercise);
        databaseProgramExercises = FirebaseDatabase.getInstance().getReference("program_exercise");

        findViews();
        getDataFromPreviousActivity();
        for(int i = 0; i<Integer.parseInt(program.getProgramWeeks()); i++){
            addWeekField(i);
        }




    }

    private void findViews(){
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        getSupportActionBar().setTitle("Program Exercises");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        parentLinearLayout = findViewById(R.id.parentLinearLayout);

//         finishbtn = findViewById(R.id.finishBtn);
//         finishbtn.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View view) {
//                 String upload = databaseProgramExercises.push().getKey();
//                 databaseProgramExercises.child(upload).setValue();
//             }
//         });
    }

    private void getDataFromPreviousActivity(){
        if (getIntent().getStringExtra("action") != null
                && getIntent().getStringExtra("action").equals("edit")) {
            program = (Program) getIntent().getSerializableExtra("program");
            action = "edit";
            //TODO: add resume  values for edit
//            resumeValues();
        } else {
            program = (Program) getIntent().getSerializableExtra("program");
        }
    }

    public void addWeekField(int index) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.program_week_exercise_layout_item, null);
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());

//        EditText editText = rowView.findViewById(R.id.stepTxt);
//        editText.setContentDescription(String.valueOf(count));
//        editText.requestFocus();
//        editText.setText(step);
//        editTexts.add(editText);
//
//        FloatingActionButton fab = rowView.findViewById(R.id.addStep);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                deleteField(editText, rowView);
//            }
//        });

        TextView weekLbl = rowView.findViewById(R.id.weekLbl);
        weekLbl.setText("Week " + (index+1));

        FloatingActionButton fab = rowView.findViewById(R.id.addExerciseBtn);
        fab.setContentDescription(String.valueOf(index));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                AddWeekExerciseDialog dialogFragment = AddWeekExerciseDialog.newInstance(index);
                dialogFragment.show(ft, "dialog");
            }
        });

        RecyclerView recyclerView = rowView.findViewById(R.id.programWeekExercise);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        programExercises.add(recyclerView);
        weekExerciseAdapters.add(new WeekExerciseAdapter());
        weekExercises.add(new ArrayList<>());
//        resetContentDescriptions();
//        listEt();
    }

    public void addWeekExercise(int week, ProgramExercise programExercise){
        weekExercises.get(week).add(programExercise);
        weekExerciseAdapters.set(week, new WeekExerciseAdapter(this, weekExercises.get(week)));
        programExercises.get(week).setAdapter(weekExerciseAdapters.get(week));

    }

    public void getAllWeekExercises(){
        for(int i = 0 ; i<weekExercises.size(); i++){
            for(int i2 = 0 ; i2 < weekExercises.get(i).size(); i2++){
                ProgramExercise programExercise = weekExercises.get(i).get(i2);
                allProgramExercises.add(programExercise);
            }
        }
    }
}
