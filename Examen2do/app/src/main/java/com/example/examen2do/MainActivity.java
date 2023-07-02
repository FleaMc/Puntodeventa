package com.example.examen2do;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    Button log;
    EditText user2,pass2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        log = findViewById(R.id.log);
        user2=findViewById(R.id.emailEditText);
        pass2=findViewById(R.id.pass);



        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            usuarios("https://mcflea.000webhostapp.com/android/checar_usuario.php?usuario="+user2.getText());
            }
        });



    }
    public void usuarios(String url) {
        String contraseña1= String.valueOf(pass2.getText());
        JsonArrayRequest stringaray=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {

                        jsonObject = response.getJSONObject(i);
                        String usuario2 = jsonObject.getString("usuario");
                        String tipo = jsonObject.getString("tipo");
                        String contraseñat = jsonObject.getString("contrasena");
                        if(contraseña1.equals(contraseñat)){
                            Intent inten =new Intent(MainActivity.this, menu.class);
                            inten.putExtra("tipo", tipo);
                            inten.putExtra("usuario", usuario2);
                            startActivity(inten);
                        }else{
                             Toast.makeText(MainActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "json"+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "no existe usuario", Toast.LENGTH_LONG).show();
                System.out.println(error.getMessage());
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringaray);
    }

}