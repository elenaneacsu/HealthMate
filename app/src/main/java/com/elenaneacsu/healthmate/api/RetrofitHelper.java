package com.elenaneacsu.healthmate.api;

import com.elenaneacsu.healthmate.model.FoodDetailResponse;
import com.elenaneacsu.healthmate.model.FoodRequest;
import com.elenaneacsu.healthmate.model.FoodResponse;
import com.elenaneacsu.healthmate.model.RecipeResponse;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.elenaneacsu.healthmate.utils.Constants.APP_ID_FOOD;
import static com.elenaneacsu.healthmate.utils.Constants.APP_ID_RECIPE;
import static com.elenaneacsu.healthmate.utils.Constants.APP_KEY_FOOD;
import static com.elenaneacsu.healthmate.utils.Constants.APP_KEY_RECIPE;
import static com.elenaneacsu.healthmate.utils.Constants.BASE_URL;

public class RetrofitHelper {
    private static Retrofit mRetrofit;

    public static Retrofit getRetrofitInstance() {
        if (mRetrofit == null) {
            mRetrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    public static Observable<FoodResponse> getFoodResponse(String query) {
        FoodRequestService requestService = getRetrofitInstance().create(FoodRequestService.class);
        return requestService.responseFoodService(query);
    }

    public static Observable<FoodResponse> getBarcodeResponse(long upc) {
        FoodRequestService requestService = getRetrofitInstance().create(FoodRequestService.class);
        return requestService.responseBarcodeService(upc);
    }

    public static Observable<FoodDetailResponse> getFoodDetailResponse(FoodRequest foodRequest) {
        FoodRequestService requestService = getRetrofitInstance().create(FoodRequestService.class);
        return requestService.responseDetailService(foodRequest);
    }

    public static Observable<RecipeResponse> getRecipeResponse(String query, int from, int to) {
        FoodRequestService requestService = getRetrofitInstance().create(FoodRequestService.class);
        return requestService.responseRecipeService(query, from, to);
    }

    public interface FoodRequestService {
        @GET(BASE_URL + "api/food-database/parser?app_id=" + APP_ID_FOOD + "&app_key="
                + APP_KEY_FOOD)
        Observable<FoodResponse> responseFoodService(
                @Query("ingr") String query
        );

        @GET(BASE_URL + "api/food-database/parser?app_id=" + APP_ID_FOOD + "&app_key="
                + APP_KEY_FOOD)
        Observable<FoodResponse> responseBarcodeService(
                @Query("upc") long upc
        );

        @Headers({
                "Content-Type:application/json"})
        @POST(BASE_URL + "api/food-database/nutrients?app_id=" + APP_ID_FOOD + "&app_key="
                + APP_KEY_FOOD)
        Observable<FoodDetailResponse> responseDetailService(
                @Body FoodRequest foodRequest);

        @GET(BASE_URL + "search?app_id=" + APP_ID_RECIPE + "&app_key="
                + APP_KEY_RECIPE)
        Observable<RecipeResponse> responseRecipeService(
                @Query("q") String query,
                @Query("from") int from,
                @Query("to") int to
        );
    }

}
