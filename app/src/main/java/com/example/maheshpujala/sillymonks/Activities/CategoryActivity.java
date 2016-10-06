package com.example.maheshpujala.sillymonks.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.maheshpujala.sillymonks.Adapters.ListAdapter;
import com.example.maheshpujala.sillymonks.Api.VolleyRequest;
import com.example.maheshpujala.sillymonks.Model.Article;
import com.example.maheshpujala.sillymonks.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by maheshpujala on 12/9/16.
 */
public class CategoryActivity extends AppCompatActivity implements  View.OnClickListener, SearchView.OnQueryTextListener {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    SearchView searchView;
    TextView tolly,bolly,molly,kolly,holly,creator,home;
    ImageView  home_img;
    PublisherAdView mPublisherAdView;
    CollapsingToolbarLayout collapse_toolbar;
    NestedScrollView scrollView;
    String id,name,original;
    long  wood_id;
    JSONArray article;
    LinkedHashMap categories,cat_articles,articles_total_count;
    List<Article> articles;
    public String currentTabTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        wood_id = getIntent().getExtras().getLong("clicked");
        Log.e("BUNDLE EXTRAS",""+wood_id);
        sendRequest();

        mPublisherAdView = (PublisherAdView) findViewById(R.id.publisherAdView);
        mPublisherAdView.setAdSizes(AdSize.MEDIUM_RECTANGLE);

        PublisherAdRequest adRequest = new PublisherAdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("90EE0E29646EF0CBAC99567D3827BAF5")// My Genymotion
                .addTestDevice("9E24EA1846195D46BA5800679368D5E2")// MOTO G 4.5  marshmallow
                .addTestDevice("E8A785BC1EC7B41E36D183611BEAE615")// MOTO E  4.3 kitkat
                .addTestDevice("1DEFD3C3E725D34AD35682EFAC30169E")// Karbon 4     kitkat
                .addTestDevice("568D4320C2F8B11064876CC8CAE9DAF9")// Genymotion api 19

                .build();
        mPublisherAdView.loadAd(adRequest);

        scrollView = (NestedScrollView) findViewById (R.id.scroll_nested);
        scrollView.setFillViewport (true);
        scrollView.setNestedScrollingEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        home = (TextView) findViewById(R.id.home);
        home.setOnClickListener(this);
        tolly = (TextView) findViewById(R.id.tolly);
        tolly.setOnClickListener(this);
        bolly = (TextView) findViewById(R.id.bolly);
        bolly.setOnClickListener(this);
        kolly = (TextView) findViewById(R.id.kolly);
        kolly.setOnClickListener(this);
        molly = (TextView) findViewById(R.id.molly);
        molly.setOnClickListener(this);
        holly = (TextView) findViewById(R.id.holly);
        holly.setOnClickListener(this);
        creator = (TextView) findViewById(R.id.creator);
        creator.setOnClickListener(this);
        home_img = (ImageView) findViewById(R.id.home_img);
        home_img.setOnClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        Toast.makeText(getApplicationContext(),"selected"+tab.getPosition(),Toast.LENGTH_SHORT ).show();
                        List <Article> articles = (List<Article>) cat_articles.get(tab.getText());

                        currentTabTitle = (String) tab.getText();
                        Log.e("currentTabTitle",currentTabTitle);

                    }
                });
    }

    private void sendRequest() {
        String url_cat_list =getResources().getString(R.string.main_url)+getResources().getString(R.string.category_list_url)+wood_id+getResources().getString(R.string.os_tag);
        Log.i("------------URL------------",url_cat_list);
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
                articles = new ArrayList<Article>();

                for (int k = 0; k < cat_articles_list.length(); k++) {
                    JSONObject articles_json = cat_articles_list.getJSONObject(k);

                    articles.add(new Article(articles_json.getString("id"),
                            articles_json.getString("title"),
                            articles_json.getString("large")));
                }
                cat_articles.put(name,articles);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Set<String> set = categories.keySet();

//populate set
        for (String cat_name : set) {
            Bundle b = new Bundle();

            b.putSerializable("articles", (Serializable) cat_articles.get(cat_name));
            b.putSerializable("categories",  categories);
            b.putString("category_name",cat_name);
            b.putSerializable("articles_total_count",articles_total_count);

            Log.e("FROM CATEGORY ACTIVITY","\n"+set);
            Log.e("\ncategory name= "+cat_name,"\nArticles Size= "+ ((List<Article>)cat_articles.get(cat_name)).size());

            CategoryFragment fragment = new CategoryFragment();
            fragment.setArguments(b);
            adapter.addFrag(fragment, cat_name);
            Log.e("ADDING FRAGEMENT+++++++++",cat_name);

        }
        // adapter.addFrag(new CategoryGalleryFragment(), "Gallery");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        int id =view.getId();
        if (id == R.id.home_img || id == R.id.home){
            finish();
        }
        if (id == R.id.tolly){
            tolly.setVisibility(View.GONE);
            bolly.setVisibility(View.VISIBLE);
            holly.setVisibility(View.VISIBLE);
            kolly.setVisibility(View.VISIBLE);
            molly.setVisibility(View.VISIBLE);
            creator.setVisibility(View.VISIBLE);

            toolbar.setTitle("Tollywood");

        }
        if (id == R.id.bolly){
            bolly.setVisibility(View.GONE);
            tolly.setVisibility(View.VISIBLE);
            holly.setVisibility(View.VISIBLE);
            kolly.setVisibility(View.VISIBLE);
            molly.setVisibility(View.VISIBLE);
            creator.setVisibility(View.VISIBLE);

            toolbar.setTitle("Bollywood");
        }
        if (id == R.id.kolly){
            kolly.setVisibility(View.GONE);
            bolly.setVisibility(View.VISIBLE);
            holly.setVisibility(View.VISIBLE);
            tolly.setVisibility(View.VISIBLE);
            molly.setVisibility(View.VISIBLE);
            creator.setVisibility(View.VISIBLE);

            toolbar.setTitle("Kollywood");

        }
        if (id == R.id.molly){
            molly.setVisibility(View.GONE);
            bolly.setVisibility(View.VISIBLE);
            holly.setVisibility(View.VISIBLE);
            kolly.setVisibility(View.VISIBLE);
            tolly.setVisibility(View.VISIBLE);
            creator.setVisibility(View.VISIBLE);

            toolbar.setTitle("Mollywood");

        }
        if (id == R.id.holly){
            holly.setVisibility(View.GONE);
            bolly.setVisibility(View.VISIBLE);
            tolly.setVisibility(View.VISIBLE);
            kolly.setVisibility(View.VISIBLE);
            molly.setVisibility(View.VISIBLE);
            creator.setVisibility(View.VISIBLE);

            toolbar.setTitle("Hollywood");

        }
        if (id == R.id.creator){
            creator.setVisibility(View.GONE);
            bolly.setVisibility(View.VISIBLE);
            holly.setVisibility(View.VISIBLE);
            kolly.setVisibility(View.VISIBLE);
            molly.setVisibility(View.VISIBLE);
            tolly.setVisibility(View.VISIBLE);

            toolbar.setTitle("Creators");
        }
        if (id == R.id.publisherAdView) {
            Toast.makeText(this, "clicked ADVERTISEMENT", Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(this,""+query,Toast.LENGTH_SHORT).show();
        //  searchView.setIconified(true);
        searchView.onActionViewCollapsed();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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
            mFragmentTitleList.add("          "+title+"          ");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
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
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(this);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:

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
}