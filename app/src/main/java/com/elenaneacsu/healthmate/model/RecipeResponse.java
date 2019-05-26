package com.elenaneacsu.healthmate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeResponse {
    @SerializedName("q")
    @Expose
    private String q;
    @SerializedName("from")
    @Expose
    private int from;
    @SerializedName("to")
    @Expose
    private int to;
    @SerializedName("more")
    @Expose
    private boolean more;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("hits")
    @Expose
    private List<Hit> hits = null;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Hit> getHits() {
        return hits;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }
}
