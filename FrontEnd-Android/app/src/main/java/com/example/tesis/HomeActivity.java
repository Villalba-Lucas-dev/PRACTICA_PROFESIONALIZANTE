package com.example.tesis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnPanelAdmin = findViewById(R.id.btnPanelAdmin); // Enlazamos el boton de admin
        Button btnPanelEntrenador = findViewById(R.id.btnPanelEntrenador);
        LinearLayout llMiPerfil = findViewById(R.id.llMiPerfil);

        // 1. Recuperamos las credenciales de la memoria del celular
        SharedPreferences prefs = getSharedPreferences("GymAppPrefs", MODE_PRIVATE);
        // Pedimos el rol. Si por algun motivo hay un error y no hay rol, le damos "CLIENTE" por defecto por seguridad.
        String rolUsuario = prefs.getString("ROL_USUARIO", "CLIENTE");
        btnPanelAdmin.setVisibility(View.GONE);
        btnPanelEntrenador.setVisibility(View.GONE);

        // 2. Lógica de control de acceso
        if ("ADMIN".equals(rolUsuario)) {
            // Si es admin, hacemos aparecer el botón
            btnPanelAdmin.setVisibility(View.VISIBLE);
        } else if ("ENTRENADOR".equals(rolUsuario)) {
            // Si es entrenador, mostramos su sección de alumnos
            btnPanelEntrenador.setVisibility(View.VISIBLE);
        }

        // 3. Acción del boton Administrador
        btnPanelAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });

        // 4. Acción de Cerrar Sesion
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Importante: Borrar los datos de la sesión al salir para que no quede logueado
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();

                // Volvemos al Login
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        llMiPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hacemos el salto a la pantalla de Perfil
                Intent intent = new Intent(HomeActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        // Dejamos preparado el clic del entrenador para el futuro
        btnPanelEntrenador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.widget.Toast.makeText(HomeActivity.this, "Próximamente: Panel de Entrenador", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }
}