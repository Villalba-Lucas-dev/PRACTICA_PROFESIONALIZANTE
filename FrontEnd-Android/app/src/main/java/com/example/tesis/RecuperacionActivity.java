package com.example.tesis;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RecuperacionActivity extends AppCompatActivity {

    EditText etCorreo;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperacion);

        enlazarVistas();


    }

    private void enlazarVistas()
    {
        etCorreo = findViewById(R.id.etCorreo);
        button   = findViewById(R.id.button);

    }

    private void configEventos()
    {


    }



}
