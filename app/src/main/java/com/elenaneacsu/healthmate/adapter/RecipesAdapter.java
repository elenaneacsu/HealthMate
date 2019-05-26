package com.elenaneacsu.healthmate.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.elenaneacsu.healthmate.R;

import com.elenaneacsu.healthmate.model.Hit;
import com.elenaneacsu.healthmate.model.Recipe;
import com.elenaneacsu.healthmate.screens.recipe.RecipeActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.elenaneacsu.healthmate.utils.Constants.RECIPE_CLICKED;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private Context mContext;
    private List<Hit> mHitList;

    public RecipesAdapter(List<Hit> hitList) {
        this.mHitList = hitList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        View itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.recipe_item, viewGroup, false);
        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder viewHolder, int i) {
        Hit hit = mHitList.get(i);
        if (hit != null) {
            Recipe recipe = hit.getRecipe();
            viewHolder.mRecipe = recipe;
            Picasso.get().load(recipe.getImage()).into(viewHolder.mImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (mHitList == null) {
            return 0;
        } else {
            return mHitList.size();
        }
    }


    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private Recipe mRecipe;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, RecipeActivity.class);
                    intent.putExtra(RECIPE_CLICKED, mRecipe);
                    mContext.startActivity(intent);
                }
            });
            mImageView = itemView.findViewById(R.id.imageview_recipe);
        }
    }
}
