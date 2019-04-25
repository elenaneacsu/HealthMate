package com.elenaneacsu.healthmate.screens.loghealthfactor.model;

import com.elenaneacsu.healthmate.model.entity.SearchedFood;

import io.reactivex.Observable;

public interface ILogHealthFactorModel {

    Observable<SearchedFood> searchFood(String format,
                                        Integer max_results,
                                        String method,
                                        String apiKey,
                                        String nonce,
                                        String signature,
                                        String signature_method,
                                        Long timestamp,
                                        String version,
                                        Integer page_number,
                                        String search_expression);



}
