package com.elenaneacsu.healthmate.model;

import com.elenaneacsu.healthmate.api.IApiInterface;
import com.elenaneacsu.healthmate.model.entity.GetFood;

import io.reactivex.Observable;

public class GetFoodModel implements IGetFoodModel {

    private IApiInterface mIApiInterface;

    public GetFoodModel(IApiInterface IApiInterface) {
        mIApiInterface = IApiInterface;
    }

    @Override
    public Observable<GetFood> getFood(String apiKey, String signature_method, Long timestamp,
                                       String nonce, String version, String signature,
                                       String method, Long food_id, String format) {
        return mIApiInterface.getFood(apiKey, signature_method, timestamp,
                nonce, version, signature,
                method, food_id, format);
    }
}
