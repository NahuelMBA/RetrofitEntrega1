package com.example.retrofit_entrega1.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.retrofit_entrega1.R;
import com.example.retrofit_entrega1.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float acceleration = 0f;
    private float currentAcceleration = SensorManager.GRAVITY_EARTH;
    private float lastAcceleration = SensorManager.GRAVITY_EARTH;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnReset = findViewById(R.id.btnReset);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        viewModel.getToken().observe(this, token -> {
            SharedPreferences sp = getSharedPreferences("token.xml", MODE_PRIVATE);
            sp.edit().putString("token", "Bearer " + token).apply();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });

        viewModel.getMensajeToast().observe(this, mensaje -> {
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
        });

        btnLogin.setOnClickListener(v -> {
            String correo = etEmail.getText().toString();
            String clave = etPassword.getText().toString();

            if (correo.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.login(correo, clave);
        });

        btnReset.setOnClickListener(v -> viewModel.resetearPassword());

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
            float delta = Math.abs(currentAcceleration - lastAcceleration);
            acceleration = acceleration * 0.9f + delta;

            if (acceleration > 12) {
                acceleration = 0f;
                Intent intentLlamada = new Intent(Intent.ACTION_DIAL);
                intentLlamada.setData(Uri.parse("tel:123456789"));
                startActivity(intentLlamada);
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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
