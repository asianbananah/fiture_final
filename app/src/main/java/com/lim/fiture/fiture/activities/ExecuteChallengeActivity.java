package com.lim.fiture.fiture.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.fragments.ChallengeCompleteDialog;
import com.lim.fiture.fiture.fragments.ChallengeDetailsDialog;
import com.lim.fiture.fiture.models.DailyChallenge;
import com.lim.fiture.fiture.models.User;
import com.lim.fiture.fiture.util.GlobalUser;
import com.lim.fiture.fiture.util.StepDetector;
import com.lim.fiture.fiture.util.StepListener;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ExecuteChallengeActivity extends AppCompatActivity implements SensorEventListener, StepListener {

    private TextView instructionsTxt, kmTxt, stepsTxt, timeTxt;
    private Button startChallengeBtn;

    private DailyChallenge dailyChallenge;
    private User mUser;

    //pedometer
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private int numSteps;
    private float distanceRun;

    //timer
    private long startTime = 0;

    private DatabaseReference userReference;
    private KonfettiView viewKonfetti;

    DecimalFormat d = new DecimalFormat("#.##");

    private DatabaseReference completedChallengeRef;
    private String TAG = "ExecuteChallengeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_challenge);

        getUser();
        getDataFromPreviousActivity();
        findViews();
        showChallengeDetailsDialog();
    }

    public void getDataFromPreviousActivity() {
        dailyChallenge = (DailyChallenge) getIntent().getSerializableExtra("dailyChallenge");
    }

    private void getUser(){
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        Log.d(TAG, "userId: " + userId);
//        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
//        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d(TAG, "dataSnapshot: " + dataSnapshot.toString());
//                mUser = dataSnapshot.getValue(User.class);
//                Log.d(TAG, "mUser: " + mUser.getiD());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        mUser = GlobalUser.getmUser();
        Log.d(TAG, "globalUser: " + mUser.toString());
    }

    public void findViews() {

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        getSupportActionBar().setTitle(dailyChallenge.getChallengeName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);


        instructionsTxt = findViewById(R.id.instructionsTxt);
        instructionsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChallengeDetailsDialog();
            }
        });

        kmTxt = findViewById(R.id.kmTxt);
        stepsTxt = findViewById(R.id.stepsTxt);
        timeTxt = findViewById(R.id.timeTxt);
        startChallengeBtn = findViewById(R.id.startChallengeBtn);
        viewKonfetti = findViewById(R.id.viewKonfetti);
        startChallengeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numSteps = 0;

                startTimer();
                sensorManager.registerListener(ExecuteChallengeActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(ExecuteChallengeActivity.this);
        stopTimer();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    sensorEvent.timestamp, sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        stepsTxt.setText(String.valueOf(numSteps));
        d.format(getDistanceRun(numSteps));
        kmTxt.setText((String.format(String.valueOf(getDistanceRun(numSteps)),"%.02f", d)));

        if(isChallengeCompleted()){
            sensorManager.unregisterListener(ExecuteChallengeActivity.this);
            stopTimer();
            showChallengeCompleteDialog();
        }
    }

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timeTxt.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    private void showChallengeDetailsDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ChallengeDetailsDialog challengeDetailsDialog = ChallengeDetailsDialog.newInstance(dailyChallenge);
        challengeDetailsDialog.show(fm, "fragment_challenge_details");
    }

    private void showChallengeCompleteDialog(){
        FragmentManager fm = getSupportFragmentManager();
        ChallengeCompleteDialog challengeCompleteDialog = ChallengeCompleteDialog.newInstance(dailyChallenge);
        challengeCompleteDialog.show(fm, "fragment_challenge_com");

        viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5))
                .setPosition(-50f, viewKonfetti.getWidth() + 50f, -50f, -50f)
                .streamFor(300, 5000L);
    }

    public void startTimer() {
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    public void stopTimer() {
        timerHandler.removeCallbacks(timerRunnable);
        timeTxt.setText("0:00");
    }


    public float getDistanceRun(long numSteps) {
        //int userHeight = mUser.getHeightInCm();
        distanceRun = (float) (numSteps * 78) / (float) 100000;

        return distanceRun;

    }


//    public float getUsersDistanceRun(long numSteps) {
//        int userHeight = mUser.getHeightInCm();
//
//        float userMeasurementWomen = (float) (userHeight * 0.413);
//        float women = (float) (numSteps * userMeasurementWomen) / (float) 100000;
//
//        float userMeasurementMen = (float) (userHeight * 0.415);
//        float men = (float) (numSteps * userMeasurementMen) / (float) 100000;
//
//        switch (mUser.getGender()) {
//            case "Female":
//                distanceRun = women;
//                break;
//            case "Male":
//                distanceRun = men;
//                break;
//
//        }
//        return distanceRun;
//    }

    public boolean isChallengeCompleted(){
        switch (dailyChallenge.getTrackVal()){
            case "Steps":
                if(numSteps >= dailyChallenge.getChallengeGoalNum())
                    return true;
                break;
            case "Distance":
                if(distanceRun >= dailyChallenge.getChallengeGoalNum())
                    return true;
                break;
        }
        return false;
    }
}
