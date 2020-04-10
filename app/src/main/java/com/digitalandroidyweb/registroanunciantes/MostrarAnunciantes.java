package com.digitalandroidyweb.registroanunciantes;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.digitalandroidyweb.registroanunciantes.Adaptor.ExampleAdaptor;
import com.digitalandroidyweb.registroanunciantes.Adaptor.ExampleItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MostrarAnunciantes extends AppCompatActivity implements ExampleAdaptor.OnItemClickListener  {
    public static final String EXTRA_ID= "id_nombre";
    public static final String EXTRA_NOMBRE= "Nombre";
    public static final String EXTRA_DIRECCION= "Direccion";
    public static final String EXTRA_BARRIO= "Barrio";
    public static final String EXTRA_CATEGORIA= "Categoria";
    public static final String EXTRA_SUBCATEGORIA= "SubCategoria";

    private RecyclerView mRecyclerView;
    private ExampleAdaptor mExampleAdaptor;
    private ArrayList<ExampleItem> mexampleItems;
    private RequestQueue mRequestQueue;
    private ProgressBar progressBar;
    private EditText search_users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_anunciantes);

        mRecyclerView = findViewById(R.id.recycler_view_registrar);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mexampleItems = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);

        CargarListado();

        search_users = findViewById(R.id.search_users);

        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                BuscarAnunciante(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //parseJSON();
    }

    private void CargarListado() {
        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference().child("PreRegistrosIbague");
        //      final Query nm= FirebaseDatabase.getInstance().getReference("Registros");


        nm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mexampleItems = new ArrayList<ExampleItem>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                        ExampleItem l=npsnapshot.getValue(ExampleItem.class);
                        mexampleItems.add(l);
                    }
                    mExampleAdaptor=new ExampleAdaptor(getApplicationContext(), mexampleItems);
                    mRecyclerView.setAdapter(mExampleAdaptor);
                    mExampleAdaptor.setOnClickItemListener(MostrarAnunciantes.this);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void BuscarAnunciante(String nombre) {
        final Query nm= FirebaseDatabase.getInstance().getReference().child("PreRegistrosIbague")
                .orderByChild("Nombre")
                .startAt(nombre)
                .endAt(nombre+"\uf8ff");
        //      final Query nm= FirebaseDatabase.getInstance().getReference("Registros");


        nm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mexampleItems.clear();// = new ArrayList<ExampleItem>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                        ExampleItem l=npsnapshot.getValue(ExampleItem.class);
                        mexampleItems.add(l);
                    }
                    mExampleAdaptor=new ExampleAdaptor(getApplicationContext(), mexampleItems);
                    mRecyclerView.setAdapter(mExampleAdaptor);
                    mExampleAdaptor.setOnClickItemListener(MostrarAnunciantes.this);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void parseJSON() {
        String url = "http://www.digitalandroidservices.com/api/informacion/listaranunciantes.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("Registros");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                String id= hit.getString("id_nombre");
                                String nombre = hit.getString("Nombre");
                                String direccion= hit.getString("Direccion");
                                String barrio= hit.getString("Barrio");
                                String categoria= hit.getString("Categoria");
                                String subcategoria= hit.getString("SubCategoria");
                                mexampleItems.add(new ExampleItem(id, nombre,direccion,barrio,categoria,subcategoria));

                            }

                            mExampleAdaptor = new ExampleAdaptor(MostrarAnunciantes.this, mexampleItems);
                            mRecyclerView.setAdapter(mExampleAdaptor);
                           mExampleAdaptor.setOnClickItemListener(MostrarAnunciantes.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });


        mRequestQueue.add(request);

    }



    @Override
    public void onItemClick(int position) {
        Intent registar = new Intent(this, EditarAnunciante.class);
        ExampleItem exampleItem = mexampleItems.get(position);
        registar.putExtra(EXTRA_ID, exampleItem.getId_Nombre());
        registar.putExtra(EXTRA_NOMBRE, exampleItem.getNombre());
        registar.putExtra(EXTRA_DIRECCION, exampleItem.getDireccion());
        registar.putExtra(EXTRA_BARRIO, exampleItem.getBarrio());
        //registar.putExtra(EXTRA_CATEGORIA, exampleItem.getCategoria());
        //registar.putExtra(EXTRA_SUBCATEGORIA, exampleItem.getSubCategoria());
        startActivity(registar);
    }
}
