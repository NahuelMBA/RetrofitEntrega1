package com.example.retrofit_entrega1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.retrofit_entrega1.api.ApiClient;
import com.example.retrofit_entrega1.model.Inmueble;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends ViewModel {

    private MutableLiveData<List<Inmueble>> inmueblesLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mensajeToast = new MutableLiveData<>();

    public LiveData<List<Inmueble>> getInmuebles() {
        return inmueblesLiveData;
    }

    public LiveData<String> getMensajeToast() {
        return mensajeToast;
    }

    public void cargarInmuebles(String token) {
        ApiClient.getApi().obtenerInmuebles(token).enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    inmueblesLiveData.setValue(response.body());
                } else {
                    mensajeToast.setValue("Error al cargar la lista");
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                mensajeToast.setValue("Error de conexión");
            }
        });
    }

    public void actualizarInmueble(String token, Inmueble update, Runnable onSuccess, Runnable onFailure) {
        ApiClient.getApi().actualizarInmueble(token, update).enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful()) {
                    mensajeToast.setValue("Estado guardado");
                    if (onSuccess != null) onSuccess.run();
                } else {
                    mensajeToast.setValue("Error " + response.code() + " al guardar");
                    if (onFailure != null) onFailure.run();
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                mensajeToast.setValue("Error de conexión al actualizar");
                if (onFailure != null) onFailure.run();
            }
        });
    }
}