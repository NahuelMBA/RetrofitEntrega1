package com.example.retrofit_entrega1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.retrofit_entrega1.api.ApiClient;
import com.example.retrofit_entrega1.model.Inmueble;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInmuebleViewModel extends ViewModel {

    private MutableLiveData<String> mensajeToast = new MutableLiveData<>();
    private MutableLiveData<Boolean> disponibilidadActualizada = new MutableLiveData<>();

    public LiveData<String> getMensajeToast() {
        return mensajeToast;
    }

    public LiveData<Boolean> getDisponibilidadActualizada() {
        return disponibilidadActualizada;
    }

    public void actualizarDisponibilidad(String token, Inmueble inmueble, boolean nuevoEstado) {
        Inmueble update = new Inmueble();
        update.setIdInmueble(inmueble.getIdInmueble());
        update.setDireccion(inmueble.getDireccion());
        update.setUso(inmueble.getUso());
        update.setTipo(inmueble.getTipo());
        update.setAmbientes(inmueble.getAmbientes());
        update.setSuperficie(inmueble.getSuperficie());
        update.setLatitud(inmueble.getLatitud());
        update.setLongitud(inmueble.getLongitud());
        update.setValor(inmueble.getValor());
        update.setImagen(inmueble.getImagen());
        update.setDisponible(nuevoEstado);

        ApiClient.getApi().actualizarInmueble(token, update).enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful()) {
                    mensajeToast.setValue("Disponibilidad actualizada");
                    disponibilidadActualizada.setValue(nuevoEstado);
                } else {
                    mensajeToast.setValue("Error " + response.code() + " al guardar cambios");
                    disponibilidadActualizada.setValue(!nuevoEstado);
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                mensajeToast.setValue("Error de red");
                disponibilidadActualizada.setValue(!nuevoEstado);
            }
        });
    }
}
