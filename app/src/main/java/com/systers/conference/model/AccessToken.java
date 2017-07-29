package com.systers.conference.model;

import com.google.gson.annotations.SerializedName;

public class AccessToken {
    @SerializedName("accesstoken")
    private String token;

    public String getToken() {
        return token;
    }
}
