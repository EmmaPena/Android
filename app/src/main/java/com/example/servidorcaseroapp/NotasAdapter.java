package com.example.servidorcaseroapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NotasAdapter extends RecyclerView.Adapter<NotasAdapter.NotaViewHolder> {

    private List<Nota> listaNotas = new ArrayList<>();

    public void setNotas(List<Nota> nuevasNotas) {
        this.listaNotas = nuevasNotas;
        notifyDataSetChanged();
    }

    public Nota getNotaEn(int position) {
        return listaNotas.get(position);
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nota, parent, false);
        return new NotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        Nota nota = listaNotas.get(position);
        holder.tvTexto.setText(nota.getTexto());
    }

    @Override
    public int getItemCount() {
        return listaNotas.size();
    }

    static class NotaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTexto;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTexto = itemView.findViewById(R.id.tvTextoNota);
        }
    }
}