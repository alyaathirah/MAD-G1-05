package com.example.recipeapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText usernameView;
    private EditText emailView;
    private EditText passView;
    private EditText rePassView;
    private Button register;

    DBHelper DB = DBHelper.getInstance();
    private int userID;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);

//        myButton.setEnabled(false);
        usernameView = view.findViewById(R.id.et_name);
        emailView = view.findViewById(R.id.et_email);
        passView = view.findViewById(R.id.et_password);
        rePassView = view.findViewById(R.id.et_repassword);

        register = (Button) view.findViewById(R.id.btn_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkViewEntered()) {
                    if(DB.checkEmailExist(emailView.getText().toString())){
                        Toast.makeText(getActivity(), "Email already exists", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    else if (passView.getText().toString().equals(rePassView.getText().toString())) {
                        userID = DB.createUser(usernameView.getText().toString(), emailView.getText().toString(), passView.getText().toString());
                    }
//                user.setUserData(userID,usernameView.getText().toString(), emailView.getText().toString(), passView.getText().toString());
                    System.out.println(userID);
                }
            }
        });

        return view;
    }
    public boolean checkViewEntered(){
        if(TextUtils.isEmpty(usernameView.getText())) {
            return false;
        }
        if(TextUtils.isEmpty(emailView.getText())) {
            return false;
        }
        if(TextUtils.isEmpty(passView.getText())) {
            return false;
        }
        if(TextUtils.isEmpty(rePassView.getText())) {
            return false;
        }

        return true;
    }
}