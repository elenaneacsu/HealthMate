package com.elenaneacsu.healthmate.api;

import com.elenaneacsu.healthmate.model.FoodResponse;

import io.reactivex.Observable;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.elenaneacsu.healthmate.utils.Constants.APP_ID;
import static com.elenaneacsu.healthmate.utils.Constants.APP_KEY;
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

    public static Observable<FoodResponse> getResponse(String query) {
        Retrofit retrofit = getRetrofitInstance();
        RequestService requestService = retrofit.create(RequestService.class);
        return requestService.responseService(query);
    }

    public interface RequestService {
        @GET(BASE_URL+"food-database/parser?app_id="+APP_ID+"&app_key="
                +APP_KEY)
        Observable<FoodResponse> responseService(
                @Query("ingr") String query
        );

    }
}
