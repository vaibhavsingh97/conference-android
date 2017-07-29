package com.systers.conference.api;

import com.systers.conference.util.APIUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class APIClient {
    private static final int CONNECT_TIMEOUT_MILLIS = 20 * 1000; // 15s

    private static final int READ_TIMEOUT_MILLIS = 50 * 1000; // 20s

    private final ETouchesAPI eTouchesAPI;

    public APIClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient().newBuilder()
                .connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .addInterceptor(new ETouchesInterceptor());

        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        eTouchesAPI = retrofit.create(ETouchesAPI.class);
    }

    public ETouchesAPI geteTouchesAPI() {
        return eTouchesAPI;
    }
}
