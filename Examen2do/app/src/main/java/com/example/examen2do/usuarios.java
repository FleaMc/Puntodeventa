package com.example.examen2do;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class usuarios extends AppCompatActivity {
    EditText en,eu,ep;
    Button reg,agrr,elim,act,hom,clean;
    String usuario = "",tipo="";

    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        Bundle extras = getIntent().getExtras();
        usuario = extras.getString("usuario");
        spinner = findViewById(R.id.spusers);
        en = (EditText) findViewById(R.id.etnom);
        eu = (EditText) findViewById(R.id.etus);
        ep = (EditText) findViewById(R.id.etpass);
        reg = (Button) findViewById(R.id.btadd);
        agrr = (Button) findViewById(R.id.btbus);
        elim = (Button) findViewById(R.id.btelim);
        clean =  (Button) findViewById(R.id.btclean);
        act = (Button) findViewById(R.id.btact);
        hom = (Button) findViewById(R.id.bthome);
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ep.setText("");
                eu.setText("");
                en.setText("");
            }
        });
        agrr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Buscar(v);
            }
        });
        elim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Eliminar(v);
            }
        });
        act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizar(v);
            }
        });
        hom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(usuarios.this,menu.class);
                intent.putExtra("usuario",usuario);
                startActivity(intent);
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registro(v);
            }
        });

    }

    public void Registro(View view){

        String nom = en.getText().toString();
        String id=eu.getText().toString();
        String pass=ep.getText().toString();
        tipo = spinner.getSelectedItem().toString();
        if (!id.isEmpty() && !nom.isEmpty() && !pass.isEmpty()){
            String url = "https://mcflea.000webhostapp.com/android/insertarpro.php";
            Map<String, String> cadena = new HashMap<String, String>();
            cadena.put("opcion", "usu");
            cadena.put("usuario", id);
            cadena.put("contrasena", pass);
            cadena.put("nombre", nom);
            cadena.put("tipo", tipo);
            usuarioinser(url, cadena);

        }


    }

    public void Buscar(View view){
        String id=eu.getText().toString();
        if(!id.isEmpty()){
            String url = "https://mcflea.000webhostapp.com/android/buscarusuario.php?usuario="+id;
            buscarp(url);
        }else{
            Toast.makeText(usuarios.this,"llena el camp",Toast.LENGTH_SHORT).show();
        }
    }
    public void Eliminar(View view){

        String id2=eu.getText().toString();
        if(!id2.isEmpty()){
            String url = "https://mcflea.000webhostapp.com/android/eliminarus.php";
            elimus(url, id2);
            eu.setText("");

        }else{
            Toast.makeText(usuarios.this,"llena campo id",Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizar(View view){

        String id = eu.getText().toString();
        String nom = en.getText().toString();
        String pas = ep.getText().toString();
        tipo = spinner.getSelectedItem().toString();
        if(id!="" && nom!="" && pas!="" && tipo!=""){
        String url = "https://mcflea.000webhostapp.com/android/actualizarpr.php";
        Map<String, String> cadena = new HashMap<String, String>();
        cadena.put("usuario", id);
        cadena.put("contrasena", pas);
        cadena.put("nombre",nom);
        cadena.put("tipo", tipo);
        cadena.put("opcion","usuario");
        actusuario(url, cadena);
        }else{
            Toast.makeText(this, "campos vacios", Toast.LENGTH_SHORT).show();
        }

    }
    private void usuarioinser(String url, final Map<String, String> params) {
        StringRequest stringrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "usuario insertado", Toast.LENGTH_LONG).show();
                        eu.setText("");
                        en.setText("");
                        ep.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "void" +error.getMessage(), Toast.LENGTH_LONG).show();
                        System.out.println(error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringrequest);
    }
    private void buscarp(String s) {
        JsonArrayRequest stringaray=new JsonArrayRequest(s, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        eu.setText(jsonObject.getString("usuario"));
                        ep.setText(jsonObject.getString("contrasena"));
                        en.setText(jsonObject.getString("nombre"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "error ", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "void" +error.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(error.getMessage());
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringaray);
    }
    private void elimus(String url, String id) {
        StringRequest stringrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "usuario elimnado", Toast.LENGTH_LONG).show();
                        eu.setText("");
                        en.setText("");
                        ep.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "no existe", Toast.LENGTH_LONG).show();
                        System.out.println(error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("usuario", String.valueOf(id));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringrequest);
    }
    private void actusuario(String url, final Map<String, String> params) {
        StringRequest stringrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "usuario modificado", Toast.LENGTH_LONG).show();
                        eu.setText("");
                        en.setText("");
                        ep.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "void" +error.getMessage(), Toast.LENGTH_LONG).show();
                        System.out.println(error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringrequest);
    }

}