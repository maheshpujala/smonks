package com.example.maheshpujala.sillymonks.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maheshpujala.sillymonks.Api.NetworkCheck;
import com.example.maheshpujala.sillymonks.R;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {
    private CoordinatorLayout container;
    private TextView abt,login,terms,cnt,advt,share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        // NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //  navigationView.setNavigationItemSelectedListener(this);
        login = (TextView)findViewById(R.id.signin);
        login.setOnClickListener(this);
        abt = (TextView)findViewById(R.id.about);
        abt.setOnClickListener(this);
        terms = (TextView) findViewById(R.id.tandc);
        terms.setOnClickListener(this);
        cnt = (TextView) findViewById(R.id.contact);
        cnt.setOnClickListener(this);
        advt = (TextView) findViewById(R.id.advertise);
        advt.setOnClickListener(this);
        share = (TextView)findViewById(R.id.sharetheapp);
        share.setOnClickListener(this);
        container = (CoordinatorLayout) findViewById(R.id.layout_container);
        checkConnection();

    }

    private void checkConnection() {
        if (NetworkCheck.isInternetAvailable(MainActivity.this))  //if connection available
        {

        } else {

            Snackbar.make(container, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Refresh", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkConnection();
                        }
                    }).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.signin) {
            Toast.makeText(this, "clicked SIGN IN", Toast.LENGTH_SHORT).show();
            Intent signin = new Intent(this,LoginActivity.class);
            startActivity(signin);
        }

        if (id == R.id.about) {
            Toast.makeText(this, "clicked ABOUT", Toast.LENGTH_SHORT).show();
            Intent about = new Intent(this,AboutAndTerms.class);
            int requestCode = 1;
            about.putExtra("requestCode", requestCode);
            startActivityForResult(about,requestCode);
        }

        if (id == R.id.tandc) {
            Toast.makeText(this, "clicked TERMS AND CONDITIONS", Toast.LENGTH_SHORT).show();
            Intent tandc = new Intent(this,AboutAndTerms.class);
            int requestCode = 2;
            tandc.putExtra("requestCode", requestCode);
            startActivityForResult(tandc,requestCode);
        }

        if (id == R.id.contact) {
            Toast.makeText(this, "clicked CONTACT US", Toast.LENGTH_SHORT).show();
            Intent contact = new Intent(this,ContactAndAdvertise.class);
            int requestCode = 3;
            contact.putExtra("requestCode", requestCode);
            startActivityForResult(contact,requestCode);
        }

        if (id == R.id.advertise) {
            Toast.makeText(this, "clicked ADVERTISE WITH US ", Toast.LENGTH_SHORT).show();
            Intent advertise = new Intent(this,ContactAndAdvertise.class);
            int requestCode = 4;
            advertise.putExtra("requestCode", requestCode);
            startActivityForResult(advertise,requestCode);
        }
        if (id == R.id.sharetheapp) {
            Toast.makeText(this, "clicked ADVERTISE WITH US ", Toast.LENGTH_SHORT).show();
            Intent advertise = new Intent(this,ContactAndAdvertise.class);
            int requestCode = 5;
            advertise.putExtra("requestCode", requestCode);
            startActivityForResult(advertise,requestCode);
        }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.textView2) {
//            // Handle the camera action
//            Toast.makeText(this,"clicked",Toast.LENGTH_SHORT).show();
//        } else if (id == R.id.textView3) {
//
//        } else if (id == R.id.textView4) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
    }
