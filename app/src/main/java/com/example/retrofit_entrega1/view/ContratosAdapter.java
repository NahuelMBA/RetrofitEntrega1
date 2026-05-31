package com.example.retrofit_entrega1.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofit_entrega1.R;
import com.example.retrofit_entrega1.model.Contrato;

import java.util.List;

public class ContratosAdapter extends RecyclerView.Adapter<ContratosAdapter.ViewHolder> {
    private List<Contrato> lista;
    private OnVerPagosListener listener;

    public interface OnVerPagosListener {
        void onVerPagos(Contrato contrato);
    }

    public ContratosAdapter(List<Contrato> lista, OnVerPagosListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contrato, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contrato contrato = lista.get(position);

        if (contrato.getInmueble() != null) {
            holder.tvDireccion.setText(contrato.getInmueble().getDireccion());
        }
        holder.tvFechas.setText(contrato.getFechaInicio() + " - " + contrato.getFechaFinalizacion());
        holder.tvMonto.setText("$" + contrato.getMontoAlquiler());

        if (contrato.getInquilino() != null) {
            holder.tvInquilino.setText("Inquilino: " + contrato.getInquilino().getNombre() + " " + contrato.getInquilino().getApellido());
        }

        holder.btnVerPagos.setOnClickListener(v -> listener.onVerPagos(contrato));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDireccion, tvFechas, tvMonto, tvInquilino;
        Button btnVerPagos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDireccion = itemView.findViewById(R.id.tvContratoDireccion);
            tvFechas = itemView.findViewById(R.id.tvContratoFechas);
            tvMonto = itemView.findViewById(R.id.tvContratoMonto);
            tvInquilino = itemView.findViewById(R.id.tvContratoInquilino);
            btnVerPagos = itemView.findViewById(R.id.btnVerPagos);
        }
    }
}
