package com.elenaneacsu.healthmate.api;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.util.Arrays;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static com.elenaneacsu.healthmate.utils.Constants.API_KEY;
import static com.elenaneacsu.healthmate.utils.Constants.APP_SECRET;
import static com.elenaneacsu.healthmate.utils.Constants.HMAC_SHA1_ALGORITHM;

public class ApiRequest {

    public static String[] generateOauthParamsSearch(String searchFood, Long i) {
        return new String[]{
                "oauth_consumer_key=" + API_KEY,
                "oauth_signature_method=HMAC-SHA1",
                "oauth_timestamp=" +
                        Long.valueOf(System.currentTimeMillis() * 2).toString(),
                "oauth_nonce=" + nonce(),
                "oauth_version=1.0",
                "format=json",
                "method=foods.search",
                "search_expression=" + searchFood,
                "page_number=" + i,
                "max_results=" + 30};
    }

    private static String[] generateOauthParamsGet() {
        return new String[]{
                "oauth_consumer_key=" + API_KEY,
                "oauth_signature_method=HMAC-SHA1",
                "oauth_timestamp=" +
                        Long.valueOf(System.currentTimeMillis() * 2).toString(),
                "oauth_nonce=" + nonce(),
                "oauth_version=1.0",
                "format=json"};
    }

    public static String sign(String method, String uri, String[] params) {
        String[] p = {method, Uri.encode(uri), Uri.encode(paramify(params))};
        APP_SECRET += "&";
        String s = join(p, "&");
        SecretKey sk = new SecretKeySpec(APP_SECRET.getBytes(), HMAC_SHA1_ALGORITHM);
        APP_SECRET = APP_SECRET.substring(0, APP_SECRET.length() - 1);
        try {
            Mac m = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            m.init(sk);
            return Uri.encode(new String(Base64.encode(m.doFinal(s.getBytes()), Base64.DEFAULT)).trim());
        } catch (java.security.NoSuchAlgorithmException e) {
            Log.w("FatSecret_TEST FAIL", e.getMessage());
            return null;
        } catch (java.security.InvalidKeyException e) {
            Log.w("FatSecret_TEST FAIL", e.getMessage());
            return null;
        }
    }

    public static String paramify(String[] params) {
        String[] p = Arrays.copyOf(params, params.length);
        Arrays.sort(p);
        return join(p, "&");
    }

    private static String join(String[] array, String separator) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0)
                b.append(separator);
            b.append(array[i]);
        }
        return b.toString();
    }

    public static String nonce() {
        Random r = new Random();
        StringBuilder n = new StringBuilder();
        for (int i = 0; i < r.nextInt(8) + 2; i++)
            n.append(r.nextInt(26) + 'a');
        return n.toString();
    }
}
