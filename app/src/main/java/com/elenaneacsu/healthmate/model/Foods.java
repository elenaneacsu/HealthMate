package com.elenaneacsu.healthmate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Foods {

@SerializedName("food")
@Expose
private Food food;
@SerializedName("max_results")
@Expose
private String maxResults;
@SerializedName("page_number")
@Expose
private String pageNumber;
@SerializedName("total_results")
@Expose
private String totalResults;

public Food getFood() {
return food;
}

public void setFood(Food food) {
this.food = food;
}

public String getMaxResults() {
return maxResults;
}

public void setMaxResults(String maxResults) {
this.maxResults = maxResults;
}

public String getPageNumber() {
return pageNumber;
}

public void setPageNumber(String pageNumber) {
this.pageNumber = pageNumber;
}

public String getTotalResults() {
return totalResults;
}

public void setTotalResults(String totalResults) {
this.totalResults = totalResults;
}

}