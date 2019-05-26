package com.elenaneacsu.healthmate.api;

import com.elenaneacsu.healthmate.model.FoodResponse;
import com.elenaneacsu.healthmate.model.RecipeResponse;

import io.reactivex.Observable;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
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
        Retrofit retrofit = getRetrofitInstance();
        FoodRequestService requestService = retrofit.create(FoodRequestService.class);
        return requestService.responseService(query);
    }

    public static Observable<RecipeResponse> getRecipeResponse(String query, int from, int to) {
        Retrofit retrofit = getRetrofitInstance();
        RecipeRequestService requestService = retrofit.create(RecipeRequestService.class);
        return requestService.responseService(query, from, to);
    }

    public interface FoodRequestService {
        @GET(BASE_URL+"api/food-database/parser?app_id="+ APP_ID_FOOD +"&app_key="
                + APP_KEY_FOOD)
        Observable<FoodResponse> responseService(
                @Query("ingr") String query
        );
    }

    public interface RecipeRequestService {
        @GET(BASE_URL+"search?app_id="+APP_ID_RECIPE+"&app_key="
                +APP_KEY_RECIPE)
        Observable<RecipeResponse> responseService(
                @Query("q") String query,
                @Query("from") int from,
                @Query("to") int to
        );
    }
}
