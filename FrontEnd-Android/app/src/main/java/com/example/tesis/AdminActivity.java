package com.example.tesis;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView rvUsuarios;
    private UsuarioAdapter adapter;
    private GymApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        rvUsuarios = findViewById(R.id.rvUsuarios);
        rvUsuarios.setLayoutManager(new LinearLayoutManager(this));

        apiService = RetrofitClient.getClient().create(GymApiService.class);

        cargarUsuariosDesdeBackend();

        ImageButton btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish()); // finish() destruye la pantalla actual y vuelve a la anterior
    }

    private void cargarUsuariosDesdeBackend() {
        Call<List<ClienteResponse>> call = apiService.obtenerUsuarios();
        call.enqueue(new Callback<List<ClienteResponse>>() {
            @Override
            public void onResponse(Call<List<ClienteResponse>> call, Response<List<ClienteResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Inicializamos el adaptador con el Listener
                    adapter = new UsuarioAdapter(response.body(), new UsuarioAdapter.OnAdminClickListener() {
                        @Override
                        public void onCambiarRolClick(ClienteResponse usuario, View boton) {
                            mostrarMenuRoles(usuario, boton);
                        }

                        @Override
                        public void onEliminarClick(ClienteResponse usuario) {
                            mostrarDialogoEliminar(usuario);
                        }

                        @Override
                        public void onReactivarClick(ClienteResponse usuario) {
                            mostrarDialogoReactivar(usuario);
                        }
                    });
                    rvUsuarios.setAdapter(adapter);
                } else {
                Toast.makeText(AdminActivity.this, "Error HTTP: " + response.code(), Toast.LENGTH_LONG).show();
            }
            }
            @Override
            public void onFailure(Call<List<ClienteResponse>> call, Throwable t) {}
        });
    }

    // logica de cambio de rol
    private void mostrarMenuRoles(ClienteResponse usuario, View boton) {
        PopupMenu popup = new PopupMenu(this, boton);
        popup.getMenu().add("CLIENTE");
        popup.getMenu().add("ENTRENADOR");
        popup.getMenu().add("ADMIN");

        popup.setOnMenuItemClickListener(item -> {
            String nuevoRol = item.getTitle().toString();
            ejecutarCambioRol(usuario.getIdUsuario(), nuevoRol);
            return true;
        });
        popup.show();
    }

    private void ejecutarCambioRol(Integer idUsuario, String nuevoRol) {
        Call<Void> call = apiService.cambiarRol(idUsuario, new CambioRolRequest(nuevoRol));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AdminActivity.this, "Rol actualizado a " + nuevoRol, Toast.LENGTH_SHORT).show();
                    cargarUsuariosDesdeBackend(); // Recargamos la lista para ver el cambio
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }

    // logica de eliminacion logica
    private void mostrarDialogoEliminar(ClienteResponse usuario) {
        new AlertDialog.Builder(this)
                .setTitle("Desactivar Cuenta")
                .setMessage("¿Estás seguro que deseas desactivar a " + usuario.getNombre() + "? No podrá iniciar sesión.")
                .setPositiveButton("Desactivar", (dialog, which) -> ejecutarDesactivacion(usuario.getIdUsuario()))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void ejecutarDesactivacion(Integer idUsuario) {
        Call<Void> call = apiService.desactivarUsuario(idUsuario);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AdminActivity.this, "Cuenta desactivada", Toast.LENGTH_SHORT).show();
                    cargarUsuariosDesdeBackend(); // Recargamos la lista
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }

    // reactivacion
    private void mostrarDialogoReactivar(ClienteResponse usuario) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Reactivar Cuenta")
                .setMessage("¿Deseas devolverle el acceso a " + usuario.getNombre() + "?")
                .setPositiveButton("Reactivar", (dialog, which) -> ejecutarReactivacion(usuario.getIdUsuario()))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void ejecutarReactivacion(Integer idUsuario) {
        Call<Void> call = apiService.reactivarUsuario(idUsuario);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    android.widget.Toast.makeText(AdminActivity.this, "Cuenta reactivada", android.widget.Toast.LENGTH_SHORT).show();
                    cargarUsuariosDesdeBackend(); // Recargamos la lista
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }
}