package com.example.retrofit_entrega1.view;

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

import com.example.retrofit_entrega1.R;
import com.example.retrofit_entrega1.databinding.FragmentInmueblesBinding;
import com.example.retrofit_entrega1.viewmodel.InmueblesViewModel;
import com.example.retrofit_entrega1.model.Inmueble;

public class InmueblesFragment extends Fragment {

    private FragmentInmueblesBinding binding;
    private InmueblesAdapter adapter;
    private InmueblesViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInmueblesBinding.inflate(inflater, container, false);

        binding.rvInmuebles.setLayoutManager(new GridLayoutManager(getContext(), 2));

        binding.fabAgregarInmueble.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.cargarInmuebleFragment));

        viewModel = new ViewModelProvider(this).get(InmueblesViewModel.class);

        viewModel.getInmuebles().observe(getViewLifecycleOwner(), listaInmuebles -> {
            adapter = new InmueblesAdapter(listaInmuebles, getContext(), inmueble -> {
                prepararActualizacion(inmueble);
            });
            binding.rvInmuebles.setAdapter(adapter);
        });

        viewModel.getMensajeToast().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.cargarInmuebles();
    }

    private void prepararActualizacion(Inmueble inmueble) {
        final boolean nuevoEstado = inmueble.isDisponible();
        Inmueble update = crearInmuebleLimpio(inmueble, nuevoEstado);

        viewModel.actualizarInmueble(update,
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
