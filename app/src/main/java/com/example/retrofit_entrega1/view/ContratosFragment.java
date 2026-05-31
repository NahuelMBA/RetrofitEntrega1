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

public class ContratosFragment extends Fragment {

    private RecyclerView rvAlquilados;
    private ContratosViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contratos, container, false);

        rvAlquilados = view.findViewById(R.id.rvContratos);
        rvAlquilados.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(this).get(ContratosViewModel.class);

        viewModel.getAlquilados().observe(getViewLifecycleOwner(), listaAlquilados -> {
            AlquiladosAdapter adapter = new AlquiladosAdapter(listaAlquilados, getContext(), R.id.detalleContratoFragment);
            rvAlquilados.setAdapter(adapter);
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
        viewModel.cargarAlquilados(token);
    }
}
