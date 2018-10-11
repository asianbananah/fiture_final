package com.lim.fiture.fiture.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.adapters.DailyChallengeAdapter;
import com.lim.fiture.fiture.adapters.DailyChallengeImageAdapter;
import com.lim.fiture.fiture.models.DailyChallenge;

import java.util.ArrayList;

public class DailyChallengesDetailsActivity extends AppCompatActivity {

    private ViewPager mviewPager;
    private TextView challengeName, challengeDescription;

    private RecyclerView.LayoutManager layoutManager;
    private DailyChallengeAdapter dailyChallengeAdapter;

    private DailyChallenge dailyChallenge;
    private DatabaseReference challengeReference;

    private ArrayList<DailyChallenge> dailyChallenges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_challenges_details);

        findViews();
        getDataFromPreviousActivity();
        setProgramDetails();
    }

    private void getDataFromPreviousActivity(){

        if (getIntent().getSerializableExtra("dailyChallenges")!= null)
            dailyChallenge = (DailyChallenge) getIntent().getSerializableExtra("dailyChallenges");
    }

    private void findViews(){
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        getSupportActionBar().setTitle("Daily Challenge Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        challengeName = findViewById(R.id.challengeName);
        challengeDescription = findViewById(R.id.challengeDescription);



        layoutManager = new LinearLayoutManager(this);

        mviewPager = findViewById(R.id.viewPageAndroid);

    }

    private void setProgramDetails(){
        DailyChallengeImageAdapter adapterView = new DailyChallengeImageAdapter(this, dailyChallenge.getChallengeImages());
        mviewPager.setAdapter(adapterView);

        challengeName.setText(dailyChallenge.getChallengeName());
        challengeDescription.setText(dailyChallenge.getChallengeDesc());



    }
}
