package com.lim.fiture.fiture.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.FloatMath;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by User on 12/10/2018.
 */

public class MotionSensor {
    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    private Context context;
    private static final int MINIMUM_MOVEMENT_SCORE = 100;
    private int movementScore;

    public MotionSensor(Context context) {
        this.context = context;
    }


    public void instantiateSensor(){
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    public void detectMotion(SensorEvent event){
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mGravity = event.values.clone();
            // Shake detection
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float)Math.sqrt(x*x + y*y + z*z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            // Make this higher or lower according to how much
            // motion you want to detect
            if(mAccel > 3){
                movementScore++;
                Log.d("movementScore: ", String.valueOf(movementScore));
            }
        }
    }

    public boolean userMoved(){
        if(movementScore >= MINIMUM_MOVEMENT_SCORE)
            return true;
        else
            return false;
    }
}
