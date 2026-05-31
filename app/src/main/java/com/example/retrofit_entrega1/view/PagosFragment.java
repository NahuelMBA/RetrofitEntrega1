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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofit_entrega1.R;
import com.example.retrofit_entrega1.viewmodel.ContratosViewModel;

public class PagosFragment extends Fragment {

    private RecyclerView rvPagos;
    private ContratosViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pagos, container, false);

        rvPagos = view.findViewById(R.id.rvPagos);
        rvPagos.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(this).get(ContratosViewModel.class);

        int idContrato = 0;
        if (getArguments() != null) {
            idContrato = getArguments().getInt("idContrato", 0);
        }

        viewModel.getPagos().observe(getViewLifecycleOwner(), listaPagos -> {
            PagosAdapter adapter = new PagosAdapter(listaPagos);
            rvPagos.setAdapter(adapter);
        });

        viewModel.getMensajeToast().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        });

        SharedPreferences sp = requireActivity().getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        viewModel.cargarPagos(token, idContrato);

        return view;
    }
}
