package com.example.maheshpujala.sillymonks.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.maheshpujala.sillymonks.R;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences mPrefs;
    final String welcomeScreenShownPref = "welcomeScreenShown";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // To make activity full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        WebView logo = (WebView) findViewById(R.id.view_logo);
        logo.loadUrl("file:///android_asset/logotab.html");

        loadConfig();

        getScreensize();
    }

    private void loginCheck() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // second argument is the default to use if the preference can't be found
        Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);

        if (!welcomeScreenShown) {
            // here you can launch another activity if you like
            // the code below will display a popup
            Log.e("welcomeScreenShown",""+!welcomeScreenShown);

            Intent start = new Intent(SplashActivity.this,LoginActivity.class);
            start.putExtra("From Splash","show_skip");
            startActivity(start);
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean(welcomeScreenShownPref, true);
            editor.apply(); // Very important to save the preference
        }
        else{

            Intent start = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(start);

        }
    }

    private void getScreensize() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
    }


    private void loadConfig() {
        int SPLASH_TIME_OUT = 4200;
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                loginCheck();
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
