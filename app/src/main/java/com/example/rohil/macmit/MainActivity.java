package com.example.rohil.macmit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.rohil.macmit.R.string.drawer_close;
import static com.example.rohil.macmit.R.string.drawer_open;

public class MainActivity extends AppCompatActivity {



    private DrawerLayout mDrawerLayout;

    private NavigationView navigationView;
    TextView textViewNavHeaderTitle;
    TextView textViewNavHeaderSubtitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);





        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        textViewNavHeaderTitle = (TextView) headerView.findViewById(R.id.nav_header_title);
        textViewNavHeaderSubtitle = (TextView) headerView.findViewById(R.id.nav_header_subtitle);


        Context context = MainActivity.this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key_user), Context.MODE_PRIVATE);
        Toast.makeText(MainActivity.this, "uid: "+sharedPref.getString("uid", "000"), Toast.LENGTH_SHORT).show();

        if (sharedPref.getString("uid", "000").compareTo("000")!=0){

                textViewNavHeaderTitle.setText(sharedPref.getString("name", ""));
                textViewNavHeaderSubtitle.setText(sharedPref.getString("email", ""));

        }
        else
        {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        navigationView.addHeaderView(headerView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                Fragment fragment;
                FragmentManager fragmentManager = getSupportFragmentManager();


                //Closing drawer on item click
                mDrawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_user:

                        fragment = new UserFragment();

                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();


                        return false;

                    // For rest of the options we just show a toast on click

                    case R.id.nav_books:

                        fragment = new NewSectionFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();

                        return false;

                    case R.id.nav_add:

                        fragment = new AddItemFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .commit();

                        return false;

                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return false;

                }

            }
        });

        final CharSequence mDrawerTitle = getTitle();
        final CharSequence mTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        final ActionBarDrawerToggle mDrawerToggle;
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, drawer_open, drawer_close){

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                toolbar.setTitle(mTitle);
                invalidateOptionsMenu();
                // creates call to onPrepareOptionsMenu()
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (toolbar!=null) {
                    toolbar.setTitle(mDrawerTitle);
                }else{
                    Toast.makeText(MainActivity.this, "Toolbar is none", Toast.LENGTH_SHORT).show();
                }
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();



    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key_user), Context.MODE_PRIVATE);
        sharedPref.edit().clear();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





}
