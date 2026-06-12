package com.example.retrofit_entrega1.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.retrofit_entrega1.databinding.FragmentLogoutBinding;
import com.example.retrofit_entrega1.viewmodel.LogoutViewModel;

public class LogoutFragment extends Fragment {

    private FragmentLogoutBinding binding;
    private LogoutViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLogoutBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(LogoutViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mostrarDialogo();
    }

    private void mostrarDialogo() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Cierre de sesión")
                .setMessage("¿Está seguro de que desea cerrar la sesión?")
                .setCancelable(false)
                .setPositiveButton("ACEPTAR", (dialog, which) -> {
                    viewModel.cerrarSesion();
                    Intent intent = new Intent(requireActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    requireActivity().finish();
                })
                .setNegativeButton("CANCELAR", (dialog, which) -> {
                    dialog.dismiss();
                    Navigation.findNavController(requireView()).navigateUp();
                })
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
