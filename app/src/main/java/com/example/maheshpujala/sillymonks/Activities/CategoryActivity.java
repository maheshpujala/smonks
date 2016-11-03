package com.example.maheshpujala.sillymonks.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.example.maheshpujala.sillymonks.Network.Connectivity;
import com.example.maheshpujala.sillymonks.Network.VolleyRequest;
import com.example.maheshpujala.sillymonks.Model.Article;
import com.example.maheshpujala.sillymonks.R;
import com.example.maheshpujala.sillymonks.Utils.HelperMethods;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by maheshpujala on 12/9/16.
 */
public class CategoryActivity extends AppCompatActivity implements  View.OnClickListener {
    private Toolbar toolbar;
   // private TabLayout tabLayout;
    PagerTabStrip pagerTabStrip;
    private ViewPager viewPager;
    SearchView searchView;
    TextView home,toolbar_title;
    ImageView  home_img;
    PublisherAdView mPublisherAdView;
    /*NestedScrollView scrollView;*/
    String id,name,wood_id,current_wood,newWood,selected_woodId,imageSize;
    LinkedHashMap categories,cat_articles,articles_total_count;
    List<Article> articles;
    public String currentTabTitle;
    DrawerLayout drawer;
    HashMap woodNames_Map;
    Map<String, String>  sorted_woodNames_Map;
    ListAdapter adapter;
    ListView   listForWoods;
    List<String> navBarWoodNames,allWoodNames;
    ProgressDialog progressdialog;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar_title = (TextView)findViewById(R.id.toolbar_title);
        Intent it = getIntent();
        wood_id = it.getExtras().getString("wood_id");

        Bundle bundle = it.getExtras();
        woodNames_Map = (HashMap) bundle.getSerializable("woodNames_Map");
        sorted_woodNames_Map = new TreeMap<>(woodNames_Map);

        allWoodNames  = new ArrayList<>(sorted_woodNames_Map.values()) ;

        current_wood = (String) woodNames_Map.get(wood_id);
        toolbar_title.setText(""+current_wood);
        navBarWoodNames = new ArrayList<>(allWoodNames);
        navBarWoodNames.remove(current_wood);



        mPublisherAdView = (PublisherAdView) findViewById(R.id.publisherAdView);
        mPublisherAdView.setAdSizes(AdSize.SMART_BANNER);

        PublisherAdRequest adRequest = new PublisherAdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
//                .addTestDevice("90EE0E29646EF0CBAC99567D3827BAF5")// My Genymotion
//                .addTestDevice("9E24EA1846195D46BA5800679368D5E2")// MOTO G 4.5  marshmallow
//                .addTestDevice("E8A785BC1EC7B41E36D183611BEAE615")// MOTO E  4.3 kitkat
//                .addTestDevice("1DEFD3C3E725D34AD35682EFAC30169E")// Karbon 4     kitkat
//                .addTestDevice("568D4320C2F8B11064876CC8CAE9DAF9")// Genymotion api 19

                .build();
        mPublisherAdView.loadAd(adRequest);

        /*scrollView = (NestedScrollView) findViewById (R.id.scroll_nested);
        scrollView.setFillViewport (true);
        scrollView.setNestedScrollingEnabled(true);*/
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        home = (TextView) findViewById(R.id.home);
        home.setOnClickListener(this);
        home_img = (ImageView) findViewById(R.id.home_img);
        home_img.setOnClickListener(this);
        listForWoods = (ListView) findViewById(R.id.nav_list);
        adapter = new ListAdapter(this,navBarWoodNames,1);
        listForWoods.setAdapter(adapter);
        listForWoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_woodId = (String) HelperMethods.getKeyFromValue(sorted_woodNames_Map,navBarWoodNames.get(position));

                newWood = navBarWoodNames.get(position);
                toolbar.setTitle(newWood);
                navBarWoodNames.clear();
                navBarWoodNames.addAll(allWoodNames);
                navBarWoodNames.remove(newWood);

                this.changeWood(selected_woodId);
                listForWoods.setAdapter(adapter);

                drawer.closeDrawer(GravityCompat.START);
            }

            private void changeWood(String selected_woodId) {
                finish();
                Intent it = new Intent(CategoryActivity.this, CategoryActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("woodNames_Map",woodNames_Map);
                b.putString("wood_id",selected_woodId);
                it.putExtras(b);
                startActivity(it);
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pagerTabStrip =(PagerTabStrip) findViewById(R.id.pagerTabStrip);
        pagerTabStrip.setTabIndicatorColor(Color.parseColor("#e54425"));

        progressdialog = new ProgressDialog(CategoryActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCancelable(false);
        checkConnection();
    }
    public void checkConnection() {
        if (Connectivity.isConnected(CategoryActivity.this))  //if connection available
        {
            progressdialog.show();
            sendRequest(wood_id);

        } else {
            progressdialog.dismiss();
            Connectivity.showDialog(CategoryActivity.this);
        }
    }


    private void sendRequest(String WoodID) {
        String url_cat_list =getResources().getString(R.string.main_url)+getResources().getString(R.string.category_list_url)+WoodID+getResources().getString(R.string.os_tag);
// Request a JsonObject response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url_cat_list, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getData(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        reportError(error);
                    }
                });
