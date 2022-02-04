package com.example.recipeapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    DBHelper DB;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayout recipeList1 = (LinearLayout) view.findViewById(R.id.recipe_sublist1);
        LinearLayout recipeList2 = (LinearLayout) view.findViewById(R.id.recipe_sublist2);
        for(int i=0; i<15; i++){
            View recipeCard = LayoutInflater.from(getActivity()).inflate(R.layout.recipe_card, container, false);
            TextView name = (TextView)recipeCard.findViewById(R.id.recipe_name);
            name.setText("Recipe Name "+i);

            if(i%2 == 0) {
                recipeList1.addView(recipeCard);
                View spacing = new View(getActivity());
                spacing.setMinimumHeight(20);
                recipeList1.addView(spacing);
            }
            else{
                recipeList2.addView(recipeCard);
                View spacing = new View(getActivity());
                spacing.setMinimumHeight(20);
                recipeList2.addView(spacing);
            }

            //set onclick on recipeCard layout
            int finalI = i;
            recipeCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getContext(), String.valueOf(finalI), Toast.LENGTH_LONG).show();
                    TestFragment testFragment = new TestFragment();
                    testFragment.setRecipeID(finalI);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.home_container, testFragment); // f2_container is your FrameLayout container
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
        }


//        TextView textView = (TextView) view.findViewById(R.id.my_image);

//        LinearLayout starLayout = (LinearLayout) view.findViewById(R.id.star_layout);
//        List<ImageView> star = new ArrayList<>();
//        starLayout.setBackgroundColor(Color.TRANSPARENT);
//        for(int i=0; i<5; i++){
//            star.add(new ImageView(getActivity()));
//            star.get(i).setImageResource(R.drawable.ic_star);
//            starLayout.addView(star.get(i));
//        }

        return view;
    }
}