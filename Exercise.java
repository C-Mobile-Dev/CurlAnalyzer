package com.kgt.kyle.perfectbicepcurl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Kyle on 12/12/2016.
 */

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_GRAVITY;
import static android.hardware.Sensor.TYPE_GYROSCOPE;

public class Exercise extends AppCompatActivity implements SensorEventListener {

    //all the sensors
    private Sensor gyroscope,gravity,accelerometer,magneticField;
    //store the last know acceleration force along the y-axis
    private float acclLastX,acclLastY,acclLastZ;
    //check if the phone has started accelerating
    private boolean accInit = false;

    private SensorManager sensorMger;
    //count the reps
    private int rep_counter,weight;
    //check if the rep is going up or down
    private boolean counterFlag = false;
    private boolean startFlag = false;

    private int goodPace,fastPace,slowPace,totalPaceCounts;
    private int rotated_true,rotated_false;

    private float[]mGravity;
    private float[]mGeomag;
    private float roll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_action);

        sensorMger = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorMger.getDefaultSensor(TYPE_ACCELEROMETER);
        gyroscope = sensorMger.getDefaultSensor(TYPE_GYROSCOPE);
        gravity = sensorMger.getDefaultSensor(Sensor.TYPE_GRAVITY);
        magneticField = sensorMger.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        registerListeners();

        final EditText weight_sel = (EditText)findViewById(R.id.weight_entry);
        weight_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weight_sel.setText("");
            }
        });


        final Button start =(Button)findViewById(R.id.startBtn);
        final Button stop = (Button)findViewById(R.id.stopBtn);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                weight = Integer.parseInt(weight_sel.getText().toString());
                weight_sel.setEnabled(false);
                stop.setEnabled(true);
                //Track workout statistics
                startFlag = true;

                //count the reps done
                rep_counter=0;
                //track pace statistics
                goodPace =0;
                fastPace=0;
                slowPace=0;
                totalPaceCounts=0;
                //tack curl form
                rotated_false=0;
                rotated_true = 0;
            }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop.setEnabled(false);
                startFlag = false;
                double curl_form = calculateRotationForm(rotated_true,rotated_false);
                double pace_form = calaculatePaceForm(totalPaceCounts,goodPace);

                ExerciseDataDbHelper dbHelper = new ExerciseDataDbHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                dbHelper.insertExerciseEntery(db,rep_counter,weight,curl_form,pace_form);

                Toast.makeText(Exercise.this,"Your results have been recorded. Check them in the Exercise history",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void registerListeners(){
        sensorMger.registerListener(this,gravity,SensorManager.SENSOR_DELAY_NORMAL);
        sensorMger.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        sensorMger.registerListener(this,gyroscope,SensorManager.SENSOR_DELAY_NORMAL);
        sensorMger.registerListener(this,magneticField,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterListeners(){
        sensorMger.unregisterListener(this, accelerometer);
        sensorMger.unregisterListener(this, gyroscope);
        sensorMger.unregisterListener(this, magneticField);
        sensorMger.unregisterListener(this, gravity);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor active = event.sensor;

        ImageView direction = (ImageView)findViewById(R.id.direction);
        ImageView vail = (ImageView)findViewById(R.id.speedvail);

        ImageView toLeftRot = (ImageView)findViewById(R.id.leftRot);
        ImageView toRightRot = (ImageView)findViewById(R.id.rightRot);

        TextView repCounter = (TextView)findViewById(R.id.repCounter);
        //if the start button has not been clicked
        //don't start recording the session.
        if(startFlag) {
            if (active.getType() == Sensor.TYPE_ACCELEROMETER) {
                mGravity = event.values;
               /*
                * Monitor the change in velocity along the y-axis
                * And record pace attributes of cycle
                */
                if (!accInit) {
                    event.values[0] = acclLastX;
                    event.values[1] = acclLastY;
                    event.values[2] = acclLastZ;
                    accInit = true;
                } else {
                    totalPaceCounts++;
                    float yVal = event.values[1] - acclLastY;
                    if (yVal < 0.5) {
                        vail.setImageResource(getResources().getIdentifier(
                                "speedup", "drawable", "com.kgt.kyle.perfectbicepcurl"));
                        slowPace++;
                    } else if (yVal < 1.8 || yVal > 0.5) {
                        vail.setImageResource(getResources().getIdentifier(
                                "holdpace", "drawable", "com.kgt.kyle.perfectbicepcurl"));
                        goodPace++;
                    } else if (yVal > 1.9) {
                        vail.setImageResource(getResources().getIdentifier(
                                "slowdown", "drawable", "com.kgt.kyle.perfectbicepcurl"));
                        fastPace++;
                    }
                }
            }
            //get the magnetic values array
            if (active.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mGeomag = event.values;
            }
            /*
             * Calculate the azimuth, pitch and roll
             */
            if (mGravity != null && mGeomag != null) {
                float r[] = new float[9];
                float i[] = new float[9];

                boolean success = SensorManager.getRotationMatrix(r, i, mGravity, mGeomag);
                if (success) {
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(r, orientation);
                    roll = orientation[2];

                    if (roll > 0.25) {
                        toLeftRot.setImageResource(getResources().getIdentifier(
                                "arrowleft", "drawable", "com.kgt.kyle.perfectbicepcurl"));
                        toRightRot.setImageResource(0);
                        rotated_true++;
                    } else if (roll < -0.25) {
                        toRightRot.setImageResource(getResources().getIdentifier(
                                "arrowright", "drawable", "com.kgt.kyle.perfectbicepcurl"));
                        toLeftRot.setImageResource(0);
                        rotated_true++;
                    } else if (roll < -0.25 || roll < 0.25) {
                        toRightRot.setImageResource(0);
                        toLeftRot.setImageResource(0);
                        rotated_false++;
                    }
                }
            }

            if (active.getType() == Sensor.TYPE_GRAVITY) {
                /*
                 * Get the gravitational force to determine a rep and direction
                 */
                if (event.values[1] > 6.7) {

                    direction.setImageResource(getResources().getIdentifier(
                            "arrow_up", "drawable", "com.kgt.kyle.perfectbicepcurl"));
                    if (counterFlag) {
                        counterFlag = false;
                        rep_counter++;
                        repCounter.setText(String.valueOf(rep_counter));
                    }
                } else if (event.values[1] < -6.5) {

                    direction.setImageResource(getResources().getIdentifier(
                            "arrowdown", "drawable", "com.kgt.kyle.perfectbicepcurl"));
                    counterFlag = true;
                }
            }
        }
        else{
            //Do not do anything
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //do nothing
    }
    //calculate te percentage of curl technique if the arm has rotated to much or not
    public double calculateRotationForm(int rotation_true_count,int rotation_false_count){

        int total_rot_count = rotation_false_count + rotation_true_count;

        double total = total_rot_count;
        double rot_T = rotation_true_count;
        double rot_F = rotation_false_count;

        double percentage_T = Math.round((rot_T / total) * 100);

        return percentage_T;
    }

    //calculate the percentage of the good pace through out the exercise
    public double calaculatePaceForm(int totalPaceCounts,int goodPaceCount){

        double total = totalPaceCounts;
        double good_pace = goodPaceCount;

        double percentage_G = Math.round((good_pace / total) * 100);

        return percentage_G;
    }

    protected void onResume() {
        super.onResume();
        registerListeners();
    }

    protected void onPause() {
        super.onPause();
        unregisterListeners();
    }
}
