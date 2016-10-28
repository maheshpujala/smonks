package com.example.maheshpujala.sillymonks.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.maheshpujala.sillymonks.Model.Article;
import com.example.maheshpujala.sillymonks.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import java.io.Serializable;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by maheshpujala on 12/10/16.
 */

public class ArticleActivity extends AppCompatActivity implements MoPubInterstitial.InterstitialAdListener{

    ViewPager mViewPager;
    TextView toolbar_title;
    String articles_total_count,categoryName,categoryID,wood_id,articleID,id,selected_position;
    Bundle b;
    List <Article> articles;
    private MoPubInterstitial mInterstitial;
    int swipeCount = 2;
    Intent getIds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.activity_article);

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


        getIds = getIntent();

        articleID = getIds.getExtras().getString("articleID");
        categoryID = getIds.getExtras().getString("categoryID");
        wood_id = getIds.getExtras().getString("wood_id");
        articles = (List<Article>) getIds.getExtras().getSerializable("articles");
        selected_position = getIds.getExtras().getString("selected_position");
        if(getIds.getExtras().getString("identifyActivity").equalsIgnoreCase("RelatedArticles")){
            toolbar_title.setText(articles.get(Integer.parseInt(selected_position)).getFirstCatName());

        }else if (getIds.getExtras().getString("identifyActivity").equalsIgnoreCase("FavoriteArticles")) {
            toolbar_title.setText(getIds.getExtras().getString("categoryName"));
        }else
            toolbar_title.setText(getIds.getExtras().getString("categoryName"));


        mViewPager = (ViewPager) findViewById(R.id.pager);
        ArticleAdapter adapter = new ArticleAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(articles.size());
        mViewPager.setCurrentItem(Integer.parseInt(selected_position));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                if (swipeCount % 5 == 1) {
                    if (swipeCount >= 10) {
                        loadAd();
                        swipeCount = 1;
                    }
                }
                swipeCount++;
                Article article = articles.get(arg0);

                if(getIds.getExtras().getString("identifyActivity").equalsIgnoreCase("relatedArticles")){
                    toolbar_title.setText(article.getFirstCatName());
                }else{
                    toolbar_title.setText(getIds.getExtras().getString("categoryName"));
                }

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
        CallbackManager mFacebookCallbackManager = CallbackManager.Factory.create();
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
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        mInterstitial.destroy();
        super.onDestroy();
    }
}