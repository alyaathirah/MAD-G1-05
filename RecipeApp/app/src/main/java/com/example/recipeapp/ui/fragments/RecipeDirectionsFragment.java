package com.example.recipeapp.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeDirectionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDirectionsFragment extends NavigableFragment {
    private DirectionsListener mListener;
    private List<Direction> directionList;
    private DirectionAdapter directionAdapter;
    private DatabaseAdapter databaseAdapter;

    private RecyclerView directionRecyclerView;
    private TextView emptyView;
    private Button addButton;
    private EditText directionField;

    public RecipeDirectionsFragment() {
        // Required empty public constructor
    }

    public static RecipeDirectionsFragment newInstance(Recipe recipe) {
        RecipeDirectionsFragment fragment = new RecipeDirectionsFragment();

        if (recipe != null) {
            Bundle args = new Bundle();
            args.putParcelableArrayList("directions", (ArrayList<Direction>) recipe.getDirections());
            fragment.setArguments(args);
        }

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_directions, container, false);
        databaseAdapter = DatabaseAdapter.getInstance(getActivity());

        Bundle args = getArguments();
        if (args != null)
            directionList = args.getParcelableArrayList("directions");
        if (directionList == null)
            directionList = new ArrayList<>();

        directionRecyclerView = view.findViewById(R.id.recyclerView);
        emptyView = view.findViewById(R.id.empty_view);
        addButton = view.findViewById(R.id.add_button);
        directionField = view.findViewById(R.id.directionField);
        directionAdapter = new DirectionAdapter(getActivity(), directionList);
        directionAdapter.setDirectionListener(position -> {
            directionList.remove(position);
            toggleEmptyView();
            directionAdapter.notifyDataSetChanged();
        });

        toggleEmptyView();

        directionRecyclerView.setHasFixedSize(true);
        directionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        directionRecyclerView.setAdapter(directionAdapter);

        addButton.setOnClickListener(v -> {
            Log.i("DAO", "Add button pressed.");
            String newDirection = directionField.getText().toString();
            Log.i("DAO", "New direction: " + newDirection);
            if (!newDirection.isEmpty()) {
                Log.i("DAO", "Directions list BEFORE: " + directionList);
                directionField.setText("");
                directionList.add(new Direction(newDirection));
                Log.i("DAO", "Directions list AFTER: " + directionList);
                toggleEmptyView();
                directionAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void toggleEmptyView() {
        if (directionList.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            directionRecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            directionRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DirectionsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DirectionsListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onNext() {
        if (mListener != null) {
            Log.i("DAO", "Steps finished: " + directionList);
            mListener.onStepsFinished(directionList);
        }
    }

    public interface DirectionsListener {
        void onStepsFinished(List<Direction> directions);
    }
}