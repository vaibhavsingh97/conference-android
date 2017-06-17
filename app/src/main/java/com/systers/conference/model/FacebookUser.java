package com.systers.conference.model;

import com.google.gson.annotations.SerializedName;

public class FacebookUser {
    @SerializedName("id")
    private String accountId;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    private String email;

    private Work[] work;

    private Picture picture;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getCompany() {
        if (work != null) {
            return work[0].getEmployer().getName();
        }
        return null;
    }

    public String getRole() {
        if (work != null) {
            return work[0].getPosition().getName();
        }
        return null;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getPictureUrl() {
        if (picture != null) {
            return picture.getData().getProfilePictureUrl();
        }
        return null;
    }
}

class Position {
    private String name;

    public String getName() {
        return name;
    }
}

class Employer {
    private String name;

    public String getName() {
        return name;
    }
}

class Picture {
    private Data data;

    public Data getData() {
        return data;
    }
}

class Data {
    private Boolean is_silhouette;
    private String url;

    public String getProfilePictureUrl() {
        return url;
    }
}

class Work {
    private Employer employer;
    private Position position;

    public Employer getEmployer() {
        return employer;
    }

    public Position getPosition() {
        return position;
    }
}


