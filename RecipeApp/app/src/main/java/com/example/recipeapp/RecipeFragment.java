package com.example.recipeapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeFragment extends Fragment {
    DBHelper DB;
    User user;
    private int recipeID = 0;
    IngredientsRecord ingredientsRecord;
    double totalPrice;
    TextView TVTotal;

    private Button alertBtn;
    Button BtnAddToCart, BtnYoutube;
    private int PERMISSION_CODE = 1;

    RatingBar ratingStar;
    final float[] myRating = {0};

    FloatingActionButton BtnShare, BtnLike, BtnAdd, BtnFAQ;
    Boolean isFabOpen = false;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Integer recipe_id;
    private String recipe_name;
    private Double recipe_rating;
    private String recipe_difficulty;
    private Integer recipe_duration;
    private String recipe_url;
    String recipe_description;
    Bitmap recipe_image;


    public RecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeFragment newInstance(String param1, String param2) {
        RecipeFragment fragment = new RecipeFragment();
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
    public void setRecipeID(int finalI) {
        recipeID = finalI;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        //get database and cursor alya
        DB = DBHelper.getInstance(getActivity());
        user = User.getInstance();
        Cursor cursor = DB.getRecipe(recipeID);

        while(cursor.moveToNext()){//every row
            recipe_id = (Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("recipe_id"))));
            recipe_name = (cursor.getString(cursor.getColumnIndexOrThrow("name")));
            recipe_rating = (Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("rating"))));
            recipe_difficulty = (cursor.getString(cursor.getColumnIndexOrThrow("difficulty")));
            recipe_duration = (Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("duration"))));
            recipe_description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            recipe_url = cursor.getString(cursor.getColumnIndexOrThrow("url"));

            byte[] byteImage = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
            Bitmap recipe_image = BitmapFactory.decodeByteArray(byteImage, 0 ,byteImage.length);

            ImageView foodImage = view.findViewById(R.id.IVFood);
            foodImage.setImageBitmap(recipe_image);

        }
        //get steps from database by calling recipe id
        ArrayList<String> step_text = new ArrayList<>();
        ArrayList<Integer> step_id = new ArrayList<>();
        cursor = DB.getSteps(recipeID);
        while(cursor.moveToNext()){//multiple row of steps
            step_id.add(cursor.getInt(cursor.getColumnIndexOrThrow("step_id")));
            step_text.add(cursor.getString(cursor.getColumnIndexOrThrow("text")));
        }
        //input steps in instructions list
        LinearLayout steplist = view.findViewById(R.id.step_list_layout);
        for(int i=0; i<step_id.size(); i++){
            TextView stepText = new TextView(getActivity());
            stepText.setText((step_id.get(i)+1)+". "+step_text.get(i));
            stepText.setTextColor(getResources().getColor(R.color.colorPrimaryText));
            stepText.setTextSize(14);

            stepText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            stepText.setPadding(30, 10, 80, 10);
            steplist.addView(stepText);
        }

        //get ingredient from database by calling recipe id
        ArrayList<Integer> ingredient_id = new ArrayList<>();
        ArrayList<String> ingredient_name = new ArrayList<>();
        ArrayList<Double> ingredient_quantity = new ArrayList<>();
        ArrayList<String> ingredient_unit = new ArrayList<>();

        cursor = DB.getIngredients(recipeID);
        while(cursor.moveToNext()){//multiple row of steps
            ingredient_id.add(cursor.getInt(cursor.getColumnIndexOrThrow("ir_id")));
            ingredient_name.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            ingredient_quantity.add(cursor.getDouble(cursor.getColumnIndexOrThrow("quantity")));
            ingredient_unit.add(cursor.getString(cursor.getColumnIndexOrThrow("unit")));
        }

        LinearLayout ingredientlist = view.findViewById(R.id.ingredient_list_layout);
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        for(int i=0; i<ingredient_id.size(); i++){
            CheckBox checkBox = new CheckBox(getActivity());
            checkBox.setPadding(20,5,20,5);
            checkBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            checkBox.setTextSize(16);
            DecimalFormat format = new DecimalFormat("0.#");
            if(ingredient_unit.get(i) == null)
                checkBox.setText(ingredient_name.get(i)+" "+format.format(ingredient_quantity.get(i)));
            else
                checkBox.setText(ingredient_name.get(i)+" "+format.format(ingredient_quantity.get(i))+" "+ingredient_unit.get(i));

            checkBoxes.add(checkBox);
            ingredientlist.addView(checkBoxes.get(i));
        }

        //SETUP ADD TO CART BUTTON
        BtnAddToCart = (Button) view.findViewById(R.id.BtnAddToCart);
        BtnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("ADDED TO INGREDIENT LIST");
                for(int i=0; i<checkBoxes.size(); i++){
                    if(checkBoxes.get(i).isChecked())
                        DB.addIngredientToList(user.id, ingredient_id.get(i));
                }
            }
        });
        //click checkbox onclick alya


        //fatini
        ButterKnife.bind(getActivity());
