package com.example.retrofit_entrega1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilFragment extends Fragment {

    private EditText etCodigo, etDni, etNombre, etApellido, etEmail, etTelefono;
    private Button btnEditarGuardar, btnCambiarClave;
    private boolean enModoEdicion = false;
    private Propietario propietarioActual;
    private String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        etCodigo = view.findViewById(R.id.etCodigo);
        etDni = view.findViewById(R.id.etDni);
        etNombre = view.findViewById(R.id.etNombre);
        etApellido = view.findViewById(R.id.etApellido);
        etEmail = view.findViewById(R.id.etEmail);
        etTelefono = view.findViewById(R.id.etTelefono);
        btnEditarGuardar = view.findViewById(R.id.btnEditarGuardar);
        btnCambiarClave = view.findViewById(R.id.btnCambiarClave);

        SharedPreferences sp = requireActivity().getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        token = sp.getString("token", "");

        obtenerDatos();

        btnEditarGuardar.setOnClickListener(v -> {
            if (!enModoEdicion) {
                habilitar(true);
            } else {
                guardarDatos();
            }
        });

        btnCambiarClave.setOnClickListener(v -> mostrarDialogoCambioClave());

        return view;
    }

    private void obtenerDatos() {
        ApiClient.getApi().obtenerPerfil(token).enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    propietarioActual = response.body();
                    etCodigo.setText(String.valueOf(propietarioActual.getIdPropietario()));
                    etDni.setText(propietarioActual.getDni());
                    etNombre.setText(propietarioActual.getNombre());
                    etApellido.setText(propietarioActual.getApellido());
                    etEmail.setText(propietarioActual.getEmail());
                    etTelefono.setText(propietarioActual.getTelefono());
                }
            }
            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {}
        });
    }

    private void habilitar(boolean habilitar) {
        enModoEdicion = habilitar;
        etDni.setEnabled(habilitar);
        etNombre.setEnabled(habilitar);
        etApellido.setEnabled(habilitar);
        etEmail.setEnabled(habilitar);
        etTelefono.setEnabled(habilitar);
        btnEditarGuardar.setText(habilitar ? "GUARDAR" : "EDITAR MIS DATOS");
    }

    private void guardarDatos() {
        propietarioActual.setDni(etDni.getText().toString());
        propietarioActual.setNombre(etNombre.getText().toString());
        propietarioActual.setApellido(etApellido.getText().toString());
        propietarioActual.setEmail(etEmail.getText().toString());
        propietarioActual.setTelefono(etTelefono.getText().toString());
        
        int idOriginal = propietarioActual.getIdPropietario();
        propietarioActual.setIdPropietario(0);

        ApiClient.getApi().actualizarPerfil(token, propietarioActual).enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
                    if (response.body() != null) {
                        propietarioActual = response.body();
                        etCodigo.setText(String.valueOf(propietarioActual.getIdPropietario()));
                    } else {
                        propietarioActual.setIdPropietario(idOriginal);
                    }
                    habilitar(false);
                }
            }
            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                propietarioActual.setIdPropietario(idOriginal);
                Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoCambioClave() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Cambiar Contraseña");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etActual = new EditText(getContext());
        etActual.setHint("Contraseña Actual");
        layout.addView(etActual);

        final EditText etNueva = new EditText(getContext());
        etNueva.setHint("Nueva Contraseña");
        layout.addView(etNueva);

        builder.setView(layout);

        builder.setPositiveButton("CAMBIAR", (dialog, which) -> {
            String actual = etActual.getText().toString();
            String nueva = etNueva.getText().toString();
            ApiClient.getApi().cambiarPassword(token, actual, nueva).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Toast.makeText(getContext(), response.isSuccessful() ? "Contraseña cambiada" : "Error al cambiar", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {}
            });
        });
        builder.setNegativeButton("CANCELAR", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}