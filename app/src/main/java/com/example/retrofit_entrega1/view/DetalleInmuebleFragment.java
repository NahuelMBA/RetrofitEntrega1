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
import com.bumptech.glide.Glide;
import com.example.retrofit_entrega1.databinding.FragmentDetalleInmuebleBinding;
import com.example.retrofit_entrega1.model.Inmueble;
import com.example.retrofit_entrega1.viewmodel.DetalleInmuebleViewModel;

public class DetalleInmuebleFragment extends Fragment {

    private FragmentDetalleInmuebleBinding binding;
    private Inmueble inmueble;
    private DetalleInmuebleViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(DetalleInmuebleViewModel.class);

        viewModel.getMensajeToast().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        });

        viewModel.getDisponibilidadActualizada().observe(getViewLifecycleOwner(), estado -> {
            inmueble.setDisponible(estado);
            binding.cbDetalleDisponible.setChecked(estado);
        });

        if (getArguments() != null) {
            inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                cargarDatos();
            }
        }

        binding.cbDetalleDisponible.setOnClickListener(v -> {
            if (inmueble != null) {
                viewModel.actualizarDisponibilidad(inmueble, binding.cbDetalleDisponible.isChecked());
            }
        });

        return binding.getRoot();
    }

    private void cargarDatos() {
        binding.etDetalleCodigo.setText(String.valueOf(inmueble.getIdInmueble()));
        binding.etDetalleDireccion.setText(inmueble.getDireccion());
        binding.etDetalleUso.setText(inmueble.getUso());
        binding.etDetalleTipo.setText(inmueble.getTipo());
        binding.etDetalleAmbientes.setText(String.valueOf(inmueble.getAmbientes()));
        binding.etDetallePrecio.setText(String.valueOf(inmueble.getValor()));
        binding.cbDetalleDisponible.setChecked(inmueble.isDisponible());

        Glide.with(requireContext())
                .load("https://capacitacion.alwaysdata.net/" + inmueble.getImagen())
                .into(binding.ivDetalleInmueble);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
