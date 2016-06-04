package com.example.rohil.macmit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private View mProgress_SignUp_View;
    private View mSignUpView;
    private boolean isMapClicked = false;
    private LatLng mSelectedCoordinates;
    private UserSignUpTask mAuthTask = null;
    EditText editTextEmail ;
    EditText editTextPassword ;
    EditText editTextName;
    EditText editTextPhone ;
    EditText editTextAddress ;

    JSONParser jsonParser = new JSONParser();


    private static String url_register = "http://127.0.0.1:8000/project1/v1/register";

    // JSON Node names




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button mButton = (Button) findViewById(R.id.sign_up_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mSignUpView = findViewById(R.id.sign_up_form);
        mProgress_SignUp_View = findViewById(R.id.sign_up_progress);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextName = (EditText) findViewById(R.id.name);
        editTextPhone = (EditText) findViewById(R.id.phoneNumber);
        editTextAddress = (EditText) findViewById(R.id.address);

        Intent intent = getIntent();
        editTextEmail.setText(intent.getStringExtra("email"));
        editTextPassword.setText(intent.getStringExtra("password"));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_sign_up);
        mapFragment.getMapAsync(this);
        mMap=mapFragment.getMap();
        mMap.setMyLocationEnabled(true);
        setUpMap();



    }

    private void setUpMap() {

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        if (isMapClicked == false){
            Toast.makeText(SignUpActivity.this, "Please choose your address on the map", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true);

        String Email = editTextEmail.getText().toString();
        String Pass = editTextPassword.getText().toString();
        String Name = editTextName.getText().toString();
        String Phone = editTextPhone.getText().toString();
        String Address = editTextAddress.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(Pass) && !isPasswordValid(Pass)) {
            editTextPassword.setError(getString(R.string.error_invalid_password));
            focusView = editTextPassword;
            cancel = true;
        }
        else if (TextUtils.isEmpty(Pass)){

            editTextPassword.setError(getString(R.string.error_field_required));
            focusView = editTextPassword;
            cancel = true;
        }

        if (!TextUtils.isEmpty(Email) && !isEmailValid(Email)) {
            editTextEmail.setError(getString(R.string.error_invalid_password));
            focusView = editTextEmail;
            cancel = true;
        }
        else if (TextUtils.isEmpty(Email)){

            editTextEmail.setError(getString(R.string.error_field_required));
            focusView = editTextEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserSignUpTask(mSelectedCoordinates, Email, Pass, Name, Phone, Address);
            mAuthTask.execute((Void) null);
        }




    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mSignUpView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignUpView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignUpView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgress_SignUp_View.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgress_SignUp_View.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgress_SignUp_View.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgress_SignUp_View.setVisibility(show ? View.VISIBLE : View.GONE);
            mSignUpView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

        mMap.clear();
        mSelectedCoordinates = latLng;
        mMap.addMarker(new MarkerOptions()
                .title("Location")
                .snippet("Your Addrdd")
                .position(latLng));
        isMapClicked=true;

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    public class UserSignUpTask extends AsyncTask<Void, Void, Boolean> {

        private final LatLng selectedLocation;
        String Email ;
        String Pass ;
        String Name ;
        String Phone ;
        String Address ;
        String uid;
        private volatile boolean value;
        JSONObject json;

        UserSignUpTask(LatLng latLng, String email, String pass, String name, String phone, String address) {

            selectedLocation = latLng;
            Email=email;
            Pass=pass;
            Name=name;
            Phone=phone;
            Address=address;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("name", Name));
            param.add(new BasicNameValuePair("email", Email));
            param.add(new BasicNameValuePair("password", Pass));
            param.add(new BasicNameValuePair("phone_number", Phone));
            param.add(new BasicNameValuePair("addesss", Address));
            param.add(new BasicNameValuePair("address_lat", selectedLocation.latitude+""));
            param.add(new BasicNameValuePair("address_long", selectedLocation.longitude+""));


            json = jsonParser.makeHttpRequest(url_register,
                    "POST", param);


            try {
                return json.getBoolean("error");
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }

    }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            Toast.makeText(getApplication(), json.toString(), Toast.LENGTH_SHORT).show();
            if (!success) {

                finish();
            } else {
                editTextPassword.setError("Invalid Password");
                editTextPassword.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}
