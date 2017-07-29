package com.systers.conference.model;

import com.google.gson.annotations.SerializedName;

public class SessionList {
    @SerializedName("sessionid")
    private String sessionId;

    @SerializedName("sessionkey")
    private String sessionKey;

    public String getSessionId() {
        return sessionId;
    }

    public String getSessionKey() {
        return sessionKey;
    }
}
