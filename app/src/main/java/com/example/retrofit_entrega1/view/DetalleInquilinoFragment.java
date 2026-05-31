package com.example.retrofit_entrega1.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.retrofit_entrega1.R;
import com.example.retrofit_entrega1.model.Inmueble;
import com.example.retrofit_entrega1.viewmodel.InquilinosViewModel;

public class DetalleInquilinoFragment extends Fragment {

    private TextView tvCodigo, tvNombre, tvApellido, tvDni, tvDireccion, tvTelefono, tvEmail, tvGarante, tvTelGarante;
    private InquilinosViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_inquilino, container, false);

        tvCodigo = view.findViewById(R.id.tvInqCodigo);
        tvNombre = view.findViewById(R.id.tvInqNombre);
        tvApellido = view.findViewById(R.id.tvInqApellido);
        tvDni = view.findViewById(R.id.tvInqDni);
        tvDireccion = view.findViewById(R.id.tvInqDireccion);
        tvTelefono = view.findViewById(R.id.tvInqTelefono);
        tvEmail = view.findViewById(R.id.tvInqEmail);
        tvGarante = view.findViewById(R.id.tvInqGarante);
        tvTelGarante = view.findViewById(R.id.tvInqTelGarante);

        viewModel = new ViewModelProvider(this).get(InquilinosViewModel.class);

        viewModel.getInquilino().observe(getViewLifecycleOwner(), inquilino -> {
            tvCodigo.setText("Código\n" + inquilino.getIdInquilino());
            tvNombre.setText("Nombre\n" + inquilino.getNombre());
            tvApellido.setText("Apellido\n" + inquilino.getApellido());
            tvDni.setText("DNI\n" + inquilino.getDni());
            tvDireccion.setText("Dirección\n" + inquilino.getDireccion());
            tvTelefono.setText("Teléfono\n" + inquilino.getTelefono());
            tvEmail.setText("E-mail\n" + inquilino.getEmail());
            tvGarante.setText("Garante\n" + inquilino.getNombreGarante());
            tvTelGarante.setText("Teléfono garante\n" + inquilino.getTelefonoGarante());
        });

        viewModel.getMensajeToast().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        });

        if (getArguments() != null) {
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                SharedPreferences sp = requireActivity().getSharedPreferences("token.xml", Context.MODE_PRIVATE);
                String token = sp.getString("token", "");
                viewModel.cargarInquilinoPorInmueble(token, inmueble.getIdInmueble());
            }
        }

        return view;
    }
}
