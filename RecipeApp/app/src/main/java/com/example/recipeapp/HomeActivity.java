package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.fragment);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);


//        LinearLayout starLayout = (LinearLayout) findViewById(R.id.star_layout);
//        List<ImageView> star = new ArrayList<>();
//        starLayout.setBackgroundColor(Color.TRANSPARENT);
//        for(int i=0; i<5; i++){
//            star.add(new ImageView(HomeActivity.this));
//            star.get(i).setImageResource(R.drawable.ic_star);
//            starLayout.addView(star.get(i));
//        }


    }

}