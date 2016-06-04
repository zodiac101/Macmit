package com.example.rohil.macmit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rohil on 12/28/2015.
 */
public class AddItemFragment extends Fragment {

    String url_getbooks = "http://127.0.0.1:8000/project1/v1/alluserbooks";
    JSONParser jsonParser = new JSONParser();
    ArrayList<HashMap<String, String>> booksList;
    JSONArray books = null;
    ListView listView;
    SimpleAdapter adapter;
    Button buttonAdd;



    private static final String TAG_AUTHOR = "author";
    private static final String TAG_BOOK = "book";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_section_add_item,container, false);

        booksList = new ArrayList<HashMap<String, String>>();
        SharedPreferences sharedPref = getContext().getSharedPreferences(
                getString(R.string.preference_file_key_user), Context.MODE_PRIVATE);

        if (sharedPref.getString("uid", "000").compareTo("000")!=0){

            new LoadAllUserBooks().execute();

        }

        buttonAdd = (Button)rootView.findViewById(R.id.buttonAddBook);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Button Pressed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), InsertActivity.class);
                startActivity(intent);
            }
        });




        listView = (ListView)rootView.findViewById(R.id.listViewAddItem);

        adapter = new SimpleAdapter(
                getContext(), booksList,
                R.layout.list_item_search, new String[] { TAG_BOOK,
                TAG_AUTHOR},
                new int[] { R.id.BookName, R.id.BookAuthor });
        // updating listview


        listView.setAdapter(adapter);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPref = getContext().getSharedPreferences(
                getString(R.string.preference_file_key_user), Context.MODE_PRIVATE);

        if (sharedPref.getString("uid", "000").compareTo("000")!=0){

            new LoadAllUserBooks().execute();

        }

    }


    class LoadAllUserBooks extends AsyncTask< String, String, String>{

        ProgressDialog pDialog;


        public LoadAllUserBooks() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {

            List<NameValuePair> param = new ArrayList<NameValuePair>();

            Context context = getContext();
            SharedPreferences sharedPref = context.getSharedPreferences(
                    getString(R.string.preference_file_key_user), Context.MODE_PRIVATE);
            // getting JSON string from URL

            JSONObject json = jsonParser.makeHttpRequest(url_getbooks, "GET", param, sharedPref.getString("uid", "000") );



            try {
                // Checking for SUCCESS TAG
                boolean success = json.getBoolean("error");
                Log.d("All books: ", json.toString());

                if (success == false) {
                    // products found
                    // Getting Array of Products
                    books = json.getJSONArray("books");

                    // looping through All Products
                    for (int i = 0; i < books.length(); i++) {
                        JSONObject c = books.getJSONObject(i);

                        // Storing each json item in variable
                        String author = c.getString(TAG_AUTHOR);
                        String book = c.getString(TAG_BOOK);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_AUTHOR, author);
                        map.put(TAG_BOOK, book);

                        // adding HashList to ArrayList
                        booksList.add(map);
                    }
                } else {
                    Toast.makeText(getContext(), "No books FOUND", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog= new ProgressDialog(getContext());
            pDialog.setMessage("Loading Books. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            adapter.notifyDataSetChanged();


        }
    }


}