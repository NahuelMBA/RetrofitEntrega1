package com.example.retrofit_entrega1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.retrofit_entrega1.api.ApiClient;
import com.example.retrofit_entrega1.model.Contrato;
import com.example.retrofit_entrega1.model.Inmueble;
import com.example.retrofit_entrega1.model.Inquilino;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinosViewModel extends ViewModel {

    private MutableLiveData<List<Inmueble>> alquiladosLiveData = new MutableLiveData<>();
    private MutableLiveData<Inquilino> inquilinoLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mensajeToast = new MutableLiveData<>();
    private List<Contrato> contratosCache = new ArrayList<>();

    public LiveData<List<Inmueble>> getAlquilados() {
        return alquiladosLiveData;
    }

    public LiveData<Inquilino> getInquilino() {
        return inquilinoLiveData;
    }

    public LiveData<String> getMensajeToast() {
        return mensajeToast;
    }

    public void cargarAlquilados(String token) {
        ApiClient.getApi().obtenerContratos(token).enqueue(new Callback<List<Contrato>>() {
            @Override
            public void onResponse(Call<List<Contrato>> call, Response<List<Contrato>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    contratosCache = response.body();
                    Map<Integer, Inmueble> unicos = new LinkedHashMap<>();
                    for (Contrato c : contratosCache) {
                        if (c.getInmueble() != null) {
                            unicos.put(c.getInmueble().getIdInmueble(), c.getInmueble());
                        }
                    }
                    alquiladosLiveData.setValue(new ArrayList<>(unicos.values()));
                } else {
                    mensajeToast.setValue("Error al cargar datos");
                }
            }

            @Override
            public void onFailure(Call<List<Contrato>> call, Throwable t) {
                mensajeToast.setValue("Error de conexión");
            }
        });
    }

    public void cargarInquilinoPorInmueble(String token, int idInmueble) {
        ApiClient.getApi().obtenerContratos(token).enqueue(new Callback<List<Contrato>>() {
            @Override
            public void onResponse(Call<List<Contrato>> call, Response<List<Contrato>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Contrato c : response.body()) {
                        if (c.getInmueble() != null && c.getInmueble().getIdInmueble() == idInmueble && c.getInquilino() != null) {
                            inquilinoLiveData.setValue(c.getInquilino());
                            return;
                        }
                    }
                    mensajeToast.setValue("No se encontró inquilino");
                } else {
                    mensajeToast.setValue("Error al cargar datos");
                }
            }

            @Override
            public void onFailure(Call<List<Contrato>> call, Throwable t) {
                mensajeToast.setValue("Error de conexión");
            }
        });
    }
}
