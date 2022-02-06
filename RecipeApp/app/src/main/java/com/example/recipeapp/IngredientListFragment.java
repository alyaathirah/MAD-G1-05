package com.example.recipeapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IngredientListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientListFragment extends Fragment {
    DBHelper DB;
    User user;
    Cursor cursor;
    ArrayList<CheckBox> checkBoxes;
    //get ingredient from database by calling recipe id
    LinearLayout ingredientlist;
    ArrayList<Integer> ingredient_id = new ArrayList<>();
    ArrayList<String> ingredient_name = new ArrayList<>();
    ArrayList<Double> ingredient_quantity = new ArrayList<>();
    ArrayList<String> ingredient_unit = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IngredientListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IngredientListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IngredientListFragment newInstance(String param1, String param2) {
        IngredientListFragment fragment = new IngredientListFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

        //go create recipe activity
//        Intent myIntent = new Intent(getActivity(), UpdateRecipe.class);
//        startActivity(myIntent);
        DB = DBHelper.getInstance(getActivity());
        user = User.getInstance();
        ingredientlist = view.findViewById(R.id.ingredient_list_user);

        cursor = DB.getIngredientList(user.id);
        while(cursor.moveToNext()){//multiple row of steps
            ingredient_id.add(cursor.getInt(cursor.getColumnIndexOrThrow("iteming_id")));
            ingredient_name.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            ingredient_quantity.add(cursor.getDouble(cursor.getColumnIndexOrThrow("quantity")));
            ingredient_unit.add(cursor.getString(cursor.getColumnIndexOrThrow("unit")));
        }

        checkBoxes = new ArrayList<>();
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
        Button BtnClear = (Button) view.findViewById(R.id.BtnClearIng);
        BtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB.clearAllIngredients(user.id);
                cursor = DB.getIngredientList(user.id);
                showToast("INGREDIENT LIST CLEARED");
                IngredientListFragment frg = new IngredientListFragment();
                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        cursor = DB.getIngredientList(user.id);
//        while(cursor.moveToNext()){//multiple row of steps
//            ingredient_id.add(cursor.getInt(cursor.getColumnIndexOrThrow("iteming_id")));
//            ingredient_name.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
//            ingredient_quantity.add(cursor.getDouble(cursor.getColumnIndexOrThrow("quantity")));
//            ingredient_unit.add(cursor.getString(cursor.getColumnIndexOrThrow("unit")));
//        }
//
//        checkBoxes = new ArrayList<>();
//        for(int i=0; i<ingredient_id.size(); i++){
//            CheckBox checkBox = new CheckBox(getActivity());
//            checkBox.setPadding(20,5,20,5);
//            checkBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            checkBox.setTextSize(16);
//            DecimalFormat format = new DecimalFormat("0.#");
//            if(ingredient_unit.get(i) == null)
//                checkBox.setText(ingredient_name.get(i)+" "+format.format(ingredient_quantity.get(i)));
//            else
//                checkBox.setText(ingredient_name.get(i)+" "+format.format(ingredient_quantity.get(i))+" "+ingredient_unit.get(i));
//
//            checkBoxes.add(checkBox);
//            ingredientlist.addView(checkBoxes.get(i));
//        }
    }

    //TOAST FOR FAVORITE and ADD_TO_CART BUTTON
    private void showToast(String message) {
        Toast.makeText(this.getContext(),message,Toast.LENGTH_SHORT).show();
    }
}