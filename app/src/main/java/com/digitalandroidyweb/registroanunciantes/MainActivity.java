package com.digitalandroidyweb.registroanunciantes;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    Button registrar, mostrar, buscar, registrar2;

    Spinner spinner;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registrar = findViewById(R.id.registrar_pre);
        mostrar = findViewById(R.id.mostrar);
        buscar = findViewById(R.id.buscar_nombre);
        registrar2 = findViewById(R.id.registrar2);

        registrar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registro_produccion = new Intent (MainActivity.this, RegistroProduccion.class);
                startActivity(registro_produccion);
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrar = new Intent(MainActivity.this, RegistrarAnunciante.class);
                startActivity(registrar);
            }
        });
        mostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mostar = new Intent( MainActivity.this, MostrarAnunciantes.class);
                startActivity(mostar);
            }
        });
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
