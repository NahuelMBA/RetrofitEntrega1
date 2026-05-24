package com.example.retrofit_entrega1;

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

public class LogoutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mostrarDialogo();
        return inflater.inflate(R.layout.fragment_logout, container, false);
    }

    private void mostrarDialogo() {
        new AlertDialog.Builder(getContext())
                .setTitle("Cierre de sesión")
                .setMessage("¿Está seguro de que desea cerrar la sesión?")
                .setPositiveButton("ACEPTAR", (dialog, which) -> {
                    SharedPreferences sp = requireActivity().getSharedPreferences("token.xml", Context.MODE_PRIVATE);
                    sp.edit().clear().apply();
                    Intent intent = new Intent(requireActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    requireActivity().finish();
                })
                .setNegativeButton("CANCELAR", (dialog, which) -> {
                    requireActivity().onBackPressed();
                })
                .show();
    }
}