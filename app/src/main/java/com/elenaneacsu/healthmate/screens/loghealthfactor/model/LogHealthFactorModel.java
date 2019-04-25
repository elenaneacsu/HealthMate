package com.elenaneacsu.healthmate.screens.loghealthfactor.model;

import com.elenaneacsu.healthmate.api.IApiInterface;
import com.elenaneacsu.healthmate.api.RetrofitClient;
import com.elenaneacsu.healthmate.model.entity.SearchedFood;

import io.reactivex.Observable;

public class LogHealthFactorModel implements ILogHealthFactorModel {

    private IApiInterface mIApiInterface;

    public LogHealthFactorModel() {
        mIApiInterface = RetrofitClient.getRetrofitInstance()
                .create(IApiInterface.class);
    }


    @Override
    public Observable<SearchedFood> searchFood(String format, Integer max_results, String method, String apiKey, String nonce, String signature, String signature_method, Long timestamp, String version, Integer page_number, String search_expression) {
        return mIApiInterface.searchFood(
                format,
                max_results,
                method,
                apiKey,
                nonce,
                signature,
                signature_method,
                timestamp,
                version,
                page_number,
                search_expression
        );
    }
}
