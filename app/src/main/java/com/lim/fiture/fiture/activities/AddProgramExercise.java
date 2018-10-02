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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
    private String action = "";
    private Button finishbtn;

    private TextView exerciseNameTxt, setsTxt, repsTxt, restTxt;

    private int mWeek;
    private ProgramExercise programExercise;
    private ArrayList<Exercise> exercises = new ArrayList<>();
    private ArrayList<String> exerciseNames = new ArrayList<>();

    private boolean isListening = false;


    DatabaseReference databaseProgramExercises;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_program_exercise);
        databaseProgramExercises = FirebaseDatabase.getInstance().getReference("ProgramExercise");

        findViews();
        getDataFromPreviousActivity();
        for (int i = 0; i < Integer.parseInt(program.getProgramWeeks()); i++) {
            addWeekField(i, program.getProgramsId());
        }

        if (action.equals("edit")) {
            resumeProgramExercises();
        }


    }

    private void findViews() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        getSupportActionBar().setTitle("Program Exercises");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        exerciseNameTxt = findViewById(R.id.exerciseNameTxt);
        setsTxt = findViewById(R.id.setsTxt);
        repsTxt = findViewById(R.id.repsTxt);
        restTxt = findViewById(R.id.restTxt);

        parentLinearLayout = findViewById(R.id.parentLinearLayout);

        finishbtn = findViewById(R.id.finishBtn);
        finishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isListening = false;
                databaseProgramExercises.removeEventListener(childEventListener);
                getAllWeekExercises();
                for (int i = 0; i < allProgramExercises.size(); i++) {
                    String programExerciseId = "";
                    if (allProgramExercises.get(i).getProgramExerciseId() == null) {
                        programExerciseId = databaseProgramExercises.child(program.getProgramsId())
                                .push()
                                .getKey();
                        allProgramExercises.get(i).setProgramExerciseId(programExerciseId);
                    } else {
                        programExerciseId = allProgramExercises.get(i).getProgramExerciseId();
                    }
                    databaseProgramExercises
                            .child(program.getProgramsId())
                            .child(programExerciseId)
                            .setValue(allProgramExercises.get(i));
                }
                startActivity(new Intent(AddProgramExercise.this, AddProgramImages.class)
                        .putExtra("program",program)
                        .putExtra("action",action));
            }
        });
    }

    private void getDataFromPreviousActivity() {
        if (getIntent().getStringExtra("action") != null
                && getIntent().getStringExtra("action").equals("edit")) {
            program = (Program) getIntent().getSerializableExtra("program");
            action = "edit";

        } else {
            program = (Program) getIntent().getSerializableExtra("program");
        }
    }

    public void addWeekField(int index, String programId) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.program_week_exercise_layout_item, null);
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());


        TextView weekLbl = rowView.findViewById(R.id.weekLbl);
        weekLbl.setText("Week " + (index + 1));

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
        Log.d("programWeekz", index + "");

    }

    public void addWeekExercise(int week, ProgramExercise programExercise) {
        weekExercises.get(week).add(programExercise);
        weekExerciseAdapters.set(week, new WeekExerciseAdapter(this, weekExercises.get(week)));
        programExercises.get(week).setAdapter(weekExerciseAdapters.get(week));

    }

    public void getAllWeekExercises() {
        for (int i = 0; i < weekExercises.size(); i++) {
            for (int i2 = 0; i2 < weekExercises.get(i).size(); i2++) {
                ProgramExercise programExercise = weekExercises.get(i).get(i2);
                programExercise.setProgramId(program.getProgramsId());
                allProgramExercises.add(programExercise);
            }
        }
    }

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if(isListening) {
                ProgramExercise programExercise = dataSnapshot.getValue(ProgramExercise.class);
                assignExerciseToWeekFromEdit(programExercise);
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
    };

    private void resumeProgramExercises() {
        isListening = true;
        databaseProgramExercises.child(program.getProgramsId()).addChildEventListener(childEventListener);
    }

    private void assignExerciseToWeekFromEdit(ProgramExercise programExercise) {
        try {
            Log.d("programWeeky: " , weekExercises.size() + "");

            weekExercises.get(programExercise.getWeek()).add(programExercise);
            weekExerciseAdapters.set((programExercise.getWeek()), new WeekExerciseAdapter(this, weekExercises.get(programExercise.getWeek())));
            programExercises.get(programExercise.getWeek()).setAdapter(weekExerciseAdapters.get(programExercise.getWeek()));
            Log.d("moymoyyz", programExercise.getProgramExerciseId());
        } catch (Exception e) {
            Log.d("genericException", "genericExceptionThrown: " + programExercise.getProgramExerciseId());
            databaseProgramExercises.child(program.getProgramsId()).child(programExercise.getProgramExerciseId()).removeValue();
            e.printStackTrace();
        }
    }
}
