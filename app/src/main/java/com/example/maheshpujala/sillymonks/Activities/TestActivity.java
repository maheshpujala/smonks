package com.example.maheshpujala.sillymonks.Activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.maheshpujala.sillymonks.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

/**
 * Created by maheshpujala on 21/9/16.
 */
public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    PublisherAdView mPublisherAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbar.setTitle("Title");

        mPublisherAdView = (PublisherAdView) findViewById(R.id.publisherAdView);
        mPublisherAdView.setAdSizes(AdSize.MEDIUM_RECTANGLE);

        PublisherAdRequest adRequest = new PublisherAdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("90EE0E29646EF0CBAC99567D3827BAF5")// My Genymotion
                .addTestDevice("9E24EA1846195D46BA5800679368D5E2")// MOTO G 4.5  marshmallow
                .addTestDevice("E8A785BC1EC7B41E36D183611BEAE615")// MOTO E  4.3 kitkat
                .addTestDevice("1DEFD3C3E725D34AD35682EFAC30169E")// Karbon 4     kitkat

                .build();

        mPublisherAdView.loadAd(adRequest);
        mPublisherAdView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id =view.getId();
        if (id == R.id.publisherAdView) {
            Toast.makeText(this, "clicked ADVERTISEMENT", Toast.LENGTH_SHORT).show();

        }
    }
}
