package com.elenaneacsu.healthmate.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FoodRequest {

@SerializedName("ingredients")
@Expose
private List<Ingredient> ingredients = null;

public List<Ingredient> getIngredients() {
return ingredients;
}

public void setIngredients(List<Ingredient> ingredients) {
this.ingredients = ingredients;
}

}