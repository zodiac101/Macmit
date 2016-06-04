package com.example.rohil.macmit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



public class UserFragment extends Fragment {


    View mUserView;
    View mUserProgress;
    TextView textViewName ;
    TextView textViewAddress ;
    TextView textViewEmail ;
    TextView textViewPhone ;
    Toolbar toolbar;
    Button buttonLogOut;
    Button buttonUpdate;



    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);


        textViewName = (TextView)rootView.findViewById(R.id.textViewName);
        textViewAddress = (TextView)rootView.findViewById(R.id.textViewAddress);
        textViewEmail = (TextView)rootView.findViewById(R.id.textViewEmail);
        textViewPhone = (TextView)rootView.findViewById(R.id.textViewPhone);
        buttonLogOut = (Button)rootView.findViewById(R.id.buttonLogOut);
        buttonUpdate = (Button)rootView.findViewById(R.id.buttonUpdateUserInfo);



        Context context = getContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key_user), Context.MODE_PRIVATE);

        final SharedPreferences.Editor editor = sharedPref.edit();

        textViewName.setText(sharedPref.getString("name"," "));
        textViewEmail.setText(sharedPref.getString("email", " "));
        textViewPhone.setText(sharedPref.getString("phoneNumber", " "));
        textViewAddress.setText(sharedPref.getString("address", " "));


        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.clear();
                editor.commit();

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), UserUpdateActivity.class);
                startActivity(intent);

            }
        });

        return rootView;



    }


}
