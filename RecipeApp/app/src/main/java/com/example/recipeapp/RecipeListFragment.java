package com.example.recipeapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.FloatRange;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeListFragment extends Fragment {
    DBHelper DB;
    User user = User.getInstance();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeListFragment newInstance(String param1, String param2) {
        RecipeListFragment fragment = new RecipeListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        DB = DBHelper.getInstance(getActivity());
        DB = DBHelper.getInstance();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        //test data alya
        Random rand = new Random();
//        DB.addLikedRecipe(user.id, rand.nextInt(4)+1);

        //need image, name, rating, difficulty, duration
        ArrayList<Integer> recipe_id = DB.getLikedRecipes(user.id);
        ArrayList<String> recipe_name = new ArrayList<>();
        ArrayList<Double> recipe_rating = new ArrayList<>();
        ArrayList<String> recipe_difficulty = new ArrayList<>();
        ArrayList<Integer> recipe_duration = new ArrayList<>();
        ArrayList<Bitmap> recipe_image = new ArrayList<>();

        for(int i=0; i<recipe_id.size(); i++){
            Cursor cursor = DB.getRecipe(recipe_id.get(i));
            while(cursor.moveToNext()){
                recipe_name.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                recipe_rating.add(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("rating"))));
                recipe_difficulty.add(cursor.getString(cursor.getColumnIndexOrThrow("difficulty")));
                recipe_duration.add(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("duration"))));

                byte[] byteImage = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                recipe_image.add(BitmapFactory.decodeByteArray(byteImage, 0 ,byteImage.length));
            }
        }
        LinearLayout recipeList1 = (LinearLayout) view.findViewById(R.id.recipe_sublist1);
        LinearLayout recipeList2 = (LinearLayout) view.findViewById(R.id.recipe_sublist2);
        for(int i=0; i<recipe_id.size(); i++){
            View recipeCard = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_card, container, false);
            //set recipe name
            TextView name = (TextView)recipeCard.findViewById(R.id.recipe_name);
            name.setText(recipe_name.get(i));
            //set rating
            LinearLayout starLayout = (LinearLayout)recipeCard.findViewById(R.id.star_layout);
            List<ImageView> star = new ArrayList<>();
            starLayout.setBackgroundColor(Color.TRANSPARENT);
            for(int j=0; j<5; j++){
                star.add(new ImageView(getActivity()));
                if(recipe_rating.get(i) - j >= 1)
                    star.get(j).setImageResource(R.drawable.ic_star);
                else if(recipe_rating.get(i) - j > 0)
                    star.get(j).setImageResource(R.drawable.ic_star_half);
                else
                    star.get(j).setImageResource(R.drawable.ic_star_border);
                starLayout.addView(star.get(j));
            }
            //set difficulty
            TextView difficulty = (TextView)recipeCard.findViewById(R.id.difficulty);
            difficulty.setText(recipe_difficulty.get(i));
            //set duration
            TextView duration = (TextView)recipeCard.findViewById(R.id.cooking_duration);
            duration.setText(recipe_duration.get(i).toString()+" mins");
            //set image
            ImageView imageFood = (ImageView)recipeCard.findViewById(R.id.recipe_image);
            imageFood.setImageBitmap(recipe_image.get(i));

            if(i%2 == 0) {
                recipeList1.addView(recipeCard);
                View spacing = new View(getActivity());
                spacing.setMinimumHeight(30);
                recipeList1.addView(spacing);
            }
            else{
                recipeList2.addView(recipeCard);
                View spacing = new View(getActivity());
                spacing.setMinimumHeight(30);
                recipeList2.addView(spacing);
            }

            //set onclick on recipeCard layout
            int finalI = i;
            recipeCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getContext(), String.valueOf(finalI), Toast.LENGTH_LONG).show();
                    RecipeFragment testFragment = new RecipeFragment();
                    testFragment.setRecipeID(recipe_id.get(finalI)); //send recipeID
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.recipe_list_container, testFragment); // home_container is your FrameLayout container
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
        }
        FloatingActionButton addRecipeBtn = view.findViewById(R.id.addRecipeButton);
        addRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go Update recipe activity
                Intent myIntent = new Intent(getActivity(), UpdateRecipe.class);
                startActivity(myIntent);
            }
        });

        return view;
    }
}