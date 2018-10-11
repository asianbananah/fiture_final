package com.lim.fiture.fiture.activities;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
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
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.fragments.AdminDailyChallengeFragment;
import com.lim.fiture.fiture.fragments.AdminExerciseFragment;
import com.lim.fiture.fiture.fragments.AdminProgramFragment;


public class AdminActivity extends AppCompatActivity implements View.OnClickListener {


    private com.github.clans.fab.FloatingActionButton addExerciseBtn, addProgramButton, addChallengeBtn;
    private ImageView loseFilter, gainFilter, maintainFilter, allFilter;

    //TODO: instance variables for firebase database
    private static final String TAG = "AdminActivity";
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mAuth = FirebaseAuth.getInstance();
        findViews();

        AdminExerciseFragment adminExerciseFragment = new AdminExerciseFragment();
        loadFragment(adminExerciseFragment);
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
            case R.id.addChallengeBtn:
                startActivity(new Intent(this, AddDailyChallengesDetails.class));
        }
    }


    @SuppressLint("ResourceType")
    public void findViews() {
        addExerciseBtn = findViewById(R.id.addExerciseBtn);
        addExerciseBtn.setOnClickListener(this);
        addProgramButton = findViewById(R.id.addProgramButton);
        addProgramButton.setOnClickListener(this);
        addChallengeBtn = findViewById(R.id.addChallengeBtn);
        addChallengeBtn.setOnClickListener(this);



        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        bar.setTitle("Fiture Admin");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin, menu);
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_exercises:
                    AdminExerciseFragment adminExerciseFragment = new AdminExerciseFragment();
                    loadFragment(adminExerciseFragment);
                    return true;
                case R.id.navigation_myprograms:
                    AdminProgramFragment adminProgramFragment = new AdminProgramFragment();
                    loadFragment(adminProgramFragment);
                    return true;
                case R.id.navigation_dailychallenges:
                    AdminDailyChallengeFragment adminDailyChallengeFragment = new AdminDailyChallengeFragment();
                    loadFragment(adminDailyChallengeFragment);
                    break;
            }
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

