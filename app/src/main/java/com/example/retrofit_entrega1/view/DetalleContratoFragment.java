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

import com.example.retrofit_entrega1.R;
import com.example.retrofit_entrega1.databinding.FragmentDetalleContratoBinding;
import com.example.retrofit_entrega1.model.Inmueble;
import com.example.retrofit_entrega1.viewmodel.ContratosViewModel;

public class DetalleContratoFragment extends Fragment {

    private FragmentDetalleContratoBinding binding;
    private ContratosViewModel viewModel;
    private int idContrato;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleContratoBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(ContratosViewModel.class);

        viewModel.getContrato().observe(getViewLifecycleOwner(), contrato -> {
            idContrato = contrato.getIdContrato();
            binding.tvContCodigo.setText("Código contrato\n" + contrato.getIdContrato());
            binding.tvContFechaInicio.setText("Fecha inicio\n" + contrato.getFechaInicio());
            binding.tvContFechaFin.setText("Fecha finalización\n" + contrato.getFechaFinalizacion());
            binding.tvContMonto.setText("Monto de alquiler\n$" + contrato.getMontoAlquiler());

            if (contrato.getInquilino() != null) {
                binding.tvContInquilino.setText("Inquilino\n" + contrato.getInquilino().getNombre() + " " + contrato.getInquilino().getApellido());
            }
            if (contrato.getInmueble() != null) {
                binding.tvContInmueble.setText("Inmueble\nInmueble en " + contrato.getInmueble().getDireccion());
            }
        });

        viewModel.getMensajeToast().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        });

        binding.btnVerPagos.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("idContrato", idContrato);
            Navigation.findNavController(v).navigate(R.id.pagosFragment, bundle);
        });

        if (getArguments() != null) {
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                viewModel.cargarContratoPorInmueble(inmueble.getIdInmueble());
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
