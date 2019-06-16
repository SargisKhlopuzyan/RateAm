package com.sargis.kh.rateam.models.response_branches;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Title implements Serializable {

    @SerializedName("en") public String en;

    @SerializedName("am") public String am;

    @SerializedName("ru") public String ru;

}