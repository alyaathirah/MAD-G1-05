package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {
    User user = User.getInstance();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        if(user.id == -1) {//if user null
            //go login activity
            Intent myIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(myIntent);
        }

        TextView usernameView = view.findViewById(R.id.pro_username);
        usernameView.setText(user.username);
        TextView userIDView = view.findViewById(R.id.pro_userid);
        userIDView.setText(String.valueOf(user.id));
        TextView emailView = view.findViewById(R.id.pro_emailid);
        emailView.setText(user.email);

        TextView logoutView = view.findViewById(R.id.logoutBtn);
        logoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
                showToast("Logged Out");
                //go login activity
                Intent myIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(myIntent);
            }
        });
        return view;
    }
    private void showToast(String message) {
        Toast.makeText(this.getContext(),message,Toast.LENGTH_SHORT).show();
    }
    public void logOut(){
        user.clearData();
    }
}