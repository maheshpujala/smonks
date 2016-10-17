package com.example.maheshpujala.sillymonks.Activities;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.maheshpujala.sillymonks.R;

/**
 * Created by maheshpujala on 13/10/16.
 */

public class SearchableActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        handleIntent(getIntent());
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("onNewIntenr","WEntered");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.e("handleIntent","WEntered"+intent);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            showResults(query);
        }
    }
    private void showResults(String query) {
        // Query your data set and show results
        // ...
        Log.e("showResults","WEntered"+query);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

    //    searchView.setOnQueryTextListener(this);
        return true;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        // sendSearchRequest(query);
        Intent searchResult = new Intent(this,RelatedArticles.class);
        searchResult.putExtra("identifyActivity","SearchResult");
        searchResult.putExtra("searchQuery",query);
        startActivity(searchResult);
      //  searchView.onActionViewCollapsed();
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:

            case R.id.action_notify:
                Intent notify = new Intent(this,NotificationsAndAlerts.class);
                startActivity(notify);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
