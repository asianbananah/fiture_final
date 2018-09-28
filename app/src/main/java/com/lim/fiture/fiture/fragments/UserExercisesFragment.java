package com.lim.fiture.fiture.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.activities.AdminActivity;
import com.lim.fiture.fiture.adapters.ExercisesAdapter;
import com.lim.fiture.fiture.adapters.UserRecommendedExercisesAdapter;
import com.lim.fiture.fiture.models.Exercise;
import com.lim.fiture.fiture.models.User;

import java.util.ArrayList;

public class UserExercisesFragment extends Fragment {
    private View rootView;
    private RecyclerView absExercises, backExercises, bicepsExercises, chestExercises, forearmsExercises,
            glutesExercises, shouldersExercises, tricepsExercises, upperLegsExercises, lowerLegsExercises, heartCardioExercises;
    private RecyclerView.LayoutManager absManager, backManager, bicepsManager, chestManager, forearmsManager,
            glutesManager, shouldersManager, tricepsManager, upperLegsManager, lowerLegsManager, heartCardioManager;
    private UserRecommendedExercisesAdapter absAdapter, backAdapter, bicepsAdapter, chestAdapter, forearmsAdapter,
            glutesAdapter, shouldersAdapter, tricepsAdapter, upperLegsAdapter, lowerLegsAdapter, heartCardioAdapter;

    private User mUser;
    private ArrayList<Exercise> absList, backList, bicepsList, chestList, forearmsList,
            glutesList, shouldersList, tricepslist, upperLegsList, lowerLegsList, heartCardioList;

    private DatabaseReference exerciseReference;
    private String TAG = "userExercisesFragment";
    private RelativeLayout exercisesRelative;
    private ProgressBar progressBar;


    public UserExercisesFragment() {
        // Required empty public constructor
    }

    public static UserExercisesFragment newInstance(User user) {

        Bundle args = new Bundle();

        UserExercisesFragment fragment = new UserExercisesFragment();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_user_exercises, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        exerciseReference = FirebaseDatabase.getInstance().getReference("Exercises");
        mUser = (User) getArguments().getSerializable("user");
        findViews();
    }

    private void findViews() {
        absExercises = rootView.findViewById(R.id.absExercises);
        backExercises = rootView.findViewById(R.id.backExercises);
        bicepsExercises = rootView.findViewById(R.id.bicepsExercises);
        chestExercises = rootView.findViewById(R.id.chestExercises);
        forearmsExercises = rootView.findViewById(R.id.forearmsExercises);
        glutesExercises = rootView.findViewById(R.id.glutesExercises);
        shouldersExercises = rootView.findViewById(R.id.shouldersExercises);
        tricepsExercises = rootView.findViewById(R.id.tricepsExercises);
        upperLegsExercises = rootView.findViewById(R.id.upperLegsExercises);
        lowerLegsExercises = rootView.findViewById(R.id.lowerLegsExercises);
        heartCardioExercises = rootView.findViewById(R.id.heartCardioExercises);
        exercisesRelative = rootView.findViewById(R.id.exercisesRelative);
        progressBar = rootView.findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        absManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        backManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        bicepsManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        chestManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        forearmsManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        glutesManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        shouldersManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        tricepsManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        upperLegsManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        lowerLegsManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        heartCardioManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        absExercises.setLayoutManager(absManager);
        backExercises.setLayoutManager(backManager);
        bicepsExercises.setLayoutManager(bicepsManager);
        chestExercises.setLayoutManager(chestManager);
        forearmsExercises.setLayoutManager(forearmsManager);
        glutesExercises.setLayoutManager(glutesManager);
        shouldersExercises.setLayoutManager(shouldersManager);
        tricepsExercises.setLayoutManager(tricepsManager);
        upperLegsExercises.setLayoutManager(upperLegsManager);
        lowerLegsExercises.setLayoutManager(lowerLegsManager);
        heartCardioExercises.setLayoutManager(heartCardioManager);

        absList = new ArrayList<>();
        backList = new ArrayList<>();
        bicepsList = new ArrayList<>();
        chestList = new ArrayList<>();
        forearmsList = new ArrayList<>();
        glutesList = new ArrayList<>();
        shouldersList = new ArrayList<>();
        tricepslist = new ArrayList<>();
        upperLegsList = new ArrayList<>();
        lowerLegsList = new ArrayList<>();
        heartCardioList = new ArrayList<>();

        loadExercisesForMuscleGroup("Abs", mUser.getFitnessLevel(), mUser.getFitnessGoal());
        loadExercisesForMuscleGroup("Back", mUser.getFitnessLevel(), mUser.getFitnessGoal());
        loadExercisesForMuscleGroup("Biceps", mUser.getFitnessLevel(), mUser.getFitnessGoal());
        loadExercisesForMuscleGroup("Chest", mUser.getFitnessLevel(), mUser.getFitnessGoal());
        loadExercisesForMuscleGroup("Forearms", mUser.getFitnessLevel(), mUser.getFitnessGoal());
        loadExercisesForMuscleGroup("Glutes", mUser.getFitnessLevel(), mUser.getFitnessGoal());
        loadExercisesForMuscleGroup("Shoulders", mUser.getFitnessLevel(), mUser.getFitnessGoal());
        loadExercisesForMuscleGroup("Triceps", mUser.getFitnessLevel(), mUser.getFitnessGoal());
        loadExercisesForMuscleGroup("Upper Legs", mUser.getFitnessLevel(), mUser.getFitnessGoal());
        loadExercisesForMuscleGroup("Lower Legs", mUser.getFitnessLevel(), mUser.getFitnessGoal());
        loadExercisesForMuscleGroup("Heart/Cardio", mUser.getFitnessLevel(), mUser.getFitnessGoal());
    }

