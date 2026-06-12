package com.example.retrofit_entrega1.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.retrofit_entrega1.api.ApiClient;
import com.example.retrofit_entrega1.model.Propietario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    private MutableLiveData<Propietario> perfilLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mensajeToast = new MutableLiveData<>();

    public PerfilViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Propietario> getPerfil() {
        return perfilLiveData;
    }

    public LiveData<String> getMensajeToast() {
        return mensajeToast;
    }

    private String getToken() {
        SharedPreferences sp = getApplication().getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }

    public void obtenerPerfil() {
        ApiClient.getApi().obtenerPerfil(getToken()).enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    perfilLiveData.setValue(response.body());
                } else {
                    mensajeToast.setValue("Error al obtener perfil");
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                mensajeToast.setValue("Error de conexión");
            }
        });
    }

    public void actualizarPerfil(Propietario propietario) {
        ApiClient.getApi().actualizarPerfil(getToken(), propietario).enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        perfilLiveData.setValue(response.body());
                    }
                    mensajeToast.setValue("Perfil actualizado");
                } else {
                    mensajeToast.setValue("Error al actualizar");
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                mensajeToast.setValue("Error de conexión");
            }
        });
    }

    public void cambiarPassword(String actual, String nueva) {
        ApiClient.getApi().cambiarPassword(getToken(), actual, nueva).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    mensajeToast.setValue("Contraseña cambiada");
                } else {
                    mensajeToast.setValue("Error al cambiar contraseña");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mensajeToast.setValue("Error de conexión");
            }
        });
    }
}
