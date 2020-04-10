package com.digitalandroidyweb.registroanunciantes;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistrarAnunciante extends AppCompatActivity {

    private static String URL_REGIST = "http://www.digitalandroidservices.com/api/informacion/registro_informacion.php";
    private static String URL_CARGAR = "http://www.digitalandroidservices.com/api/informacion/listarsubcategorias.php";

    private EditText Nombre, Direccion, Barrio;

    private Button guardar;

    private Spinner CategoriaSpinner, SpinnerSubcategoria;
    ArrayList<String> Subcategoria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_anunciante);
        Subcategoria=new ArrayList<>();
        Nombre = findViewById(R.id.nombre);
        Direccion= findViewById(R.id.direccion);
        Barrio= findViewById(R.id.barrio);
        CategoriaSpinner = findViewById(R.id.spinner);
        SpinnerSubcategoria= findViewById(R.id.spinnersubcategoria);
        guardar = findViewById(R.id.guardar);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Nombre.getText().toString().isEmpty() || Direccion.getText().toString().isEmpty() || Barrio.getText().toString().isEmpty()) {
                    Toast.makeText(RegistrarAnunciante.this, "Algun campo esta vacio", Toast.LENGTH_SHORT).show();
                }else{
                    Guardar();
                    Nombre.setText("");
                    Direccion.setText("");
                    Barrio.setText("");

                }

            }

        });
        loadSpinner(URL_CARGAR);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,  R.array.Categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CategoriaSpinner.setAdapter(adapter);

      /*  ArrayAdapter<CharSequence> adaptersub = ArrayAdapter.createFromResource(this,  R.array.Subcategoria, android.R.layout.simple_spinner_item);
        adaptersub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerSubcategoria.setAdapter(adaptersub);
*/


    }

    private void loadSpinner(String url){
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                       JSONArray jsonArray=jsonObject.getJSONArray("Registros");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String subcat=jsonObject1.getString("nombre");
                            Subcategoria.add(subcat);
                        }

                    SpinnerSubcategoria.setAdapter(new ArrayAdapter<String>(RegistrarAnunciante.this, android.R.layout.simple_spinner_dropdown_item, Subcategoria));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }


    public void Guardar(){
        final String Nombre = this.Nombre.getText().toString().trim();
        final String Direccion= this.Direccion.getText().toString().trim();
        final String Barrio= this.Barrio.getText().toString().trim();
        final String Categoria = this.CategoriaSpinner.getSelectedItem().toString();
        final String SubCategoria = this.SpinnerSubcategoria.getSelectedItem().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(RegistrarAnunciante.this, "Registro exitoso!", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(Re.this, LoginActivity.class));
                            } else if (success.equals("2")){
                                final Dialog mailDialog = new Dialog(RegistrarAnunciante.this);
                                mailDialog.getWindow();
                                android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(RegistrarAnunciante.this, R.style.AppTheme).create();
                                alertDialog.setTitle("Error");
                                alertDialog.setMessage("Error en el registro, intenta nuevamente");
                                alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                alertDialog.show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegistrarAnunciante.this, "Algo salio mal! " + e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegistrarAnunciante.this, "Algo salio mal! " + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Nombre", Nombre);
                params.put("Direccion", Direccion);
                params.put("Barrio", Barrio);
                params.put("Categoria", Categoria);
                params.put("SubCategoria", SubCategoria);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }
}
