package com.uth.apputh;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.uth.apputh.dao.ClienteDAO;
import com.uth.apputh.models.Cliente;

public class MainActivity extends AppCompatActivity {

    TextInputEditText nombre, apellido, edad, correo;
    MaterialButton btnAgregar, btnVerClientes;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        nombre = findViewById(R.id.nombre);
        apellido = findViewById(R.id.apellido);
        edad = findViewById(R.id.edad);
        correo = findViewById(R.id.correo);

        btnAgregar = findViewById(R.id.btnAgregar);
        btnVerClientes = findViewById(R.id.btnVerClientes);

        btnAgregar.setOnClickListener(v -> {

            String sNombre = nombre.getText().toString().trim();
            String sApellido = apellido.getText().toString().trim();
            String sEdad = edad.getText().toString().trim();
            String sCorreo = correo.getText().toString().trim();

            nombre.setError(null);
            apellido.setError(null);
            edad.setError(null);
            correo.setError(null);

            if (sNombre.isEmpty()) {
                nombre.setError("El nombre es obligatorio");
                nombre.requestFocus();
                return;
            }
            if (sApellido.isEmpty()) {
                apellido.setError("El apellido es obligatorio");
                apellido.requestFocus();
                return;
            }
            if (sEdad.isEmpty()) {
                edad.setError("La edad es obligatoria");
                edad.requestFocus();
                return;
            }

            int valorEdad;
            try {
                valorEdad = Integer.parseInt(sEdad);
            } catch (NumberFormatException e) {
                edad.setError("Ingrese un número entero válido");
                edad.requestFocus();
                return;
            }
            if (valorEdad <= 0) {
                edad.setError("La edad debe ser mayor que cero");
                edad.requestFocus();
                return;
            }

            if (sCorreo.isEmpty()) {
                correo.setError("El correo es obligatorio");
                correo.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(sCorreo).matches()) {
                correo.setError("Formato de correo inválido");
                correo.requestFocus();
                return;
            }

            Cliente cliente = new Cliente();
            cliente.setNombre(sNombre);
            cliente.setApellido(sApellido);
            cliente.setEdad(valorEdad);
            cliente.setCorreo(sCorreo);

            ClienteDAO dao = new ClienteDAO(this);
            long resultado = dao.insertar(cliente);

            if (resultado != -1) {
                Toast.makeText(this, "Persona registrada correctamente", Toast.LENGTH_SHORT).show();
                nombre.setText("");
                apellido.setText("");
                edad.setText("");
                correo.setText("");
            } else {
                Toast.makeText(this, "Error al guardar la persona", Toast.LENGTH_LONG).show();
            }
        });

        btnVerClientes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ClientesActivity.class);
            startActivity(intent);
        });
    }
}