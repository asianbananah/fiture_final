
package com.lim.fiture.fiture.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.models.DailyChallenge;

public class AddDailyChallengesDetails extends AppCompatActivity {

    private EditText challengeName, challengeDesc, challengeGoalNum, durationTxt;
    private Spinner spinner, track, fitnessGoal;
    private Button nextBtn;

    private DailyChallenge dailyChallenge;
    private String action = "";

    private DatabaseReference challengeReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_challenges_details);
        challengeReference = FirebaseDatabase.getInstance().getReference("DailyChallenge");

        findViews();
        getDataFromPreviousActivity();


    }

    private void getDataFromPreviousActivity() {
        if (getIntent().getStringExtra("action") != null
                && getIntent().getStringExtra("action").equals("edit")) {
            dailyChallenge = (DailyChallenge) getIntent().getSerializableExtra("dailyChallenge");
            action = "edit";
            resumeValues();

        }
    }

    private void resumeValues() {
        //TODO: add code here for resume
        Log.d("hehehehhee", "resumeValues: "+dailyChallenge.getChallengeName());
        challengeName.setText(dailyChallenge.getChallengeName());
        challengeDesc.setText(dailyChallenge.getChallengeDesc());
        challengeGoalNum.setText(dailyChallenge.getChallengeGoalNum()+ "");
        durationTxt.setText(dailyChallenge.getChallengeDuration() + "");
        setSpinnerValues(R.array.Difficulty, spinner, dailyChallenge.getChallengeLevel());
        setSpinnerValues(R.array.challengeTrackValue, track, dailyChallenge.getTrackVal());
        setSpinnerValues(R.array.type, fitnessGoal, dailyChallenge.getChallengeFitnessGoal());
    }

    private void setSpinnerValues(int resource, Spinner spinner, String compareValue) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, resource, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (compareValue != null) {
            int spinnerPosition = adapter.getPosition(compareValue);
            spinner.setSelection(spinnerPosition);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals("edit")) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.delete_menu, menu);
            return true;
        }else
            return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete Challenge");
                builder.setMessage("Are you sure you want to delete this challenge?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (dialogInterface, i) -> {

                    FirebaseDatabase.getInstance().getReference().child("DailyChallenge").child(dailyChallenge.getChallengeId()).removeValue();
                    startActivity(new Intent(AddDailyChallengesDetails.this,AdminActivity.class));

                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                break;
        }
        return true;
    }

    public void findViews() {
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        bar.setTitle("Challenge Details");

        challengeName = findViewById(R.id.challengeName);
        challengeDesc = findViewById(R.id.challengeDesc);
        challengeGoalNum = findViewById(R.id.challengeGoalNum);
        durationTxt = findViewById(R.id.durationTxt);
        spinner = findViewById(R.id.difficulty);
        track = findViewById(R.id.track);
        fitnessGoal = findViewById(R.id.fitnessGoal);
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!action.equals("edit")) {
                    dailyChallenge = new DailyChallenge();
                }
                dailyChallenge.setChallengeName(challengeName.getText().toString());
                dailyChallenge.setChallengeDesc(challengeDesc.getText().toString());
                dailyChallenge.setChallengeGoalNum(Integer.parseInt(challengeGoalNum.getText().toString()));
                dailyChallenge.setChallengeDuration(Integer.parseInt(durationTxt.getText().toString()));
                dailyChallenge.setChallengeLevel(spinner.getSelectedItem().toString());
                dailyChallenge.setTrackVal(track.getSelectedItem().toString());
                if(!dailyChallenge.getTrackVal().equals("Time"))
                    dailyChallenge.setUsePedometer(true);
                else
                    dailyChallenge.setUsePedometer(false);
                dailyChallenge.setChallengeFitnessGoal(fitnessGoal.getSelectedItem().toString());

                startActivity(new Intent(AddDailyChallengesDetails.this, AddDailyChallengesImages.class)
                        .putExtra("dailyChallenge", dailyChallenge)
                        .putExtra("action", action));
            }
        });
    }
}
