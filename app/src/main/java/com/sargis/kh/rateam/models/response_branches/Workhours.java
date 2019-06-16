package com.sargis.kh.rateam.models.response_branches;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Workhours implements Serializable {

    @SerializedName("days") public String days;

    @SerializedName("hours") public String hours;

}