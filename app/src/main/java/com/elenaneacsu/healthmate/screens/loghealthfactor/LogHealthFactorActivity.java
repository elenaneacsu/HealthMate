package com.elenaneacsu.healthmate.screens.loghealthfactor;


import android.annotation.SuppressLint;
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
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.adapter.FoodAdapter;
import com.elenaneacsu.healthmate.model.entity.Food;
import com.elenaneacsu.healthmate.model.entity.SearchedFood;
import com.elenaneacsu.healthmate.screens.loghealthfactor.model.LogHealthFactorModel;
import com.elenaneacsu.healthmate.utils.InternetUtils;

import java.util.Arrays;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.elenaneacsu.healthmate.utils.Constants.HMAC_SHA1_ALGORITHM;
import static com.elenaneacsu.healthmate.utils.Constants.SEARCH_QUERY;

public class LogHealthFactorActivity extends AppCompatActivity {
    private FoodAdapter mFoodAdapter;
    private List<Food> mFoodList;

    private LogHealthFactorModel mLogHealthFactorModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_health_factor);

        mLogHealthFactorModel = new LogHealthFactorModel();

        setUpRecyclerView();
        searchFood("pasta", 0L);
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

    @SuppressLint("CheckResult")
    private void searchFood(final String item, Long page) {

        final Long currentTimeMillis = System.currentTimeMillis();
        final String nounce = "test" + System.currentTimeMillis();
        String httpMethod = "GET";
        String requestUrl = "https://platform.fatsecret.com/rest/server.api";
        String[] normalizedParams = {
                "oauth_consumer_key=" + "ee14d5de36ca4f059aecfc748a9cc017",
                "oauth_signature_method=" + "HMAC-SHA1",
                "oauth_timestamp=" + currentTimeMillis,
                "oauth_nonce= " + nounce,
                "oauth_version=" + "1.0",
                "page_number=" + 0,
                "format=" + "json",
                "search_expression=" + item,
                "max_results=" + 50,
                "method=" + "foods.search"
        };

        Arrays.sort(normalizedParams);
        String concatenatedParams = TextUtils.join("&", normalizedParams);

        String singatureBaseString = httpMethod + "&" + Uri.encode(requestUrl) + "&" + Uri.encode(concatenatedParams);

        String accessSecret = "f1ac2ec9969e4fe1ae45804f70c98ba2" + "&";
        String consumerSecret = "";

        String key = consumerSecret + "&" + accessSecret;

        String signature = "";

        SecretKey sk = new SecretKeySpec(accessSecret.getBytes(), "HmacSHA1");
        try {
            Mac m = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            m.init(sk);
            signature = Uri.encode(new String(Base64.encode(m.doFinal(singatureBaseString.getBytes()), Base64.DEFAULT)).trim());
        } catch (java.security.NoSuchAlgorithmException e) {
            Log.w("FatSecret_TEST FAIL", e.getMessage());
        } catch (java.security.InvalidKeyException e) {
            Log.w("FatSecret_TEST FAIL", e.getMessage());
        }

        final String finalSignature = signature;
        InternetUtils
                .checkInternet(this) // we check the internet connection
                .subscribeOn(Schedulers.io())
                // observe on main thread because we want to show a loader for user
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .observeOn(Schedulers.computation())
                .flatMap(new Function<Boolean, Observable<SearchedFood>>() {
                    @Override
                    public Observable<SearchedFood> apply(Boolean aBoolean) throws Exception {
                        return
                                mLogHealthFactorModel.searchFood(
                                        "json",
                                        50,
                                        "foods.search",
                                        "ee14d5de36ca4f059aecfc748a9cc017",
                                        nounce,
                                        finalSignature,
                                        "HMAC-SHA1",
                                        currentTimeMillis,
                                        "1.0",
                                        0,
                                        item)
                                        .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SearchedFood>() {
                    @Override
                    public void accept(SearchedFood searchedFood) throws Exception {
                        Log.d("TAG", "accept: " + searchedFood.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("TAG", "accept: " + throwable.getMessage());
                    }
                });


    }

    private String getSignature() {
        String signature = "";
        return signature;
    }
}
