package com.example.maheshpujala.sillymonks.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maheshpujala.sillymonks.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheshpujala on 12/9/16.
 */
public class CategoryActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView tolly,bolly,molly,kolly,holly,creator,home;
    ImageView  home_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        adapter.addFrag(new CategoryFragment(), "ONE");
        adapter.addFrag(new CategoryFragment(), "TWO");
        adapter.addFrag(new CategoryFragment(), "THREE");
        adapter.addFrag(new CategoryFragment(), "FOUR");
        adapter.addFrag(new CategoryFragment(), "FIVE");
        adapter.addFrag(new CategoryFragment(), "SIX");
        adapter.addFrag(new CategoryFragment(), "SEVEN");
        adapter.addFrag(new CategoryFragment(), "EIGHT");
        adapter.addFrag(new CategoryFragment(), "NINE");
        adapter.addFrag(new CategoryFragment(), "TEN");
        viewPager.setAdapter(adapter);
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
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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

             if(getSupportActionBar()!=null) {
                 getSupportActionBar().setTitle("Tollywood");
             }
         }
        if (id == R.id.bolly){
            bolly.setVisibility(View.GONE);
            tolly.setVisibility(View.VISIBLE);
            holly.setVisibility(View.VISIBLE);
            kolly.setVisibility(View.VISIBLE);
            molly.setVisibility(View.VISIBLE);
            creator.setVisibility(View.VISIBLE);
            if(getSupportActionBar()!=null) {
                getSupportActionBar().setTitle("Bollywood");
            }
        }
        if (id == R.id.kolly){
            kolly.setVisibility(View.GONE);
            bolly.setVisibility(View.VISIBLE);
            holly.setVisibility(View.VISIBLE);
            tolly.setVisibility(View.VISIBLE);
            molly.setVisibility(View.VISIBLE);
            creator.setVisibility(View.VISIBLE);
            if(getSupportActionBar()!=null) {
                getSupportActionBar().setTitle("Kollywood");
            }
        }
        if (id == R.id.molly){
            molly.setVisibility(View.GONE);
            bolly.setVisibility(View.VISIBLE);
            holly.setVisibility(View.VISIBLE);
            kolly.setVisibility(View.VISIBLE);
            tolly.setVisibility(View.VISIBLE);
            creator.setVisibility(View.VISIBLE);
            if(getSupportActionBar()!=null) {
                getSupportActionBar().setTitle("Mollywood");
            }
        }
        if (id == R.id.holly){
            holly.setVisibility(View.GONE);
            bolly.setVisibility(View.VISIBLE);
            tolly.setVisibility(View.VISIBLE);
            kolly.setVisibility(View.VISIBLE);
            molly.setVisibility(View.VISIBLE);
            creator.setVisibility(View.VISIBLE);
            if(getSupportActionBar()!=null) {
                getSupportActionBar().setTitle("Hollywood");
            }
        }
        if (id == R.id.creator){
            creator.setVisibility(View.GONE);
            bolly.setVisibility(View.VISIBLE);
            holly.setVisibility(View.VISIBLE);
            kolly.setVisibility(View.VISIBLE);
            molly.setVisibility(View.VISIBLE);
            tolly.setVisibility(View.VISIBLE);
            if(getSupportActionBar()!=null) {
                getSupportActionBar().setTitle("Creator");
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
            mFragmentTitleList.add("          "+title+"          ");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        // Associate searchable configuration with the SearchView
        // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

}
