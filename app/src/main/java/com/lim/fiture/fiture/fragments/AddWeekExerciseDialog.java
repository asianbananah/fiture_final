package com.lim.fiture.fiture.fragments;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.activities.AddProgramExercise;
import com.lim.fiture.fiture.activities.AdminActivity;
import com.lim.fiture.fiture.adapters.ExercisesAdapter;
import com.lim.fiture.fiture.models.Exercise;
import com.lim.fiture.fiture.models.ProgramExercise;

import java.util.ArrayList;

public class AddWeekExerciseDialog extends DialogFragment {
    private static final String WEEK = "week";

    private int mWeek;
    private View rootView;

    private Spinner exerciseSpinner, daySpinner;
    private EditText setsTxt, repsTxt, restTxt;
    private Button okBtn, cancelBtn;

    private ArrayList<Exercise> exercises = new ArrayList<>();
    private ArrayList<String> exerciseNames = new ArrayList<>();

    private ArrayAdapter<String> exerciseAdapter;

    private ProgramExercise programExercise;

    private String TAG = "AddWeekExerciseDialog";
    private DatabaseReference exerciseReference;

    public AddWeekExerciseDialog() {
        // Required empty public constructor
    }

    public static AddWeekExerciseDialog newInstance(int week) {
        AddWeekExerciseDialog fragment = new AddWeekExerciseDialog();
        Bundle args = new Bundle();
        args.putInt(WEEK, week);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exerciseReference = FirebaseDatabase.getInstance().getReference("Exercises");
        if (getArguments() != null) {
            mWeek = getArguments().getInt(WEEK);
            Toast.makeText(getActivity(), "Week: " + mWeek, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_week_exercise_dialog, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        programExercise = new ProgramExercise();
        findViews();
        getExerciseList();
    }

    private void findViews(){
        exerciseSpinner = rootView.findViewById(R.id.exerciseSpinner);
        daySpinner = rootView.findViewById(R.id.daySpinner);
        setsTxt = rootView.findViewById(R.id.setsTxt);
        repsTxt = rootView.findViewById(R.id.repsTxt);
        restTxt = rootView.findViewById(R.id.restTxt);
        okBtn = rootView.findViewById(R.id.okBtn);
        cancelBtn = rootView.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                programExercise.setProgramId();
                //TODO: lacks program ID
                try {
                    programExercise.setSets(Integer.parseInt(setsTxt.getText().toString()));
                    programExercise.setReps(Integer.parseInt(repsTxt.getText().toString()));
                    programExercise.setRest(Integer.parseInt(restTxt.getText().toString()));
                    programExercise.setDay(daySpinner.getSelectedItem().toString());
                    programExercise.setWeek(mWeek);
                    programExercise.setExerciseName(exerciseSpinner.getSelectedItem().toString());
                    for (int i = 0; i < exercises.size(); i++) {
                        if (exercises.get(i).getExerciseName().equals(exerciseSpinner.getSelectedItem().toString())) {
                            programExercise.setExerciseId(exercises.get(i).getExerciseId());
                        }
                    }
                    ((AddProgramExercise) getActivity()).addWeekExercise(mWeek, programExercise);
                    dismiss();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    int x = 0;
    private void getExerciseList(){
        exerciseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    Exercise exercise = dataSnapshot.getValue(Exercise.class);
                    Log.d(TAG, "error in: " + x++);
                    Log.d(TAG, exercise.toString());
                    exerciseNames.add(exercise.getExerciseName());
                    exercises.add(exercise);

                    exerciseAdapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_item, exerciseNames);
                    exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    exerciseSpinner.setAdapter(exerciseAdapter);
                    exerciseSpinner.setSelection(0);
                }
                catch (Exception e){
                    e.printStackTrace();
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
}
