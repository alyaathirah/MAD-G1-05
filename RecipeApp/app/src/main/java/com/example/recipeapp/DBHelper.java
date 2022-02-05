package com.example.recipeapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper{
    private static DBHelper mInstance = null;
    Context context;
    public DBHelper(Context context) {
        super(context, "Recipe.db", null, 1);
        this.context = context;
    }
    public static DBHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DBHelper(ctx.getApplicationContext());

        }
        if(doesDatabaseExist(ctx, "Recipe.db") == false)
            mInstance.createBasicRecipe(ctx);
        return mInstance;
    }
    private static boolean doesDatabaseExist(Context context, String dbName) { //prevent multiple creation of basic recipes
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    void createBasicRecipe(Context context){
        String[] files = {"ratatouille.txt","easy_italian_sausage_spaghetti.txt","gourmet_mushroom _risotto.txt","homemade_lasagna.txt"};
//        String[] files = {"ratatouille.txt"};
        for(int i=0; i<files.length; i++) {
            try {
                loadRecipe(context.getAssets().open("recipetext/"+files[i]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onCreate(SQLiteDatabase DB) { //one time only

//        DB.execSQL("drop Table if exists Recipe");
//        DB.execSQL("create Table Userdetails(name TEXT primary key , contact TEXT, dob TEXT)");

        //USER TABLES
        DB.execSQL("create Table user(user_id INTEGER primary key AUTOINCREMENT, username TEXT, email TEXT, phone TEXT, address TEXT, password TEXT, image TEXT)");

        //SHOPPING LIST TABLES
        DB.execSQL("create Table list_shopping(sl_id INTEGER primary key, user_id INTEGER, foreign key(user_id) references user(user_id))");
        DB.execSQL("create Table item_stock(stock_id INTEGER primary key, image TEXT default null, name TEXT, price DECIMAL, weight DECIMAL)"); //items shop selling
        DB.execSQL("create Table item_shopping(itemshop_id INTEGER primary key, qty INTEGER, subprice DECIMAL, stock_id INTEGER, sl_id INTEGER," +
                    "foreign key(stock_id) references item_stock(stock_id), " +
                    "foreign key(sl_id) references list_shopping(sl_id))");

        //LIKED RECIPE
        DB.execSQL("create Table liked_recipe(liked_id INTEGER primary key AUTOINCREMENT, user_id INTEGER, recipe_id INTEGER," +
                    "foreign key(user_id) references user(user_id)," +
                    "foreign key(recipe_id) references recipe(recipe_id))");

        //INGREDIENT LIST TABLES
        DB.execSQL("create Table list_ingredient(il_id INTEGER primary key, user_id INTEGER, foreign key(user_id) references user(user_id))");
        DB.execSQL("create Table item_ingredient(iteming_id INTEGER primary key, name TEXT, weight DECIMAL)");

        //RECIPE TABLES
        DB.execSQL("create Table recipe(recipe_id INTEGER primary key AUTOINCREMENT, name TEXT, description TEXT, rating INTEGER, image BLOB, user_id INTEGER, url TEXT, duration INTEGER, difficulty INTEGER, tags TEXT," +
                    "foreign key(user_id) references user(user_id) )");
        DB.execSQL("create Table recipe_ingredient(ir_id INTEGER, name TEXT, quantity DECIMAL, recipe_id INTEGER, unit TEXT," +
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
    public SQLiteDatabase getDB(){
        return this.getWritableDatabase();
    }
    public Boolean createRecipe(String name, String description, double rating, byte[] image, String url, int duration, String difficulty, String tags, String[] steps, String[] ingredients, double[] quantity, String[] unit)
    {//recipe(recipe_id INTEGER primary key AUTOINCREMENT, name TEXT, description TEXT, rating INTEGER, image TEXT,duration INTEGER, difficulty INTEGER, tags TEXT)
        SQLiteDatabase DB = this.getWritableDatabase();
//        resetTables(DB);

        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("rating", rating);
        contentValues.put("image", image);
        contentValues.put("url", url);
        contentValues.put("duration", duration);
        contentValues.put("difficulty", difficulty);
        contentValues.put("tags", tags);

        long result=DB.insert("recipe", null, contentValues); //returns recipe_id
        System.out.println("ID: " +result);

        //add ingredients
        for(int i=0; i<ingredients.length; i++) {
            //insert into recipe_ingredient table
            ContentValues ingreContent = new ContentValues();
            ingreContent.put("recipe_id", result);
            ingreContent.put("ir_id", i);
            ingreContent.put("name", ingredients[i]);
            ingreContent.put("quantity", quantity[i]);
            ingreContent.put("unit", unit[i]);
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
        getRecipe((int) result);
        getSteps((int) result);
        getIngredients((int) result);


        if(result==-1){
            return false;
        }else{
            return true;
        }
    }
    public void addLikedRecipe(int userID, int recipeID){//liked_id INTEGER primary key AUTOINCREMENT, user_id INTEGER, recipe_id INTEGER
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("user_id", userID);
        contentValues.put("recipe_id", recipeID);

        long result=DB.insert("liked_recipe", null, contentValues); //returns liked_id
        getLikedRecipes(userID);//test
    }
    public Cursor getLikedRecipes(int userID){//return all recipe_id
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from recipe where recipe.recipe_id = liked_recipe.recipe_id and user_id = "+userID, null);
        return cursor;
    }
    public Cursor getRecipe(int recipeID){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from recipe where recipe_id = "+recipeID, null);
        return cursor;
    }
    public Cursor getRecipeAll(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from recipe", null);
//        Cursor cursor = DB.rawQuery("Select * from recipe where recipe_id < 2", null);
        return cursor;
    }

    public Cursor getSteps(int recipeID){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from step where recipe_id = "+recipeID, null);
        return cursor;
    }
    public Cursor getIngredients(int recipeID){
//        ir_id INTEGER, name TEXT, quantity DECIMAL, recipe_id INTEGER, unit TEXT
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from recipe_ingredient where recipe_id = "+recipeID, null);
        return cursor;
    }


    public void loadRecipe(InputStream p) {
        SQLiteDatabase DB = this.getWritableDatabase();
        //read from file
        InputStream is = p;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        int insertOrder = 0;
        ArrayList<String> recipeArr = new ArrayList<>();
        ArrayList<String> stepArr = new ArrayList<>();
        ArrayList<String> ingredientArr = new ArrayList<>();

        while (true) {
            try {
                if (!((line = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
//            System.out.println(line);//output line
            if(line.equalsIgnoreCase(".recipe")){
                insertOrder = 0;
                continue;
            }
            if(line.equalsIgnoreCase(".ingredient")){
                insertOrder = 1;
                continue;
            }
            if(line.equalsIgnoreCase(".steps")){
                insertOrder = 2;
                continue;
            }

            switch(insertOrder){
                case 0:
                    recipeArr.add(line);
                    break;
                case 1:
                    ingredientArr.add(line);
                    break;
                case 2:
                    stepArr.add(line);
                    break;
            }

        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //starts entering into db
        //recipe array elements -> / ingredient(array elements -> array[](by commas) / steps array elements ->
        //createRecipe(String name, String description, double rating, String image, String url, String[] steps, String[] ingredients, double[] quantity, String[] unit)
        String name = recipeArr.get(0);
        String description = recipeArr.get(1);
        double rating = Double.parseDouble(recipeArr.get(2));
        byte[] image = convertImageToBlob(recipeArr.get(3));

        String url = recipeArr.get(4);
        int duration = Integer.parseInt(recipeArr.get(5));
        String difficulty = recipeArr.get(6);
        String tags = recipeArr.get(7);

        String[] steps = stepArr.toArray(new String[stepArr.size()]);

        String[] ingredients = new String[ingredientArr.size()];
        double[] quantity = new double[ingredientArr.size()];
        String[] unit = new String[ingredientArr.size()];

        for(int i=0; i<ingredientArr.size(); i++){
            String[] arr = ingredientArr.get(i).split(",");
            try {
                ingredients[i] = arr[0];
                quantity[i] = Double.parseDouble(arr[1]);
                unit[i] = arr[2];
            }catch (ArrayIndexOutOfBoundsException e){
                continue;
            }
        }
        createRecipe(name, description, rating, image, url, duration,  difficulty,  tags, steps, ingredients, quantity, unit);
    }

    public byte[] convertImageToBlob(String filePath){
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open("images/"+filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if(filePath.contains(".png"))
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        if(filePath.contains(".jpg") || filePath.contains(".jpeg"))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int maxSize = 100;
        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        byte[] bArray = bos.toByteArray();

        return bArray;
    }
    public Cursor getdata ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Userdetails", null);
        return cursor;
    }
}