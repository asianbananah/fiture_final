package com.lim.fiture.fiture.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.fragments.LeaderboardFragment;
import com.lim.fiture.fiture.fragments.UserDailyChallenges2Fragment;
import com.lim.fiture.fiture.fragments.UserDailyChallengesFragment;
import com.lim.fiture.fiture.fragments.UserExercisesFragment;
import com.lim.fiture.fiture.fragments.UserHistory;
import com.lim.fiture.fiture.fragments.UserProfileFragment;
import com.lim.fiture.fiture.fragments.UserProgramsFragment;
import com.lim.fiture.fiture.models.DailyChallenge;
import com.lim.fiture.fiture.models.User;
import com.lim.fiture.fiture.models.WeightHistory;
import com.lim.fiture.fiture.util.BottomNavigationViewHelper;
import com.lim.fiture.fiture.util.GlobalUser;

import java.util.Calendar;


/**
 * Created by User on 21/09/2018.
 */

public class UserMainActivity extends AppCompatActivity {

    private ActionBar toolBar;
    private DatabaseReference databaseUser;

    private User mUser;
    private String mUserId;
    private static final String TAG = "UserMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_admin_bottom_nav_bar);

//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//
//        // attaching bottom sheet behaviour - hide / show on scroll
//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
//        layoutParams.setBehavior(new BottomNavigationBehavior());
//
//        // load the store fragment by default
//        // ..

        mUserId = getIntent().getStringExtra("userId");
        Log.d("idddUser: ", mUserId);
        databaseUser = FirebaseDatabase.getInstance().getReference("Users").child(mUserId);
        databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mUser = dataSnapshot.getValue(User.class);
                    GlobalUser.setmUser(mUser);
                    UserProfileFragment profileFragment = UserProfileFragment.newInstance(mUser);
                    loadFragment(profileFragment);
                    Log.d("userCheck1", mUser.toString());
                } else {
                    Log.d(TAG, "no data for user");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        findViews();
    }

    private void findViews() {
        toolBar = getSupportActionBar();
        toolBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        toolBar.setTitle("Profile");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_user:
                    toolBar.setTitle("Profile");
                    UserProfileFragment profileFragment = UserProfileFragment.newInstance(mUser);
                    loadFragment(profileFragment);
                    return true;
                case R.id.navigation_exercises: //exercises or programs
                    toolBar.setTitle("Programs");
                   UserDailyChallengesFragment userDailyChallengesFragment = UserDailyChallengesFragment.newInstance(mUser);
                   loadFragment(userDailyChallengesFragment);
                    return true;
                case R.id.navigation_dailychallenges:
                    toolBar.setTitle("Daily Challenges");
                    UserDailyChallenges2Fragment userDailyChallenges2Fragment = UserDailyChallenges2Fragment.newInstance(mUser);
                    loadFragment(userDailyChallenges2Fragment);
                    return true;
                case R.id.navigation_myprograms:
                    toolBar.setTitle("My Programs");
                    UserProgramsFragment userProgramsFragment = UserProgramsFragment.newInstance(mUser);
                    loadFragment(userProgramsFragment);
                    return true;


            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history:
                toolBar.setTitle("History");
                UserHistory userHistory = new UserHistory();
                loadFragment(userHistory);
                return true;
              //  Toast.makeText(this, "History", Toast.LENGTH_SHORT).show();
            case R.id.action_leaderboard:
                toolBar.setTitle("Leaderboard");
                LeaderboardFragment leaderboardFragment = LeaderboardFragment.newInstance(mUser);
                loadFragment(leaderboardFragment);
                return true;
            case R.id.action_edit:
                startActivity(new Intent(UserMainActivity.this, EditProfileActivity.class)
                        .putExtra("user",GlobalUser.getmUser())
                        .putExtra("action","edit"));
                return true;
            case R.id.action_logout:
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserMainActivity.this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public User getCurrentUser(){
        return this.mUser;
    }
}

