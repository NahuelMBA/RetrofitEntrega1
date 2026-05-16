package com.example.retrofit_entrega1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesFragment extends Fragment {
    private RecyclerView rvInmuebles;
    private InmueblesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inmuebles, container, false);
        rvInmuebles = view.findViewById(R.id.rvInmuebles);
        rvInmuebles.setLayoutManager(new LinearLayoutManager(getContext()));
        obtenerInmuebles();
        return view;
    }

    private void obtenerInmuebles() {
        SharedPreferences sp = getActivity().getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        ApiClient.getApi().obtenerInmuebles(token).enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new InmueblesAdapter(response.body(), getContext(), inmueble -> {
                        actualizarInmueble(inmueble);
                    });
                    rvInmuebles.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al obtener inmuebles", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarInmueble(Inmueble inmueble) {
        SharedPreferences sp = getActivity().getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        ApiClient.getApi().actualizarInmueble(token, inmueble).enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Estado actualizado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}