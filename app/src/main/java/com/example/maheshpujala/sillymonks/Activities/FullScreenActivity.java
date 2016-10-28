package com.example.maheshpujala.sillymonks.Activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.maheshpujala.sillymonks.Adapters.FullScreenImageAdapter;
import com.example.maheshpujala.sillymonks.R;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import java.util.ArrayList;

/**
 * Created by maheshpujala on 28/9/16.
 */

public class FullScreenActivity extends AppCompatActivity implements MoPubInterstitial.InterstitialAdListener {
    private MoPubInterstitial mInterstitial;
    int swipeCount = 2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // To make activity full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_activity);
        ArrayList<String> imageURL = getIntent().getExtras().getStringArrayList("imageURL");
        int clicked_image = getIntent().getExtras().getInt("clicked_image");


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_fullscreen_image);

        FullScreenImageAdapter adapter = new FullScreenImageAdapter(FullScreenActivity.this, imageURL);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(clicked_image);

        try {
            mInterstitial = new MoPubInterstitial(this, getResources().getString(R.string.mopub_interstitial_ad_id));
            mInterstitial.setInterstitialAdListener(this);
        } catch (IllegalStateException e) {
            Log.e("mopub inter", "" + e.getLocalizedMessage());
        }

        loadAd();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

                // TODO Auto-generated method stub
                if (swipeCount % 5 == 1) {
                    if (swipeCount >= 5) {
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
    @Override
    protected void onResume () {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        Log.e("onBackPressed","___________Entered__________");
        this.finish();
    }

    // InterstitialAdListener methods
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
    @Override
    protected void onDestroy() {
        mInterstitial.destroy();
        super.onDestroy();
    }
}
