package com.lim.fiture.fiture.activities;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
//import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.adapters.AdminExerciseFiltersAdapter;
import com.lim.fiture.fiture.adapters.ExercisesAdapter;
import com.lim.fiture.fiture.fragments.ConfirmationDialog;
import com.lim.fiture.fiture.models.Exercise;
import com.lim.fiture.fiture.models.ExerciseFilter;

import java.util.ArrayList;


public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView exercisesList;
    private RecyclerView.LayoutManager layoutManager;
    private com.github.clans.fab.FloatingActionButton addExerciseBtn, addProgramButton;
    private ExercisesAdapter exercisesAdapter;
    private ArrayList<Exercise> exercises = new ArrayList<>();
    private ImageView loseFilter, gainFilter, maintainFilter, allFilter;

    private RecyclerView filtersRecycler;
    private RecyclerView.LayoutManager filterLayoutManager;
    private ArrayList<ExerciseFilter> exerciseFilters = new ArrayList<>();
    private AdminExerciseFiltersAdapter filtersAdapter;

    //TODO: instance variables for firebase database
    private DatabaseReference exerciseReference;
    private static final String TAG = "AdminActivity";
    private FirebaseAuth mAuth;
    private SwipeRefreshLayout swipeRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth = FirebaseAuth.getInstance();
        exerciseReference = FirebaseDatabase.getInstance().getReference("Exercises");
        findViews();
        loadExercises();


    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }
    }


    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addExerciseBtn:
                startActivity(new Intent(this, AddExerciseStepOne.class));
                break;
            case R.id.addProgramButton:
                startActivity(new Intent(this, AddProgramDetails.class));
                break;
        }
    }


    @SuppressLint("ResourceType")
    public void findViews() {
        addExerciseBtn = findViewById(R.id.addExerciseBtn);
        addExerciseBtn.setOnClickListener(this);
        addProgramButton = findViewById(R.id.addProgramButton);
        addProgramButton.setOnClickListener(this);
        exercisesList = findViewById(R.id.exercisesList);
        layoutManager = new LinearLayoutManager(this);
        exercisesList.setLayoutManager(layoutManager);

        filtersRecycler = findViewById(R.id.filtersList);
        filterLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        filtersRecycler.setLayoutManager(filterLayoutManager);
        exerciseFilters = new ArrayList<>();
        exerciseFilters.add(new ExerciseFilter("Filter", R.drawable.ic_filter));
        exerciseFilters.add(new ExerciseFilter("All", R.drawable.ic_all_exercises));
        exerciseFilters.add(new ExerciseFilter("Lose", R.drawable.ic_lose));
        exerciseFilters.add(new ExerciseFilter("Maintain", R.drawable.ic_maintain));
        exerciseFilters.add(new ExerciseFilter("Gain", R.drawable.ic_gain));
        filtersAdapter = new AdminExerciseFiltersAdapter(exerciseFilters, this);
        filtersRecycler.setAdapter(filtersAdapter);


        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeColors(Color.BLUE);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                exercises = new ArrayList<>();
                loadExercises();
                swipeRefresh.setRefreshing(false);
            }
        });

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        bar.setTitle("Fiture Admin");


    }

    public void maintainFilterExercises() {
        exercises = new ArrayList<>();
        exerciseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot reportSnapshot : dataSnapshot.getChildren()) {
                    Exercise exercise = reportSnapshot.getValue(Exercise.class);
                    if (exercise.getType().equals("Maintain")) {


                        exercises.add(exercise);
                        exercisesAdapter = new ExercisesAdapter(AdminActivity.this, exercises);
                        exercisesList.setAdapter(exercisesAdapter);


                        Log.e("maintainExercises", exercise.getExerciseName());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void gainFilterExercises() {
        exercises = new ArrayList<>();
        exerciseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot reportSnapshot : dataSnapshot.getChildren()) {
                    Exercise exercise = reportSnapshot.getValue(Exercise.class);
                    if (exercise.getType().equals("Gain")) {

                        exercises.add(exercise);
                        exercisesAdapter = new ExercisesAdapter(AdminActivity.this, exercises);
                        exercisesList.setAdapter(exercisesAdapter);
                        Log.e("gainExercises", exercise.getExerciseName());
                    }
                }
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
                        exercisesAdapter = new ExercisesAdapter(AdminActivity.this, exercises);
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

    public void loadExercises() {
        exerciseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Exercise exercise = dataSnapshot.getValue(Exercise.class);
//                Log.d(TAG, exercise.toString());
                exercises.add(exercise);
                exercisesAdapter = new ExercisesAdapter(AdminActivity.this, exercises);
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

    public void showConfirmationDialog(String title, String message, Exercise exercise, int position) {
        Log.d("posTest1: ", position + "");
        FragmentManager fm = getFragmentManager();
        ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance(title, message, exercise, position);
        confirmationDialog.show(fm, "fragment_alert");
    }

    public void updateExerciseListAfterDelete(int position) {
        Log.d("posTest2: ", position + "");
        exercises.remove(position);
        exercisesAdapter.notifyItemRemoved(position);
        exercisesAdapter.notifyItemRangeChanged(position, exercises.size());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.programs:
                startActivity(new Intent(AdminActivity.this, ProgramActivity.class));
                return true;
            case R.id.action_search:
                Toast.makeText(this, "Implement search", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminActivity.this,LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

