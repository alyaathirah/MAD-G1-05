package com.example.recipeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "Recipe.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("drop Table if exists Recipe");
        DB.execSQL("create Table Userdetails(name TEXT primary key , contact TEXT, dob TEXT)");

        //USER TABLES
        DB.execSQL("create Table user(user_id INTEGER primary key AUTOINCREMENT, username TEXT, email TEXT, phone TEXT, address TEXT, password TEXT, image TEXT)");

        //SHOPPING LIST TABLES
        DB.execSQL("create Table list_shopping(sl_id INTEGER primary key, user_id INTEGER, foreign key(user_id) references user(user_id))");
        DB.execSQL("create Table item_stock(stock_id INTEGER primary key, image TEXT default null, name TEXT, price DECIMAL, weight DECIMAL)"); //items shop selling
        DB.execSQL("create Table item_shopping(itemshop_id INTEGER primary key, qty INTEGER, subprice DECIMAL, stock_id INTEGER, sl_id INTEGER," +
                    "foreign key(stock_id) references item_stock(stock_id), " +
                    "foreign key(sl_id) references list_shopping(sl_id))");

        //INGREDIENT LIST TABLES
        DB.execSQL("create Table list_ingredient(il_id INTEGER primary key, user_id INTEGER, foreign key(user_id) references user(user_id))");
        DB.execSQL("create Table item_ingredient(iteming_id INTEGER primary key, name TEXT, weight DECIMAL)");

        //RECIPE TABLES
        DB.execSQL("create Table recipe(recipe_id INTEGER primary key AUTOINCREMENT, name TEXT, description TEXT, rating INTEGER, image TEXT, user_id INTEGER," +
                    "foreign key(user_id) references user(user_id) )");
        DB.execSQL("create Table recipe_ingredient(ir_id INTEGER, name TEXT, weight DECIMAL, recipe_id INTEGER, " +
                    "primary key(recipe_id, ir_id)," +
                    "foreign key(recipe_id) references recipe(recipe_id))");
        DB.execSQL("create Table step(recipe_id INTEGER, step_id INTEGER, text TEXT," +
                    "primary key(recipe_id, step_id)," +
                    "foreign key(recipe_id) references recipe(recipe_id))");



    }
    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int ii) {
        DB.execSQL("drop Table if exists Recipe");
    }
    public void resetTables(SQLiteDatabase DB){
        DB.execSQL("delete from "+ "recipe");
        DB.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + "recipe" + "'");

        DB.execSQL("delete from "+ "recipe_ingredient");
        DB.execSQL("delete from "+ "step");
    }
    public Boolean createRecipe(String name, String description, double rating, String image, String[] steps, String[] ingredients, double[] weight)
    {//recipe(recipe_id INTEGER primary key AUTOINCREMENT, name TEXT, description TEXT, rating INTEGER, image TEXT)
        SQLiteDatabase DB = this.getWritableDatabase();
//        resetTables(DB);
        //complete reset
//        DB.execSQL("delete from "+ "recipe_ingredient");
//        DB.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + "recipe_ingredient" + "'");

        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("rating", rating);
        contentValues.put("image", image);

        long result=DB.insert("recipe", null, contentValues); //returns recipe_id
        System.out.println("ID: " +result);

        //add ingredients
        for(int i=0; i<ingredients.length; i++) {
            //insert into recipe_ingredient table
            ContentValues ingreContent = new ContentValues();
            ingreContent.put("recipe_id", result);
            ingreContent.put("ir_id", i);
            ingreContent.put("name", ingredients[i]);
            ingreContent.put("weight", weight[i]);
            DB.insert("recipe_ingredient", null, ingreContent);
        }
        //add steps
        for(int i=0; i<steps.length; i++) {
            //insert into step table
            ContentValues stepContent = new ContentValues();
            stepContent.put("recipe_id", result);
            stepContent.put("step_id", i);
            stepContent.put("text", steps[i]);
            DB.insert("step", null, stepContent);
        }



        //get recipe table
        Cursor cursor = DB.rawQuery("Select * from recipe where recipe_id = "+result, null);
        System.out.println("HELLO");

        while(cursor.moveToNext()){
            System.out.println("Id :"+cursor.getString(0)+"\n");
            System.out.println("Name :"+cursor.getString(1)+"\n");
            System.out.println("Desc :"+cursor.getString(2)+"\n");
            System.out.println("Steps :"+cursor.getString(3)+"\n");
            System.out.println("Rating :"+cursor.getString(4)+"\n");
            System.out.println("Image :"+cursor.getString(5)+"\n");
        }
        cursor = DB.rawQuery("Select * from step where recipe_id = "+result, null);
        while(cursor.moveToNext()){
            System.out.println();
            System.out.println("Recipe Id :"+cursor.getString(0)+"\n");
            System.out.println("Step Id :"+cursor.getString(1)+"\n");
            System.out.println("Step Text :"+cursor.getString(2)+"\n");
        }

        cursor = DB.rawQuery("Select * from recipe_ingredient where recipe_id = "+result, null);
        while(cursor.moveToNext()){
            System.out.println();
            System.out.println("Table Id :"+cursor.getString(0)+"\n");
            System.out.println("Name :"+cursor.getString(1)+"\n");
            System.out.println("Weight :"+cursor.getString(2)+"\n");
            System.out.println("Recipe Id :"+cursor.getString(3)+"\n");
        }

        if(result==-1){
            return false;
        }else{
            return true;
        }


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