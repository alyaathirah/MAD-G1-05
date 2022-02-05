package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    NavController navController;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        DB = new DBHelper(this);
        DB = DBHelper.getInstance(this);
//        createBasicRecipe();
//        DB.createBasicRecipe(this);
//        Intent intent = new Intent(this, HomeFragment.class);
//        intent.putExtra("object", DB);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.fragment);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);



    }
//    void createBasicRecipe(){
//        String[] files = {"ratatouille.txt"};
//        for(int i=0; i<files.length; i++) {
//            try {
//                DB.loadRecipe(getAssets().open("recipetext/"+files[i]));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

}