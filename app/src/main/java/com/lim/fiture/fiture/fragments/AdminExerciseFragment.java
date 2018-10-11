package com.lim.fiture.fiture.fragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.lim.fiture.fiture.activities.AdminActivity;
import com.lim.fiture.fiture.adapters.AdminExerciseFiltersAdapter;
import com.lim.fiture.fiture.adapters.ExercisesAdapter;
import com.lim.fiture.fiture.models.Exercise;
import com.lim.fiture.fiture.models.ExerciseFilter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminExerciseFragment extends Fragment {

    private RecyclerView exercisesList;
    private RecyclerView.LayoutManager layoutManager;
    private ExercisesAdapter exercisesAdapter;
    private ArrayList<Exercise> exercises = new ArrayList<>();

    private RecyclerView filtersRecycler;
    private RecyclerView.LayoutManager filterLayoutManager;
    private ArrayList<ExerciseFilter> exerciseFilters = new ArrayList<>();
    private AdminExerciseFiltersAdapter filtersAdapter;

    private SwipeRefreshLayout swipeRefresh;
    private View rootView;
    private DatabaseReference exerciseReference;



    public AdminExerciseFragment() {
        // Required empty public constructor
    }

    public static AdminExerciseFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AdminExerciseFragment fragment = new AdminExerciseFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_admin_exercise, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        exerciseReference = FirebaseDatabase.getInstance().getReference("Exercises");
        findViews();
        loadExercises();
    }

    public void findViews(){
        exercisesList = rootView.findViewById(R.id.exercisesList);
        layoutManager = new LinearLayoutManager(getActivity());
        exercisesList.setLayoutManager(layoutManager);

        filtersRecycler = rootView.findViewById(R.id.filtersList);
        filterLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        filtersRecycler.setLayoutManager(filterLayoutManager);
        exerciseFilters = new ArrayList<>();
        exerciseFilters.add(new ExerciseFilter("Filter", R.drawable.ic_filter));
        exerciseFilters.add(new ExerciseFilter("All", R.drawable.ic_all_exercises));
        exerciseFilters.add(new ExerciseFilter("Lose", R.drawable.ic_lose));
        exerciseFilters.add(new ExerciseFilter("Maintain", R.drawable.ic_maintain));
        exerciseFilters.add(new ExerciseFilter("Gain", R.drawable.ic_gain));
        filtersAdapter = new AdminExerciseFiltersAdapter(exerciseFilters, getActivity(), AdminExerciseFragment.this);
        filtersRecycler.setAdapter(filtersAdapter);


        swipeRefresh = rootView.findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeColors(Color.BLUE);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                exercises = new ArrayList<>();
                loadExercises();
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    public void loadExercises() {
        exerciseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Exercise exercise = dataSnapshot.getValue(Exercise.class);
//                Log.d(TAG, exercise.toString());
                exercises.add(exercise);
                exercisesAdapter = new ExercisesAdapter(getActivity(), exercises);
                exercisesList.setAdapter(exercisesAdapter);

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

    public void filterExercises(String exerciseFilter){
        exercises = new ArrayList<>();
        exerciseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Exercise exercise = dataSnapshot.getValue(Exercise.class);
                try {
                    if (exercise.getType().equals(exerciseFilter)) {
                        exercises.add(exercise);
                        exercisesAdapter = new ExercisesAdapter(getActivity(), exercises);
                        exercisesList.setAdapter(exercisesAdapter);
                        Log.e(exerciseFilter + "exercises", exercise.getExerciseName());
                    }
                }catch (Exception e){
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



//    public void showConfirmationDialog(String title, String message, Exercise exercise, int position) {
//        Log.d("posTest1: ", position + "");
//        FragmentManager fm = getFragmentManager();
//        ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance(title, message, exercise, position);
//        confirmationDialog.show(fm, "fragment_alert");
//    }
//
//    public void updateExerciseListAfterDelete(int position) {
//        Log.d("posTest2: ", position + "");
//        exercises.remove(position);
//        exercisesAdapter.notifyItemRemoved(position);
//        exercisesAdapter.notifyItemRangeChanged(position, exercises.size());
//    }
}
