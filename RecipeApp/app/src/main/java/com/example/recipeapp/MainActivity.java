package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    NavController navController;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.fragment);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        DB = new DBHelper(this);
        String[] steps = {"step1", "step2", "step3", "step4"};
        String[] ingredients = {"flour", "chicken", "oil","cheese","chilli powder"};
        double[] weight = {500, 1000, 10, 5, 0.5};
        DB.createRecipe("String name", "String description",4.5, "String image", steps, ingredients, weight);
    }
}