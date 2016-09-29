package com.example.maheshpujala.sillymonks.Activities;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maheshpujala.sillymonks.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import java.util.ArrayList;
import java.util.List;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//       collapse_toolbar= (CollapsingToolbarLayout)findViewById(R.id.collapsing);
//        collapse_toolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

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
        drawer.setDrawerListener(toggle);
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
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new CategoryFragment(), "Premium Content");
        adapter.addFrag(new CategoryFragment(), "News");
        adapter.addFrag(new CategoryFragment(), "Teasers and Trailers");
        adapter.addFrag(new CategoryFragment(), "Music");
        adapter.addFrag(new CategoryFragment(), "Movies");
        adapter.addFrag(new CategoryFragment(), "Reviews");
        adapter.addFrag(new CategoryFragment(), "Celebrities");
        adapter.addFrag(new CategoryGalleryFragment(), "Gallery");
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
