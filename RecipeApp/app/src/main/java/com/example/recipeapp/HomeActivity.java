package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        LinearLayout starLayout = (LinearLayout) findViewById(R.id.star_layout);
        List<ImageView> star = new ArrayList<>();
        starLayout.setBackgroundColor(Color.TRANSPARENT);
        for(int i=0; i<5; i++){
            star.add(new ImageView(HomeActivity.this));
            star.get(i).setImageResource(R.drawable.ic_star);
            starLayout.addView(star.get(i));
        }


    }

}