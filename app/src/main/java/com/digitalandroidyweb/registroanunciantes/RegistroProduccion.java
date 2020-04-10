package com.digitalandroidyweb.registroanunciantes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class RegistroProduccion extends AppCompatActivity {

    private static String URL_REGIST = "http://www.digitalandroidservices.com/api/produccion/registro_informacion.php";
    private static String URL_CARGAR = "http://www.digitalandroidservices.com/api/informacion/listarsubcategorias.php";


    private EditText Nombre_Produccion, Descripcion_Produccion, Telefono_Produccion, Direccion_Produccion, Barrio_Produccion,
                    Horario_Produccion, Destacado_Produccion;


    private TextView Latitud_Produccion;
    private Button btn_guardar_produccion;

    private Spinner SpinnerSubcategoria;
    ArrayList<String> Subcategoria;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_produccion);

        validar();

        Subcategoria = new ArrayList<>();
        Nombre_Produccion = findViewById(R.id.nombre_anunciante);
        Descripcion_Produccion = findViewById(R.id.descripcion_anunciante);
        Telefono_Produccion = findViewById(R.id.telefono_anunciante);
        Direccion_Produccion = findViewById(R.id.direccion_anunciante);
        Barrio_Produccion = findViewById(R.id.barrio_anunciante);
        Horario_Produccion = findViewById(R.id.horario_anunciante);
        Destacado_Produccion= findViewById(R.id.destacado_anunciante);
        Latitud_Produccion = findViewById(R.id.latitud_anunciante);
       // CategoriaSpinner = findViewById(R.id.spinner);
        SpinnerSubcategoria = findViewById(R.id.spinnersubcategoriaproduccion);
        btn_guardar_produccion = findViewById(R.id.btn_guardar_produccion);

        btn_guardar_produccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Nombre_Produccion.getText().toString().isEmpty() || Descripcion_Produccion.getText().toString().isEmpty() || Telefono_Produccion.getText().toString().isEmpty()) {
                    Toast.makeText(RegistroProduccion.this, "Valida los campos Nombre, Descripcion y Telefono", Toast.LENGTH_SHORT).show();
                } else {
                    Guardar();
                    Nombre_Produccion.setText("");
                    Descripcion_Produccion.setText("");
                    Telefono_Produccion.setText("");
                    Direccion_Produccion.setText("");
                    Barrio_Produccion.setText("");
                    Horario_Produccion.setText("");
                    Destacado_Produccion.setText("");

                }

            }

        });
        loadSpinner(URL_CARGAR);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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

                    SpinnerSubcategoria.setAdapter(new ArrayAdapter<String>(RegistroProduccion.this, android.R.layout.simple_spinner_dropdown_item, Subcategoria));
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
        final String Nombre_Produccion = this.Nombre_Produccion.getText().toString().trim();
        final String Descripcion_Produccion = this.Descripcion_Produccion.getText().toString().trim();
        final String Telefono_Produccion= this.Telefono_Produccion.getText().toString().trim();
        final String Direccion_Produccion= this.Direccion_Produccion.getText().toString().trim();
        final String Barrio_Produccion= this.Barrio_Produccion.getText().toString().trim();
        final String Horario_Produccion = this.Horario_Produccion.getText().toString().trim();
        final String Destacado_Produccion = this.Destacado_Produccion.getText().toString().trim();

        final String SubCategoria = this.SpinnerSubcategoria.getSelectedItem().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(RegistroProduccion.this, "Registro exitoso!", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(Re.this, LoginActivity.class));
                            } else if (success.equals("2")){
                                final Dialog mailDialog = new Dialog(RegistroProduccion.this);
                                mailDialog.getWindow();
                                AlertDialog alertDialog = new AlertDialog.Builder(RegistroProduccion.this, R.style.AppTheme).create();
                                alertDialog.setTitle("Error");
                                alertDialog.setMessage("Error en el registro, intenta nuevamente");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                alertDialog.show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegistroProduccion.this, "Algo salio mal! " + e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegistroProduccion.this, "Algo salio mal! " + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Nombre_Produccion", Nombre_Produccion);
                params.put("Descripcion_Produccion", Descripcion_Produccion);
                params.put("Telefono_Produccion", Telefono_Produccion);
                params.put("Direccion_Produccion", Direccion_Produccion);
                params.put("Barrio_Produccion", Barrio_Produccion);
                params.put("Horario_Produccion", Horario_Produccion);
                params.put("Destacado_Anunciante", Destacado_Produccion);
                params.put("SubCategoria", SubCategoria);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean validar() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(RegistroProduccion.this);
            builder.setMessage("Tu GPS esta desactivado, deseas activarlo?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
        if ((checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {


            return true;
        }

        if ((shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION))) {
            cargarDialogoRecomendacion();
        } else {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, 100);
        }

        return false;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //si existiera un boton
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
            }
        } else {
            SolicitarPermisosManual();
        }
    }

    private void SolicitarPermisosManual() {

        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(RegistroProduccion.this);
        dialogo.setTitle("Permisos necesarios");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(DialogInterface dialog, int i) {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 100);
            }
        });
        dialogo.show();
    }
}
