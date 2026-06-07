package com.example.tesis;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilActivity extends AppCompatActivity {

    private EditText etNombre, etApellido, etCorreo, etEdad, etObs, etRol;
    private Button btnGuardar;
    private GymApiService apiService;
    private Integer idUsuarioLogueado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        etNombre = findViewById(R.id.etPerfilNombre);
        etApellido = findViewById(R.id.etPerfilApellido);
        etCorreo = findViewById(R.id.etPerfilCorreo);
        etEdad = findViewById(R.id.etPerfilEdad);
        btnGuardar = findViewById(R.id.btnGuardarPerfil);
        etObs = findViewById(R.id.etPerfilObs);
        etRol = findViewById(R.id.etPerfilRol);

        apiService = RetrofitClient.getClient().create(GymApiService.class);

        // 1. Obtener el ID de la memoria del celular
        SharedPreferences prefs = getSharedPreferences("GymAppPrefs", MODE_PRIVATE);
        idUsuarioLogueado = prefs.getInt("ID_USUARIO", -1);

        if (idUsuarioLogueado != -1) {
            cargarDatosPerfil();
        } else {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show();
        }

        // 2. Evento del botón Guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ejecutamos las validaciones antes de enviar a la red
                if (validarCampos()) {
                    guardarCambios();
                }
            }
        });

        ImageButton btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish()); // finish() destruye la pantalla actual y vuelve a la anterior
    }

    // precargar datos
    private void cargarDatosPerfil() {
        Call<ClienteResponse> call = apiService.obtenerMiPerfil(idUsuarioLogueado);
        call.enqueue(new Callback<ClienteResponse>() {
            @Override
            public void onResponse(Call<ClienteResponse> call, Response<ClienteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ClienteResponse cliente = response.body();

                    etNombre.setText(cliente.getNombre());
                    etApellido.setText(cliente.getApellido());
                    etCorreo.setText(cliente.getMail()); // El correo se muestra pero no se edita
                    etRol.setText(cliente.getRol());

                    if (cliente.getEdad() != null) {
                        etEdad.setText(String.valueOf(cliente.getEdad()));
                    }

                    if (cliente.getObservacionesMedicas() != null) {
                        etObs.setText(cliente.getObservacionesMedicas());
                    }
                }
            }
            @Override
            public void onFailure(Call<ClienteResponse> call, Throwable t) {}
        });
    }

    // validaciones
    private boolean validarCampos() {
        String nombreStr = etNombre.getText().toString().trim();
        String apellidoStr = etApellido.getText().toString().trim();
        String edadStr = etEdad.getText().toString().trim();

        if (nombreStr.isEmpty()) {
            etNombre.setError("El nombre es obligatorio");
            return false;
        }
        if (apellidoStr.isEmpty()) {
            etApellido.setError("El apellido es obligatorio");
            return false;
        }
        if (edadStr.isEmpty()) {
            etEdad.setError("La edad es obligatoria");
            return false;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadStr);
        } catch (NumberFormatException e) {
            etEdad.setError("Formato de edad inválido");
            return false;
        }

        if (edad < 15) {
            etEdad.setError("La edad mínima es de 15 años"); // error edad minima
            return false;
        } else if (edad > 100) {
            etEdad.setError("La edad máxima es de 100 años"); // error edad maxima
        }

        return true;
    }

    // enviar al servidor
    private void guardarCambios() {
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        Integer edad = Integer.parseInt(etEdad.getText().toString().trim());
        String observaciones = etObs.getText().toString().trim();

        ActualizarPerfilRequest request = new ActualizarPerfilRequest(nombre, apellido, edad, observaciones);
        Call<Void> call = apiService.actualizarMiPerfil(idUsuarioLogueado, request);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PerfilActivity.this, "¡Perfil actualizado!", Toast.LENGTH_SHORT).show();
                    finish(); // Volvemos al Home
                } else if (response.code() == 400) {
                    Toast.makeText(PerfilActivity.this, "Error de validación en el servidor", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }
}