package com.lim.fiture.fiture.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.adapters.ProgramDetailsImageAdapter;
import com.lim.fiture.fiture.fragments.ChallengeCompleteDialog;
import com.lim.fiture.fiture.fragments.NoMovementDialog;
import com.lim.fiture.fiture.models.DailyChallenge;
import com.lim.fiture.fiture.util.MotionSensor;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class ActivityExecuteChallengeTrackTime extends AppCompatActivity implements SensorEventListener {

    private ViewPager viewPageAndroid;
    private Button startBtn, stopBtn;
    private TextView timerTxt, challengeName, challengeDesc;

    private DailyChallenge dailyChallenge;
    private CountDownTimer countDownTimer;

    private SensorManager sensorMan;
    private Sensor accelerometer;

    private MotionSensor motionSensor;
    private KonfettiView viewKonfetti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_challenge_track_time);

        getDataFromPreviousActivity();

        sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        findViews();
    }

    private void getDataFromPreviousActivity(){
        dailyChallenge = (DailyChallenge) getIntent().getSerializableExtra("dailyChallenge");
    }

    private void findViews(){
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        getSupportActionBar().setTitle("Daily Challenge");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPageAndroid = findViewById(R.id.viewPageAndroid);
        ProgramDetailsImageAdapter adapterView = new ProgramDetailsImageAdapter(this, dailyChallenge.getChallengeImages());
        viewPageAndroid.setAdapter(adapterView);

        viewKonfetti = findViewById(R.id.viewKonfetti);

        timerTxt = findViewById(R.id.timerTxt);
        challengeName = findViewById(R.id.challengeName);
        challengeName.setText(dailyChallenge.getChallengeName());
        challengeDesc = findViewById(R.id.challengeDesc);
        challengeDesc.setText(dailyChallenge.getChallengeDesc());

        startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensorMan.registerListener(ActivityExecuteChallengeTrackTime.this, accelerometer,
                        SensorManager.SENSOR_DELAY_UI);
                motionSensor = new MotionSensor(ActivityExecuteChallengeTrackTime.this);
                motionSensor.instantiateSensor();
                countDownTimer = new CountDownTimer(dailyChallenge.getChallengeGoalNum()*60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        int seconds = (int) (millisUntilFinished / 1000);
                        int minutes = seconds / 60;
                        seconds = seconds % 60;
                        timerTxt.setText(String.format("%d:%02d", minutes, seconds));
                    }

                    public void onFinish() {
                        if(motionSensor.userMoved())
                            showChallengeCompleteDialog();
                        else {
                            FragmentManager fm = getSupportFragmentManager();
                            NoMovementDialog noMovementDialog = NoMovementDialog.newInstance(dailyChallenge);
                            noMovementDialog.show(fm, "fragment_challenge_incom");
                        }
                    }

                }.start();
            }
        });
        stopBtn = findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Exit challenge");
                builder.setMessage("Are you sure you want quit this challenge?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                    sensorMan.unregisterListener(ActivityExecuteChallengeTrackTime.this);
                    finish();
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        sensorMan.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        motionSensor.detectMotion(sensorEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

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
}
