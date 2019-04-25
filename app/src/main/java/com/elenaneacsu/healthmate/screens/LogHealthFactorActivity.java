package com.elenaneacsu.healthmate.screens;


import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.adapter.FoodAdapter;
import com.elenaneacsu.healthmate.model.Food;
import com.elenaneacsu.healthmate.model.Foods;
import com.elenaneacsu.healthmate.model.SearchedFood;
import com.elenaneacsu.healthmate.network.FoodApiInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.elenaneacsu.healthmate.network.ApiRequest.generateOauthParamsSearch;
import static com.elenaneacsu.healthmate.network.ApiRequest.nonce;
import static com.elenaneacsu.healthmate.network.ApiRequest.sign;
import static com.elenaneacsu.healthmate.utils.Constants.APP_KEY;
import static com.elenaneacsu.healthmate.utils.Constants.APP_METHOD_GET;
import static com.elenaneacsu.healthmate.utils.Constants.APP_METHOD_SEARCH;
import static com.elenaneacsu.healthmate.utils.Constants.BASE_URL;
import static com.elenaneacsu.healthmate.utils.Constants.HMAC_SHA1;
import static com.elenaneacsu.healthmate.utils.Constants.SEARCH_QUERY;

public class LogHealthFactorActivity extends AppCompatActivity {
    private FoodAdapter mFoodAdapter;
    private List<Food> mFoodList;

    private FoodApiInterface mFoodApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_health_factor);

        setUpRecyclerView();
        searchFood("pasta", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchableInfo);

        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sharedPreferences.edit().putString(SEARCH_QUERY, query).apply();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                finish();
                return false;
            }
        });
        return true;
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mFoodAdapter = new FoodAdapter(mFoodList, getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mFoodAdapter);
    }

    private void searchFood(final String item, int page) {
        List<String> params = new ArrayList<>(Arrays.asList(generateOauthParamsSearch(item, page)));
        String[] template = new String[1];
        String signature = sign(APP_METHOD_GET, BASE_URL, params.toArray(template));
//        Call<SearchedFood> searchedFoodCall = mFoodApiInterface.getFoods("json", 30, "foods.search", APP_KEY,
//                nonce(), signature, HMAC_SHA1, System.currentTimeMillis() * 2, "1.0", item, page);
        Log.d("searchFood", "searchFood: "+signature);

    }
}
