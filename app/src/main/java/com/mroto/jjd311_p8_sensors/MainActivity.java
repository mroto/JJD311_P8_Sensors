package com.mroto.jjd311_p8_sensors;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final float SENSOR_UMBRAL = SensorManager.GRAVITY_EARTH*3;
    private static final int SENSOR_DELAY = SensorManager.SENSOR_DELAY_NORMAL*50;

    private SensorManager mSensorManager;
    private List<Sensor> mSensorList;
    private Sensor mAccelerometer;
    private RelativeLayout mMainLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(MainActivity.TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mMainLayout = (RelativeLayout) this.findViewById(R.id.main_layout);

        this.mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        //List all sensors:
        this.mSensorList = this.mSensorManager.getSensorList(Sensor.TYPE_ALL);
        String sensorString = "";
        StringBuilder sensorStringBuilder = new StringBuilder("");
        for (Sensor aSensor : this.mSensorList) {
            sensorStringBuilder.append(aSensor.getName());
            sensorStringBuilder.append("\n");
        }
        sensorString = sensorStringBuilder.toString();
        Toast.makeText(this, sensorString, Toast.LENGTH_LONG).show();
        Log.e(MainActivity.TAG, sensorString);

        //Check accelerometer:
        this.mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (this.mAccelerometer != null) {
            Toast.makeText(this, "Accelerometer sensor found", Toast.LENGTH_LONG).show();
            Log.e(MainActivity.TAG, "Accelerometer sensor found");
        } else {
            Toast.makeText(this, "Accelerometer sensor NOT found!", Toast.LENGTH_LONG).show();
            Log.e(MainActivity.TAG, "Accelerometer sensor NOT found!");
        }
    }

    @Override
    protected void onResume() {
        Log.e(MainActivity.TAG, "onResume");
        super.onResume();
        this.mSensorManager.registerListener(this, this.mAccelerometer, MainActivity.SENSOR_DELAY);
    }

    @Override
    protected void onPause() {
        Log.e(MainActivity.TAG, "onPause");
        super.onPause();
        this.mSensorManager.unregisterListener(this, this.mAccelerometer);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        float sum=x+y+z;
        Log.e(MainActivity.TAG, "onSensorChanged: sensorEvent.values{" + sensorEvent.values[0]+","+sensorEvent.values[1]+","+sensorEvent.values[2]+"} => sum="+sum);
        if(sum > MainActivity.SENSOR_UMBRAL){
            Log.e(MainActivity.TAG, "Shake it!");
            if (this.mMainLayout.getBackground() instanceof ColorDrawable)
            {
                if (((ColorDrawable) this.mMainLayout.getBackground()).getColor() == ContextCompat.getColor(this, R.color.lightBlue))
                    this.mMainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.lightRed));
                else
                    this.mMainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.lightBlue));
            }
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.e(MainActivity.TAG, "onAccuracyChanged");

    }
}
