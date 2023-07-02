package com.example.examen2do;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class vender extends AppCompatActivity {
    Button me,add,canc,ven,bus,scan2,lim;
    String pr="";
    String cant="",cantt2="";
    int cantor=0,cantt=0;
    String[] productos = new String[20]; int contador = 0;;
    EditText edtmod,edtnom,edtprec,edtcat,edtpr,edtcod,edtcap,edtcol,edt,edtsisop;
    TextView cant1;
    String usuario ="", resetear="",tipo="";
    int id = 0,ij=0,total=0,total2=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vender);
        bus = (Button) findViewById(R.id.btbus);
        cant1 = (TextView) findViewById(R.id.tvcantb);
        me = (Button) findViewById(R.id.bthome);
        add = (Button) findViewById(R.id.btadd);
        canc = (Button) findViewById(R.id.btelim);
        ven = (Button) findViewById(R.id.btact);
        edtcod = (EditText) findViewById(R.id.etcod);
        edtcat = (EditText) findViewById(R.id.etcan);
        scan2=(Button) findViewById(R.id.bttnscan);
        Bundle extras = getIntent().getExtras();
        usuario = extras.getString("usuario");
        tipo = extras.getString("tipo");
        canc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productos = new String[20]; poner2();
                pr = "";
                String s="https://mcflea.000webhostapp.com/android/consultanr.php";
                JsonArrayRequest stringaray=new JsonArrayRequest(s, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jsonObject = null;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                jsonObject = response.getJSONObject(i);
                                String id=(jsonObject.getString("Id"));
                                String cant=(jsonObject.getString("cant"));
                                String url = "https://mcflea.000webhostapp.com/android/actualizarpr.php";
                                Map<String, String> cadena = new HashMap<String, String>();
                                cadena.put("Id", id);
                                cadena.put("cant", cant);
                                cadena.put("opcion","res");
                                actualizarpr(url, cadena);

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
                RequestQueue requestQueue= Volley.newRequestQueue(vender.this);
                requestQueue.add(stringaray);

            }
        });

     scan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanner2();
            }
        });
       bus.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               poner2();edtcat.setText("");
           }
       });
        ven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                String[] arreglo = pr.split(";");
                Intent intent = new Intent(vender.this, listacompra.class);
                Bundle extras = new Bundle();
                extras.putStringArray("arreglof", arreglo);
                intent.putExtras(extras);
                intent.putExtra("usuario",usuario);
                startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(vender.this, "Nada que vender", Toast.LENGTH_SHORT).show();
                }
            }
        });

        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(vender.this,menu.class);
                    intent.putExtra("usuario",usuario);
                    intent.putExtra("tipo",tipo);
                    startActivity(intent);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=edtcod.getText().toString();
                if(id!=""){
                String cantr=edtcat.getText().toString(); //cantidad que selecciona cliente
                int cantr2=Integer.parseInt(cantr); //cantidad del cliente a entero
                String url="https://mcflea.000webhostapp.com/android/buscarpr.php?Id="+id;
                JsonArrayRequest stringaray=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jsonObject = null;
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                jsonObject = response.getJSONObject(i);
                                String idc=(jsonObject.getString("Id"));
                                String nombrec=(jsonObject.getString("nombre"));
                                String modelo=(jsonObject.getString("modelo"));
                                String capacidad =(jsonObject.getString("capacidad"));
                                String precio=(jsonObject.getString("precio"));
                                String color=(jsonObject.getString("color"));
                                String sis_op=(jsonObject.getString("sis_op"));
                                String cant=(jsonObject.getString("cant"));
                                String cant_t=(jsonObject.getString("cant_t"));
                                int cantc2 = Integer.parseInt(cant_t);
                                if(cantr2<=cantc2){ //si la cantidad es menor o igual a la disponible se agrega producto
                                    cantt=cantt-cantr2; //para restar la cantidad temporalmente
                                    String url = "https://mcflea.000webhostapp.com/android/actualizarpr.php";
                                    Map<String, String> cadena = new HashMap<String, String>();
                                    cadena.put("Id", idc);
                                    String cantm = Integer.toString(cantt);
                                    cadena.put("cant", cantm);
                                    cadena.put("opcion","res");
                                    actualizarpr(url, cadena);
                                    pr=pr+idc+","+nombrec+","+modelo+","+capacidad+","+precio+","+color+","+sis_op+","+edtcat.getText().toString()+","+cantt+";";
                                    productos[contador] = pr;
                                    resetear=resetear+","+idc;
                                    contador++;
                                    Toast.makeText(vender.this, "Articulo agregado", Toast.LENGTH_SHORT).show();
                                    poner2();
                                }else{
                                    Toast.makeText(vender.this, "No contamos con el stock", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "json"+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        edtcat.setText("");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "no se encuentra", Toast.LENGTH_LONG).show();
                        System.out.println(error.getMessage());
                    }
                });
                RequestQueue requestQueue= Volley.newRequestQueue(vender.this);
                requestQueue.add(stringaray);

                }else{
                    Toast.makeText(vender.this, "No selecciono ningun producto", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void poner2(){
        String id=edtcod.getText().toString();
        String url="https://mcflea.000webhostapp.com/android/buscarpr.php?Id="+id;
        JsonArrayRequest stringaray=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {

                        jsonObject = response.getJSONObject(i);
                        cant=(jsonObject.getString("cant"));
                        cantt2=(jsonObject.getString("cant_t"));


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "json"+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                cantt = Integer.parseInt(cantt2);
                cantor = Integer.parseInt(cant);
                cant1.setText("Cantidad: "+cantt);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "no se encuentra", Toast.LENGTH_LONG).show();
                System.out.println(error.getMessage());
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringaray);

    }

        public void scanner2() {
        IntentIntegrator integrador = new IntentIntegrator(vender.this);
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

                edtcod.setText(resultado.getContents());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void reinicio(){
        productos = new String[20];
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


}