package com.example.examen2do;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class vendido extends AppCompatActivity  {
    Button menu,bus;
    EditText ven,etid,etpr,etmon;
    TextView vende;
    String usuario = "",tipo="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        usuario = extras.getString("usuario");
        tipo = extras.getString("tipo");
        setContentView(R.layout.activity_vendido);
        bus = (Button) findViewById(R.id.btbus);
        menu = (Button) findViewById(R.id.btmenu);
        vende = (TextView) findViewById(R.id.tvvende);
        ven = (EditText) findViewById(R.id.etven);
        etid = (EditText) findViewById(R.id.etid);
        etpr = (EditText) findViewById(R.id.etart);
        etmon = (EditText) findViewById(R.id.etmont);
        llen();
        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=etid.getText().toString();
                String s="https://mcflea.000webhostapp.com/android/consultarven2.php?id="+id;
                JsonArrayRequest stringaray=new JsonArrayRequest(s, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jsonObject = null;
                        String venc="";
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                jsonObject = response.getJSONObject(i);
                                etpr.setText(jsonObject.getString("articulos"));
                                etmon.setText(jsonObject.getString("importe"));
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
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
                RequestQueue requestQueue= Volley.newRequestQueue(vendido.this);
                requestQueue.add(stringaray);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(vendido.this,menu.class);
                intent.putExtra("usuario",usuario);
                tipo = extras.getString("tipo");
                startActivity(intent);
            }
        });
    }
    public void llen(){
        vende.setText(usuario);
        String s="https://mcflea.000webhostapp.com/android/consultarven.php?vendedor="+usuario;
        JsonArrayRequest stringaray=new JsonArrayRequest(s, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                String venc="";
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        String cant=(jsonObject.getString("Id_venta"));
                        venc=venc+cant+",";
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                ven.setText(venc);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "void" +error.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(error.getMessage());
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(vendido.this);
        requestQueue.add(stringaray);
        }
}