package com.example.retrofit_entrega1.view;

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
import androidx.lifecycle.ViewModelProvider;

import com.example.retrofit_entrega1.R;
import com.example.retrofit_entrega1.model.Propietario;
import com.example.retrofit_entrega1.viewmodel.PerfilViewModel;

public class PerfilFragment extends Fragment {

    private EditText etCodigo, etDni, etNombre, etApellido, etEmail, etTelefono;
    private Button btnEditarGuardar, btnCambiarClave;
    private boolean enModoEdicion = false;
    private Propietario propietarioActual;
    private String token;
    private PerfilViewModel viewModel;

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

        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        viewModel.getPerfil().observe(getViewLifecycleOwner(), propietario -> {
            propietarioActual = propietario;
            etCodigo.setText(String.valueOf(propietario.getIdPropietario()));
            etDni.setText(propietario.getDni());
            etNombre.setText(propietario.getNombre());
            etApellido.setText(propietario.getApellido());
            etEmail.setText(propietario.getEmail());
            etTelefono.setText(propietario.getTelefono());
            habilitar(false);
        });

        viewModel.getMensajeToast().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        });

        viewModel.obtenerPerfil(token);

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
        viewModel.actualizarPerfil(token, propietarioActual);
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
            viewModel.cambiarPassword(token, actual, nueva);
        });
        builder.setNegativeButton("CANCELAR", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
