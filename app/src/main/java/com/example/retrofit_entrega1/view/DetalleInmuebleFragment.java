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
import com.bumptech.glide.Glide;
import com.example.retrofit_entrega1.api.ApiClient;
import com.example.retrofit_entrega1.R;
import com.example.retrofit_entrega1.model.Inmueble;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInmuebleFragment extends Fragment {
    private EditText etCodigo, etDireccion, etUso, etTipo, etAmbientes, etPrecio;
    private CheckBox cbDisponible;
    private ImageView ivInmueble;
    private Inmueble inmueble;

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

        if (getArguments() != null) {
            inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                cargarDatos();
            }
        }

        cbDisponible.setOnClickListener(v -> {
            if (inmueble != null) {
                actualizarDisponibilidad();
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
        etPrecio.setText("$" + inmueble.getValor());
        cbDisponible.setChecked(inmueble.isDisponible());

        Glide.with(requireContext())
                .load("https://capacitacion.alwaysdata.net/" + inmueble.getImagen())
                .into(ivInmueble);
    }

    private void actualizarDisponibilidad() {
        SharedPreferences sp = requireActivity().getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        
        final boolean nuevoEstado = cbDisponible.isChecked();
        inmueble.setDisponible(nuevoEstado);
        Inmueble update = crearInmuebleLimpio(inmueble, nuevoEstado);

        ApiClient.getApi().actualizarInmueble(token, update).enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (isAdded() && getContext() != null) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Disponibilidad actualizada", Toast.LENGTH_SHORT).show();
                    } else {
                        inmueble.setDisponible(!nuevoEstado);
                        cbDisponible.setChecked(!nuevoEstado);
                        Toast.makeText(getContext(), "Error " + response.code() + " al guardar cambios", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                if (isAdded() && getContext() != null) {
                    inmueble.setDisponible(!nuevoEstado);
                    cbDisponible.setChecked(!nuevoEstado);
                    Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Inmueble crearInmuebleLimpio(Inmueble inmueble, boolean disponible) {
        Inmueble update = new Inmueble();
        update.setIdInmueble(inmueble.getIdInmueble());
        update.setDireccion(inmueble.getDireccion());
        update.setUso(inmueble.getUso());
        update.setTipo(inmueble.getTipo());
        update.setAmbientes(inmueble.getAmbientes());
        update.setSuperficie(inmueble.getSuperficie());
        update.setLatitud(inmueble.getLatitud());
        update.setLongitud(inmueble.getLongitud());
        update.setValor(inmueble.getValor());
        update.setImagen(inmueble.getImagen());
        update.setDisponible(disponible);
        update.setIdPropietario(0);
        return update;
    }
}
