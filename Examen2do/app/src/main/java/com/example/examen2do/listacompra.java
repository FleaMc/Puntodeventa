package com.example.examen2do;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
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

public class listacompra extends AppCompatActivity {
    String cantb="";
    EditText com2,tot;
    String modifcant="",cod,nom,id,mod,cap,pre,col,sis,cant,cantor,listaCompra="";String[] temp1 = new String[20];
    int total=0,suma=0,prec=0;
    Button comprar,reg;
    TextView com;

    StringBuilder builder = new StringBuilder();
    String usuario = "",tipo="";
    String cantn="",Id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listacompra);
        com = (TextView) findViewById(R.id.compras);
        tot=(EditText) findViewById(R.id.ettotal);
        comprar=(Button)findViewById(R.id.btbuy);
        reg=(Button) findViewById(R.id.bthome);
        Bundle extras = getIntent().getExtras();
        usuario = extras.getString("usuario");
        tipo = extras.getString("tipo");
        arr();
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(listacompra.this,vender.class);
                intent.putExtra("usuario",usuario);
                intent.putExtra("tipo",tipo);
                startActivity(intent);
            }
        });
        comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String compras2=com.getText().toString();
                String list = listaCompra;

                if (!compras2.isEmpty()){
                    String url = "https://mcflea.000webhostapp.com/android/insertarpro.php";
                    Map<String, String> cadena = new HashMap<String, String>();
                    cadena.put("opcion", "ventas");
                    cadena.put("vendedor", usuario);
                    cadena.put("articulos", list);
                    cadena.put("importe", ""+suma);
                    venta(url, cadena);
                    String[] arreglo = modifcant.split(",");
                    for (int j = 0; j < arreglo.length; j++) {
                        Id = arreglo[j];
                        actualizacion(Id);
                            }

                        listaCompra="";

                }

            }
        });
    }
    public void arr(){
        Bundle extras = getIntent().getExtras();

        String[] miArray = extras.getStringArray("arreglof");
        int c=0;
        for (int i = 0; i < miArray.length; i++ ) {
            builder.append(miArray[i] +"\n");
            if(i!=miArray.length-1){
                builder.append(",");
            }
            String myString = miArray[i];
            String[] datosSeparados = myString.split(",");
            int res=0;
            for (int j = 0; j < datosSeparados.length; j++) {
                id = datosSeparados[0];
                nom = datosSeparados[1];
                mod = datosSeparados[2];
                cap = datosSeparados[3];
                pre = datosSeparados[4];
                prec=Integer.parseInt(pre);
                col = datosSeparados[5];
                sis = datosSeparados[6];
                cant = datosSeparados[7];
                int cant2=Integer.parseInt(cant);

                if(j==7){
                    suma=suma+(prec*cant2);
                    modifcant=modifcant+id+",";
                }

            }

            total=total+prec;

            listaCompra=listaCompra+"Nombre: "+nom +","+"Modelo: "+mod+", Capacidad: "+cap+",Precio: "+pre +", Cantidad: "+cant+"\n"
                    +"-----------------------------------------"+"\n" ;
        }

        String miString = builder.toString();
        com.setText(listaCompra);
        tot.setText("Total: " +suma);
}
    private void venta(String url, final Map<String, String> params) {
        StringRequest stringrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "venta realizada", Toast.LENGTH_LONG).show();
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
    private void actualizarpr(String url, final Map<String, String> params) {
        StringRequest stringrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), "void" +error.getMessage(), Toast.LENGTH_LONG).show();
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
    public void actualizacion(String id){
        String url="https://mcflea.000webhostapp.com/android/buscarpr.php?Id="+id;
        JsonArrayRequest stringaray=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        cantn=(jsonObject.getString("cant_t"));
                        String url2 = "https://mcflea.000webhostapp.com/android/actualizarpr.php";
                        Map<String, String> cadena2 = new HashMap<String, String>();
                        cadena2.put("opcion","vend");
                        cadena2.put("Id", id);
                        cadena2.put("cant", cantn);
                        actualizarpr(url2, cadena2);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "json"+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "no se encuentra", Toast.LENGTH_LONG).show();
                System.out.println(error.getMessage());
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(listacompra.this);
        requestQueue.add(stringaray);

    }
}