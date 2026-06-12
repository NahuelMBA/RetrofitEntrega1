package com.example.retrofit_entrega1.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.retrofit_entrega1.api.ApiClient;
import com.example.retrofit_entrega1.model.Inmueble;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CargarInmuebleViewModel extends AndroidViewModel {

    private MutableLiveData<String> mensajeToast = new MutableLiveData<>();
    private MutableLiveData<Boolean> cargaExitosa = new MutableLiveData<>();

    public CargarInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getMensajeToast() {
        return mensajeToast;
    }

    public LiveData<Boolean> getCargaExitosa() {
        return cargaExitosa;
    }

    private String getToken() {
        SharedPreferences sp = getApplication().getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }

    public void cargarInmueble(String direccion, String uso, String tipo, String ambientes, String superficie, String precio, Uri uriImagen) {
        if (direccion.isEmpty() || uso.isEmpty() || tipo.isEmpty() || ambientes.isEmpty() || superficie.isEmpty() || precio.isEmpty() || uriImagen == null) {
            mensajeToast.setValue("Complete todos los campos");
            return;
        }

        JsonObject inmueble = new JsonObject();
        inmueble.addProperty("direccion", direccion);
        inmueble.addProperty("uso", uso);
        inmueble.addProperty("tipo", tipo);
        inmueble.addProperty("ambientes", Integer.parseInt(ambientes));
        inmueble.addProperty("superficie", Integer.parseInt(superficie));
        inmueble.addProperty("latitud", 0.0);
        inmueble.addProperty("longitud", 0.0);
        inmueble.addProperty("valor", Double.parseDouble(precio));
        inmueble.addProperty("disponible", false);
        RequestBody inmuebleBody = RequestBody.create(MediaType.parse("application/json"), inmueble.toString());

        File file = new File(getApplication().getCacheDir(), "temp.jpg");
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), uriImagen);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bos.toByteArray());
            fos.close();
        } catch (IOException e) {
            mensajeToast.setValue("Error al procesar la imagen");
            return;
        }

        MultipartBody.Part bodyImagen = MultipartBody.Part.createFormData("imagen", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        ApiClient.getApi().cargarInmueble(getToken(), bodyImagen, inmuebleBody).enqueue(new Callback<Inmueble>() {
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
