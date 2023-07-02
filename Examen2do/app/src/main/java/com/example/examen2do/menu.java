package com.example.examen2do;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class menu extends AppCompatActivity {
    TextView ve,ven,use,add,man,user;


    String usuario = "",tipo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ven = (TextView) findViewById(R.id.vender);
        use = (TextView) findViewById(R.id.use);
        add = (TextView) findViewById(R.id.addpr);
        ve=(TextView)findViewById(R.id.sales);
        user=(TextView)findViewById(R.id.txtUs);
        Bundle extras = getIntent().getExtras();
        usuario = extras.getString("usuario");
        tipo = extras.getString("tipo");
        usuario();



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this,agregarpr.class);
                intent.putExtra("usuario",usuario);
                intent.putExtra("tipo",tipo);
                startActivity(intent);
            }
        });
        ven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(menu.this,vender.class);
                intent.putExtra("usuario",usuario);
                intent.putExtra("tipo",tipo);
                startActivity(intent);
            }
        });
        use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this,usuarios.class);
                intent.putExtra("usuario",usuario);
                startActivity(intent);
            }
        });
        ve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this,vendido.class);
                intent.putExtra("usuario",usuario);
                startActivity(intent);
            }
        });
    }
    public void usuario(){
        user.setText(usuario);
        if(tipo.equals("admin")){

        }else{
            use.setVisibility(View.GONE);


        }

    }

}