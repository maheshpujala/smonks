package com.example.maheshpujala.sillymonks.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.maheshpujala.sillymonks.Api.NetworkCheck;
import com.example.maheshpujala.sillymonks.R;

public class SplashActivity extends AppCompatActivity {
    private WebView logo;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // To make activity full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo =(WebView)findViewById(R.id.view_logo);
        logo.loadUrl("file:///android_asset/logotab.html");

        loadAndParseConfig();

    }


    private void loadAndParseConfig() {
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