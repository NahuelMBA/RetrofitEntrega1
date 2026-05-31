package com.example.retrofit_entrega1.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofit_entrega1.R;
import com.example.retrofit_entrega1.model.Pago;

import java.util.List;

public class PagosAdapter extends RecyclerView.Adapter<PagosAdapter.ViewHolder> {
    private List<Pago> lista;

    public PagosAdapter(List<Pago> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pago, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pago pago = lista.get(position);
        holder.tvCodigo.setText("Código pago: " + pago.getIdPago());
        holder.tvNumero.setText("Detalle: " + pago.getDetalle());
        holder.tvContrato.setText("Código de contrato: " + pago.getIdContrato());
        holder.tvImporte.setText("Importe: $" + pago.getMonto());
        holder.tvFecha.setText("Fecha de pago: " + pago.getFechaPago());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCodigo, tvNumero, tvContrato, tvImporte, tvFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigo = itemView.findViewById(R.id.tvPagoCodigo);
            tvNumero = itemView.findViewById(R.id.tvPagoNumero);
            tvContrato = itemView.findViewById(R.id.tvPagoContrato);
            tvImporte = itemView.findViewById(R.id.tvPagoImporte);
            tvFecha = itemView.findViewById(R.id.tvPagoFecha);
        }
    }
}
