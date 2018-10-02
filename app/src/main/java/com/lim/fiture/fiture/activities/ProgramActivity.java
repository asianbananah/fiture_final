package com.lim.fiture.fiture.activities;

import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.adapters.ProgramActivityAdapter;
import com.lim.fiture.fiture.fragments.ConfirmationDialog;
import com.lim.fiture.fiture.models.Program;

import java.util.ArrayList;

public class ProgramActivity extends AppCompatActivity {

    private RecyclerView programList;
    private RecyclerView.LayoutManager layoutManager;
    private ProgramActivityAdapter programActivityAdapter;
    private ArrayList<Program> programs = new ArrayList<>();

    private DatabaseReference programReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);

        programReference = FirebaseDatabase.getInstance().getReference("Program");
        findViews();
        loadExercises();

    }

    public void findViews(){
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Programs");

        programList = findViewById(R.id.programList);
        layoutManager = new LinearLayoutManager(this);
        programList.setLayoutManager(layoutManager);

    }

    public void loadExercises() {
        programReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Program program = dataSnapshot.getValue(Program.class);
                Log.d("program", program.toString());
                programs.add(program);
                programActivityAdapter = new ProgramActivityAdapter(ProgramActivity.this, programs);
                programList.setAdapter(programActivityAdapter);

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
    public void showConfirmationDialog(String title, String message, Program program, int position) {
        Log.d("posTest1: ", position + "");
        FragmentManager fm = getFragmentManager();
       ConfirmationDialog confirmationDialogProgram = ConfirmationDialog.newInstance(title, message, program, position);
        confirmationDialogProgram.show(fm, "fragment_alert");
    }

    public void updateProgramListAfterDelete(int position) {
        Log.d("posTest2: ", position + "");
        programs.remove(position);
        programActivityAdapter.notifyItemRemoved(position);
        programActivityAdapter.notifyItemRangeChanged(position, programs.size());
    }


}
