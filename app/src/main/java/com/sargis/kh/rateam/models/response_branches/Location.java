package com.sargis.kh.rateam.models.response_branches;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Location implements Serializable {

    @SerializedName("lat") public Float lat;

    @SerializedName("lng") public Float lng;

}