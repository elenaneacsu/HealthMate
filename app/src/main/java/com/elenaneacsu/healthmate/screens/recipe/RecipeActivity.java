package com.elenaneacsu.healthmate.screens.recipe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.adapter.ListAdapter;
import com.elenaneacsu.healthmate.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.elenaneacsu.healthmate.utils.Constants.RECIPE_CLICKED;


public class RecipeActivity extends AppCompatActivity {

    private ImageView mImageView;
    private TextView mTextViewCalories;
    private TextView mTextViewWeight;
    private TextView mTextViewUrl;
    private RecyclerView mRecyclerViewIngredients;
    private RecyclerView mRecyclerViewLabels;

    private Recipe mRecipe;
    private List<String> mIngredients = new ArrayList<>();
    private List<String> mLabels = new ArrayList<>();
    private ListAdapter mIngredientsAdapter;
    private ListAdapter mLabelsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Toolbar toolbar = findViewById(R.id.toolbar_recipe);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initView();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mRecipe = (Recipe) bundle.getSerializable(RECIPE_CLICKED);
            setTitle(mRecipe.getLabel());
            mIngredients.addAll(mRecipe.getIngredientLines());
            mLabels.addAll(mRecipe.getDietLabels());
            mLabels.addAll(mRecipe.getHealthLabels());
            mTextViewUrl.setText(mRecipe.getUrl());
            mTextViewWeight.setText(mRecipe.getTotalWeight()+" g");
            mTextViewCalories.setText(mRecipe.getCalories()+" kcal");
            Picasso.get().load(mRecipe.getImage()).into(mImageView);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setUpRecyclerView();
    }

    private void initView() {
        mImageView = findViewById(R.id.imageview_recipedetail);
        mRecyclerViewIngredients = findViewById(R.id.recyclerview_ingredients);
        mRecyclerViewLabels = findViewById(R.id.recyclerview_healthlabels);
        mTextViewCalories = findViewById(R.id.textview_calories);
        mTextViewWeight = findViewById(R.id.textview_weight);
        mTextViewUrl = findViewById(R.id.textview_url);
    }

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager ingredientsLayoutManager = new LinearLayoutManager(this);
        mIngredientsAdapter = new ListAdapter(mIngredients);
        mRecyclerViewIngredients.setLayoutManager(ingredientsLayoutManager);
        mRecyclerViewIngredients.setAdapter(mIngredientsAdapter);

        RecyclerView.LayoutManager labelsLayoutManager = new LinearLayoutManager(this);
        mLabelsAdapter = new ListAdapter(mLabels);
        mRecyclerViewLabels.setLayoutManager(labelsLayoutManager);
        mRecyclerViewLabels.setAdapter(mLabelsAdapter);
    }


}
