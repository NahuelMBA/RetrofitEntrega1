package com.example.retrofit_entrega1.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.retrofit_entrega1.api.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> loginExitoso = new MutableLiveData<>();
    private MutableLiveData<String> mensajeToast = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getLoginExitoso() {
        return loginExitoso;
    }

    public LiveData<String> getMensajeToast() {
        return mensajeToast;
    }

    private void guardarToken(String token) {
        SharedPreferences sp = getApplication().getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        sp.edit().putString("token", "Bearer " + token).apply();
    }

    public void login(String usuario, String clave) {
        ApiClient.getApi().login(usuario, clave).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    guardarToken(response.body());
                    loginExitoso.setValue(true);
                } else {
                    mensajeToast.setValue("Credenciales incorrectas");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mensajeToast.setValue("Error de red");
            }
        });
    }

    public void resetearPassword() {
        ApiClient.getApi().resetearPassword().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    mensajeToast.setValue("Datos reseteados. Clave: DEEKQW");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mensajeToast.setValue("Error al resetear");
            }
        });
    }
}
