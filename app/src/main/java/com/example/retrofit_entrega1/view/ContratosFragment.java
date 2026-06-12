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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.retrofit_entrega1.R;
import com.example.retrofit_entrega1.databinding.FragmentContratosBinding;
import com.example.retrofit_entrega1.viewmodel.ContratosViewModel;

public class ContratosFragment extends Fragment {

    private FragmentContratosBinding binding;
    private ContratosViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentContratosBinding.inflate(inflater, container, false);

        binding.rvContratos.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(this).get(ContratosViewModel.class);

        viewModel.getAlquilados().observe(getViewLifecycleOwner(), listaAlquilados -> {
            AlquiladosAdapter adapter = new AlquiladosAdapter(listaAlquilados, getContext(), R.id.detalleContratoFragment);
            binding.rvContratos.setAdapter(adapter);
        });

        viewModel.getMensajeToast().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.cargarAlquilados();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
