package com.sargis.kh.rateam.models.response_branches;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Branch implements Serializable {

    @SerializedName("head") public Integer head;

    @SerializedName("title") public Title title;

    @SerializedName("address") public Address address;

    @SerializedName("location") public Location location;

    @SerializedName("contacts") public String contacts;

    @SerializedName("workhours") public List<Workhours> workhours;

}