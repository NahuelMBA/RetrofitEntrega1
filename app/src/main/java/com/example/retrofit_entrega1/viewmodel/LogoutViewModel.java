package com.example.retrofit_entrega1.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class LogoutViewModel extends AndroidViewModel {

    public LogoutViewModel(@NonNull Application application) {
        super(application);
    }

    public void cerrarSesion() {
        SharedPreferences sp = getApplication().getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }
}
