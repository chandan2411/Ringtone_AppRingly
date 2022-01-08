package com.ringly.customer_app.views.activities;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.ringly.customer_app.R;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private Toolbar tbMainSearch;
    private ListView lvToolbarSerch;
    private String TAG = SearchActivity.class.getSimpleName();
    String[] arrays = new String[]{"98411", "98422", "98433", "98444", "98455"};
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpViews();
    }

    private void setUpViews() {
       /* tbMainSearch = (Toolbar)findViewById(R.id.tb_toolbarsearch);
        lvToolbarSerch =(ListView) findViewById(R.id.lv_toolbarsearch);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrays);
        lvToolbarSerch.setAdapter(adapter);
        setSupportActionBar(tbMainSearch);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem mSearchmenuItem = menu.findItem(R.id.menu_toolbarsearch);
        SearchView searchView = (SearchView) mSearchmenuItem.getActionView();
        searchView.setQueryHint("enter Text");
        searchView.setOnQueryTextListener(this  );
        Log.d(TAG, "onCreateOptionsMenu: mSearchmenuItem->" + mSearchmenuItem.getActionView());
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit: query->"+query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(TAG, "onQueryTextChange: newText->" + newText);
        adapter.getFilter().filter(newText);
        return true;
    }
}