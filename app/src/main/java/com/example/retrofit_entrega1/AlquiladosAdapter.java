package com.example.retrofit_entrega1;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class AlquiladosAdapter extends RecyclerView.Adapter<AlquiladosAdapter.ViewHolder> {
    private List<Inmueble> lista;
    private Context context;
    private int destino;

    public AlquiladosAdapter(List<Inmueble> lista, Context context, int destino) {
        this.lista = lista;
        this.context = context;
        this.destino = destino;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alquilado, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Inmueble inmueble = lista.get(position);
        holder.tvDireccion.setText(inmueble.getDireccion());
        
        Glide.with(context)
                .load("https://capacitacion.alwaysdata.net/" + inmueble.getImagen())
                .into(holder.ivInmueble);

        holder.btnVer.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("inmueble", inmueble);
            Navigation.findNavController(v).navigate(destino, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDireccion;
        ImageView ivInmueble;
        Button btnVer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDireccion = itemView.findViewById(R.id.tvAlquiladoDireccion);
            ivInmueble = itemView.findViewById(R.id.ivAlquilado);
            btnVer = itemView.findViewById(R.id.btnVerAlquilado);
        }
    }
}