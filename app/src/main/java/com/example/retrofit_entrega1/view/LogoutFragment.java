package com.example.retrofit_entrega1.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.retrofit_entrega1.R;

public class LogoutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logout, container, false);
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
                    SharedPreferences sp = requireActivity().getSharedPreferences("token.xml", Context.MODE_PRIVATE);
                    sp.edit().clear().apply();
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
}
