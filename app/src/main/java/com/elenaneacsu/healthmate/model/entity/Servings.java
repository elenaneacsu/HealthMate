package com.elenaneacsu.healthmate.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Servings {

@SerializedName("serving")
@Expose
private Serving serving;

public Serving getServing() {
return serving;
}

public void setServing(Serving serving) {
this.serving = serving;
}

}