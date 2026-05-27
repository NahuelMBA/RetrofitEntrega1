package com.example.retrofit_entrega1.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.retrofit_entrega1.api.ApiClient;
import com.example.retrofit_entrega1.R;
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

public class CargarInmuebleFragment extends Fragment {
    private EditText etDireccion, etUso, etTipo, etAmbientes, etSuperficie, etPrecio;
    private Button btnSeleccionar, btnCargar;
    private ImageView ivFoto;
    private Uri uriImagen;

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    uriImagen = result.getData().getData();
                    ivFoto.setImageURI(uriImagen);
                    ivFoto.setVisibility(View.VISIBLE);
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cargar_inmueble, container, false);
        etDireccion = view.findViewById(R.id.etCargarDireccion);
        etUso = view.findViewById(R.id.etCargarUso);
        etTipo = view.findViewById(R.id.etCargarTipo);
        etAmbientes = view.findViewById(R.id.etCargarAmbientes);
        etSuperficie = view.findViewById(R.id.etCargarSuperficie);
        etPrecio = view.findViewById(R.id.etCargarPrecio);
        btnSeleccionar = view.findViewById(R.id.btnSeleccionarFoto);
        btnCargar = view.findViewById(R.id.btnCargarInmueble);
        ivFoto = view.findViewById(R.id.ivCargarFoto);

        btnSeleccionar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryLauncher.launch(intent);
        });

        btnCargar.setOnClickListener(v -> cargarInmueble());
        return view;
    }

    private void cargarInmueble() {
        String direccion = etDireccion.getText().toString();
        String uso = etUso.getText().toString();
        String tipo = etTipo.getText().toString();
        String ambientes = etAmbientes.getText().toString();
        String superficie = etSuperficie.getText().toString();
        String precio = etPrecio.getText().toString();

        if (direccion.isEmpty() || uso.isEmpty() || tipo.isEmpty() || ambientes.isEmpty() || superficie.isEmpty() || precio.isEmpty() || uriImagen == null) {
            Toast.makeText(getContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
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

        File file = new File(requireContext().getCacheDir(), "temp.jpg");
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uriImagen);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bos.toByteArray());
            fos.close();
        } catch (IOException e) { return; }

        MultipartBody.Part bodyImagen = MultipartBody.Part.createFormData("imagen", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        SharedPreferences sp = requireActivity().getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        ApiClient.getApi().cargarInmueble(token, bodyImagen, inmuebleBody).enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (isAdded()) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Cargado con éxito", Toast.LENGTH_SHORT).show();
                        requireActivity().onBackPressed();
                    } else {
                        Toast.makeText(getContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                if (isAdded()) Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
