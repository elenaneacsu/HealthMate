package com.elenaneacsu.healthmate.screens.logging.food;


import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
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
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.elenaneacsu.healthmate.utils.Constants.FOOD_CLICKED;
import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

public class LogFoodActivity extends AppCompatActivity implements FoodAdapter.ItemClickListener {
    private ProgressDialog mProgressDialog;
    private FoodAdapter mFoodAdapter;
    private List<Hint> mHintList = new ArrayList<>();
    private String scanCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_food);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setUpRecyclerView();
        createProgressDialog();
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
                query.replaceAll(" ", "%20");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_scan:
                scanBarcode();
                return true;
            case android.R.id.home:
                finish();
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(View view, Hint hint) {
        Intent intent = new Intent(getApplicationContext(), FoodDetailActivity.class);
        intent.putExtra(FOOD_CLICKED, hint);
        startActivity(intent);
    }

    private void searchFood(final String item) {
        RetrofitHelper.getFoodResponse(item)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mProgressDialog.show();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mProgressDialog.dismiss();
                    }
                })
                .subscribe(new Observer<FoodResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FoodResponse foodResponse) {
                        mHintList.clear();
                        mHintList.addAll(foodResponse.getHints());
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

    private void searchFoodBarcode(String barcode) {
        RetrofitHelper.getBarcodeResponse(Long.parseLong(barcode))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mProgressDialog.show();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mProgressDialog.dismiss();
                    }
                })
                .subscribe(new Observer<FoodResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FoodResponse foodResponse) {
                        mHintList.clear();
                        mHintList.addAll(foodResponse.getHints());
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

    private void scanBarcode() {
        Intent intent = new Intent(LogFoodActivity.this, ScannerActivity.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                scanCode = data.getStringExtra("barcode");
                if (scanCode != null) {
                    searchFoodBarcode(scanCode);
                }
            }
        }
    }

    private void createProgressDialog() {
        mProgressDialog = new ProgressDialog(LogFoodActivity.this, R.style.AlertDialogStyle);
        mProgressDialog.setTitle("Searching...");
        mProgressDialog.setCancelable(false);
    }
}
