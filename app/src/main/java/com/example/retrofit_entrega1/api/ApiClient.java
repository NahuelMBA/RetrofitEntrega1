package com.example.retrofit_entrega1.api;

import com.example.retrofit_entrega1.model.Contrato;
import com.example.retrofit_entrega1.model.Inmueble;
import com.example.retrofit_entrega1.model.Pago;
import com.example.retrofit_entrega1.model.Propietario;

import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class ApiClient {
    private static final String BASE_URL = "https://capacitacion.alwaysdata.net/";

    public interface ApiInmobiliaria {
        @FormUrlEncoded
        @POST("api/propietarios/login")
        Call<String> login(@Field("Usuario") String usuario, @Field("Clave") String clave);

        @GET("api/propietarios")
        Call<Propietario> obtenerPerfil(@Header("Authorization") String token);

        @PUT("api/propietarios/actualizar")
        Call<Propietario> actualizarPerfil(@Header("Authorization") String token, @Body Propietario propietario);

        @FormUrlEncoded
        @PUT("api/Propietarios/changePassword")
        Call<String> cambiarPassword(@Header("Authorization") String token, @Field("currentPassword") String currentPassword, @Field("newPassword") String newPassword);

        @GET("api/inmuebles")
        Call<List<Inmueble>> obtenerInmuebles(@Header("Authorization") String token);

        @PUT("api/Inmuebles/actualizar")
        Call<Inmueble> actualizarInmueble(@Header("Authorization") String token, @Body Inmueble inmueble);

        @Multipart
        @POST("api/Inmuebles/cargar")
        Call<Inmueble> cargarInmueble(@Header("Authorization") String token, @Part MultipartBody.Part imagen, @Part("inmueble") RequestBody inmueble);

        @GET("api/contratos")
        Call<List<Contrato>> obtenerContratos(@Header("Authorization") String token);

        @GET("api/pagos/contrato/{id}")
        Call<List<Pago>> obtenerPagos(@Header("Authorization") String token, @Path("id") int id);

        @PUT("api/propietarios/fix-id3")
        Call<String> resetearPassword();
    }

    public static ApiInmobiliaria getApi() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInmobiliaria.class);
    }
}