// Add the request to the RequestQueue.
        VolleyRequest.getInstance().addToRequestQueue(jsObjRequest);
    }


    public void getData(JSONObject json) {
        try {
            String connectType=Connectivity.connectionType(CategoryActivity.this);
            if(Connectivity.isConnectedWifi(CategoryActivity.this)){
                    Log.e("isConnectedWifi CHECK",""+"ORIGINAL IMAGE!!!!!!!!!!!!!!");
                    imageSize = "original";
                }else if(connectType.equalsIgnoreCase("CDMA")||connectType.equalsIgnoreCase("EDGE")||connectType.equalsIgnoreCase("GPRS")||connectType.equalsIgnoreCase("1xRTT")){
                    Log.e("connectType CHECK",""+"SMALL IMAGE!!!!!!!!!!!!!!");
                imageSize = "small";

            }
                else if (connectType.equalsIgnoreCase("HSDPA")||connectType.equalsIgnoreCase("HSPA")||connectType.equalsIgnoreCase("HSUPA")||connectType.equalsIgnoreCase("UMTS")||connectType.equalsIgnoreCase("HSPA+")){
                    Log.e("connectType CHECK",""+"Medium IMAGE!!!!!!!!!!!!!!");

                imageSize = "medium";

            }
                else if (connectType.equalsIgnoreCase("LTE")){
                    Log.e("connectType CHECK",""+"Large IMAGE!!!!!!!!!!!!!!");

                imageSize = "large";

            }else{
                imageSize = "small";

            }

            JSONArray wood_cat_list = json.getJSONArray("categories");

            categories = new LinkedHashMap();
            cat_articles = new LinkedHashMap();
            articles_total_count = new LinkedHashMap();

            for (int i = 0; i < wood_cat_list.length(); i++) {
                JSONObject jsonobject = wood_cat_list.getJSONObject(i);

                id = jsonobject.getString("id");
                name = jsonobject.getString("name");
                articles_total_count.put(name,jsonobject.getString("articles_count"));
                categories.put(name,id);

                JSONArray cat_articles_list = jsonobject.getJSONArray("articles");
                articles = new ArrayList<>();

                for (int k = 0; k < cat_articles_list.length(); k++) {
                    JSONObject articles_json = cat_articles_list.getJSONObject(k);

                    articles.add(new Article(articles_json.getString("id"),
                            articles_json.getString("title"),
                            articles_json.getString(imageSize),
                            articles_json.getString("published_at"),
                            articles_json.getString("likes_count"),
                            articles_json.getString("comments_count")));
                }
                cat_articles.put(name,articles);

            }
            Log.e("MORE ARTICLES",""+articles);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        setupViewPager(viewPager);
        progressdialog.dismiss();
    }

    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Set<String> set = categories.keySet();

     //   adapter.getPageTitle(selectedPagePos)

//populate set
        for (String cat_name : set) {
            Bundle b = new Bundle();

            b.putSerializable("articles", (Serializable) cat_articles.get(cat_name));
            b.putSerializable("categories",  categories);
            b.putString("category_name",cat_name);
            b.putString("wood_id",wood_id);
            b.putSerializable("articles_total_count",articles_total_count);

            CategoryFragment fragment = new CategoryFragment();
            CategoryGridFragment grid_fragment = new CategoryGridFragment();

            if (cat_name.equalsIgnoreCase("gallery")){
                grid_fragment.setArguments(b);
                adapter.addFrag(grid_fragment, cat_name);
                adapter.notifyDataSetChanged();

            }else{
                fragment.setArguments(b);
                adapter.addFrag(fragment, cat_name);
                adapter.notifyDataSetChanged();
            }
        }
        viewPager.setAdapter(adapter);


    }

    @Override
    public void onClick(View view) {
        int id =view.getId();
        if (id == R.id.home_img || id == R.id.home){
            finish();
        }

        if (id == R.id.publisherAdView) {
            Toast.makeText(this, "clicked ADVERTISEMENT", Toast.LENGTH_LONG).show();

        }

        drawer.closeDrawer(GravityCompat.START);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "FROM SEARCH VIEW ON CLCIK", Toast.LENGTH_LONG).show();
                searchView.onActionViewCollapsed();
                Intent searchResult = new Intent(CategoryActivity.this,RelatedArticles.class);
                searchResult.putExtra("identifyActivity","SearchResult");
                searchResult.putExtra("searchQuery","");
                startActivity(searchResult);

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:
            Log.e("onOptions Item Selected","search");
                return true;
            case R.id.action_notify:
                Intent notify = new Intent(this,NotificationsAndAlerts.class);
                startActivity(notify);
                Log.e("onOptions Item Selected","action_notify");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            // searchView.onActionViewCollapsed();
        } else {
            super.onBackPressed();
        }
    }
//    public void onDestroy() {
//        Log.e("++++++++++++++++++++++++++++++++++++++++++++++","ON DESTROY");
//       finish();
//        super.onDestroy();
//    }
    private void reportError(VolleyError error) {
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
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(CategoryActivity.this, R.style.myDialog));

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Unable to connect with the server.Try again after some time.")
                    .setTitle("Server Error");

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();

            dialog.show();
            progressdialog.dismiss();

        }
    }

}