package com.example.tesis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private List<ClienteResponse> listaUsuarios;
    private OnAdminClickListener listener;

    // 1. Creamos la interfaz (el "telefono")
    public interface OnAdminClickListener {
        void onCambiarRolClick(ClienteResponse usuario, View boton);
        void onEliminarClick(ClienteResponse usuario);
        void onReactivarClick(ClienteResponse usuario);
    }

    // 2. Actualizamos el constructor para recibir el listener
    public UsuarioAdapter(List<ClienteResponse> listaUsuarios, OnAdminClickListener listener) {
        this.listaUsuarios = listaUsuarios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        ClienteResponse usuario = listaUsuarios.get(position);

        holder.tvNombreUsuario.setText(usuario.getNombre() + " " + usuario.getApellido());
        holder.tvEmailUsuario.setText(usuario.getMail());
        holder.btnCambiarRol.setText("Rol: " + usuario.getRol() + " ▼");

        // 1. El clic para cambiar de rol siempre es el mismo
        holder.btnCambiarRol.setOnClickListener(v -> listener.onCambiarRolClick(usuario, v));

        // 2. Logica dinamica para activar y desactivar
        if (!usuario.isActivo()) {
            // Diseño Inactivo (Rojo y Verde)
            holder.tvNombreUsuario.setTextColor(0xFFFF5252);
            holder.btnEliminar.setText("♻️");
            holder.btnEliminar.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF4CAF50));

            // Reactivar
            holder.btnEliminar.setOnClickListener(v -> listener.onReactivarClick(usuario));

        } else {
            // Diseño Activo (Blanco y Rojo)
            holder.tvNombreUsuario.setTextColor(0xFFFFFFFF);
            holder.btnEliminar.setText("🗑️");
            holder.btnEliminar.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFFF5252));

            // Desactivar
            holder.btnEliminar.setOnClickListener(v -> listener.onEliminarClick(usuario));
        }
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreUsuario, tvEmailUsuario;
        Button btnCambiarRol, btnEliminar;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreUsuario = itemView.findViewById(R.id.tvNombreUsuario);
            tvEmailUsuario = itemView.findViewById(R.id.tvEmailUsuario);
            btnCambiarRol = itemView.findViewById(R.id.btnCambiarRol);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}