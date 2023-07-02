package com.example.examen2do;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class sqllite extends SQLiteOpenHelper {
    public sqllite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "Registros", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase basedatos) {
        basedatos.execSQL("create table usuarios(user text primary key, pass text,nombre text,id_venta,tipo text)");
        basedatos.execSQL("create table ventas(Id_venta integer primary key autoincrement, vendedor text,articulos text,importe int)");
        basedatos.execSQL("create table productos(Id text primary key,nombre text,modelo text,capacidad text," +
                " precio float,color text,sis_op text, cant text,cant_t text,id_venta int,vendedor text,foreign key(Id)" +
                " references ventas(Id_venta))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
