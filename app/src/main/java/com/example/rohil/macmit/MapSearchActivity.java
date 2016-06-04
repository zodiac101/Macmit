package com.example.rohil.macmit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapSearchActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private View mProgressView;
    private View mSearchView;
    TextView textViewMapSearchName;
    TextView textViewMapSearchAddress;
    TextView textViewMapSearchPhone;

    JSONParser jsonParser = new JSONParser();

    String url_getbookinfo = "http://127.0.0.1:8000/project1/v1/bookinfo";

    String book_selected;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMapSearch);
        mapFragment.getMapAsync(this);
        mMap=mapFragment.getMap();
        mMap.setMyLocationEnabled(true);

        mProgressView = findViewById(R.id.mapSearchProgress);
        mSearchView = findViewById(R.id.mapSearchView);


        textViewMapSearchName = (TextView)findViewById(R.id.textViewMapSearchName);
        textViewMapSearchAddress =(TextView) findViewById(R.id.textViewMapSearchAddress);
        textViewMapSearchPhone = (TextView) findViewById(R.id.textViewMapSearchPhoneNumber);

        Intent intent = getIntent();
        book_selected  = intent.getStringExtra("itemClicked");


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                String[] str = marker.getSnippet().split("\n", 2);
                textViewMapSearchName.setText(marker.getTitle());
                textViewMapSearchAddress.setText(str[1]);
                textViewMapSearchPhone.setText(str[0]);

                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Toast.makeText(MapSearchActivity.this, "MAP READY", Toast.LENGTH_SHORT).show();

        new LoadBookInfo(book_selected).execute();



    }


    class LoadBookInfo extends AsyncTask< String, String, String> {

        ProgressDialog pDialog;
        String book_selected;
        JSONArray books ;


        public LoadBookInfo(String str) {
            super();
            book_selected = str;
        }

        @Override
        protected String doInBackground(String... params) {

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("book", book_selected));
            Context context = getApplicationContext();
            SharedPreferences sharedPref = context.getSharedPreferences(
                    getString(R.string.preference_file_key_user), Context.MODE_PRIVATE);
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url_getbookinfo, "POST", param, sharedPref.getString("uid", "000") );


            try {
                // Checking for SUCCESS TAG
                boolean success = json.getBoolean("error");
                Log.d("All books info: ", json.toString());

                if (success == false) {
                    // products found
                    // Getting Array of Products
                    books = json.getJSONArray("bookinfo");


                    // looping through All Products

                } else {
                    Toast.makeText(getApplicationContext(), "No books FOUND", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            super.onPreExecute();
            pDialog= new ProgressDialog(MapSearchActivity.this);
            pDialog.setMessage("Loading Books. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            if (books==null)
                Toast.makeText(getApplicationContext(), "boks is null", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(getApplicationContext(), books.toString(), Toast.LENGTH_SHORT).show();

                for (int i = 0; i < books.length(); i++) {

                    try {
                        JSONObject c = books.getJSONObject(i);

                        LatLng latLng = new LatLng(Double.parseDouble(c.getString("address_lat")),
                                (Double.parseDouble(c.getString("address_long"))));
                        Toast.makeText(getApplicationContext(), latLng.toString(), Toast.LENGTH_SHORT).show();

                        mMap.addMarker(new MarkerOptions().position(latLng)
                                .title(c.getString("name"))
                                .snippet(c.getString("phone_number") + "\n"
                                        + c.getString("address")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Storing each json item in variable
                }
            }


        }
    }





}
