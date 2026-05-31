package com.example.retrofit_entrega1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.retrofit_entrega1.api.ApiClient;
import com.example.retrofit_entrega1.model.Inmueble;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CargarInmuebleViewModel extends ViewModel {

    private MutableLiveData<String> mensajeToast = new MutableLiveData<>();
    private MutableLiveData<Boolean> cargaExitosa = new MutableLiveData<>();

    public LiveData<String> getMensajeToast() {
        return mensajeToast;
    }

    public LiveData<Boolean> getCargaExitosa() {
        return cargaExitosa;
    }

    public void cargarInmueble(String token, MultipartBody.Part imagen, RequestBody inmuebleBody) {
        ApiClient.getApi().cargarInmueble(token, imagen, inmuebleBody).enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful()) {
                    mensajeToast.setValue("Cargado con éxito");
                    cargaExitosa.setValue(true);
                } else {
                    mensajeToast.setValue("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                mensajeToast.setValue("Error de red");
            }
        });
    }
}
