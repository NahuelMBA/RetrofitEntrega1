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

import com.example.retrofit_entrega1.databinding.FragmentPagosBinding;
import com.example.retrofit_entrega1.viewmodel.ContratosViewModel;

public class PagosFragment extends Fragment {

    private FragmentPagosBinding binding;
    private ContratosViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPagosBinding.inflate(inflater, container, false);

        binding.rvPagos.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(this).get(ContratosViewModel.class);

        int idContrato = 0;
        if (getArguments() != null) {
            idContrato = getArguments().getInt("idContrato", 0);
        }

        viewModel.getPagos().observe(getViewLifecycleOwner(), listaPagos -> {
            PagosAdapter adapter = new PagosAdapter(listaPagos);
            binding.rvPagos.setAdapter(adapter);
        });

        viewModel.getMensajeToast().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        });

        viewModel.cargarPagos(idContrato);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