//        setupActionBar();


        //SETUP FAB BUTTON
        BtnShare = view.findViewById(R.id.BtnShare);
        BtnLike = view.findViewById(R.id.BtnLike);
        BtnAdd = view.findViewById(R.id.BtnAdd);
        BtnFAQ = view.findViewById(R.id.BtnFAQ);

        BtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFabOpen){
                    showFabMenu();
                }else{
                    closeFabMenu();
                }
            }
        });

        //SETUP SHARE BUTTON
        BtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String Body = "Download this App";
                String Sub = "http://play.google.com";
                intent.putExtra(Intent.EXTRA_TEXT, Body);
                intent.putExtra(Intent.EXTRA_TEXT, Sub);
                startActivity(Intent.createChooser(intent, "Share using"));

            }
        });

        //SETUP LIKE BUTTON
        BtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //alya
                DB.addLikedRecipe(user.id, recipeID);
                showToast("Add to Favorite");
            }

        });

//        //SETUP ADD TO CART BUTTON
//        BtnAddToCart = (Button) view.findViewById(R.id.BtnAddToCart);
//        BtnAddToCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showToast("ADDED TO CART");
//            }
//        });

        // SETUP RATING BAR
        ratingStar = view.findViewById(R.id.BarRate);
        ratingStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int rate = (int) rating;
                String message = null;

                myRating[0] = ratingBar.getRating();

                switch (rate){
                    case 1:
                        message = "Sorry to hear that! :(";
                        break;

                    case 2:
                        message = "We always accept any suggestions to improve!";
                        break;

                    case 3:
                        message = "Good enough!";
                        break;

                    case 4:
                        message = "Great! Thank You.";
                        break;

                    case 5:
                        message = "Awesome!";
                        break;
                }

                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });

        //SETUP INGREDIENT
        ingredientsRecord = new IngredientsRecord();
        TVTotal = view.findViewById(R.id.TVTotal);

        //SETUP ALERT BUTTON FOR INGREDIENTS
        alertBtn = (Button) view.findViewById(R.id.BtnInfo);
        alertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("IMPORTANT INFORMATION: ");
                builder.setMessage("Ingredients other than CHICKEN, MEAT, and FISH are sold in a certain amount of grams or oz.\n" +
                        "\nPlease refer FAQs for more details.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });

        //SETUP YOUTUBE BUTTON
        BtnYoutube = (Button) view.findViewById(R.id.BtnYoutube);
        BtnYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl(recipe_url);
            }
        });

        //alya
        TextView recipeName = view.findViewById(R.id.TVFoodName);
        recipeName.setText(recipe_name);

        TextView descDetails = view.findViewById(R.id.TVDescDetails);
        descDetails.setText(recipe_description);
        return view;

    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(webIntent);
    }

    //TOAST FOR FAVORITE and ADD_TO_CART BUTTON
    private void showToast(String message) {
        Toast.makeText(this.getContext(),message,Toast.LENGTH_SHORT).show();
    }

    //FAB BUTTON ANIMATION
    private void showFabMenu() {
        isFabOpen = true;
        BtnAdd.animate().rotationBy(45);
        BtnFAQ.animate().translationY(-getResources().getDimension(R.dimen.standard_70));
        BtnShare.animate().translationY(-getResources().getDimension(R.dimen.standard_140));
        BtnLike.animate().translationY(-getResources().getDimension(R.dimen.standard_210));
    }
    private void closeFabMenu() {
        isFabOpen = false;
        BtnAdd.animate().rotationBy(45);
        BtnFAQ.animate().translationY(0);
        BtnShare.animate().translationY(0);
        BtnLike.animate().translationY(0);
    }

    //SETUP ACTION BAR
    private void setupActionBar(){
        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorWhite));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorWhite));
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }

        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    private void setSupportActionBar(Toolbar toolbar) {
    }

    /***
     @Override
     public boolean onCreateOptionsMenu(Menu menu){
     getMenuInflater().inflate(R.menu.menu_detail, menu);
     MenuItem favoriteItem = menu.findItem(R.id.favorite);
     Drawable favoriteItemColor = favoriteItem.getIcon();
     setupColorActionBarIcon(favoriteItemColor);
     return true;

     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item){
     switch (item.getItemId()){
     case android.R.id.home:
     onBackPressed();
     return true;
     default:
     return super.onOptionsItemSelected(item);
     }
     }
     ***/

    //INGREDIENTS
    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

//        switch (view.getId()){
//            case R.id.CBChicken:
//                if (checked)
//                    ingredientsRecord.setChicken(9.30);
//                else
//                    ingredientsRecord.setChicken(0.0);
//                break;
//
//        }

        TVTotal.setText("TOTAL: RM " + calculateTotal());

    }
    private String calculateTotal(){
        DecimalFormat format = new DecimalFormat("###,###,##0.00");

        totalPrice = ingredientsRecord.getButter() + ingredientsRecord.getFlour() + ingredientsRecord.getChicken()
                + ingredientsRecord.getGarlic() + ingredientsRecord.getHoney() + ingredientsRecord.getSoySos();
        return format.format(totalPrice);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}