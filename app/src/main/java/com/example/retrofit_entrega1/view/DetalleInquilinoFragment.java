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

import com.example.retrofit_entrega1.databinding.FragmentDetalleInquilinoBinding;
import com.example.retrofit_entrega1.model.Inmueble;
import com.example.retrofit_entrega1.viewmodel.InquilinosViewModel;

public class DetalleInquilinoFragment extends Fragment {

    private FragmentDetalleInquilinoBinding binding;
    private InquilinosViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleInquilinoBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(InquilinosViewModel.class);

        viewModel.getInquilino().observe(getViewLifecycleOwner(), inquilino -> {
            binding.tvInqCodigo.setText("Código\n" + inquilino.getIdInquilino());
            binding.tvInqNombre.setText("Nombre\n" + inquilino.getNombre());
            binding.tvInqApellido.setText("Apellido\n" + inquilino.getApellido());
            binding.tvInqDni.setText("DNI\n" + inquilino.getDni());
            binding.tvInqDireccion.setText("Dirección\n" + inquilino.getDireccion());
            binding.tvInqTelefono.setText("Teléfono\n" + inquilino.getTelefono());
            binding.tvInqEmail.setText("E-mail\n" + inquilino.getEmail());
            binding.tvInqGarante.setText("Garante\n" + inquilino.getNombreGarante());
            binding.tvInqTelGarante.setText("Teléfono garante\n" + inquilino.getTelefonoGarante());
        });

        viewModel.getMensajeToast().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        });

        if (getArguments() != null) {
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                viewModel.cargarInquilinoPorInmueble(inmueble.getIdInmueble());
            }
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
