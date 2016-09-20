package com.example.maheshpujala.sillymonks.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maheshpujala.sillymonks.Adapters.ListAdapter;
import com.example.maheshpujala.sillymonks.Api.NetworkCheck;
import com.example.maheshpujala.sillymonks.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {
    private CoordinatorLayout container;
    ListView home_list;
    View mDownView;
    int   mDownPosition;
    PublisherAdView mPublisherAdView;
    ImageView profile_pic,fb_button,twitter_button,gplus_button;
    TextView login;

    private final String[] values = new String[] { "Android List View",
            "Tollywood",
            "Bollywood",
            "Kollywood",
            "Mollywood",
            "Hollywood",
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
        profile_pic = (ImageView) findViewById(R.id.profile_image);
        profile_pic.setOnClickListener(this);
        login = (TextView) findViewById(R.id.signin);
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

        fb_button = (ImageView) findViewById(R.id.fb_button);
        fb_button.setOnClickListener(this);
        twitter_button = (ImageView) findViewById(R.id.twitter_button);
        twitter_button.setOnClickListener(this);
        gplus_button = (ImageView) findViewById(R.id.gplus_button);
        gplus_button.setOnClickListener(this);


        checkConnection();
        AdSize customAdSize = new AdSize(360, 180);

        mPublisherAdView = (PublisherAdView) findViewById(R.id.publisherAdView);
       // mPublisherAdView.setAdSizes(customAdSize);

        mPublisherAdView.setAdSizes(AdSize.MEDIUM_RECTANGLE);

        PublisherAdRequest adRequest = new PublisherAdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("90EE0E29646EF0CBAC99567D3827BAF5")// My Genymotion
                .addTestDevice("9E24EA1846195D46BA5800679368D5E2")// MOTO G 4.5  marshmallow
                .addTestDevice("E8A785BC1EC7B41E36D183611BEAE615")// MOTO E  4.3 kitkat
                .addTestDevice("1DEFD3C3E725D34AD35682EFAC30169E")// Karbon 4     kitkat

                .build();
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
                Intent it = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(it);
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
                   try{


                     mDownPosition = home_list.getPositionForView(mDownView);
                    Log.e("position in on touch","clicked"+mDownPosition);
                    if (mDownPosition == 0){
                        mPublisherAdView.dispatchTouchEvent(motionEvent);
                    }
                   }catch (Exception e){
                       Log.e("Exception","error");
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

            new AlertDialog.Builder(this)
                    .setTitle("Connection error")
                    .setMessage("Unable to connect with the server.Check your internet connection and try again.")
                    .setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                           checkConnection();
                        }
                    })
//                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            // do nothing
//                        }
//                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.signin) {
            Toast.makeText(this, "clicked SIGN IN", Toast.LENGTH_SHORT).show();
            Intent signin = new Intent(this,LoginActivity.class);
            int requestCode = 6;
            startActivityForResult(signin,requestCode);
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
        if (id == R.id.fb_button){
            String facebookUrl = "https://www.facebook.com/sillymonks/";
            try {
                int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
                if (versionCode >= 3002850) {
                    Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    ;
                } else {
                    // open the Facebook app using the old method (fb://profile/id or fb://page/id)
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/336227679757310")));
                }
            } catch (PackageManager.NameNotFoundException e) {
                // Facebook is not installed. Open the browser
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
            }
        }
        if (id == R.id.gplus_button){
            Intent intent = null;
            try {
                // get the Google Plus app if possible
                getPackageManager().getPackageInfo("com.google.android.apps.plus", 0);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/+SillymonksnetworkDotCom/posts"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } catch (Exception e) {
                // no Google Plus app, revert to browser
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/+SillymonksnetworkDotCom/posts"));
            }
            startActivity(intent);
        }
        if (id == R.id.twitter_button){
            Intent intent = null;
            try {
                // get the Twitter app if possible
                getPackageManager().getPackageInfo("com.twitter.android", 0);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/SillyMonks"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } catch (Exception e) {
                // no Twitter app, revert to browser
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/SillyMonks"));
            }
            startActivity(intent);
        }

        if (id == R.id.sharetheapp) {
            Toast.makeText(this, "clicked Share THe APp", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.publisherAdView) {
            Toast.makeText(this, "clicked ADVERTISEMENT", Toast.LENGTH_SHORT).show();

        }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 6) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.hasExtra("FB_id")) {

                    String fb_id=data.getStringExtra("FB_id");
                    String fb_name=data.getStringExtra("FB_name");
                 //   String fb_email=data.getStringExtra("FB_email");
                    Log.e("User FB_DETAILS ", fb_name+fb_id);
                    login.setText("My Profile");

                    Picasso.with(this).load("https://graph.facebook.com/" + fb_id + "/picture?type=large").into(profile_pic);

                    }

                if (data.hasExtra("pname")){
                    String g_name=data.getStringExtra("pname");
                    String g_email=data.getStringExtra("pemail");
                    Log.e("User Gplus_DETAILS", g_name+g_email);
                    login.setText("My Profile");

                    Picasso.with(this).load(data.getStringExtra("pphoto")).resize(200, 200).into(profile_pic);
                    // Saving user credentials on successful login case
//                    PrefUtils.saveToPrefs(NavigationDrawer.this, PREFS_LOGIN_USERNAME_KEY, g_name);
//                    PrefUtils.saveToPrefs(NavigationDrawer.this, PREFS_LOGIN_EMAIL_KEY, g_email);
                }
            }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }//onActivityResult
    }
