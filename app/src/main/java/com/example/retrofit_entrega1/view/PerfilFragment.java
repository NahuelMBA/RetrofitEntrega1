package com.example.retrofit_entrega1.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.retrofit_entrega1.databinding.FragmentPerfilBinding;
import com.example.retrofit_entrega1.model.Propietario;
import com.example.retrofit_entrega1.viewmodel.PerfilViewModel;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private boolean enModoEdicion = false;
    private Propietario propietarioActual;
    private PerfilViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        viewModel.getPerfil().observe(getViewLifecycleOwner(), propietario -> {
            propietarioActual = propietario;
            binding.etCodigo.setText(String.valueOf(propietario.getIdPropietario()));
            binding.etDni.setText(propietario.getDni());
            binding.etNombre.setText(propietario.getNombre());
            binding.etApellido.setText(propietario.getApellido());
            binding.etEmail.setText(propietario.getEmail());
            binding.etTelefono.setText(propietario.getTelefono());
            habilitar(false);
        });

        viewModel.getMensajeToast().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        });

        viewModel.obtenerPerfil();

        binding.btnEditarGuardar.setOnClickListener(v -> {
            if (!enModoEdicion) {
                habilitar(true);
            } else {
                guardarDatos();
            }
        });

        binding.btnCambiarClave.setOnClickListener(v -> mostrarDialogoCambioClave());

        return binding.getRoot();
    }

    private void habilitar(boolean habilitar) {
        enModoEdicion = habilitar;
        binding.etDni.setEnabled(habilitar);
        binding.etNombre.setEnabled(habilitar);
        binding.etApellido.setEnabled(habilitar);
        binding.etEmail.setEnabled(habilitar);
        binding.etTelefono.setEnabled(habilitar);
        binding.btnEditarGuardar.setText(habilitar ? "GUARDAR" : "EDITAR MIS DATOS");
    }

    private void guardarDatos() {
        propietarioActual.setDni(binding.etDni.getText().toString());
        propietarioActual.setNombre(binding.etNombre.getText().toString());
        propietarioActual.setApellido(binding.etApellido.getText().toString());
        propietarioActual.setEmail(binding.etEmail.getText().toString());
        propietarioActual.setTelefono(binding.etTelefono.getText().toString());
        viewModel.actualizarPerfil(propietarioActual);
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
            viewModel.cambiarPassword(actual, nueva);
        });
        builder.setNegativeButton("CANCELAR", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
