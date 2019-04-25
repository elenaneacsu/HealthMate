package com.elenaneacsu.healthmate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchedFood {

@SerializedName("foods")
@Expose
private Foods foods;

public Foods getFoods() {
return foods;
}

public void setFoods(Foods foods) {
this.foods = foods;
}

}