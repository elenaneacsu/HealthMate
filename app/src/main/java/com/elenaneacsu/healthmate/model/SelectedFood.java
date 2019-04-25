package com.elenaneacsu.healthmate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectedFood {

@SerializedName("food")
@Expose
private Food food;

public Food getFood() {
return food;
}

public void setFood(Food food) {
this.food = food;
}

}