package com.example.retrofit_entrega1.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.retrofit_entrega1.R;
import com.example.retrofit_entrega1.model.Inmueble;
import com.example.retrofit_entrega1.viewmodel.ContratosViewModel;

public class DetalleContratoFragment extends Fragment {

    private TextView tvCodigo, tvFechaInicio, tvFechaFin, tvMonto, tvInquilino, tvInmueble;
    private Button btnPagos;
    private ContratosViewModel viewModel;
    private int idContrato;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_contrato, container, false);

        tvCodigo = view.findViewById(R.id.tvContCodigo);
        tvFechaInicio = view.findViewById(R.id.tvContFechaInicio);
        tvFechaFin = view.findViewById(R.id.tvContFechaFin);
        tvMonto = view.findViewById(R.id.tvContMonto);
        tvInquilino = view.findViewById(R.id.tvContInquilino);
        tvInmueble = view.findViewById(R.id.tvContInmueble);
        btnPagos = view.findViewById(R.id.btnVerPagos);

        viewModel = new ViewModelProvider(this).get(ContratosViewModel.class);

        viewModel.getContrato().observe(getViewLifecycleOwner(), contrato -> {
            idContrato = contrato.getIdContrato();
            tvCodigo.setText("Código contrato\n" + contrato.getIdContrato());
            tvFechaInicio.setText("Fecha inicio\n" + contrato.getFechaInicio());
            tvFechaFin.setText("Fecha finalización\n" + contrato.getFechaFinalizacion());
            tvMonto.setText("Monto de alquiler\n$" + contrato.getMontoAlquiler());

            if (contrato.getInquilino() != null) {
                tvInquilino.setText("Inquilino\n" + contrato.getInquilino().getNombre() + " " + contrato.getInquilino().getApellido());
            }
            if (contrato.getInmueble() != null) {
                tvInmueble.setText("Inmueble\nInmueble en " + contrato.getInmueble().getDireccion());
            }
        });

        viewModel.getMensajeToast().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        });

        btnPagos.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("idContrato", idContrato);
            Navigation.findNavController(v).navigate(R.id.pagosFragment, bundle);
        });

        if (getArguments() != null) {
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                SharedPreferences sp = requireActivity().getSharedPreferences("token.xml", Context.MODE_PRIVATE);
                String token = sp.getString("token", "");
                viewModel.cargarContratoPorInmueble(token, inmueble.getIdInmueble());
            }
        }

        return view;
    }
}
