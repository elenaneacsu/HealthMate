package com.elenaneacsu.healthmate.api;

import com.elenaneacsu.healthmate.model.entity.GetFood;
import com.elenaneacsu.healthmate.model.entity.SearchedFood;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IApiInterface {

    @GET("server.api/")
    Observable<SearchedFood> searchFood(@Query("format") String format,
                                        @Query("max_results") Integer max_results,
                                        @Query("method") String method,
                                        @Query("oauth_consumer_key") String apiKey,
                                        @Query("oauth_nonce") String nonce,
                                        @Query("oauth_signature") String signature,
                                        @Query("oauth_signature_method") String signature_method,
                                        @Query("oauth_timestamp") Long timestamp,
                                        @Query("oauth_version") String version,
                                        @Query("page_number") Integer page_number,
                                        @Query("search_expression") String search_expression);

    @GET("server.api/")
    Observable<GetFood> getFood(@Query("oauth_consumer_key") String apiKey,
                                @Query("oauth_signature_method") String signature_method,
                                @Query("oauth_timestamp") Long timestamp,
                                @Query("oauth_nonce") String nonce,
                                @Query("oauth_version") String version,
                                @Query("oauth_signature") String signature,
                                @Query("method") String method,
                                @Query("food_id") Long food_id,
                                @Query("format") String format);
}
