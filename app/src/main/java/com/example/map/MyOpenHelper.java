package com.example.map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Canvas;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyOpenHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;

    public MyOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        String qUser = "CREATE TABLE user\n" +
                "\t(id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "      login TEXT NOT NULL UNIQUE,\n" +
                "      pass  TEXT NOT NULL,\n" +
                "      email TEXT NOT NULL UNIQUE)";
        db.execSQL(qUser);

        String qCategory = "CREATE TABLE category \n" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                "    name TEXT  NOT NULL)";
        db.execSQL(qCategory);

        String qMarkers = "CREATE TABLE markers \n" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                "    name        TEXT    NOT NULL,\n" +
                "    lat         FLOAT   NOT NULL,\n" +
                "    lot         FLOAT   NOT NULL,\n" +
                "    img         TEXT            ,\n" +
                "    description TEXT            ,\n" +
                "    idCategory  INTEGER NOT NULL,\n" +
                "    FOREIGN KEY(idCategory) REFERENCES category(id))";
        db.execSQL(qMarkers);



        db.execSQL("INSERT INTO user(login, pass, email) VALUES('test','test','test')");
        db.execSQL("INSERT INTO category(name) VALUES('The park')");
        db.execSQL("INSERT INTO category(name) VALUES('Plage')");
        db.execSQL("INSERT INTO category(name) VALUES('Hospitals')");
        db.execSQL("INSERT INTO category(name) VALUES('Dentistry')");
        db.execSQL("INSERT INTO category(name) VALUES('Pharmacy')");
        db.execSQL("INSERT INTO category(name) VALUES('Cafe')");
        db.execSQL("INSERT INTO category(name) VALUES('Bakery')");
        db.execSQL("INSERT INTO category(name) VALUES('Supermarket')");
        db.execSQL("INSERT INTO category(name) VALUES('Mall')");
        db.execSQL("INSERT INTO category(name) VALUES('Music store')");
        db.execSQL("INSERT INTO category(name) VALUES('Electronics store')");
        db.execSQL("INSERT INTO category(name) VALUES('Clothing store')");
        db.execSQL("INSERT INTO category(name) VALUES('Household store')");
        db.execSQL("INSERT INTO category(name) VALUES('Vape shop')");
        db.execSQL("INSERT INTO category(name) VALUES('ATM')");
        db.execSQL("INSERT INTO category(name) VALUES('Bank')");
        db.execSQL("INSERT INTO category(name) VALUES('WC')");
        db.execSQL("INSERT INTO category(name) VALUES('Railway station')");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Арыш мае',55.802663, 49.176096, 8)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        Cursor c;
//        while (c.moveToNext()){
//            arr.add(new Category(c.getInt(0), c.getString(1))){
//        }
//        }
    }
//    ArrayList<Category> arr = new ArrayList<>();
//    class Category{
//        int id;
//        String name;
//        public Category(int id, String name){
//            this.id = id;
//            this.name = name;
//        }
//    }
}
