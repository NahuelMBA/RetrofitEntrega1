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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesFragment extends Fragment {
    private RecyclerView rvInmuebles;
    private InmueblesAdapter adapter;
    private FloatingActionButton fabAgregar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inmuebles, container, false);
        rvInmuebles = view.findViewById(R.id.rvInmuebles);
        fabAgregar = view.findViewById(R.id.fabAgregarInmueble);
        rvInmuebles.setLayoutManager(new LinearLayoutManager(getContext()));
        fabAgregar.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.cargarInmuebleFragment));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        obtenerInmuebles();
    }

    private void obtenerInmuebles() {
        SharedPreferences sp = requireActivity().getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        ApiClient.getApi().obtenerInmuebles(token).enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    List<Inmueble> listaCompleta = response.body();
                    adapter = new InmueblesAdapter(listaCompleta, getContext(), inmueble -> {
                        actualizarInmueble(inmueble);
                    });
                    rvInmuebles.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                if (isAdded()) Toast.makeText(getContext(), "Error al obtener lista", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarInmueble(Inmueble inmueble) {
        SharedPreferences sp = requireActivity().getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        final boolean nuevoEstado = inmueble.isDisponible();

        Inmueble update = crearInmuebleLimpio(inmueble, nuevoEstado);

        ApiClient.getApi().actualizarInmueble(token, update).enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (isAdded()) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Estado guardado", Toast.LENGTH_SHORT).show();
                    } else {
                        inmueble.setDisponible(!nuevoEstado);
                        if (adapter != null) adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Error " + response.code() + " al guardar", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                if (isAdded()) {
                    inmueble.setDisponible(!nuevoEstado);
                    if (adapter != null) adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Inmueble crearInmuebleLimpio(Inmueble inmueble, boolean disponible) {
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
        update.setDisponible(disponible);
        update.setIdPropietario(0);
        return update;
    }
}
