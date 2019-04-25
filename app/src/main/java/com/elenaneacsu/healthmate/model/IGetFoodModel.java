package com.elenaneacsu.healthmate.model;

import com.elenaneacsu.healthmate.model.entity.GetFood;

import io.reactivex.Observable;

public interface IGetFoodModel {

    Observable<GetFood> getFood(String apiKey, String signature_method, Long timestamp,
                                String nonce, String version, String signature,
                                String method, Long food_id, String format);
}
