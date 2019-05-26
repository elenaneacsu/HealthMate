package com.elenaneacsu.healthmate.screens.recipe;


import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.adapter.RecipesAdapter;
import com.elenaneacsu.healthmate.api.RetrofitHelper;
import com.elenaneacsu.healthmate.model.Hit;
import com.elenaneacsu.healthmate.model.RecipeResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchRecipeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ProgressDialog mProgressDialog;
    private RecipesAdapter mRecipesAdapter;
    private List<Hit> mHitList = new ArrayList<>();

    private int mFrom, mTo;
    private String mQuery;

    public SearchRecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerview_recipes);
        setUpRecyclerView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_recipe, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createProgressDialog();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.recipe_menu, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getActivity().getComponentName());
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchableInfo);

        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mQuery = query;
                mProgressDialog.show();
                mFrom = 0;
                mTo = 30;
                searchRecipe(query, mFrom, mTo, mTo);
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
                getActivity().finish();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void setUpRecyclerView() {
        int cols = 3;
        if( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE )
            cols = 5;

        GridLayoutManager manager = new GridLayoutManager(getContext(), cols);
        mRecipesAdapter = new RecipesAdapter(mHitList);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRecipesAdapter);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mFrom = mTo +1;
                mTo +=30;
                searchRecipe(mQuery, mFrom, mTo, totalItemsCount);
            }
        });
        mFrom = 0;
        mTo = 30;
    }



    public void searchRecipe(String q, final int from, int to, final int totalItemsCount) {
        RetrofitHelper.getRecipeResponse(q, from, to)
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
                .subscribe(new Observer<RecipeResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RecipeResponse recipeResponse) {
                        mHitList.clear();
                        mHitList.addAll(recipeResponse.getHits());
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(getContext(), getString(com.elenaneacsu.healthmate.R.string.no_results_found));
                    }

                    @Override
                    public void onComplete() {
                        mRecipesAdapter.notifyItemRangeChanged(from, totalItemsCount);
                    }
                });
    }

    private void createProgressDialog() {
        mProgressDialog = new ProgressDialog(getContext(), R.style.AlertDialogStyle);
        mProgressDialog.setTitle("Searching...");
        mProgressDialog.setCancelable(false);
    }

}
