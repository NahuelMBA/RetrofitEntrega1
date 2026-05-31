package com.example.retrofit_entrega1.view;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofit_entrega1.viewmodel.InmueblesViewModel;
import com.example.retrofit_entrega1.R;
import com.example.retrofit_entrega1.model.Inmueble;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InmueblesFragment extends Fragment {

    private RecyclerView rvInmuebles;
    private InmueblesAdapter adapter;
    private FloatingActionButton fabAgregar;
    private InmueblesViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inmuebles, container, false);

        rvInmuebles = view.findViewById(R.id.rvInmuebles);
        fabAgregar = view.findViewById(R.id.fabAgregarInmueble);
        rvInmuebles.setLayoutManager(new GridLayoutManager(getContext(), 2));

        fabAgregar.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.cargarInmuebleFragment));

        viewModel = new ViewModelProvider(this).get(InmueblesViewModel.class);

        viewModel.getInmuebles().observe(getViewLifecycleOwner(), listaInmuebles -> {
            adapter = new InmueblesAdapter(listaInmuebles, getContext(), inmueble -> {
                prepararActualizacion(inmueble);
            });
            rvInmuebles.setAdapter(adapter);
        });

        viewModel.getMensajeToast().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = requireActivity().getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        viewModel.cargarInmuebles(token);
    }

    private void prepararActualizacion(Inmueble inmueble) {
        SharedPreferences sp = requireActivity().getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        final boolean nuevoEstado = inmueble.isDisponible();
        Inmueble update = crearInmuebleLimpio(inmueble, nuevoEstado);

        viewModel.actualizarInmueble(token, update,
                () -> {
                },
                () -> {
                    inmueble.setDisponible(!nuevoEstado);
                    if (adapter != null) adapter.notifyDataSetChanged();
                }
        );
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
        return update;
    }
}