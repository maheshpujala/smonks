package com.example.maheshpujala.sillymonks.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maheshpujala.sillymonks.Adapters.ListAdapter;
import com.example.maheshpujala.sillymonks.Api.NetworkCheck;
import com.example.maheshpujala.sillymonks.R;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {
    private CoordinatorLayout container;
    ListView home_list;
    View mDownView;
    int   mDownPosition;
    PublisherAdView mPublisherAdView;
    private final String[] values = new String[] { "Android List View",
            "TollyWood",
            "BollyWood",
            "KollyWood",
            "MollyWood",
            "HollyWood",
            "Creators",
    };
    private final Integer[] images = new Integer[]{
            R.drawable.drawer_image,
            R.drawable.tollywood,
            R.drawable.bollywood,
            R.drawable.kollywood,
            R.drawable.mollywood,
            R.drawable.hollywood,
            R.drawable.creators
    };
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
        TextView login = (TextView) findViewById(R.id.signin);
        login.setOnClickListener(this);
        TextView abt = (TextView) findViewById(R.id.about);
        abt.setOnClickListener(this);
        TextView terms = (TextView) findViewById(R.id.tandc);
        terms.setOnClickListener(this);
        TextView cnt = (TextView) findViewById(R.id.contact);
        cnt.setOnClickListener(this);
        TextView advt = (TextView) findViewById(R.id.advertise);
        advt.setOnClickListener(this);
        TextView share = (TextView) findViewById(R.id.sharetheapp);
        share.setOnClickListener(this);
        container = (CoordinatorLayout) findViewById(R.id.layout_container);
        checkConnection();

//        FrameLayout frame_load= (FrameLayout) findViewById(R.id.main_frame);
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view =inflater.inflate(R.layout.test, frame_load, false);
//        frame_load.addView(view);

        mPublisherAdView = (PublisherAdView) findViewById(R.id.publisherAdView);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();

//        AdSize customAdSize = new AdSize(200, 200);
//        mPublisherAdView.setAdSizes(customAdSize);

        mPublisherAdView.loadAd(adRequest);
        mPublisherAdView.setOnClickListener(this);

        home_list = (ListView) findViewById(R.id.list_allwoods);

        home_list.setAdapter(new ListAdapter(this, values,images));

        home_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
            if (position!=0){
                Toast.makeText(getApplicationContext(), "clicked " + position, Toast.LENGTH_SHORT).show();
            }
            }
        });
        home_list.setOnTouchListener(new AdapterView.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Rect rect = new Rect();
                int childCount = home_list.getChildCount();
                int[] listViewCoords = new int[2];
                home_list.getLocationOnScreen(listViewCoords);
                int x = (int) motionEvent.getRawX() - listViewCoords[0];
                int y = (int) motionEvent.getRawY() - listViewCoords[1];
                View child;
                for (int i = 0; i < childCount; i++) {
                    child = home_list.getChildAt(i);
                    child.getHitRect(rect);
                    if (rect.contains(x, y)) {
                         mDownView = child; // This is your down view
                        break;
                    }
                }
                if (mDownView != null) {
                     mDownPosition = home_list.getPositionForView(mDownView);
                    Log.e("position in on touch","clicked"+mDownPosition);
                    if (mDownPosition == 0){
                        mPublisherAdView.dispatchTouchEvent(motionEvent);
                    }
                }
                view.onTouchEvent(motionEvent);
                return true;
            }
        });
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
        if (id == R.id.publisherAdView) {
            Toast.makeText(this, "clicked ADVERTISEMENT", Toast.LENGTH_SHORT).show();

        }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

    }
