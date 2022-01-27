package com.example.recipeapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeFragment extends Fragment {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        // Inflate the layout for this fragment

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
                showToast("Add to Favorite");
            }

        });

        //SETUP ADD TO CART BUTTON
        BtnAddToCart = (Button) view.findViewById(R.id.BtnAddToCart);
        BtnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("ADDED TO CART");
            }
        });

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
                gotoUrl("https://www.youtube.com/watch?v=2K_GE4dMRrM");
            }
        });
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

        switch (view.getId()){
            case R.id.CBChicken:
                if (checked)
                    ingredientsRecord.setChicken(9.30);
                else
                    ingredientsRecord.setChicken(0.0);
                break;

            case R.id.CBFlour:
                if (checked)
                    ingredientsRecord.setFlour(2.40);
                else
                    ingredientsRecord.setFlour(0.0);
                break;

            case R.id.CBGarlic:
                if (checked)
                    ingredientsRecord.setGarlic(2.00);
                else
                    ingredientsRecord.setGarlic(0.0);
                break;

            case R.id.CBHoney:
                if (checked)
                    ingredientsRecord.setHoney(12.00);
                else
                    ingredientsRecord.setHoney(0.0);
                break;

            case R.id.CBSoySos:
                if (checked)
                    ingredientsRecord.setSoySos(4.30);
                else
                    ingredientsRecord.setSoySos(0.0);
                break;
            case R.id.CBButter:
                if (checked)
                    ingredientsRecord.setButter(5.00);
                else
                    ingredientsRecord.setButter(0.0);
                break;
        }

        TVTotal.setText("TOTAL: RM " + calculateTotal());

    }
    private String calculateTotal(){
        DecimalFormat format = new DecimalFormat("###,###,##0.00");

        totalPrice = ingredientsRecord.getButter() + ingredientsRecord.getFlour() + ingredientsRecord.getChicken()
                + ingredientsRecord.getGarlic() + ingredientsRecord.getHoney() + ingredientsRecord.getSoySos();
        return format.format(totalPrice);
    }
}