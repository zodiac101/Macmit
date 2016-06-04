package com.example.rohil.macmit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InsertActivity extends AppCompatActivity {


    String url_insertbooks = "http://127.0.0.1:8000/project1/v1/books";
    JSONParser jsonParser = new JSONParser();
    TextView textViewBook;
    TextView textViewAuthor;
    Button buttonInsert;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewBook = (TextView)findViewById(R.id.book);
        textViewAuthor = (TextView)findViewById(R.id.author);
        buttonInsert = (Button)findViewById(R.id.button_insert);

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InsertBook(textViewBook.getText().toString(), textViewAuthor.getText().toString()).execute();
            }
        });




    }

    class InsertBook extends AsyncTask <String, String, String>{

        ProgressDialog pDialog;
        String book;
        String author;

        InsertBook(String book, String author){
            this.book = book;
            this.author = author;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog= new ProgressDialog(InsertActivity.this);
            pDialog.setMessage("Loading Books. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            pDialog.dismiss();
            if (s.equals("error")){
                Toast.makeText(getApplicationContext(), "Failed. Please Try Again", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Book Successfully Added", Toast.LENGTH_SHORT).show();
                textViewBook.setText("");
                textViewAuthor.setText("");
            }
        }


        @Override
        protected String doInBackground(String... params) {


            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("book", book));
            param.add(new BasicNameValuePair("author", author));

            Context context = getApplicationContext();
            SharedPreferences sharedPref = context.getSharedPreferences(
                    getString(R.string.preference_file_key_user), Context.MODE_PRIVATE);
            // getting JSON string from URL

            JSONObject json = jsonParser.makeHttpRequest(url_insertbooks, "POST", param, sharedPref.getString("uid", "000") );
            Log.d("Result: ", json.toString());


            try {
                if (json.getBoolean("error")){
                    return "error";
                }
                else
                    return json.getString("book_id");
            } catch (JSONException e) {
                e.printStackTrace();
                return "error";
            }

        }
    }

}
