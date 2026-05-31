package com.example.retrofit_entrega1.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.retrofit_entrega1.R;
import com.example.retrofit_entrega1.model.Inmueble;
import com.example.retrofit_entrega1.viewmodel.DetalleInmuebleViewModel;

public class DetalleInmuebleFragment extends Fragment {
    private EditText etCodigo, etDireccion, etUso, etTipo, etAmbientes, etPrecio;
    private CheckBox cbDisponible;
    private ImageView ivInmueble;
    private Inmueble inmueble;
    private DetalleInmuebleViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_inmueble, container, false);
        etCodigo = view.findViewById(R.id.etDetalleCodigo);
        etDireccion = view.findViewById(R.id.etDetalleDireccion);
        etUso = view.findViewById(R.id.etDetalleUso);
        etTipo = view.findViewById(R.id.etDetalleTipo);
        etAmbientes = view.findViewById(R.id.etDetalleAmbientes);
        etPrecio = view.findViewById(R.id.etDetallePrecio);
        cbDisponible = view.findViewById(R.id.cbDetalleDisponible);
        ivInmueble = view.findViewById(R.id.ivDetalleInmueble);

        viewModel = new ViewModelProvider(this).get(DetalleInmuebleViewModel.class);

        viewModel.getMensajeToast().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        });

        viewModel.getDisponibilidadActualizada().observe(getViewLifecycleOwner(), estado -> {
            inmueble.setDisponible(estado);
            cbDisponible.setChecked(estado);
        });

        if (getArguments() != null) {
            inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                cargarDatos();
            }
        }

        cbDisponible.setOnClickListener(v -> {
            if (inmueble != null) {
                SharedPreferences sp = requireActivity().getSharedPreferences("token.xml", Context.MODE_PRIVATE);
                String token = sp.getString("token", "");
                viewModel.actualizarDisponibilidad(token, inmueble, cbDisponible.isChecked());
            }
        });

        return view;
    }

    private void cargarDatos() {
        etCodigo.setText(String.valueOf(inmueble.getIdInmueble()));
        etDireccion.setText(inmueble.getDireccion());
        etUso.setText(inmueble.getUso());
        etTipo.setText(inmueble.getTipo());
        etAmbientes.setText(String.valueOf(inmueble.getAmbientes()));
        etPrecio.setText(String.valueOf(inmueble.getValor()));
        cbDisponible.setChecked(inmueble.isDisponible());

        Glide.with(requireContext())
                .load("https://capacitacion.alwaysdata.net/" + inmueble.getImagen())
                .into(ivInmueble);
    }
}
