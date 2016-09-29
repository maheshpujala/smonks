package com.example.maheshpujala.sillymonks.Activities;

import android.app.Application;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.maheshpujala.sillymonks.Adapters.FullScreenImageAdapter;
import com.example.maheshpujala.sillymonks.R;

import java.util.ArrayList;

/**
 * Created by maheshpujala on 28/9/16.
 */

public class FullScreenActivity extends AppCompatActivity {
    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;

    private final Integer[] images = new Integer[]{

            R.drawable.tollywood,
            R.drawable.bollywood,
            R.drawable.kollywood,
            R.drawable.mollywood,
            R.drawable.hollywood,
            R.drawable.creators
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // To make activity full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_activity);


        viewPager = (ViewPager) findViewById(R.id.viewpager_fullscreen_image);

        adapter = new FullScreenImageAdapter(FullScreenActivity.this,images);
        viewPager.setAdapter(adapter);
      //  viewPager.setCurrentItem(position);
    }

    @Override
    public void onBackPressed() {
        Log.e("onBackPressed","___________Entered__________");
        this.finish();
    }
}
