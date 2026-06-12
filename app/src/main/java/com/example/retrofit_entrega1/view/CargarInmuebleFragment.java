package com.example.retrofit_entrega1.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.navigation.Navigation;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.retrofit_entrega1.databinding.FragmentCargarInmuebleBinding;
import com.example.retrofit_entrega1.viewmodel.CargarInmuebleViewModel;

public class CargarInmuebleFragment extends Fragment {
    private FragmentCargarInmuebleBinding binding;
    private Uri uriImagen;
    private CargarInmuebleViewModel viewModel;

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    uriImagen = result.getData().getData();
                    binding.ivCargarFoto.setImageURI(uriImagen);
                    binding.ivCargarFoto.setVisibility(View.VISIBLE);
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCargarInmuebleBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(CargarInmuebleViewModel.class);

        viewModel.getMensajeToast().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        });

        viewModel.getCargaExitosa().observe(getViewLifecycleOwner(), exitosa -> {
            if (exitosa) {
                Navigation.findNavController(requireView()).navigateUp();
            }
        });

        binding.btnSeleccionarFoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryLauncher.launch(intent);
        });

        binding.btnCargarInmueble.setOnClickListener(v -> cargarInmueble());
        return binding.getRoot();
    }

    private void cargarInmueble() {
        viewModel.cargarInmueble(
                binding.etCargarDireccion.getText().toString(),
                binding.etCargarUso.getText().toString(),
                binding.etCargarTipo.getText().toString(),
                binding.etCargarAmbientes.getText().toString(),
                binding.etCargarSuperficie.getText().toString(),
                binding.etCargarPrecio.getText().toString(),
                uriImagen
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
