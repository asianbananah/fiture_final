package com.lim.fiture.fiture.activities;

import android.app.FragmentManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.fragments.ChallengeDetailsDialog;
import com.lim.fiture.fiture.models.DailyChallenge;
import com.lim.fiture.fiture.models.User;
import com.lim.fiture.fiture.util.StepDetector;
import com.lim.fiture.fiture.util.StepListener;

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

    //timer
    private long startTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_challenge);

        getDataFromPreviousActivity();
        findViews();
        showChallengeDetailsDialog();
    }

    public void getDataFromPreviousActivity() {
        dailyChallenge = (DailyChallenge) getIntent().getSerializableExtra("dailyChallenge");
    }

    public void findViews() {
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
        kmTxt.setText(String.valueOf(getDistanceRun(numSteps)));
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
        FragmentManager fm = getFragmentManager();
        ChallengeDetailsDialog challengeDetailsDialog = ChallengeDetailsDialog.newInstance(dailyChallenge);
        challengeDetailsDialog.show(fm, "fragment_challenge_details");
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

        float distance = (float) (numSteps * 78) / (float) 100000;
        return distance;

//        switch (mUser.getGender()){
//            case "Female":
//                float measurementWomen = (float) (userHeight * 0.413);
//                float toalDistanceRunWomen = (float) (numSteps * measurementWomen) / (float) 100000;
//                return;
//            case "Male":
//                float measurementMen = (float) (userHeight * 0.413);
//                float toalDistanceRunMen = (float) (numSteps * measurementMen) / (float) 100000;
//                return;
//        }

    }



//    public float getUsersDistanceRun(long numSteps){
//    int userHeight = mUser.getHeightInCm();
//
//        float userMeasurementWomen = (float) (userHeight * 0.413);
//        float women = (float) (numSteps * userMeasurementWomen) / (float) 100000;
//
//        float userMeasurementMen = (float) (userHeight * 0.415);
//        float men = (float) (numSteps * userMeasurementMen) / (float) 100000;
//
//        switch (mUser.getGender()){
//            case "Female":
//              return women;
//                break;
//            case "Male":
//                break;
//            return men;
//
//        }
//        return numSteps;
//
//    }

//    public float getDistanceRun(long numSteps) {
//        int userHeight = mUser.getHeightInCm();
//
//        if (mUser.getGender().equals("Female")) {
//            float userMeasuermentWomen = (float) (userHeight * 0.413);
//            float women = (float) (numSteps * userMeasuermentWomen) / (float) 100000;
//            Log.d("women", "getDistanceRun: "+ women);
//             return women;
//        } else if(mUser.getGender().equals("Male")) {
//            float userMeasuermentMen = (float) (userHeight * 0.415);
//            float men = (float) (numSteps * userMeasuermentMen) / (float) 100000;
//            Log.d("men", "getDistanceRun: "+men);
//            return men;
//        }
//
//
//
////        int userHeight = mUser.getHeightInCm();
////
////        //TODO: if women
////        float userMeasurementWomen = (float) (userHeight * 0.413);
////        float women = (float) (numSteps * userMeasurementWomen) / (float) 100000;
////
////        //TODO: if men
////        float userMeasurementMen = (float) (userHeight * 0.415);
////        float men = (float) (numSteps * userMeasurementMen) / (float) 100000;
////
////
//////        float distance = (float) (numSteps * userMeasurementWomen) / (float) 100000;
//////            return distance;
////        switch (mUser.getGender()) {
////            case "Female":
////                return women;
////            break;
////            case "Male":
////                return men;
////            break;
////        }
////
//////        if (mUser.getGender().equals("Female")) {
//////            float distance = (float) (numSteps * userMeasurementWomen) / (float) 100000;
//////
//////        } else if (mUser.getGender().equals("Male")) {
//////            float distance = (float) (numSteps * userMeasurementMen) / (float) 100000;
//////
//////        }
////        return numSteps;
////    }
////        float distance = (float) (numSteps * 78) / (float) 100000;
////        return distance;
////
//        return numSteps;
//    }
}
