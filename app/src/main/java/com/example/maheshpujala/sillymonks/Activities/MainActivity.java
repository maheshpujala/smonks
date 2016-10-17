package com.example.maheshpujala.sillymonks.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.maheshpujala.sillymonks.Adapters.ListAdapter;
import com.example.maheshpujala.sillymonks.Api.NetworkCheck;
import com.example.maheshpujala.sillymonks.Api.VolleyRequest;
import com.example.maheshpujala.sillymonks.Model.SessionManager;
import com.example.maheshpujala.sillymonks.Model.UserData;
import com.example.maheshpujala.sillymonks.R;
import com.example.maheshpujala.sillymonks.Utils.BounceListView;
import com.example.maheshpujala.sillymonks.Utils.CircleImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements View.OnClickListener,Serializable{
    BounceListView home_list;
    View mDownView;
    int   mDownPosition;
    PublisherAdView mPublisherAdView;
    ImageView fb_button,twitter_button,gplus_button;
    CircleImageView profile_pic;
    TextView login;
    Bundle bundle;
    List<String> woodNames,woodIds,woodImages;
    LinkedHashMap woodNames_Map;
    String id,name,bimages,original;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Session class instance
        session = new SessionManager(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        profile_pic = (CircleImageView) findViewById(R.id.profile_image);
        profile_pic.setOnClickListener(this);
        login = (TextView) findViewById(R.id.signin);
        login.setOnClickListener(this);
        TextView abt = (TextView) findViewById(R.id.about);

        Typeface font = Typeface.createFromAsset(getAssets(),"RobotoRegular.ttf");
        abt.setTypeface(font);
        abt.setOnClickListener(this);
        TextView terms = (TextView) findViewById(R.id.tandc);
        terms.setOnClickListener(this);
        TextView cnt = (TextView) findViewById(R.id.contact);
        cnt.setOnClickListener(this);
        TextView advt = (TextView) findViewById(R.id.advertise);
        advt.setOnClickListener(this);
        TextView share = (TextView) findViewById(R.id.sharetheapp);
        share.setOnClickListener(this);

        fb_button = (ImageView) findViewById(R.id.fb_button);
        fb_button.setOnClickListener(this);
        twitter_button = (ImageView) findViewById(R.id.twitter_button);
        twitter_button.setOnClickListener(this);
        gplus_button = (ImageView) findViewById(R.id.gplus_button);
        gplus_button.setOnClickListener(this);

        checkConnection();
        checkLogin();

        mPublisherAdView = (PublisherAdView) findViewById(R.id.publisherAdView);
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

        home_list = (BounceListView) findViewById(R.id.list_allwoods);
        home_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (position != 0) {
                    Intent it = new Intent(MainActivity.this, CategoryActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("woodNames_Map",woodNames_Map);
                    b.putString("wood_id",woodIds.get(position));

                    it.putExtras(b);
                    //   it.putExtra("wood_names", (Serializable) woodNames);
                    startActivity(it);
                }
            }
        });

        home_list.setOnTouchListener(new AdapterView.OnTouchListener() {

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
                    try {
                        mDownPosition = home_list.getPositionForView(mDownView);
                        if (mDownPosition == 0) {
                            mPublisherAdView.dispatchTouchEvent(motionEvent);
                        }
                    } catch (Exception e) {
                    }
                }
                view.onTouchEvent(motionEvent);
                return true;
            }
        });

    }

    private void checkLogin() {
        session = new SessionManager(getApplication());
        Log.e("SESSION ",""+session.isLoggedIn());

        if(session.isLoggedIn()){
            List<UserData> userData=session.getUserDetails();
            Log.e("USER ID  ",  userData.get(0).getId());
            login.setText("My Profile");
            if(userData.get(0).getLoginType().contains("google")){
                Picasso.with(this).load(userData.get(0).getId()).into(profile_pic);
            }else{
                Picasso.with(this).load("https://graph.facebook.com/" +userData.get(0).getId()+ "/picture?type=large").into(profile_pic);
            }
        }
        else{
            login.setText("Sign In");
            profile_pic.setImageResource(R.drawable.profile);
        }
    }

    public void checkConnection() {
        if (NetworkCheck.isInternetAvailable(MainActivity.this))  //if connection available
        {
        sendRequest();
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

    public void sendRequest() {
        String url_land =getResources().getString(R.string.main_url)+getResources().getString(R.string.landing_url);
// Request a JsonObject response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url_land, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getData(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.e("response Errorhome", error + "");
                        if (error instanceof NoConnectionError) {
                            Log.d("NoConnectionError>>>>>>>>>", "NoConnectionError.......");
                        } else if (error instanceof AuthFailureError) {
                            Log.d("AuthFailureError>>>>>>>>>", "AuthFailureError.......");
                        } else if (error instanceof ServerError) {
                            Log.d("ServerError>>>>>>>>>", "ServerError.......");
                        } else if (error instanceof NetworkError) {
                            Log.d("NetworkError>>>>>>>>>", "NetworkError.......");
                        } else if (error instanceof ParseError) {
                            Log.d("ParseError>>>>>>>>>", "ParseError.......");
                        }else if (error instanceof TimeoutError) {
                            Log.d("TimeoutError>>>>>>>>>", "TimeoutError.......");
                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.myDialog));

                       // 2. Chain together various setter methods to set the dialog characteristics
                       builder.setMessage("Unable to connect with the server.Try again after some time.")
                               .setTitle("Server Error");

                       // 3. Get the AlertDialog from create()
                       AlertDialog dialog = builder.create();

                            dialog.show();
                        }

                    }
                });
// Add the request to the RequestQueue.
        VolleyRequest.getInstance().addToRequestQueue(jsObjRequest);
    }

    public void getData(JSONObject json) {
        try {
            JSONArray wood =json.getJSONArray("woods") ;
            woodNames = new ArrayList<>();
            woodIds = new ArrayList<>();
            woodImages = new ArrayList<>();

            woodNames_Map = new LinkedHashMap();

            woodNames.add(0,"");
            woodIds.add(0,"");
            woodImages.add(0,"");

            for (int i = 0; i < wood.length(); i++) {
                JSONObject jsonobject = wood.getJSONObject(i);

                 id = jsonobject.getString("id");
                 name = jsonobject.getString("name");
                 bimages = jsonobject.getString("original");

                woodIds.add(id);
                woodNames.add(name);
                woodImages.add(bimages);

                woodNames_Map.put(id,name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        home_list.setAdapter(new ListAdapter(this, woodNames, woodImages,woodIds,2));

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
    public void onResume(){
        super.onResume();
        checkLogin();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.signin) {
            String login_check = login.getText().toString();
            if(login_check.contains("My Profile")){
                Intent profile = new Intent(this,MyProfileActivity.class);
                startActivity(profile);
            }else{
                Intent signin = new Intent(this,LoginActivity.class);
                int requestCode = 6;
                startActivityForResult(signin,requestCode);
            }
        }
        if (id == R.id.profile_image) {
            Toast.makeText(this, "clicked profile Image", Toast.LENGTH_SHORT).show();
            if(login.getText().toString().contains("My Profile")){
                Intent profile = new Intent(this,MyProfileActivity.class);
                startActivity(profile);
            }
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
            try{
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "SillyMonks Android Application");
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.ongo.silly_monks \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            }
            catch(Exception e)
            {
            }
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

            }
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notification, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_notify:
                Intent notify = new Intent(this,NotificationsAndAlerts.class);
                startActivity(notify);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}