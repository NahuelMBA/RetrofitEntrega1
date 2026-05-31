package com.example.retrofit_entrega1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.retrofit_entrega1.api.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<String> tokenLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mensajeToast = new MutableLiveData<>();

    public LiveData<String> getToken() {
        return tokenLiveData;
    }

    public LiveData<String> getMensajeToast() {
        return mensajeToast;
    }

    public void login(String usuario, String clave) {
        ApiClient.getApi().login(usuario, clave).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tokenLiveData.setValue(response.body());
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
