package com.example.retrofit_entrega1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.retrofit_entrega1.api.ApiClient;
import com.example.retrofit_entrega1.model.Propietario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends ViewModel {

    private MutableLiveData<Propietario> perfilLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mensajeToast = new MutableLiveData<>();

    public LiveData<Propietario> getPerfil() {
        return perfilLiveData;
    }

    public LiveData<String> getMensajeToast() {
        return mensajeToast;
    }

    public void obtenerPerfil(String token) {
        ApiClient.getApi().obtenerPerfil(token).enqueue(new Callback<Propietario>() {
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

    public void actualizarPerfil(String token, Propietario propietario) {
        ApiClient.getApi().actualizarPerfil(token, propietario).enqueue(new Callback<Propietario>() {
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

    public void cambiarPassword(String token, String actual, String nueva) {
        ApiClient.getApi().cambiarPassword(token, actual, nueva).enqueue(new Callback<String>() {
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
