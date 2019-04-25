package com.elenaneacsu.healthmate.network;

import com.elenaneacsu.healthmate.model.GetFood;
import com.elenaneacsu.healthmate.model.SearchedFood;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodApiInterface {

    @GET()
    Call<SearchedFood> getFoods(@Query("format") String format,
                                @Query("max_results") int max_results,
                                @Query("method") String method,
                                @Query("oauth_consumer_key") String apiKey,
                                @Query("oauth_nonce") String nonce,
                                @Query("oauth_signature") String signature,
                                @Query("oauth_signature_method") String signature_method,
                                @Query("oauth_timestamp") long timestamp,
                                @Query("oauth_version") String version,
                                @Query("search_expression") String search_expression,
                                @Query("page_number") int page_number);

    @GET()
    Call<GetFood> getFood(@Query("oauth_consumer_key") String apiKey,
                          @Query("oauth_signature_method") String signature_method,
                          @Query("oauth_timestamp") long timestamp,
                          @Query("oauth_nonce") String nonce,
                          @Query("oauth_version") String version,
                          @Query("oauth_signature") String signature,
                          @Query("method") String method,
                          @Query("food_id") long food_id,
                          @Query("format") String format);
}
