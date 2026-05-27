package com.example.retrofit_entrega1.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.retrofit_entrega1.R;
import com.example.retrofit_entrega1.model.Inmueble;

import java.util.List;

public class InmueblesAdapter extends RecyclerView.Adapter<InmueblesAdapter.ViewHolder> {
    private List<Inmueble> lista;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDisponibleChanged(Inmueble inmueble);
    }

    public InmueblesAdapter(List<Inmueble> lista, Context context, OnItemClickListener listener) {
        this.lista = lista;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inmueble, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Inmueble inmueble = lista.get(position);
        holder.tvDireccion.setText(inmueble.getDireccion());
        holder.tvPrecio.setText("$" + inmueble.getValor());
        holder.cbDisponible.setChecked(inmueble.isDisponible());

        Glide.with(context)
                .load("https://capacitacion.alwaysdata.net/" + inmueble.getImagen())
                .into(holder.ivInmueble);

        holder.cbDisponible.setOnClickListener(v -> {
            inmueble.setDisponible(holder.cbDisponible.isChecked());
            listener.onDisponibleChanged(inmueble);
        });

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("inmueble", inmueble);
            Navigation.findNavController(v).navigate(R.id.detalleInmuebleFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDireccion, tvPrecio;
        ImageView ivInmueble;
        CheckBox cbDisponible;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            ivInmueble = itemView.findViewById(R.id.ivInmueble);
            cbDisponible = itemView.findViewById(R.id.cbDisponible);
        }
    }
}