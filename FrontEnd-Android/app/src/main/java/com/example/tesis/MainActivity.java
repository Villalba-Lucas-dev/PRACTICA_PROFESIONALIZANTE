package com.example.tesis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // Declaramos las variables de la pantalla
    private EditText etNombre, etApellido, etEdad, etCorreo, etPassword, etConfirmarPassword;
    private Button btnRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Esto enlaza este código con el diseño visual
        setContentView(R.layout.activity_registro);

        // Conectamos las variables de Java con los IDs del XML
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etEdad = findViewById(R.id.etEdad);
        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        etConfirmarPassword = findViewById(R.id.etConfirmarPassword);

        // Escuchamos el clic del botón
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String edadStr = etEdad.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmarPassword = etConfirmarPassword.getText().toString().trim();

        // Validar campos vacíos
        if (nombre.isEmpty() || apellido.isEmpty() || edadStr.isEmpty() || correo.isEmpty() || password.isEmpty() || confirmarPassword.isEmpty()) {
            Toast.makeText(this, "Por favor completá todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Regex de Correo
        if (!correo.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            etCorreo.setError("Formato de correo inválido");
            return;
        }

        // 3. Regex de Contraseña
        // Mínimo 8 caracteres, al menos 1 número y al menos 1 letra mayúscula
        if (!password.matches("^(?=.*[0-9])(?=.*[A-Z]).{8,}$")) {
            etPassword.setError("La contraseña debe tener mín. 8 caracteres, 1 número y 1 mayúscula");
            return;
        }

        // 4. Match de contraseñas
        if (!password.equals(confirmarPassword)) {
            etConfirmarPassword.setError("Las contraseñas no coinciden");
            return;
        }

        int edad = Integer.parseInt(edadStr);
        if(edad>100 || edad<0)
        {
            etEdad.setError("usar una edad valida(0-100)");
        }
        Cliente nuevoCliente = new Cliente(nombre, apellido, edad, correo, password);

        GymApiService apiService = RetrofitClient.getClient().create(GymApiService.class);
        Call<String> call = apiService.registrarCliente(nuevoCliente);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String mensaje = response.body();
                    Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();

                    // 5. Redirección al Login
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Cierra la pantalla de registro
                } else {
                    try{
                        String mensaje = response.errorBody().string();
                        Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();

                    }catch (Exception exception)
                    {
                        Toast.makeText(MainActivity.this,"error desconocido",Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                String mensaje;

                if (t instanceof java.net.UnknownHostException) {
                    mensaje = "No hay conexión a internet";
                } else if (t instanceof java.net.ConnectException) {
                    mensaje = "No se puede conectar al servidor";
                } else if (t instanceof java.net.SocketTimeoutException) {
                    mensaje = "Tiempo de espera agotado";
                } else {
                    mensaje = "Error: " + t.getMessage();
                }

                Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        });
    }
}