package com.lim.fiture.fiture.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.fragments.ConfirmationDialog;
import com.lim.fiture.fiture.models.Exercise;
import com.lim.fiture.fiture.models.Program;

import java.util.HashMap;

/**
 * Created by User on 26/09/2018.
 */

public class AddProgramDetails extends AppCompatActivity {
    private Program program;
    private EditText programName, programDescription;
    private Spinner numofWeeks, type, difficulty;
    private Button nextBtn;
    private String action = "";

    DatabaseReference databaseProgram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_program_exercise);
        databaseProgram = FirebaseDatabase.getInstance().getReference("Program");

        findViews();
        if (getIntent().getStringExtra("action") != null
                && getIntent().getStringExtra("action").equals("edit")) {
            program = (Program) getIntent().getSerializableExtra("program");
            action = "edit";
            resumeValues();
        } else {
            program = new Program();
        }
    }

    private void resumeValues() {
        programName.setText(program.getProgramsName());
        programDescription.setText(program.getProgramDesc());
        setSpinnerValues(R.array.num_of_weeks, numofWeeks, program.getProgramWeeks());
        setSpinnerValues(R.array.type, type, program.getProgramType());
        setSpinnerValues(R.array.Difficulty, difficulty, program.getProgramDifficulty());
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


    private void findViews() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        getSupportActionBar().setTitle("Program Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        programName = findViewById(R.id.programName);
        numofWeeks = findViewById(R.id.numofWeeks);
        programDescription = findViewById(R.id.programDescription);
        type = findViewById(R.id.type);
        difficulty = findViewById(R.id.difficulty);
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!action.equals("edit")) {
                    program = new Program();
                }

                program.setProgramsName(programName.getText().toString());
                program.setProgramDesc(programDescription.getText().toString());
                program.setProgramWeeks(numofWeeks.getSelectedItem().toString());
                program.setProgramType(type.getSelectedItem().toString());
                program.setProgramDifficulty(difficulty.getSelectedItem().toString());

                if(!action.equals("edit")) {
                    String programId = databaseProgram.push().getKey();
                    program.setProgramsId(programId);
                    databaseProgram.child(programId).setValue(program);
                }else{
                    databaseProgram.child(program.getProgramsId()).setValue(program);
                }

                startActivity(new Intent(AddProgramDetails.this, AddProgramExercise.class)
                        .putExtra("program", program)
                        .putExtra("action", action));
            }
        });

    }
}