    public void loadExercisesForMuscleGroup(String muscleGroup, String difficulty, String type) {
        exerciseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Exercise exercise = dataSnapshot.getValue(Exercise.class);
                Log.d(TAG, exercise.toString());
                progressBar.setVisibility(View.GONE);
                if(exercisesRelative.getVisibility() == View.INVISIBLE)
                    exercisesRelative.setVisibility(View.VISIBLE);
                if (exercise.getMainMuscleGroup().equalsIgnoreCase(muscleGroup)
                        && exercise.getDifficulty().equalsIgnoreCase(difficulty)
                        && exercise.getType().equalsIgnoreCase(type)) {
                    switch (muscleGroup) {
                        case "Abs":
                            absList.add(exercise);
                            absAdapter = new UserRecommendedExercisesAdapter(getActivity(), absList);
                            absExercises.setAdapter(absAdapter);
                            break;
                        case "Back":
                            backList.add(exercise);
                            backAdapter = new UserRecommendedExercisesAdapter(getActivity(), backList);
                            backExercises.setAdapter(backAdapter);
                            break;
                        case "Biceps":
                            bicepsList.add(exercise);
                            bicepsAdapter = new UserRecommendedExercisesAdapter(getActivity(), bicepsList);
                            bicepsExercises.setAdapter(bicepsAdapter);
                            break;
                        case "Chest":
                            chestList.add(exercise);
                            chestAdapter = new UserRecommendedExercisesAdapter(getActivity(), chestList);
                            chestExercises.setAdapter(chestAdapter);
                            break;
                        case "Forearms":
                            forearmsList.add(exercise);
                            forearmsAdapter = new UserRecommendedExercisesAdapter(getActivity(), forearmsList);
                            forearmsExercises.setAdapter(forearmsAdapter);
                            break;
                        case "Glutes":
                            glutesList.add(exercise);
                            glutesAdapter = new UserRecommendedExercisesAdapter(getActivity(), glutesList);
                            glutesExercises.setAdapter(glutesAdapter);
                            break;
                        case "Shoulders":
                            shouldersList.add(exercise);
                            shouldersAdapter = new UserRecommendedExercisesAdapter(getActivity(), shouldersList);
                            shouldersExercises.setAdapter(shouldersAdapter);
                            break;
                        case "Triceps":
                            tricepslist.add(exercise);
                            tricepsAdapter = new UserRecommendedExercisesAdapter(getActivity(), tricepslist);
                            tricepsExercises.setAdapter(tricepsAdapter);
                            break;
                        case "Upper Legs":
                            upperLegsList.add(exercise);
                            upperLegsAdapter = new UserRecommendedExercisesAdapter(getActivity(), upperLegsList);
                            upperLegsExercises.setAdapter(upperLegsAdapter);
                            break;
                        case "Lower Legs":
                            lowerLegsList.add(exercise);
                            lowerLegsAdapter = new UserRecommendedExercisesAdapter(getActivity(), lowerLegsList);
                            lowerLegsExercises.setAdapter(lowerLegsAdapter);
                            break;
                        case "Heart/Cardio":
                            heartCardioList.add(exercise);
                            heartCardioAdapter = new UserRecommendedExercisesAdapter(getActivity(), heartCardioList);
                            heartCardioExercises.setAdapter(heartCardioAdapter);
                            break;
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

}
