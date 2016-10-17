package com.example.maheshpujala.sillymonks.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maheshpujala.sillymonks.Model.Article;
import com.example.maheshpujala.sillymonks.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import java.io.Serializable;
import java.util.List;

/**
 * Created by maheshpujala on 12/10/16.
 */

public class ArticleActivity extends AppCompatActivity implements MoPubInterstitial.InterstitialAdListener{

    ViewPager mViewPager;
    TextView toolbar_title;
    String articles_total_count,categoryName,categoryID,wood_id,articleID,id,selected_position;
    Bundle b;
    List <Article> articles;
    private CallbackManager mFacebookCallbackManager;
    private MoPubInterstitial mInterstitial;
    int swipeCount = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.article_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar_title = (TextView)findViewById(R.id.toolbar_title);
        try {
            mInterstitial = new MoPubInterstitial(this, getResources().getString(R.string.mopub_interstitial_ad_id));
            mInterstitial.setInterstitialAdListener(this);
        } catch (IllegalStateException e) {
            Log.e("mopub inter", "" + e.getLocalizedMessage());
        }

        Intent getIds = getIntent();
        articleID = getIds.getExtras().getString("articleID");
        categoryID = getIds.getExtras().getString("categoryID");
        categoryName = getIds.getExtras().getString("categoryName");
        wood_id = getIds.getExtras().getString("wood_id");
        articles = (List<Article>) getIds.getExtras().getSerializable("articles");
        selected_position = getIds.getExtras().getString("selected_position");
        Log.e("selected_position",""+selected_position);

        toolbar_title.setText(categoryName);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        ArticleAdapter adapter = new ArticleAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(articles.size());
        mViewPager.setCurrentItem(Integer.parseInt(selected_position));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

                // TODO Auto-generated method stub
                if (swipeCount % 5 == 1) {
                    if (swipeCount >= 10) {
                        loadAd();
                        swipeCount = 1;
                    }
                }
                swipeCount++;

            }

            @Override
            public void onPageScrolled(int position, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

    }
    public void loadAd() {

        try {
            mInterstitial.load();
        } catch (IllegalStateException e) {
            Log.e("mopub lodad", "" + e.getLocalizedMessage());
        }
    }
    protected void facebookSDKInitialize() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        mFacebookCallbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(getApplication());
    }

    @Override
    public void onInterstitialLoaded(MoPubInterstitial interstitial) {
        if (interstitial.isReady()) {
            mInterstitial.show();
        } else {
            // Other code
        }
    }

    @Override
    public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {

    }

    @Override
    public void onInterstitialShown(MoPubInterstitial interstitial) {

    }

    @Override
    public void onInterstitialClicked(MoPubInterstitial interstitial) {

    }

    @Override
    public void onInterstitialDismissed(MoPubInterstitial interstitial) {

    }

    public class ArticleAdapter extends FragmentPagerAdapter {
        public ArticleAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return articles.size();
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new ArticleFragment();
            Article article = articles.get(position);
            b = new Bundle();
            b.putString("articleID",article.getId());
            b.putString("categoryID",categoryID);
            b.putString("wood_id",wood_id);
            b.putString("categoryName",categoryName);
            b.putSerializable("articles", (Serializable) articles);
            fragment.setArguments(b);

            return fragment;
        }

    }
    @Override
    public void onBackPressed() {
        Log.e("onBackPressed","___________Entered__________");
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            Log.e("onBackPressed","___________Entered__________");
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        mInterstitial.destroy();
        super.onDestroy();
    }
}