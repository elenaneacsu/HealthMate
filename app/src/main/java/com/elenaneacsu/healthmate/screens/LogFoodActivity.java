package com.elenaneacsu.healthmate.screens;


import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.adapter.FoodAdapter;
import com.elenaneacsu.healthmate.api.RetrofitHelper;
import com.elenaneacsu.healthmate.model.FoodResponse;
import com.elenaneacsu.healthmate.model.Hint;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.elenaneacsu.healthmate.utils.Constants.FOOD_CLICKED;
import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

public class LogFoodActivity extends AppCompatActivity implements FoodAdapter.ItemClickListener {
    private FoodAdapter mFoodAdapter;
    private List<Hint> mHintList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_health_factor);

        setUpRecyclerView();
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
                searchFood(query);
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
        mFoodAdapter = new FoodAdapter(mHintList, getApplicationContext());
        mFoodAdapter.setItemClickListener(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mFoodAdapter);
    }

    private void searchFood(final String item) {
        RetrofitHelper.getFoodResponse(item)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<FoodResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FoodResponse foodResponse) {
                        mHintList.clear();
                        mHintList.addAll(foodResponse.getHints());
                        Log.d("tag", "onNext: " + mHintList.size());
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(getApplicationContext(), "No results found");
                    }

                    @Override
                    public void onComplete() {
                        mFoodAdapter.notifyDataSetChanged();
                    }
                });

    }

    @Override
    public void onItemClick(View view, Hint hint) {
        Intent intent = new Intent(getApplicationContext(), FoodDetailActivity.class);
        intent.putExtra(FOOD_CLICKED, hint);
        Log.d("tag", "onItemClick: "+hint.getFood().getFoodId());
        startActivity(intent);
    }
}
