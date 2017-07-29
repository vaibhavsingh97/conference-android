package com.systers.conference.api;

import android.support.annotation.NonNull;

import com.systers.conference.ConferenceApplication;
import com.systers.conference.util.AccountUtils;
import com.systers.conference.util.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class ETouchesInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        String responseBodyString = response.body().string();
        Response returnResponse = response.newBuilder().body(ResponseBody.create(responseBody.contentType(), responseBodyString.getBytes())).build();
        LogUtils.LOGE("Interceptor", responseBodyString);
        if (responseBodyString.contains("error")) {
            DataDownloadManager.getInstance().downloadToken();
            LogUtils.LOGE("Interceptor", "DownloadManager called");
            Request newRequest = request.newBuilder().url(
                    request.url().newBuilder()
                            .setQueryParameter("accesstoken", AccountUtils.getAccessToken(ConferenceApplication.getAppContext()))
                            .build()
            ).build();
            LogUtils.LOGE("Interceptor", "new request dispatched");
            LogUtils.LOGE("Interceptor", newRequest.url().toString());
            return chain.proceed(newRequest);
        } else {
            LogUtils.LOGE("Interceptor", "Outside");
            return returnResponse;
        }
    }
}
