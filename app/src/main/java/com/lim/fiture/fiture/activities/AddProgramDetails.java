package com.lim.fiture.fiture.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.models.Exercise;
import com.lim.fiture.fiture.models.Program;

import java.util.HashMap;

/**
 * Created by User on 26/09/2018.
 */

public class AddProgramDetails extends AppCompatActivity {
    private Program program;
    private EditText programName, programDescription;
    private Spinner numofWeeks, type, equipment, difficulty;
    private Button nextBtn;
    private String action = "";

    DatabaseReference databaseProgram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_program_exercise);
        databaseProgram = FirebaseDatabase.getInstance().getReference("Program");

        findViews();

    }

    private void findViews(){
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        getSupportActionBar().setTitle("Program Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        programName = findViewById(R.id.programName);
        numofWeeks = findViewById(R.id.numofWeeks);
        programDescription = findViewById(R.id.programDescription);
        type = findViewById(R.id.type);
        equipment = findViewById(R.id.equipment);
        difficulty = findViewById(R.id.difficulty);
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                program = new Program();

                program.setProgramsName(programName.getText().toString());
                program.setProgramDesc(programDescription.getText().toString());
                program.setProgramWeeks(numofWeeks.getSelectedItem().toString());
                program.setProgramType(type.getSelectedItem().toString());
                program.setProgramDifficulty(difficulty.getSelectedItem().toString());

            //   String upload = databaseProgram.push().getKey();

//               databaseProgram.push().setValue(program);
//                Log.e("keyyy", databaseProgram.push().getKey());

//                HashMap<String, String> meMap=new HashMap<String, String>();
//                meMap.put("programsName", programName.getText().toString());
//                meMap.put("programDescription",programDescription.getText().toString());
//                meMap.put("numofWeeks",numofWeeks.getSelectedItem().toString());
//                meMap.put("type",type.getSelectedItem().toString());
//                meMap.put("difficulty",difficulty.getSelectedItem().toString());

//                Intent intent = new Intent(AddProgramDetails.this, AddProgramExercise.class);
////                intent.putExtra("Program", meMap);
//                startActivity(intent);

           startActivity(new Intent(AddProgramDetails.this, AddProgramExercise.class).putExtra("program",program));
            }
        });

    }
}
