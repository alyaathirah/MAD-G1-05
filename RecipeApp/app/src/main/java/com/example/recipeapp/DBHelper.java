package com.example.recipeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "Userdata.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Userdetails(name TEXT primary key, contact TEXT, dob TEXT)");

        DB.execSQL("create Table user(user_id INTEGER primary key, username TEXT, email TEXT, phone TEXT, address TEXT, password TEXT, image TEXT)");

        DB.execSQL("create Table list_shopping(sl_id INTEGER primary key, user_id INTEGER, foreign key(user_id) references user(user_id))");
        DB.execSQL("create Table list_ingredient(il_id INTEGER primary key, user_id INTEGER, foreign key(user_id) references user(user_id))");

        DB.execSQL("create Table item_stock(stock_id INTEGER primary key, image TEXT default null, name TEXT, price DECIMAL, weight DECIMAL)");
        DB.execSQL("create Table item_shopping(itemshop_id INTEGER primary key, qty INTEGER, subprice DECIMAL, stock_id INTEGER, sl_id INTEGER," +
                    "foreign key(stock_id) references item_stock(stock_id))" +
                    "foreign key(sl_id) references list_shopping(sl_id))");

        DB.execSQL("create Table item_ingredient(iteming_id INTEGER primary key, name TEXT, weight DECIMAL)");

        DB.execSQL("create Table recipe(recipe_id INTEGER primary key, name TEXT, description TEXT, steps TEXT, rating INTEGER, image TEXT)");
        DB.execSQL("create Table recipe_ingredient(ir_id INTEGER primary key, name TEXT, weight DECIMAL, recipe_id INTEGER, " +
                   "foreign key(recipe_id) references recipe(recipe_id))");

    }
    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int ii) {
        DB.execSQL("drop Table if exists Userdetails");
    }
    public Boolean insertuserdata(String name, String contact, String dob)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("contact", contact);
        contentValues.put("dob", dob);
        long result=DB.insert("Userdetails", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }
    public Boolean updateuserdata(String name, String contact, String dob)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("contact", contact);
        contentValues.put("dob", dob);
        Cursor cursor = DB.rawQuery("Select * from Userdetails where name = ?", new String[]{name});
        if (cursor.getCount() > 0) {
            long result = DB.update("Userdetails", contentValues, "name=?", new String[]{name});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    public Boolean deletedata (String name)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Userdetails where name = ?", new String[]{name});
        if (cursor.getCount() > 0) {
            long result = DB.delete("Userdetails", "name=?", new String[]{name});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Cursor getdata ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Userdetails", null);
        return cursor;
    }
}