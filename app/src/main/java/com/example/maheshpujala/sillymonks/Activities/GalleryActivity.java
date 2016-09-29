package com.example.maheshpujala.sillymonks.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.maheshpujala.sillymonks.Adapters.ImageAdapter;
import com.example.maheshpujala.sillymonks.R;

public class GalleryActivity extends AppCompatActivity {
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        GridView gridview = (GridView) findViewById(R.id.gallery_view);
        gridview.setAdapter(new ImageAdapter(getApplicationContext(),images));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                    Toast.makeText(getApplicationContext(), "clicked " + position, Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(GalleryActivity.this, FullScreenActivity.class);
                    startActivity(it);
            }
        });
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
}
