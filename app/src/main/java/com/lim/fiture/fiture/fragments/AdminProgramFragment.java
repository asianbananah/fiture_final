package com.lim.fiture.fiture.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class AdminProgramFragment extends Fragment {

    private RecyclerView programList;
    private RecyclerView.LayoutManager layoutManager;
    private ProgramActivityAdapter programActivityAdapter;
    private ArrayList<Program> programs = new ArrayList<>();

    private DatabaseReference programReference;
    private View rootView;

    public AdminProgramFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_program, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        programReference = FirebaseDatabase.getInstance().getReference("Program");
        findViews();
        loadExercises();
    }

    public void findViews() {
        programList = rootView.findViewById(R.id.programList);
        layoutManager = new LinearLayoutManager(getActivity());
        programList.setLayoutManager(layoutManager);

    }

    public void loadExercises() {
        programReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Program program = dataSnapshot.getValue(Program.class);
                Log.d("program", program.toString());
                programs.add(program);
                programActivityAdapter = new ProgramActivityAdapter(getActivity(), programs);
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


}
