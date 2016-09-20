package com.example.maheshpujala.sillymonks.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.maheshpujala.sillymonks.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // To make activity full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        WebView logo = (WebView) findViewById(R.id.view_logo);
        logo.loadUrl("file:///android_asset/logotab.html");

        loadAndParseConfig();
        getScreensize();
    }

    private void getScreensize() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        Toast.makeText(this,width+"x"+height,Toast.LENGTH_LONG).show();
    }


    private void loadAndParseConfig() {
        int SPLASH_TIME_OUT = 4200;
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent start = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(start);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}