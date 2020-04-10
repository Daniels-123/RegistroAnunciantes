package com.digitalandroidyweb.registroanunciantes;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditarAnunciante extends AppCompatActivity {
    private static final String TAG = EditarAnunciante.class.getSimpleName(); //getting the info
    private EditText nombre, direccion, barrio;
    private TextView idtext;
    private static String URL_READ = "http://www.digitalandroidservices.com/api/informacion/read_detail.php";
    private static String URL_EDIT = "http://www.digitalandroidservices.com/api/informacion/edit_detail.php";
    private static String URL_DELETE = "http://www.digitalandroidservices.com/api/informacion/delete.php";
    public static final String EXTRA_ID= "id_nombre";
    private Button guardar, eliminar, regresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_anunciante);
        nombre = findViewById(R.id.nombre_editar);
       direccion = findViewById(R.id.direccion_editar);
        barrio = findViewById(R.id.barrio_editar);
        idtext = findViewById(R.id.id_nombre_editar);
        guardar = findViewById(R.id.btn_actualizar);
        eliminar = findViewById(R.id.btn_borrar);
        regresar = findViewById(R.id.btn_regresar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveEditDetail();
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(EditarAnunciante.this);
                dialogo1.setTitle("Importante");
                dialogo1.setMessage("¿ Desea Eliminar este registro?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        Delete();
                        finish();
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        dialogo1.cancel();
                    }
                });
                dialogo1.show();

            }
        });
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getUserDetail(){
        Intent intent = getIntent();
        final String id = intent.getStringExtra(EXTRA_ID);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")){

                                for (int i =0; i < jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String strid = object.getString("id_nombre").trim();
                                    String strnombre = object.getString("Nombre").trim();
                                    String strdireccion= object.getString("Direccion").trim();
                                    String strbarrio=object.getString("Barrio").trim();
                                    //String strcategoria= object.getString("Categoria").trim();
                                    //String strsubcategoria= object.getString("SubCategoria").trim();

                                    nombre.setText(strnombre);
                                    direccion.setText(strdireccion);
                                    barrio.setText(strbarrio);
                                    idtext.setText(strid);


                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(EditarAnunciante.this, e+"Error de conexión ", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(EditarAnunciante.this, error + "Error de conexión  ", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String > params = new HashMap<>();
                params.put("id_nombre", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void SaveEditDetail() {

        final String nombre = this.nombre.getText().toString().trim();
        final String direccion= this.direccion.getText().toString().trim();
        final String barrio= this.barrio.getText().toString().trim();
        final String id = this.idtext.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Guardando...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(EditarAnunciante.this, "El registro fue actualizado exitosamente!", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(EditarAnunciante.this, "Error "+ e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(EditarAnunciante.this, "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Nombre", nombre);
                params.put("Direccion", direccion);
                params.put("Barrio", barrio);
                params.put("id_nombre", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void Delete() {
        Intent intent = getIntent();
        final String id = intent.getStringExtra(EXTRA_ID);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("ELIMINANDO...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(EditarAnunciante.this, "El registro fue eliminado exitosamente!", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(EditarAnunciante.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(EditarAnunciante.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_nombre", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


        @Override
    protected void onResume() {
        super.onResume();
        getUserDetail();
       // Toast.makeText(this, EXTRA_ID, Toast.LENGTH_SHORT).show();
    }

}
