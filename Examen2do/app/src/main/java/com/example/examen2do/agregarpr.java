package com.example.examen2do;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class agregarpr extends AppCompatActivity {
    EditText edtid,edtnom,edtmod,edtcap,edtpr,edtsis,edtcan,edtcol;
    Button add,sea,elim,act,lim,reg,scan;
    String usuario ="",tipo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregarpr);
        Bundle extras = getIntent().getExtras();
        usuario = extras.getString("usuario");
        tipo = extras.getString("tipo");
        edtcan=(EditText) findViewById(R.id.etcan);
        edtcap=(EditText) findViewById(R.id.etcap);
        edtid=(EditText) findViewById(R.id.etcod);
        edtnom=(EditText) findViewById(R.id.etnom);
        edtmod=(EditText) findViewById(R.id.etmod);
        edtpr=(EditText) findViewById(R.id.etpr);
        edtsis=(EditText) findViewById(R.id.etsis);
        edtcol=(EditText) findViewById(R.id.etcol);
        add=(Button) findViewById(R.id.btadd);
        sea=(Button) findViewById(R.id.btbus);
        elim=(Button) findViewById(R.id.btelim);
        act=(Button) findViewById(R.id.btact);
        lim=(Button) findViewById(R.id.btclean);
        reg=(Button) findViewById(R.id.bthome);
        scan=(Button) findViewById(R.id.bttnsc);
        usuario();
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanner();
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(agregarpr.this,menu.class);
                intent.putExtra("usuario",usuario);
                intent.putExtra("tipo",tipo);
                startActivity(intent);
            }
        });
        lim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtpr.setText("");
                edtnom.setText("");
                edtcan.setText("");
                edtid.setText("");
                edtcol.setText("");
                edtcap.setText("");
                edtsis.setText("");
                edtmod.setText("");
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registro(v);
            }
        });
        sea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarpro(v);
            }
        });
        elim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar(v);
            }
        });
        act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizar(v);
            }
        });


    }



    void scanner() {
        IntentIntegrator integrador = new IntentIntegrator(agregarpr.this);
        integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrador.setPrompt("Lector de codigos");
        integrador.setCameraId(0);
        integrador.setBeepEnabled(true);
        integrador.setBarcodeImageEnabled(true);
        integrador.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult resultado= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(resultado!=null){
            if(resultado.getContents()==null){
                Toast.makeText(this, "Lectura cancelada",  Toast.LENGTH_SHORT).show();
            }else{

                edtid.setText(resultado.getContents());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void Registro(View view){
        String id = edtid.getText().toString();
        String nom = edtnom.getText().toString();
        String mod = edtmod.getText().toString();
        String gb = edtcap.getText().toString();
        String prec = edtpr.getText().toString();
        String sis = edtsis.getText().toString();
        String cant = edtcan.getText().toString();
        String col = edtcol.getText().toString();
        //https://mcflea.000webhostapp.com/android/insertarpro.php
        if (!id.isEmpty() && !nom.isEmpty() && !mod.isEmpty() &&!gb.isEmpty() && !prec.isEmpty() && !sis.isEmpty() && !cant.isEmpty()){
            String url = "https://mcflea.000webhostapp.com/android/insertarpro.php";
            Map<String, String> params = new HashMap<String, String>();
            params.put("opcion","ventas2");
            params.put("Id", id);
            params.put("nombre", nom);
            params.put("modelo", mod);
            params.put("capacidad", gb);
            params.put("precio", prec);
            params.put("sis_op", sis);
            params.put("cant", cant);
            params.put("cant_t", cant);
            params.put("color", col);
            insertarpr(url,params);
                edtpr.setText("");
                edtnom.setText("");
                edtcan.setText("");
                edtid.setText("");
                edtcol.setText("");
                edtcap.setText("");
                edtsis.setText("");
                edtmod.setText("");
            }else {
                Toast.makeText(this, "Todos los campos", Toast.LENGTH_SHORT).show();
            }
        }


    public void eliminar(View view ){
        String id2=edtid.getText().toString();
        if(!id2.isEmpty()){
            String url = "https://mcflea.000webhostapp.com/android/eliminarprod.php";
            eliminarpro(url, id2);
            Toast.makeText(agregarpr.this,"producto eliminado",Toast.LENGTH_SHORT).show();
            edtid.setText("");

        }else{
            Toast.makeText(agregarpr.this,"llena campo id",Toast.LENGTH_SHORT).show();
        }
    }
    public void actualizar(View view ){


        String id = edtid.getText().toString();
        String nom = edtnom.getText().toString();
        String mod = edtmod.getText().toString();
        String gb = edtcap.getText().toString();
        String prec = edtpr.getText().toString();
        String sis = edtsis.getText().toString();
        String cant = edtcan.getText().toString();
        String col = edtcol.getText().toString();

        if(!id.isEmpty() && !nom.isEmpty() && !mod.isEmpty() &&!gb.isEmpty() && !prec.isEmpty() && !sis.isEmpty() && !cant.isEmpty()){
            String url = "https://mcflea.000webhostapp.com/android/actualizarpr.php";
            Map<String, String> cadena = new HashMap<String, String>();
            cadena.put("Id", id);
            cadena.put("nombre", nom);
            cadena.put("modelo", mod);
            cadena.put("capacidad", gb);
            cadena.put("precio", prec);
            cadena.put("cant", cant);
            cadena.put("cant_t", cant);
            cadena.put("color", col);
            cadena.put("sis_op", sis);
            cadena.put("opcion", "act");
            actualizarpr(url, cadena);


        }else{
            Toast.makeText(agregarpr.this,"llena todos los campos",Toast.LENGTH_SHORT).show();


        }
    }
    private void actualizarpr(String url, final Map<String, String> params) {
        StringRequest stringrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "articulo modificado", Toast.LENGTH_LONG).show();
                        edtpr.setText("");
                        edtnom.setText("");
                        edtcan.setText("");
                        edtid.setText("");
                        edtcol.setText("");
                        edtcap.setText("");
                        edtsis.setText("");
                        edtmod.setText("");
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

    public void usuario(){

        if(tipo.equals("admin")){

        }else{
            elim.setVisibility(View.GONE);
            act.setVisibility(View.GONE);
            scan.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
            lim.setVisibility(View.GONE);


        }

    }
    private void insertarpr(String url, final Map<String, String> params) {
        StringRequest stringrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "producto agregado", Toast.LENGTH_LONG).show();
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
    public void buscarpro(View view ){
        String id2=edtid.getText().toString();
        if(!id2.isEmpty()){
            String url = "https://mcflea.000webhostapp.com/android/buscarpr.php?Id="+id2;
            buscarp(url);
        }else{
            Toast.makeText(agregarpr.this,"llena el camp",Toast.LENGTH_SHORT).show();
        }
    }
    private void buscarp(String s) {
        JsonArrayRequest stringaray=new JsonArrayRequest(s, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        edtid.setText(jsonObject.getString("Id"));
                        edtnom.setText(jsonObject.getString("nombre"));
                        edtmod.setText(jsonObject.getString("modelo"));
                        edtcap.setText(jsonObject.getString("capacidad"));
                        edtpr.setText(jsonObject.getString("precio"));
                        edtcol.setText(jsonObject.getString("color"));
                        edtsis.setText(jsonObject.getString("sis_op"));
                        edtcan.setText(jsonObject.getString("cant"));

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
    private void eliminarpro(String url, String id) {
        StringRequest stringrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "producto eliminado", Toast.LENGTH_LONG).show();
                        edtpr.setText("");
                        edtnom.setText("");
                        edtcan.setText("");
                        edtid.setText("");
                        edtcol.setText("");
                        edtcap.setText("");
                        edtsis.setText("");
                        edtmod.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "no existe producto", Toast.LENGTH_LONG).show();
                        System.out.println(error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Id", String.valueOf(id));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringrequest);
    }


}