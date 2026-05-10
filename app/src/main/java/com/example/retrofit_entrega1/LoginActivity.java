package com.example.retrofit_entrega1;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float acceleration = 0f;
    private float currentAcceleration = SensorManager.GRAVITY_EARTH;
    private float lastAcceleration = SensorManager.GRAVITY_EARTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            lastAcceleration = currentAcceleration;
            currentAcceleration = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = currentAcceleration - lastAcceleration;
            acceleration = acceleration * 0.9f + delta;

            if (acceleration > 12) {
                Intent intentLlamada = new Intent(Intent.ACTION_DIAL);
                intentLlamada.setData(Uri.parse("tel:123456789"));
                startActivity(intentLlamada);
                sensorManager.unregisterListener(this);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null && accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}